package uk.ed.ac.inf.Metabolic.sbmlexport;

import org.pathwayeditor.businessobjectsAPI.IRootMapObject;
import org.sbml.libsbml.Compartment;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.Species;
import org.sbml.libsbml.libsbml;

import com.sun.xml.internal.txw2.Document;

public class TestGenerator implements ISBMLGenerator {

	public SBMLDocument generateSBMLModel(IRootMapObject rmo) {
		SBMLDocument document = new SBMLDocument(2,3);
		Model m = document.createModel();
		Compartment c1 = m.createCompartment();
		c1.setId("c1");
		m.addCompartment(c1);
		Species sp1 = new Species();
		sp1.setId("ID");
		m.addSpecies(sp1);
		document.checkL2v3Compatibility();
		return document;
		
		

	}

}
