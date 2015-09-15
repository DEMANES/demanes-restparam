/**
 * File RemoteParameterizationFactory.java
 * 
 * This file is part of the demanesImplementation project.
 *
 * Copyright 2014 TNO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.artemis.demanes.lib.impl.restParameterization.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import eu.artemis.demanes.datatypes.ANES_URN;
import eu.artemis.demanes.exceptions.ParameterizationException;
import eu.artemis.demanes.exceptions.ParameterizationValueTypeException;
import eu.artemis.demanes.lib.ParameterizationProxy;
import eu.artemis.demanes.lib.RemoteParameterizationFactory;
import eu.artemis.demanes.lib.Serializer;
import eu.artemis.demanes.lib.exceptions.RemoteParameterizationException;
import eu.artemis.demanes.lib.exceptions.SerializationException;
import eu.artemis.demanes.parameterization.Parameterizable;

/**
 * Perhaps the RemoteParameterizationFactory should also be offered as a service
 * instead of exporting the package and using as a build dependency.
 * 
 * @author coenvl
 * @version 0.1
 * @since May 8, 2014
 * 
 */
@Component
public final class RemoteRESTParameterizationFactory implements
		RemoteParameterizationFactory {

	/**
	 * The serializer contains the type of serializer used for the clients that
	 * this factory builds.
	 */
	private Serializer serializer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.artemis.demanes.lib.RemoteParameterizationFactory#
	 * createRemoteParameterizable(java.lang.String)
	 */
	@Override
	public Parameterizable createRemoteParameterizable(String remoteLocation)
			throws RemoteParameterizationException {
		if (this.serializer == null)
			throw new RemoteParameterizationException(
					"Serializer unset, please provide a bundle with a valid Serializer");

		try {
			ParameterizationProxy proxy = new ParameterizationRESTClient(
					new URL(remoteLocation));
			return new RemoteRESTParameterizable(proxy);
		} catch (MalformedURLException e) {
			throw new RemoteParameterizationException("Unable to resolve "
					+ remoteLocation, e);
		}
	}

	/**
	 * Set the serializer of the RemoteParameterizationFactory
	 * 
	 * @param s
	 */
	@Reference
	public void setSerializer(Serializer s) {
		this.serializer = s;
	}

	/**
	 * RemoteParameterizable is an internal class only to return a
	 * parameterizable object.
	 * 
	 * @author coenvl
	 * @version 0.1
	 * @since May 8, 2014
	 * 
	 */
	private class RemoteRESTParameterizable implements Parameterizable {

		private final ParameterizationProxy proxy;

		public RemoteRESTParameterizable(ParameterizationProxy proxy) {
			this.proxy = proxy;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * eu.artemis.demanes.parameterization.Parameterizable#getParameter(
		 * eu.artemis.demanes.datatypes.ANES_URN)
		 */
		@Override
		public Object getParameter(ANES_URN urn)
				throws ParameterizationException {
			try {
				return serializer.deserialize(proxy.getParameter(urn));
			} catch (SerializationException e) {
				throw new ParameterizationValueTypeException(
						"Error deserializing raw parameter value", e);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * eu.artemis.demanes.parameterization.Parameterizable#listParameters()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public Set<ANES_URN> listParameters() throws ParameterizationException {
			// Deserialize the object
			Object obj;
			try {
				obj = serializer.deserialize(proxy.listParameters());
			} catch (SerializationException e) {
				e.printStackTrace();
				return null;
			}

			// Very carefully cast the object to a Set of ANES_URNs
			if (obj instanceof Set<?>) {
				Iterator<?> iter = ((Set<?>) obj).iterator();

				// Check if there is something in the set, if not return a new
				// set
				if (!iter.hasNext())
					return new HashSet<ANES_URN>();

				if (iter.next() instanceof ANES_URN)
					return (Set<ANES_URN>) obj;

				// Otherwise we did get a set, but of invalid type
				System.err
						.println("Set of invalid type obtained from remote server!");
				return null;
			} else {
				// Display another message when we didn't get a Set at all
				System.err
						.println("Invalid object obtained from remote server!");
				return null;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * eu.artemis.demanes.parameterization.Parameterizable#setParameter(
		 * eu.artemis.demanes.datatypes.ANES_URN, java.lang.Object)
		 */
		@Override
		public void setParameter(ANES_URN urn, Object value)
				throws ParameterizationException {
			try {
				proxy.setParameter(urn, serializer.serialize(value));
			} catch (SerializationException e) {
				throw new ParameterizationValueTypeException(
						"Error serializing object" + e);
			}
		}
	}
}
