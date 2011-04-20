/*
  Licensed to the Court of the University of Edinburgh (UofE) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The UofE licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
*/
package org.pathwayeditor.metabolic.sbmlexport;

import org.pathwayeditor.metabolic.ndomAPI.ICompartment;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.INdomModel;



 class CompartmentAnnotationBuilder extends AnnotationBuilder {
	private ICompartment compartment;
	CompartmentAnnotationBuilder(ICompartment comp) {
		super((INdomModel)comp);
		this.compartment=comp;
	}
	
	

	@Override
	String getRDFResources(){
		StringBuffer sb = new StringBuffer();
		sb.append(RDF_ISVERSIONOF_ST).append(getBagStart())
		.append(RDFList).append(AnnotationURLs.GO.getURL())
		                  .append( ((ICompartment)compartment).getGOTerm())
		                  .append(RDFListEnd)
		.append(getBagEnd())
		.append(RDF_ISVERSIONOF_END);
		return sb.toString();
	}

	

}
