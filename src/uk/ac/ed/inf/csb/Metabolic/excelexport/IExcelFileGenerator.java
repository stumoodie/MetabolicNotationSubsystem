package uk.ac.ed.inf.csb.Metabolic.excelexport;

import java.io.File;
import java.io.IOException;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

interface IExcelFileGenerator {
	
	/**
	 * Performs a validation check on the loaded template. This is done to safeguard us for using a false or 
	 * inappropriately loaded template file. 
	 * @return true if the template is valid
	 */
	boolean isTemplateValid () ;
	
	/**
	 * Returns a String representation of the path of the TemplateFile. There will always be a template file.
	 * @return the path of the template file, cannot be null. 
	 */
	String getTemplateLocation () ;
	
	/**
	 * Get the NDOM that is being written to the excel file.
	 * @return The NDOM, cannot be null.
	 */
	IModel getMetabolicNDOM();
	
	/**
	 * Creates a new workbook based on the template provided. Requires that the template
	 * file location is valid.
	 * @throws IllegalStateException thrown if <code>isTemplateValid() == false</code>.
	 */
	void createNewWorkbook ();
	
	/**
	 * Tests if the workbook has been created.
	 * @return true if it has, false otherwise.
	 */
	boolean wasWorkBookCreated();
	
	/**
	 * Populate the Model worksheet with **What Data? Nikos you should say what data is being written
	 *  here**. The workbook must be created before this method id called. 
	 * @throws IllegalStateException thrown if <code>wasWorkBookCreated() == false</code>.
	 */
	void populateModelPage ();
	
	/**
	 * Responsible for the insertion of data from the model to the Model page of the template.
	 * In order for this method to complete needs an already instantiated model and a valid workbook.
	 * @throws IllegalStateException in the case that the validation of the template fails.
	 * @throws NullPointerException in the case that the model is null.
	 */
	void populateCompoundPage () throws IllegalStateException, NullPointerException ;
	
	/**
	 * Responsible for the insertion of data from the model to the Model page of the template.
	 * In order for this method to complete needs an already instantiated model and a valid workbook.
	 * @throws IllegalStateException in the case that the validation of the template fails.
	 * @throws NullPointerException in the case that the model is null.
	 */
	void populateMacromoleculePage () throws IllegalStateException, NullPointerException ;
	
	/**
	 * Responsible for the insertion of data from the model to the Model page of the template.
	 * In order for this method to complete needs an already instantiated model and a valid workbook.
	 * @throws IllegalStateException in the case that the validation of the template fails.
	 * @throws NullPointerException in the case that the model is null.
	 */
	void populateReactionsPage () throws IllegalStateException, NullPointerException ;
	
	/**
	 * Writes the workbook in the given file. The target cannot be null. 
 	// ***** What happens if the target is null ***
 	 * @param target the file in which the workbook is going to be written in.
	// ** No the workbook need not be populated so we don't need this exception **
	 * @throws IllegalStateException is thrown in the case that the workbook is not populated.
	// ***  What does invalid mean? ***
	 * @throws IllegalArgumentException in the case the target is invalid.
	 * @throws IOException  	** DOCUMENT THIS EXCEPTION **
	 */
	void saveWorkBook ( File target ) throws IllegalStateException, IllegalArgumentException,  IOException ;
	

}
