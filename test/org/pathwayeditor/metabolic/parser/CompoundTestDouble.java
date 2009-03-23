package org.pathwayeditor.metabolic.parser;

import org.pathwayeditor.metabolic.ndomAPI.IRelation;
import org.pathwayeditor.metabolic.parser.MetabolicCompound;

public class CompoundTestDouble extends MetabolicCompound 
{

	public CompoundTestDouble(String id, String name, String asciiName) {
		super(id, name, asciiName);
		// TODO Auto-generated constructor stub
	}

	public boolean addActivatoryRelation(IRelation rel) {
		return super.addActivatoryRelation(rel);
	}

	public boolean addCatalyticRelation(IRelation rel) {
		return super.addCatalyticRelation(rel);
	}

	public boolean addInhibitoryRelation(IRelation rel) {
		return super.addInhibitoryRelation(rel);
	}

	public boolean addSink(IRelation rel) {
		return super.addSink(rel);
	}

	public boolean addSsource(IRelation rel) {
		return super.addSsource(rel);
	}
	
}
