/**
 * File TestParameterizable.java
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
package eu.artemis.demanes.test.lib.impl.parameterization;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import aQute.bnd.annotation.component.Component;
import eu.artemis.demanes.datatypes.ANES_URN;
import eu.artemis.demanes.exceptions.ParameterizationException;
import eu.artemis.demanes.parameterization.Parameterizable;

/**
 * TestParameterizable
 * 
 * @author coenvl
 * @version 0.1
 * @since May 8, 2014
 * 
 */
@Component
public class TestParameterizable implements Parameterizable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.artemis.demanes.parameterization.Parameterizable#getParameter(eu.artemis
	 * .demanes.datatypes.ANES_URN)
	 */
	@Override
	public Object getParameter(ANES_URN urn) throws ParameterizationException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.artemis.demanes.parameterization.Parameterizable#listParameters()
	 */
	@Override
	public Set<ANES_URN> listParameters() {
		HashSet<ANES_URN> t = new HashSet<ANES_URN>();
		try {
			t.add(new ANES_URN("urn:tno:testParameterizable"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return t;
	}

	/*@Reference(target = "(broker.type=REST)")
	public void registerAtBroker(ParameterizableRegistry broker) {
		broker.register(this);
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.artemis.demanes.parameterization.Parameterizable#setParameter(eu.artemis
	 * .demanes.datatypes.ANES_URN, java.lang.Object)
	 */
	@Override
	public void setParameter(ANES_URN urn, Object value)
			throws ParameterizationException {

	}

}
