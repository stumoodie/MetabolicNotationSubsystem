package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.sbml.libsbml.Model;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.libsbml;

import uk.ac.ed.inf.Metabolic.ExportAdapterCreationException;
import uk.ac.ed.inf.Metabolic.IExportAdapter;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;

class MetabolicSBMLExportAdapter<N extends IModel> implements IExportAdapter<N> {
	
	final long SPATIAL_DIMENSIONS = 3;
	private boolean isTargetCreated = false;
	
	//package protected to allow access from tests
	SBMLDocument document;
	
	IEntityFactory entityFactory;
	IModelFactory modelFactory;
	

	public void createTarget(IModel model) throws ExportAdapterCreationException {
		
		if (model == null) {
			throw new IllegalArgumentException("model is null");
		}
		document = new SBMLDocument();
		Model sbmlModel = getModelFactory().createSBMLModel(document, model);
	    getEntityBuilder().buildSpeciesAndCompartments(sbmlModel, model);
		//addReactions(sbmlModel, model.getReactionList());
		System.out.println(libsbml.writeSBMLToString(document));
		if(document.checkL2v3Compatibility() == 0)
			isTargetCreated = true;
	}
    
	
	private void addReactions(Model sbmlModel, List<IReaction> reactionList) {
		for (IReaction reaction: reactionList) {
			Reaction sbmlReaction = sbmlModel.createReaction();
			sbmlReaction.setId(reaction.getId());
			sbmlReaction.setName(reaction.getASCIIName());
			
		}
		
	}

	public boolean isTargetCreated() {
		return isTargetCreated;
	}

	public void writeTarget(OutputStream stream) throws IOException {
		if (stream == null) {
			throw new IllegalArgumentException("output stream is null");
		}
		if (!isTargetCreated()) {
			throw new IllegalStateException("Target not created");
		}
		

	}


	IEntityFactory getEntityBuilder() {
		if(entityFactory == null) {
			entityFactory = new EntityBuilder();
		}
		return entityFactory;
	}


	void setEntityBuilder(IEntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}


	IModelFactory getModelFactory() {
		if(modelFactory == null) {
			modelFactory = new ModelFactory();
		}
		return modelFactory;
	}


	void setModelFactory(IModelFactory modelFactory) {
		this.modelFactory = modelFactory;
	}

}
