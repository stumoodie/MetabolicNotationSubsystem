package uk.ac.ed.inf.Metabolic.excelexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.pathwayeditor.businessobjects.drawingprimitives.ICanvas;
import org.pathwayeditor.businessobjects.notationsubsystem.ExportServiceException;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationExportService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationValidationService;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationReport;
import org.pathwayeditor.businessobjects.repository.IMap;
import org.pathwayeditor.contextadapter.toolkit.validation.NotationValidationService;

import uk.ac.ed.inf.Metabolic.ExportAdapterCreationException;
import uk.ac.ed.inf.Metabolic.MetabolicNDOMValidationService;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

public class ExcelExportService implements INotationExportService {

	String DISPLAY_NAME = "Excel Export";

	String RECOMMENDED_SUFFIX = "xls";
	
	String PROPERTIES_LOCATION = "/templatepath.properties" ;

	final String TYPECODE = "Excel_Export_1.0.0";
	private INotation notation;
	private IExcelFileGenerator generator;
	private INotationSubsystem notationSubsystem;

	public ExcelExportService(INotationSubsystem provider) {
		this.notationSubsystem = provider;
		notation = provider.getNotation();
	}


	public INotationSubsystem getNotationSubsystem() {
		return notationSubsystem;
	}

	/**
	 * @throws IllegalArgumentException
	 *             if either argument is null
	 * @throws ExportServiceException
	 *             if exportFile:
	 *             <ul>
	 *             <li> Doesn't exist
	 *             <li> Is not writable.
	 *             <li> Cannot produce valid Excel
	 *             </ul>
	 */
	public void exportMap(ICanvas map, File exportFile) throws ExportServiceException {
		FileOutputStream fos = null;
		try {
			checkArgs(map, exportFile);

//	MetabolicContextValidationService validator = (MetabolicContextValidationService) notationSubsystem
//			.getValidationService();
	
			NotationValidationService validator = (NotationValidationService) notationSubsystem.getValidationService();
			validator.setMapToValidate(map);
			IModel ndom = null;
			validator.validateMap();
				
			IValidationReport report =validator.getValidationReport();//FIXME - NH what do we do with this now??
			if(!report.isMapValid()){
				String sb="Map is not valid:\n";
				throw new ExportServiceException(sb);
			}else{
				ndom=getModel(validator);
			}
//		System.out.println(validator.getValidationReport());
//		System.out.println(ndom.toString());
		
			generator = getGenerator( ndom );
		
			generator.createNewWorkbook();
		
			if ( !generator.wasWorkBookCreated() )
				throw new ExportServiceException ( "Workbook generator was not created propery" ) ;
		
			generator.populateModelPage();
			generator.populateCompoundPage();
			generator.populateReactionsPage();
			generator.populateMacromoleculePage();

			generator.saveWorkBook(exportFile);
		} catch (ExportAdapterCreationException e) {
			throw new ExportServiceException(e);
		} catch (IOException e) {
			throw new ExportServiceException(e);
		} catch(Exception e){
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
		if (validator.getValidationReport().isMapValid()) {
			return (IModel) MetabolicNDOMValidationService.getInstance(
					notationSubsystem).getNDOM();
		} else {
			return null;
		}
	}

	private void checkArgs(ICanvas canvas, File exportFile)
			throws ExportServiceException, IOException {

		if (canvas == null || exportFile == null
				|| canvas.getModel().getRootNode()== null) {
			throw new IllegalArgumentException("Arguments must not be null");
		}
		exportFile.createNewFile();

		if (!exportFile.canWrite()) {
			throw new ExportServiceException("File " + exportFile
					+ " is not writable");
		}
	}

	public String getCode() {
		return TYPECODE;
	}

	public INotation getNotation() {
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

	IExcelFileGenerator getGenerator(IModel model) throws ExportAdapterCreationException {
		try 
		{
			generator = new ExcelGenerator ( model , getTemplatePath () ) ;
			return generator;
		}
		catch ( IllegalArgumentException e)
		{
			throw new ExportAdapterCreationException () ;
		} 
		
		
	}
	
	private String getTemplatePath () throws ExportAdapterCreationException  
	{

		String path ;
		try {
			BufferedReader inputReader = new BufferedReader ( new InputStreamReader(this
		            .getClass().getClassLoader().getResourceAsStream(PROPERTIES_LOCATION))) ;
			
			try {
				path = inputReader.readLine() ;
			}
			finally
			{
				inputReader.close() ;
			}
		}
		catch ( IOException exc )
		{
			throw new ExportAdapterCreationException () ; 
		}
		
		return path;
	}

}

