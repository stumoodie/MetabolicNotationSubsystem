package uk.ac.ed.inf.Metabolic.parser;

public class TestMacromolecule extends MetabolicMacromolecule {

	public TestMacromolecule(String id, String name,
			String asciiName) {
		super(id, name, asciiName);
	}

	public boolean addCompound(MetabolicCompound e) {
		return super.addCompound(e);
	}

	public boolean addSubunit(MetabolicMacromolecule e) {
		return super.addSubunit(e);
	}
	
}
