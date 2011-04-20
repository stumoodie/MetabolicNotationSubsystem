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
package org.pathwayeditor.metabolic.sbmlexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.pathwayeditor.metabolic.sbmlexport.IModelFactory;
import org.pathwayeditor.metabolic.sbmlexport.ModelFactory;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;


@RunWith(JMock.class)
public class ModelFactoryTest {
	Mockery mockery = new JUnit4Mockery();
	IModelFactory factory;
	SBMLDocument document;
	
	static {
		System.loadLibrary("sbmlj");
	}
	
	@Before
	public void setUp() throws Exception {
		factory = new ModelFactory();
		document = new SBMLDocument();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	String ID = "ID";
	String NAME= "name";
	String DESC = "desc";
	String DETAILED_DESC = "detailed";

	@Test
	public void testCreateSBMLModel() {
		final IModel model = mockery.mock(IModel.class);
		setUpmodelExpectations(model);
		Model sbmlmodel = factory.createSBMLModel(document, model);
		mockery.assertIsSatisfied();
		assertGeneratedModelHasExpectedValues(sbmlmodel);
		assertEquals(0L,document.checkL2v3Compatibility());
	
	}
	
	private void assertGeneratedModelHasExpectedValues(Model sbmlmodel) {
		assertEquals(ID, sbmlmodel.getId());
		assertEquals(NAME, sbmlmodel.getName());
		assertTrue(sbmlmodel.getNotesString().contains(DESC));
		assertTrue(sbmlmodel.getNotesString().contains(DETAILED_DESC));
	
		
	}

	private void setUpmodelExpectations(final IModel model) {
		
		mockery.checking(new Expectations () {
			{atLeast(1).of(model).getId();will(returnValue(ID));}
			{atLeast(1).of(model).getASCIIName();will(returnValue(NAME));}
			{atLeast(1).of(model).getDescription();will(returnValue(DESC));}
			{atLeast(1).of(model).getDetailedDescription();will(returnValue(DETAILED_DESC));}
			
			
		});
		
	}

}
