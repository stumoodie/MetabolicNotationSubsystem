package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.util.ArrayList;
import java.util.List;

import org.sbml.libsbml.ASTNode;
import org.sbml.libsbml.KineticLaw;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.ModifierSpeciesReference;
import org.sbml.libsbml.Parameter;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SBase;
import org.sbml.libsbml.SpeciesReference;
import org.sbml.libsbml.XMLInputStream;
import org.sbml.libsbml.libsbml;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;
import uk.ac.ed.inf.Metabolic.ndomAPI.IRelation;

class SBMLReactionFactory implements IReactionBuilder {

	public void buildReactions(Model sbmlModel, IModel model) {
		for (IReaction reaction : model.getReactionList()) {

			// reactions msut at least one substrate or product
			if (reactionIsEmpty(reaction)) {
				continue;
			}
			// set up reaction
			Reaction sbmlReaction = sbmlModel.createReaction();
			sbmlReaction.setId(reaction.getId());
			sbmlReaction.setName(reaction.getASCIIName());
			sbmlReaction.setReversible(reaction.isReversible());

			setUpKineticLaw(sbmlReaction, reaction);

			AnnotationBuilder builder = new ReactionAnnotationBuilder(reaction);
			addAnnotationAndNotes(sbmlReaction, builder);

			// handle susbtrates
			List<IRelation> substrates = reaction.getSubstrateList();
			for (IRelation substrate : substrates) {
				sbmlReaction.addReactant(createSpeciesReference(substrate));
			}

			// handle products
			List<IRelation> products = reaction.getProductList();
			for (IRelation product : products) {
				sbmlReaction.addProduct(createSpeciesReference(product));
			}

			// handle modifiers
			List<IRelation> all = new ArrayList<IRelation>();
			all.addAll(reaction.getCatalystList());
			all.addAll(reaction.getInhibitorList());
			all.addAll(reaction.getActivatorList());
			for (IRelation modifier : all) {
				ModifierSpeciesReference msr = createModifierReference(modifier);
				sbmlReaction.addModifier(msr);
			}
		}

	}

	private ModifierSpeciesReference createModifierReference(IRelation modifier) {
		ModifierSpeciesReference msr = new ModifierSpeciesReference(modifier
				.getMolecule().getId());

		addRoleToNotes(msr, modifier.getRole());
		return msr;
	}

	private void addRoleToNotes(SBase ref, String role) {
		StringBuffer sb = new StringBuffer();
		sb.append("<p xmlns='http://www.w3.org/1999/xhtml'> Role:");
		sb.append(role);
		sb.append("</p>");
		ref.appendNotes(sb.toString());

	}

	private SpeciesReference createSpeciesReference(IRelation substrate) {
		SpeciesReference sr = new SpeciesReference(substrate.getMolecule()
				.getId(), substrate.getStoichiometry());
		addRoleToNotes(sr, substrate.getRole());
		return sr;
	}

	/*
	 * Currently put in notes as is only string
	 */
	private void setUpKineticLaw(Reaction sbmlReaction, IReaction reaction) {
		String kineticLaw = reaction.getKineticLaw();
		if (kineticLaw != null
				&& kineticLaw.trim().length() > 0) {
			ASTNode xml =null;
			if(kineticLaw.startsWith("<math")){
			 xml = libsbml
					.readMathMLFromString(kineticLaw);
			}else{
				xml = libsbml
				.parseFormula(kineticLaw);
			}
			KineticLaw law =new KineticLaw(xml);
			if(reaction.getParameters()!=null && reaction.getParameters().trim().length()>0){
				String[] pars=reaction.getParameters().split(";");
				for(String p:pars){
					String[] nv=p.split("=");
					law.addParameter(new Parameter(nv[0],nv[1]));
				}
			}
			System.out.println(law.toSBML());
			StringBuffer sb = new StringBuffer();
			sb.append("<p xmlns='http://www.w3.org/1999/xhtml'>");
			sb.append(kineticLaw);
			sb.append("</p>");
			law.appendNotes(sb.toString());
			sbmlReaction.setKineticLaw(law);
		}
	}

	private boolean reactionIsEmpty(IReaction reaction) {
		return reaction.getSubstrateList().isEmpty()
				&& reaction.getProductList().isEmpty();
	}

	/*
	 * Adds notes and annotations to the reaction object
	 */
	private void addAnnotationAndNotes(SBase sbmlObject,
			AnnotationBuilder builder) {
		String annotation = builder.buildAnnotation();
		sbmlObject.appendAnnotation(annotation);
		String notes = builder.buildNotes();
		sbmlObject.appendNotes(notes);
	}

}
