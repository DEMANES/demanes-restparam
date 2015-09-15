/**
 * File RESTParameterizationBroker.java
 * Created on 30 apr. 2014 by oliveirafilhojad
 * 
 * This file was created for DEMANES project.
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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import eu.artemis.demanes.datatypes.ANES_URN;
import eu.artemis.demanes.exceptions.ParameterizationException;
import eu.artemis.demanes.impl.parameterization.ParameterizationBroker;
import eu.artemis.demanes.lib.ParameterizableRegistry;
import eu.artemis.demanes.lib.Serializer;
import eu.artemis.demanes.lib.impl.restParameterization.ParameterizationRESTConstants;
import eu.artemis.demanes.parameterization.Parameterizable;

/**
 * RESTParameterizationBroker
 * 
 * @version 0.1
 * @since 30 apr. 2014
 * 
 */
@Component(immediate = true, properties = "broker.type=REST")
@Path(ParameterizationRESTConstants.PARAMETERIZATION_PATH)
public class RESTParameterizationBroker implements ParameterizableRegistry {

	private ParameterizableRegistry myBroker;
	
	private RESTParameterizationServer myServer;

	private Serializer mySerializer;

	@Reference(optional = false)
	public void setSerializer(Serializer s) {
		this.mySerializer = s;
	}
	
	@Activate
	public void start() {
		ParameterizationBroker broker = new ParameterizationBroker();
		this.myBroker = broker;
		this.myServer = new RESTParameterizationServer(broker, mySerializer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void register(Parameterizable p) {
		this.myBroker.register(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unregister(Parameterizable p) {
		this.myBroker.unregister(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.artemis.demanes.parameterization.Parameterizable#listParameters()
	 */
	@Path(ParameterizationRESTConstants.PARAMETERIZATION_LIST_PATH)
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] listParameters() throws ParameterizationException {
		return myServer.listParameters();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws ParameterizationException
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public void setParameter(
			@QueryParam(ParameterizationRESTConstants.QUERY_PARAM) ANES_URN urn,
			byte[] value) throws ParameterizationException {
		myServer.setParameter(urn, value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public byte[] getParameter(
			@QueryParam(ParameterizationRESTConstants.QUERY_PARAM) ANES_URN urn)
			throws ParameterizationException {
		return myServer.getParameter(urn);
	}
	
}
