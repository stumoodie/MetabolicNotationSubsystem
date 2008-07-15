package uk.ac.ed.inf.Metabolic.parser;

import org.pathwayeditor.contextadapter.toolkit.ndom.NdomException;


public class TestCompartment extends MetabolicCompartment {

	public boolean addChildCompartment(MetabolicCompartment e) throws NdomException {
		return super.addChildCompartment(e);
	}

	public boolean addCompound(MetabolicCompound e) {
		return super.addCompound(e);
	}

	public boolean addMacromolecule(MetabolicMacromolecule e) {
		return super.addMacromolecule(e);
	}

	public TestCompartment(String id, String name, String asciiName,
			MetabolicModel m) {
		super(id, name, asciiName, m);
	}

}
