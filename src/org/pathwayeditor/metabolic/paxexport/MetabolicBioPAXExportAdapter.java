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
package org.pathwayeditor.metabolic.paxexport;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import org.biopax.paxtools.io.simpleIO.SimpleExporter;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.util.IllegalBioPAXArgumentException;
import org.pathwayeditor.metabolic.ExportAdapterCreationException;
import org.pathwayeditor.metabolic.IExportAdapter;
import org.pathwayeditor.metabolic.ndomAPI.IModel;


public class MetabolicBioPAXExportAdapter<N extends IModel>  implements IExportAdapter<N> {

	private boolean isTargetCreated;
	ModelFactory modelFactory       = new ModelFactory();
	private Model paxModel;

	
	public boolean isTargetCreated() {
		return isTargetCreated;
	}

	public void writeTarget(OutputStream stream) throws IOException {
		SimpleExporter exp=new SimpleExporter(modelFactory.getLevel());
		try {
			exp.convertToOWL(paxModel, stream);
		} catch (IllegalAccessException e) {
			throw new IOException("Error in BioPAX model writing: " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IOException("Error in BioPAX model writing" + e.getMessage());
		}
		stream.flush();
	}

	public void createTarget(N model) throws ExportAdapterCreationException {
		isTargetCreated = false; //reset
		if (model == null) {
			throw new IllegalArgumentException("model is null");
		}
		try{
		paxModel = modelFactory.createPaxModel(model);
		}catch(IllegalBioPAXArgumentException e){
			throw new ExportAdapterCreationException("Error in model creation",e);
		}
		isTargetCreated=true;
	}

}
