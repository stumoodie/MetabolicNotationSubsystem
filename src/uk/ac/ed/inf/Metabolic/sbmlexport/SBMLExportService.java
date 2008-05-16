package uk.ac.ed.inf.Metabolic.sbmlexport;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.pathwayeditor.businessobjectsAPI.IMap;
import org.pathwayeditor.contextadapter.publicapi.ExportServiceException;
import org.pathwayeditor.contextadapter.publicapi.IContext;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterExportService;

import uk.ac.ed.inf.Metabolic.ExportAdapterCreationException;
import uk.ac.ed.inf.Metabolic.IExportAdapter;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

public class SBMLExportService implements IContextAdapterExportService {
	
     String DISPLAY_NAME = "SBML exportL2v3";
    
    String RECOMMENDED_SUFFIX = "sbml";
    
    final String TYPECODE= "MetSBML_1.0.0";
    private IContext context;
    private IExportAdapter<IModel>generator;
    
	public SBMLExportService(IContext context) {
		super();
		this.context = context;
		
	}

	/**
	 * @throws IllegalArgumentException if either argument is null
	 * @throws ExportServiceException if exportFile: <ul>
	 * <li> Doesn't exist
	 *  <li> Is not writable.
	 *  </ul>
	 */
	public void exportMap(IMap map, File exportFile) throws ExportServiceException {
		FileOutputStream fos = null;
		try{
		checkArgs(map, exportFile);
		
		generator = new MetabolicSBMLExportAdapter<IModel>();
		
		    
		    // replace this with real model from validation step
			generator.createTarget(new DummyModel ()); 
			fos = new FileOutputStream(exportFile);
			generator.writeTarget(fos);
		} catch (ExportAdapterCreationException e) {
			throw new ExportServiceException(e);
		} catch (IOException e) {
			throw new ExportServiceException(e);
		}finally{
			try {
				if(fos!=null)
					fos.close();	
			}catch(Exception e){}
			
		}
		  

	}

	

	private void checkArgs(IMap map, File exportFile) throws ExportServiceException, IOException {

		 
		if (map == null || exportFile ==null || map.getTheSingleRootMapObject() == null) {
			throw new IllegalArgumentException("Arguments must not be null");
		}
		 exportFile.createNewFile();
		
		if(!exportFile.canWrite()) {
			throw new ExportServiceException ("File " + exportFile + " is not writable");
		}
	}

	public String getCode() {
		return TYPECODE;
	}

	public IContext getContext() {
		return context;
	}

	public String getDisplayName() {
		return DISPLAY_NAME;
	}

	public String getRecommendedSuffix() {
		return RECOMMENDED_SUFFIX;
	}
	
	public String toString () {
		return new StringBuffer().append("Export service for context :")
		                         .append(context.toString())
		                         .append("\n Display name :").append(DISPLAY_NAME)
		                         .append("\n Code: ").append(TYPECODE)
		                         .toString();
	}

}
