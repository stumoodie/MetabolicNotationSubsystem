package uk.ac.ed.inf.Metabolic.parser;

import org.pathwayeditor.notationsubsystem.toolkit.ndom.NdomException;


public class MetabolicCompartmentTestStub extends MetabolicCompartment {

	public boolean addChildCompartment(MetabolicCompartment e) throws NdomException {
		return super.addChildCompartment(e);
	}

	public boolean addCompound(MetabolicCompound e) {
		return super.addCompound(e);
	}

	public boolean addMacromolecule(MetabolicMacromolecule e) {
		return super.addMacromolecule(e);
	}

	public MetabolicCompartmentTestStub(String id, String name, String asciiName,
			MetabolicModel m) {
		super(id, name, asciiName, m);
	}

}
