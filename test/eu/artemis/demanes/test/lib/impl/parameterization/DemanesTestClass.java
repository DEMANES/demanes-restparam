/**
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

import aQute.bnd.annotation.component.Component;
import eu.artemis.demanes.impl.annotations.DEM_GetParameter;
import eu.artemis.demanes.impl.annotations.DEM_SetParameter;
import eu.artemis.demanes.impl.annotations.DEM_TaskProperties;
import eu.artemis.demanes.lib.SelfRegister;

@Component
@DEM_TaskProperties(brokerType = "REST")
public class DemanesTestClass implements SelfRegister {

	private NullPointerException coen = new NullPointerException("HAHA");
	private Double julio = 0.0;
	private Integer madan = 0;

	@DEM_GetParameter(urn = "urn:tno:coen")
	public NullPointerException getCoen() {
		return this.coen;
	}

	@DEM_GetParameter(urn = "urn:tno:julio")
	public double getJulio() {
		return this.julio;
	}

	@DEM_GetParameter(urn = "urn:oasis:names")
	public int getMadan() {
		return this.madan;
	}

	@DEM_SetParameter(urn = "urn:tno:julio")
	public void setJulio(double value) {
		this.julio = (Double) value;
	}

	@DEM_SetParameter(urn = "urn:oasis:names")
	public void setMadan(Integer value) {
		this.madan = value;
	}
}
