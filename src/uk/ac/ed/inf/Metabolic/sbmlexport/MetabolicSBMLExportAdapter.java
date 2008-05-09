package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.IOException;
import java.io.OutputStream;

import uk.ac.ed.inf.Metabolic.ExportAdapterCreationException;
import uk.ac.ed.inf.Metabolic.IExportAdapter;

 class MetabolicSBMLExportAdapter <IModel>implements IExportAdapter <IModel>{
    boolean isTargetCreated = false;
    
	public void createTarget(IModel model) throws ExportAdapterCreationException {
	if(model == null) {
		throw new IllegalArgumentException("model is null");
	}
      isTargetCreated = true;
	}

	public boolean isTargetCreated() {
		return isTargetCreated;
	}

	public void writeTarget(OutputStream stream) throws IOException {
		if(stream == null) {
			throw new IllegalArgumentException("output stream is null");
		}
		if(!isTargetCreated()){
			throw new IllegalStateException("Target not created");
		}

	}

}
