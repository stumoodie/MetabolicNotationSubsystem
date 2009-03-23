package org.pathwayeditor.metabolic.sbmlexport;

import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;


public interface IModelFactory {

	Model createSBMLModel(SBMLDocument document,IModel model);
}
