package uk.ac.ed.inf.Metabolic.sbmlexport;

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
