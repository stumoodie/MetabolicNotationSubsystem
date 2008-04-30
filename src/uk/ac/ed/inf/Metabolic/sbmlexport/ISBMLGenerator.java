package uk.ac.ed.inf.Metabolic.sbmlexport;

import org.pathwayeditor.businessobjectsAPI.IRootMapObject;
import org.sbml.libsbml.SBMLDocument;

 public interface ISBMLGenerator {

	 SBMLDocument generateSBMLModel( IRootMapObject rmo);
}
