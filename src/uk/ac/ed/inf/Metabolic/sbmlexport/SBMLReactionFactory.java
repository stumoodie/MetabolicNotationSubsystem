package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.util.List;

import org.sbml.libsbml.Model;
import org.sbml.libsbml.ModifierSpeciesReference;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SpeciesReference;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;
import uk.ac.ed.inf.Metabolic.ndomAPI.IRelation;

class SBMLReactionFactory implements IReactionBuilder {

	public void buildReactions(Model sbmlModel, IModel model) {
		for (IReaction reaction: model.getReactionList()){
			
			// reactions msut at least one substrate or product
			if(reactionIsEmpty(reaction)){
				continue;
			}
			Reaction sbmlReaction = sbmlModel.createReaction();
			sbmlReaction.setId(reaction.getId());
			sbmlReaction.setName(reaction.getASCIIName());
			sbmlReaction.setReversible(reaction.isReversible());
			
			List<IRelation> substrates = reaction.getSubstrateList();
			for (IRelation substrate: substrates) {
			   sbmlReaction.addReactant(new SpeciesReference(substrate.getId(), substrate.getStoichiometry()));
			}
			
			List<IRelation> products = reaction.getProductList();
			for (IRelation product: products) {
			   sbmlReaction.addProduct(new SpeciesReference(product.getId(), product.getStoichiometry()));
			}
			
			List<IRelation> modifiers = reaction.getCatalystList();
			modifiers.addAll(reaction.getInhibitorList());
			modifiers.addAll(reaction.getActovatorList());
			for (IRelation modifier: modifiers) {
				ModifierSpeciesReference msr = new ModifierSpeciesReference(modifier.getId());
			   sbmlReaction.addModifier(msr);
			}
		}

	}

	private boolean reactionIsEmpty(IReaction reaction) {
		return reaction.getSubstrateList().isEmpty() && reaction.getProductList().isEmpty();
	}

}
