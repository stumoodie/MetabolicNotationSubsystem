package uk.ac.ed.inf.Metabolic.excelexport;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CellManipulator {
	
	private HSSFCellStyle style ;
	private HSSFCellStyle headerStyle ;
//	private HSSFWorkbook workbook ;
	
	public CellManipulator ( HSSFWorkbook workbook )
	{
//		this.workbook = workbook ;
		style = workbook.createCellStyle() ;
		
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN) ;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN) ;
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM) ;
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM) ;
		
		headerStyle = workbook.createCellStyle() ;
		
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM) ;
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM) ;
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM) ;
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM) ;
	}
	
	public void putStringInHeaderRow ( HSSFRow row , int index , String data )
	{
		HSSFCell cell = addStringetoCell (  row ,  index ,  data ) ;
		cell.setCellStyle(headerStyle ) ;
		
	}
	
	public String convertToRTF ( String string)
	{
//		ITextProperty textProperty = TextProperty.createTextPropertyFromString ( string , null ) ;
//		
//		return textProperty.getString() ;
	return string;//FIXME NH what happened to this functionality?
	}
	
	
	public void putStringInNormalRow ( HSSFRow row , int index , String data )
	{
		HSSFCell cell = addStringetoCell (  row ,  index ,  data ) ;
		cell.setCellStyle(style) ;
	}


	public void putDoubleInRow(HSSFRow row, int index, double number) {
		
		HSSFCell cell ;
		
		cell = row.getCell((short) index ) ;
		
		if ( cell == null )
		{
			cell = row.createCell((short) index ) ;
		}
		
		cell.setCellValue( number ) ;
		
		cell.setCellStyle(style) ;
	}


	public void putIntegerInRow(HSSFRow row, int index, int number) {
		HSSFCell cell ;
		
		cell = row.getCell((short) index ) ;
		
		if ( cell == null )
		{
			cell = row.createCell((short) index ) ;
		}
		
		cell.setCellValue( number ) ;
		cell.setCellStyle(style) ;
	}
	
	
	private HSSFCell addStringetoCell ( HSSFRow row , int index , String data )
	{
		HSSFCell cell ;
		
		cell = row.getCell((short) index ) ;
		
		if ( cell == null )
		{
			cell = row.createCell((short) index ) ;
		}
		
		cell.setCellValue( new HSSFRichTextString ( convertToRTF ( data ) ) ) ;
		
		return cell ;
	}
	
	

}
