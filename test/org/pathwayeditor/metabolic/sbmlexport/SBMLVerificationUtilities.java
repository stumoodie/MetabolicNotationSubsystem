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

import org.sbml.libsbml.Model;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SpeciesReference;

/**
 * Utility class for verifying sbml model content, could be used generally.
 * Should not have ndom specific dependencies.
 * @author Richard Adams
 *
 */
public class SBMLVerificationUtilities {
	
	/*
	 * Given a model, returns the reaction at  the specified index
	 */
	static Reaction getReactionByIndex(Model sbmlModel, int index) {
		return (Reaction)sbmlModel.getListOfReactions().get(index);
	}
	 
	/*
	 * Given a  reaction index and a product index, returns the species refererence for verification
	 */
	static  SpeciesReference getProductByIndex(Model sbmlModel,int reactionIndex, int productIndex) {
		Reaction r = ((Reaction)sbmlModel.getListOfReactions().get(reactionIndex));
		return (SpeciesReference)r.getListOfProducts().get(productIndex);
	}
	
	/*
	 * Given a  reaction index and a substrate index, returns the species refererence for verification
	 */
	static  SpeciesReference getSubstrateByIndex(Model sbmlModel,int reactionIndex, int substrateIndex) {
		Reaction r = ((Reaction)sbmlModel.getListOfReactions().get(reactionIndex));
		return (SpeciesReference)r.getListOfReactants().get(substrateIndex);
	}
}
