package uk.ac.ed.inf.Metabolic.sbmlexport;

import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

public interface IModelFactory {

	Model createSBMLModel(SBMLDocument document,IModel model);
}
