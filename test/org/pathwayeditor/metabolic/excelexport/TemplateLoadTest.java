/*
  Licensed to the Court of the University of Edinburgh (UofE) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The UofE licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
*/
package org.pathwayeditor.metabolic.excelexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pathwayeditor.metabolic.excelexport.IExcelFileGenerator;
import org.pathwayeditor.metabolic.parser.ModelTestDouble;




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
