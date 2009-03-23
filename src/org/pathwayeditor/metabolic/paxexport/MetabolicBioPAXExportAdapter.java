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
