package org.pathwayeditor.metabolic.excelexport;

import java.io.IOException;
import java.io.OutputStream;

import org.pathwayeditor.metabolic.ExportAdapterCreationException;
import org.pathwayeditor.metabolic.IExportAdapter;
import org.pathwayeditor.metabolic.ndomAPI.IModel;


public class MetabolicExcelExportAdapter<N extends IModel> implements IExportAdapter<IModel> {

	private IExcelFileGenerator generator ;
	
	public void createTarget(IModel model)
			throws ExportAdapterCreationException {
		
		try 
		{
			generator = new ExcelGenerator ( model , "../uk.ac.ed.inf.csb.Metabolic/Template.xls" ) ;
		}
		catch ( IllegalArgumentException e)
		{
			throw new ExportAdapterCreationException () ;
		}

	}

	public boolean isTargetCreated() {
		if ( generator == null )
		return false;
		
		return true ;
	}

	public void writeTarget(OutputStream stream) throws IOException {
		

	}

}
