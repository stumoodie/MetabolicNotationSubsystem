package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.IOException;
import java.io.OutputStream;

import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.SBMLError;
import org.sbml.libsbml.libsbml;
import org.sbml.libsbml.libsbmlConstants;

import uk.ac.ed.inf.Metabolic.ExportAdapterCreationException;
import uk.ac.ed.inf.Metabolic.IExportAdapter;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

class MetabolicSBMLExportAdapter<N extends IModel> implements IExportAdapter<N> {
	private static final String POSS_SBML_LIBS[] = { "xml2", "expat", "sbml", "sbmlj" };
	private static final String LIB_PREFIX = "lib";

	static {
		jniSharedLibLoader();
	}
		
	final long SPATIAL_DIMENSIONS = 3;
	private boolean isTargetCreated = false;
	
	//package protected to allow access from tests
	SBMLDocument document;
	
	IEntityFactory entityFactory     = new EntityBuilder();
	IModelFactory modelFactory       = new ModelFactory();
	IReactionBuilder reactionFactory = new SBMLReactionFactory();;
	

	static void jniSharedLibLoader(){
		try{
			// this should work, but due to a bug with eclipse plugin packaging mechanisms it will currently only work
			// on some O/Ss if the shared library path environment variable points to the sbml libs. This
			// must be set BEFORE the app is executed.
			System.loadLibrary("sbmlj");
		}
		catch(UnsatisfiedLinkError e1){
			// our fallback is to load all the poss sbml libs in dependency order on Windows it expects some
			// libs to be prefixed with a lib, Unix like O/Ss automatically prefix with lib so we need to try both forms
			for(String libStub : POSS_SBML_LIBS){
				loadLib(libStub);
			}
		}	
	}
	
	static void loadLib(String libStub){
		try{
			// try loading using just the stub name 
			System.loadLibrary(libStub);
		}
		catch(UnsatisfiedLinkError e2){
			// load with a lib prefix
			StringBuilder buf = new StringBuilder(LIB_PREFIX);
			buf.append(libStub);
			System.loadLibrary(buf.toString());
			// if this fails then we cannot load the library and we should let things take there course.
		}
	}

	public void createTarget(IModel model) throws ExportAdapterCreationException {
		isTargetCreated = false; //reset
		if (model == null) {
			throw new IllegalArgumentException("model is null");
		}
//		if(!isLibraryLoaded && !LibSBMLLoader.getInstance().loadLibrary()) {
//			throw new ExportAdapterCreationException("Could not load libSBML - only supported on Windows at present");
//		}
		// concession to testing
		if(document ==null) 
		   document = new SBMLDocument();
		Model sbmlModel = modelFactory.createSBMLModel(document, model);
	    entityFactory.buildSpeciesAndCompartments(sbmlModel, model);
		reactionFactory.buildReactions(sbmlModel, model);
		document.setConsistencyChecks(libsbmlConstants.LIBSBML_CAT_UNITS_CONSISTENCY, false);
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
	


}
