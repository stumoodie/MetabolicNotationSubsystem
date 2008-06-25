package uk.ac.ed.inf.Metabolic.parser;

import uk.ac.ed.inf.Metabolic.ndomAPI.IRelation;

public class TestCompound extends MetabolicCompound 
{

	public TestCompound(String id, String name, String asciiName) {
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
