package uk.ac.ed.inf.Metabolic.parser;

import org.pathwayeditor.contextadapter.toolkit.ndom.NdomException;

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
