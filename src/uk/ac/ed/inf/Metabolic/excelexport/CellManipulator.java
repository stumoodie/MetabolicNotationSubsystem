package uk.ac.ed.inf.Metabolic.excelexport;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.pathwayeditor.businessobjectsAPI.ITextProperty;
import org.pathwayeditor.businessobjectsAPI.TextProperty;

public class CellManipulator {
	
	public String convertToRTF ( String string)
	{
		ITextProperty textProperty = TextProperty.createTextPropertyFromString ( string , null ) ;
		
		return textProperty.getString() ;
	}
	
	
	public void putStringInRow ( HSSFRow row , int index , String data )
	{
		HSSFCell cell ;
		
		cell = row.getCell((short) index ) ;
		
		if ( cell == null )
		{
			cell = row.createCell((short) index ) ;
		}
		
		cell.setCellValue( new HSSFRichTextString ( convertToRTF ( data ) ) ) ;
	}


	public void putDoubleInRow(HSSFRow row, int index, double number) {
		
		HSSFCell cell ;
		
		cell = row.getCell((short) index ) ;
		
		if ( cell == null )
		{
			cell = row.createCell((short) index ) ;
		}
		
		cell.setCellValue( number ) ;
	}


	public void putIntegerInRow(HSSFRow row, int index, int number) {
		HSSFCell cell ;
		
		cell = row.getCell((short) index ) ;
		
		if ( cell == null )
		{
			cell = row.createCell((short) index ) ;
		}
		
		cell.setCellValue( number ) ;
	}
		
	

}
