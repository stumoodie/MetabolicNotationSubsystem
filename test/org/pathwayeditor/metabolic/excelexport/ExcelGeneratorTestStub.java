package org.pathwayeditor.metabolic.excelexport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.pathwayeditor.metabolic.excelexport.ExcelGenerator;
import org.pathwayeditor.metabolic.ndomAPI.IModel;


public class ExcelGeneratorTestStub extends ExcelGenerator {

	public ExcelGeneratorTestStub(IModel model, String templatePath)
			throws IllegalArgumentException {
		super(model, templatePath);

	}

	@Override
	protected HSSFWorkbook loadTemplateFromPath() throws IOException {
		
		InputStream inputStream = new FileInputStream ( templatePath ) ;
		
		try {
        POIFSFileSystem fs;
		fs = new POIFSFileSystem( inputStream );
		return new HSSFWorkbook(fs);
		}
		finally {
			inputStream.close() ;
		}

	}
	
	
	

}
