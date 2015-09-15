/**
 * File RESTParameterizationServer.java
 * 
 * This file is part of the eu.artemis.demanes.lib.restParameterization project.
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
package eu.artemis.demanes.lib.impl.restParameterization.server;

import eu.artemis.demanes.datatypes.ANES_URN;
import eu.artemis.demanes.exceptions.ParameterizationException;
import eu.artemis.demanes.exceptions.ParameterizationLinkException;
import eu.artemis.demanes.lib.ParameterizationProxy;
import eu.artemis.demanes.lib.Serializer;
import eu.artemis.demanes.lib.exceptions.SerializationException;
import eu.artemis.demanes.parameterization.Parameterizable;

/**
 * RESTParameterizationServer
 *
 * @author leeuwencjv
 * @version 0.1
 * @since 14 okt. 2014
 *
 */
public class RESTParameterizationServer implements ParameterizationProxy {

	private final Parameterizable broker;

	/**
	 * The Serializer translates the incoming byte[] to the objects the user
	 * wishes to set or get.
	 */
	private final Serializer serializer;

	/**
	 * @param restParameterizationBroker
	 */
	public RESTParameterizationServer(Parameterizable p, Serializer s) {
		this.broker = p;
		this.serializer = s;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] listParameters() throws ParameterizationException {
		try {
			return serializer.serialize(this.broker.listParameters());
		} catch (SerializationException e) {
			throw new ParameterizationLinkException(
					"Error serializing parameter list", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameter(ANES_URN urn, byte[] value)
			throws ParameterizationException {
		try {
			this.broker.setParameter(urn, serializer.deserialize(value));
		} catch (SerializationException e) {
			throw new ParameterizationLinkException(
					"Error deserializing value", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getParameter(ANES_URN urn) throws ParameterizationException {
		try {
			return serializer.serialize(this.broker.getParameter(urn));
		} catch (SerializationException e) {
			throw new ParameterizationLinkException("Error serializing value",
					e);
		}
	}

}
