
package uk.ac.ed.inf.csb.Metabolic.excelexport;

import java.io.File;
import java.io.IOException;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

	/**
	 * Thin interface handles the export of a given Metabolic NDOM to an excel formated file. The exact format of the
	 * file is taken from a predefined template. The interface is rensposible for providing the set of methods to the subclasses
	 * that will load the template, construct a new Excel Document and fill the document with data that is extracted from the 
	 * Metabolic NDOM provided.
	 * Both the NDOM and the path of the template file should be passed on the constructor of any subclass of this interface.
	 * 
	 * @author ntsorman
	 *
	 */
interface IExcelFileGenerator {
	
	/**
	 * Performs a validation check on the loaded template. This is done to safeguard us for using a false or 
	 * inappropriately loaded template file. 
	 * @return true if the template is valid, false otherwise.
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
	void createNewWorkbook () throws IllegalStateException;
	
	/**
	 * Tests if the workbook has been created.
	 * @return true if it has, false otherwise.
	 */
	boolean wasWorkBookCreated();
	
	/**
	 * Populate the Model worksheet with the data that is extracted from the Metabolic NMOD and has to do with the 
	 *  Map Details and the Compartments. The workbook must be created before this method is called. 
	 * @throws IllegalStateException thrown if <code>wasWorkBookCreated() == false</code>.
	 */
	void populateModelPage () throws IllegalStateException;
	
	/**
	 * Populate the Compound worksheet with the data that is extracted from the Metabolic NMOD and has to do with the 
	 *  Compounds. The workbook must be created before this method is called. 
	 * @throws IllegalStateException thrown if <code>wasWorkBookCreated() == false</code>.
	 */
	void populateCompoundPage () throws IllegalStateException ;
	
	/**
	 * Populate the Macromolecule worksheet with the data that is extracted from the Metabolic NMOD and has to do with the 
	 *  Macromolecule. The workbook must be created before this method is called. 
	 * @throws IllegalStateException thrown if <code>wasWorkBookCreated() == false</code>.
	 */
	void populateMacromoleculePage () throws IllegalStateException ;
	
	/**
	 * Populate the Reactions worksheet with the data that is extracted from the Metabolic NMOD and has to do with the 
	 *  Reactions. The workbook must be created before this method is called. 
	 * @throws IllegalStateException thrown if <code>wasWorkBookCreated() == false</code>.
	 */
	void populateReactionsPage () throws IllegalStateException ;
	
	/**
	 * Writes the workbook in the given file. The target file cannot be null. 
 	 * @param target the file in which the workbook is going to be written in.
	 * @throws IllegalArgumentException in the case the target is null.
	 * @throws IOException in the case there was an error while writing the file.
	 */
	void saveWorkBook ( File target ) throws IllegalArgumentException,  IOException ;
	

}
