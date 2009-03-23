package org.pathwayeditor.metabolic.parser;

import org.pathwayeditor.metabolic.parser.MetabolicCompartment;
import org.pathwayeditor.metabolic.parser.MetabolicCompound;
import org.pathwayeditor.metabolic.parser.MetabolicMacromolecule;
import org.pathwayeditor.metabolic.parser.MetabolicModel;
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
