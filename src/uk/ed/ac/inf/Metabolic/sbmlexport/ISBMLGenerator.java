package uk.ed.ac.inf.Metabolic.sbmlexport;

import org.pathwayeditor.businessobjectsAPI.IRootMapObject;
import org.sbml.libsbml.SBMLDocument;

 interface ISBMLGenerator {

	 SBMLDocument generateSBMLModel( IRootMapObject rmo);
}
