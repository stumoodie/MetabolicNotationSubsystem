package uk.ac.ed.inf.Metabolic.excelexport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

public class TestGenerator extends ExcelGenerator {

	public TestGenerator(IModel model, String templatePath)
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
