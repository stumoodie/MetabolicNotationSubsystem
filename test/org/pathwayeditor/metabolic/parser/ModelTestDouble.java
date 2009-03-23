package org.pathwayeditor.metabolic.parser;

import org.pathwayeditor.metabolic.parser.MetabolicCompartment;
import org.pathwayeditor.metabolic.parser.MetabolicCompound;
import org.pathwayeditor.metabolic.parser.MetabolicModel;
import org.pathwayeditor.metabolic.parser.MetabolicReaction;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.NdomException;

public class ModelTestDouble extends MetabolicModel 
{

	public ModelTestDouble(String id, String name, String asciiName) {
		super(id, name, asciiName);
	}

	public boolean addCompartment(MetabolicCompartment e) throws NdomException {
		return super.addCompartment(e);
	}

	public boolean addReaction(MetabolicReaction e) {
		return super.addReaction(e);
	}

	public void registerCompartment(MetabolicCompartment e) throws NdomException {
		super.registerCompartment(e);
	}

	public void registerCompound(MetabolicCompound c) {
		super.registerCompound(c);
	}
	
}
