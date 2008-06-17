package uk.ac.ed.inf.csb.Metabolic.excelexport;

import java.io.File;
import java.io.IOException;

public interface IExcelFileGenerator {
	
	/**
	 * Performs a validation check on the loaded template. This is done to safeguard us for using a false or 
	 * inappropriately loaded template file. 
	 * @return true if the template is valid
	 */
	public boolean isTemplateValid () ;
	
	/**
	 * Returns a String representation of the path of the TemplateFile. In the case that no such file exists, 
	 * the value returned is null.
	 * @return the path of the template file. 
	 */
	public String getTemplateLocation () ;
	
	/**
	 * Creates a new workbook based on the template that is loaded and populates it with the data from the model.
	 *In order for the method to be completed successfully, a check if the template is loaded properly is performed
	 *as well as if the model is properly instantiated.
	 * @throws IllegalStateException in the case that the validation of the template fails.
	 * @throws NullPointerException in the case that the model is null.
	 */
	public void createNewWorkbook () throws IllegalStateException, NullPointerException ;
	
	/**
	 * Responsible for the insertion of data from the model to the Model page of the template.
	 * In order for this method to complete needs an already instantiated model and a valid workbook.
	 * @throws IllegalStateException in the case that the validation of the template fails.
	 * @throws NullPointerException in the case that the model is null.
	 */
	public void populateModelPage () throws IllegalStateException, NullPointerException ;
	
	/**
	 * Responsible for the insertion of data from the model to the Model page of the template.
	 * In order for this method to complete needs an already instantiated model and a valid workbook.
	 * @throws IllegalStateException in the case that the validation of the template fails.
	 * @throws NullPointerException in the case that the model is null.
	 */
	public void populateCompoundPage () throws IllegalStateException, NullPointerException ;
	
	/**
	 * Responsible for the insertion of data from the model to the Model page of the template.
	 * In order for this method to complete needs an already instantiated model and a valid workbook.
	 * @throws IllegalStateException in the case that the validation of the template fails.
	 * @throws NullPointerException in the case that the model is null.
	 */
	public void populateMacromoleculePage () throws IllegalStateException, NullPointerException ;
	
	/**
	 * Responsible for the insertion of data from the model to the Model page of the template.
	 * In order for this method to complete needs an already instantiated model and a valid workbook.
	 * @throws IllegalStateException in the case that the validation of the template fails.
	 * @throws NullPointerException in the case that the model is null.
	 */
	public void populateReactionsPage () throws IllegalStateException, NullPointerException ;
	
	/**
	 * Writes the workbook in the given file. The target cannot be null. 
	 * @param target the file in which the workbook is going to be written in.
	 * @throws IllegalStateException is thrown in the case that the workbook is not populated.
	 * @throws IllegalArgumentException in the case the target is invalid.
	 * @throws IOException  	
	 */
	public void saveWorkBook ( File target ) throws IllegalStateException, IllegalArgumentException,  IOException ;
	

}
