package org.pathwayeditor.metabolic.parser;

import org.pathwayeditor.metabolic.parser.MetabolicReaction;
import org.pathwayeditor.metabolic.parser.MetabolicRelation;

public class ReactionTestDouble extends MetabolicReaction 
{

	public ReactionTestDouble(String id, String name, String asciiName) {
		super(id, name, asciiName);
	}

	public boolean addActivator(MetabolicRelation rel) {
		return super.addActivator(rel);
	}

	public boolean addCatalyst(MetabolicRelation rel) {
		return super.addCatalyst(rel);
	}

	public boolean addInhibitor(MetabolicRelation rel) {
		return super.addInhibitor(rel);
	}

	public boolean addProduct(MetabolicRelation rel) {
		return super.addProduct(rel);
	}

	public boolean addSubstrate(MetabolicRelation rel) {
		return super.addSubstrate(rel);
	}
}
