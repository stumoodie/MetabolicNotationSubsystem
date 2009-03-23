package org.pathwayeditor.metabolic.sbmlexport;

import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;


class ModelFactory implements IModelFactory {

	public Model createSBMLModel( SBMLDocument document, IModel model) {
		Model sbmlmodel = document.createModel(model.getId());
		sbmlmodel.setName(model.getASCIIName());
		AnnotationBuilder modelAnnotationBuilder = new ModelAnnotationBuilder(model);
		String notes = modelAnnotationBuilder.buildNotes();
		sbmlmodel.appendNotes(notes);
		return sbmlmodel;
	}

}
