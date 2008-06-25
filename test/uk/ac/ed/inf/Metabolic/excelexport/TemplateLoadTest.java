package uk.ac.ed.inf.Metabolic.excelexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ed.inf.Metabolic.parser.TestModel;



public class TemplateLoadTest {
	
	IExcelFileGenerator generator ;
	TestModel NDOModel ;
	@Before
	public void setUp() throws Exception {
		
		File file = new File ( "." ) ;
		
		String [] a = file.list() ;
		
		NDOModel = new TestModel ("id", "name", "asciiName") ;
		
		generator = new TestGenerator ( NDOModel , "Template.xls") ;
		
		generator.createNewWorkbook() ;

	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testIfTemplateisValid()
	{
		assertTrue ( generator.isTemplateValid()) ;
	}
	
	@Test
	public void testIfWorkbookIsCreatedProperly ()
	{
		assertTrue ( generator.wasWorkBookCreated() ) ;
	}
	
	@Test
	public void testIfNDOMIsReturned ()
	{
		assertEquals ( NDOModel , generator.getMetabolicNDOM()  ) ;
	}
	
	@Test
	public void testIfPathIsReturned ()
	{
		assertEquals ( "Template.xls" ,generator.getTemplateLocation()) ;
	}
}
