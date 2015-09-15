/**
 * File WSParameterizationClient.java
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

import java.net.URL;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

import eu.artemis.demanes.datatypes.ANES_URN;
import eu.artemis.demanes.exceptions.ParameterizationException;
import eu.artemis.demanes.exceptions.ParameterizationLinkException;
import eu.artemis.demanes.lib.ParameterizationProxy;
import eu.artemis.demanes.lib.impl.restParameterization.ParameterizationRESTConstants;

/**
 * The ParameterizationRESTClient is only used by the
 * RemoteRESTParameterizationFactory to serve as a proxy for the REST
 * communication. It takes the javax.ws classes to create a web client and
 * communicate with the REST Parameterization server
 * 
 * @author coenvl
 * @version 0.1
 * @since May 8, 2014
 * 
 */
class ParameterizationRESTClient implements ParameterizationProxy {

	private static final String basePath = ParameterizationRESTConstants.SERVICES_PATH
			+ ParameterizationRESTConstants.PARAMETERIZATION_PATH;

	private static final String listPath = basePath
			+ ParameterizationRESTConstants.PARAMETERIZATION_LIST_PATH;

	private static final String urnPath = basePath + "?"
			+ ParameterizationRESTConstants.QUERY_PARAM + "=";;

	private final Client client;

	private final URL target;

	public ParameterizationRESTClient(URL target) {
		this.target = target;
		client = ClientBuilder.newClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.artemis.demanes.lib.ParameterizationProxy#getParameter(eu.artemis.
	 * demanes.datatypes.ANES_URN)
	 */
	@Override
	public byte[] getParameter(ANES_URN urn) throws ParameterizationException {
		return client.target(this.target + urnPath + urn.toString())
				.request(MediaType.APPLICATION_OCTET_STREAM).get(byte[].class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.artemis.demanes.lib.ParameterizationProxy#listParameters()
	 */
	@Override
	public byte[] listParameters() throws ParameterizationException {
		try {
			return client.target(this.target + listPath)
					.request(MediaType.APPLICATION_OCTET_STREAM)
					.get(byte[].class);
		} catch (ProcessingException e) {
			throw new ParameterizationLinkException(
					"Unable to retrieve parameter list from remote host", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.artemis.demanes.lib.ParameterizationProxy#setParameter(eu.artemis.
	 * demanes.datatypes.ANES_URN, byte[])
	 */
	@Override
	public void setParameter(ANES_URN urn, byte[] value)
			throws ParameterizationException {
		StatusType status = client
				.target(this.target + urnPath + urn.toString())
				.request(MediaType.APPLICATION_OCTET_STREAM)
				.put(Entity.entity(value, MediaType.APPLICATION_OCTET_STREAM))
				.getStatusInfo();

		if (status.getFamily() != Family.SUCCESSFUL)
			throw new ParameterizationLinkException(
					"Error while setting remote urn " + urn
							+ " on remote host " + this.target);
	}

}
