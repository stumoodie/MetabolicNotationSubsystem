package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.util.List;

import org.sbml.libsbml.KineticLaw;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.ModifierSpeciesReference;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SBase;
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
			
			setUpKineticLaw(sbmlReaction, reaction);
			
			
			AnnotationBuilder builder = new ReactionAnnotationBuilder(reaction);
			addAnnotationAndNotes(sbmlReaction, builder);
			List<IRelation> substrates = reaction.getSubstrateList();
			for (IRelation substrate: substrates) {
			   sbmlReaction.addReactant(createSpeciesReference(substrate));
			}
			
			List<IRelation> products = reaction.getProductList();
			for (IRelation product: products) {
			   sbmlReaction.addProduct(createSpeciesReference(product));
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

	private SpeciesReference createSpeciesReference(IRelation substrate) {
		SpeciesReference sr = new SpeciesReference(substrate.getId(), substrate.getStoichiometry());
		StringBuffer sb = new StringBuffer();
		 sb.append("<p xmlns='http://www.w3.org/1999/xhtml'>");
		 sb.append(substrate.getRole());
		 sb.append("</p>");
		sr.appendNotes(sb.toString());
		return sr;
	}

	private void setUpKineticLaw(Reaction sbmlReaction, IReaction reaction) {
		KineticLaw law = sbmlReaction.createKineticLaw();
		StringBuffer sb = new StringBuffer();
		 sb.append("<p xmlns='http://www.w3.org/1999/xhtml'>");
		 sb.append(reaction.getKineticLaw());
		 sb.append("</p>");
		law.appendNotes(sb.toString());
		
	}

	private boolean reactionIsEmpty(IReaction reaction) {
		return reaction.getSubstrateList().isEmpty() && reaction.getProductList().isEmpty();
	}
	
	private void addAnnotationAndNotes(SBase sbmlObject, AnnotationBuilder builder) {
		String annotation = builder.buildAnnotation();
		sbmlObject.appendAnnotation(annotation);
		String notes = builder.buildNotes();
		sbmlObject.appendNotes(notes);
	}

}
