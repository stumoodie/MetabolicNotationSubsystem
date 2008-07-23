package uk.ac.ed.inf.Metabolic.excelexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ed.inf.Metabolic.parser.ModelTestDouble;



public class TemplateLoadTest {
	
	IExcelFileGenerator generator ;
	ModelTestDouble NDOModel ;
	@Before
	public void setUp() throws Exception {
		
//		File file = new File ( "." ) ;
		
//		String [] a = file.list() ;
		
		NDOModel = new ModelTestDouble ("id", "name", "asciiName") ;
		
		generator = new ExcelGeneratorTestStub ( NDOModel , "Template.xls") ;
		
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
