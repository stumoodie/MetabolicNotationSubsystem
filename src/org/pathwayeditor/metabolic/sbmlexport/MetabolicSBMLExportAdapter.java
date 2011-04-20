/*
  Licensed to the Court of the University of Edinburgh (UofE) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The UofE licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
*/
package org.pathwayeditor.metabolic.sbmlexport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.pathwayeditor.metabolic.ExportAdapterCreationException;
import org.pathwayeditor.metabolic.IExportAdapter;
import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.pathwayeditor.metabolic.util.SharedLibLoader;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.SBMLError;
import org.sbml.libsbml.libsbml;
import org.sbml.libsbml.libsbmlConstants;


class MetabolicSBMLExportAdapter<N extends IModel> implements IExportAdapter<N> {
	private static final List<String> MAND_SBML_LIBS = Arrays.asList(new String[]{ "sbml", "sbmlj" });
	private static final List<String> OPT_SBML_LIBS = Arrays.asList(new String[]{ "xml2", "expat"});
	private static final String ROOT_SBML_LIB = "sbmlj";

	static {
		SharedLibLoader.createInstance(ROOT_SBML_LIB, MAND_SBML_LIBS, OPT_SBML_LIBS).loadRootLibAndFallback();
	}
		
	final long SPATIAL_DIMENSIONS = 3;
	private boolean isTargetCreated = false;
	
	//package protected to allow access from tests
	SBMLDocument document;
	
	IEntityFactory entityFactory     = new EntityBuilder();
	IModelFactory modelFactory       = new ModelFactory();
	IReactionBuilder reactionFactory = new SBMLReactionFactory();;
	

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
