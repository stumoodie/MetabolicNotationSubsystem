package org.pathwayeditor.metabolic.sbmlexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.pathwayeditor.businessobjects.drawingprimitives.ICanvas;
import org.pathwayeditor.businessobjects.notationsubsystem.ExportServiceException;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationExportService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationValidationService;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationReport;
import org.pathwayeditor.metabolic.ExportAdapterCreationException;
import org.pathwayeditor.metabolic.IExportAdapter;
import org.pathwayeditor.metabolic.MetabolicNDOMValidationService;
import org.pathwayeditor.metabolic.ndomAPI.IModel;


public class SBMLExportService implements INotationExportService {

	String DISPLAY_NAME = "SBML export L2v3";

	String RECOMMENDED_SUFFIX = "sbml";

	final String TYPECODE = "MetSBML_1.0.0";
	private INotation notation;
	private IExportAdapter<IModel> generator;
	private INotationSubsystem notationSubSystem;

	public SBMLExportService(INotationSubsystem provider) {
		this.notationSubSystem = provider;
		notation = provider.getNotation();
	}


	public INotationSubsystem getNotationSubsystem() {
		return notationSubSystem;
	}

	/**
	 * @throws IllegalArgumentException
	 *             if either argument is null
	 * @throws ExportServiceException
	 *             if exportFile:
	 *             <ul>
	 *             <li> Doesn't exist
	 *             <li> Is not writable.
	 *             <li> Cannot produce valid SBML
	 *             </ul>
	 */
	public void exportMap(ICanvas map, File exportFile) throws ExportServiceException {
		if(map == null || exportFile == null){
			throw new IllegalArgumentException("parameters map or exportFile canot be null");
		}
		FileOutputStream fos = null;
		try {
			checkArgs(map, exportFile);

			generator = getGenerator();//new MetabolicSBMLExportAdapter<IModel>();

			INotationValidationService validator =  notationSubSystem.getValidationService();
			validator.setCanvasToValidate(map);
			IModel ndom = null;
			if (validator.isReadyToValidate()) {
				validator.validate();
				IValidationReport report =validator.getValidationReport();
				if(!report.isValid()){
					String sb="Map is not valid:\n";
					
					throw new ExportServiceException(sb);
				}else {
					ndom=getModel(validator);
				
			
				generator.createTarget(ndom);
				
				fos = new FileOutputStream(exportFile);
				generator.writeTarget(fos);
				}
			}
		} catch (ExportAdapterCreationException e) {
			throw new ExportServiceException(e);
		} catch (IOException e) {
			throw new ExportServiceException(e);
		} catch (UnsatisfiedLinkError e){
			throw new ExportServiceException(e);
		} catch (RuntimeException e){
			throw new ExportServiceException(e);
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (Exception e) {
			}

		}

	}
	
	IModel getModel(INotationValidationService validator) {
		if(validator.getValidationReport().isValid()){
		return (IModel) MetabolicNDOMValidationService.getInstance().getNDOM();
		}else{
			return null;
		}
	}

	private void checkArgs(ICanvas map, File exportFile)
			throws ExportServiceException, IOException {

		if (map == null || exportFile == null
				|| map.getModel().getRootNode() == null) {
			throw new IllegalArgumentException("Arguments must not be null");
		}
        // yes yt is, export fails without it
//		exportFile.createNewFile();

		File parent = exportFile.getParentFile();
		if (parent != null && !parent.canWrite()) {
			throw new ExportServiceException("Directory " + parent	+ " is not writable");
		}
	}

	public String getCode() {
		return TYPECODE;
	}

	public INotation getNotation(){
		return notation;
	}

	public String getDisplayName() {
		return DISPLAY_NAME;
	}

	public String getRecommendedSuffix() {
		return RECOMMENDED_SUFFIX;
	}

	public String toString() {
		return new StringBuffer().append("Export service for validationService :")
				.append(notation.toString()).append("\n Display name :").append(
						DISPLAY_NAME).append("\n Code: ").append(TYPECODE)
				.toString();
	}

	IExportAdapter<IModel> getGenerator() {
			generator = new MetabolicSBMLExportAdapter<IModel>();
		return generator;
	}

}
