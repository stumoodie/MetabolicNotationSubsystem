package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.SBMLError;
import org.sbml.libsbml.libsbml;

import uk.ac.ed.inf.Metabolic.ExportAdapterCreationException;
import uk.ac.ed.inf.Metabolic.IExportAdapter;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;

class MetabolicSBMLExportAdapter<N extends IModel> implements IExportAdapter<N> {
	static {  
		
	    SBMLLibraryLoader.getInstance().loadLibrary();
	
	}
	
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
		// concession to testing
		if(document ==null) 
		   document = new SBMLDocument();
		Model sbmlModel = getModelFactory().createSBMLModel(document, model);
	    getEntityBuilder().buildSpeciesAndCompartments(sbmlModel, model);
		//addReactions(sbmlModel, model.getReactionList());
		System.out.println(libsbml.writeSBMLToString(document));
		if(document.checkConsistency() == 0)
			isTargetCreated = true;
		else {
			StringBuffer sb = new StringBuffer();
			for(int i=0; i< document.getNumErrors();i++) {
				SBMLError err = document.getError(i);
				sb.append(err.getMessage()).append("\n");
			}
			throw new ExportAdapterCreationException("Invalid SBML document created: \n" + sb.toString());
		}
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
		String s = libsbml.writeSBMLToString(document);
		stream.write(s.getBytes());
		
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
