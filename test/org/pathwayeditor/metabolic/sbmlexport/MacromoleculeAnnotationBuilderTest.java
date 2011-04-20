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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.metabolic.ndomAPI.IMacromolecule;
import org.pathwayeditor.metabolic.sbmlexport.AnnotationBuilder;
import org.pathwayeditor.metabolic.sbmlexport.MacromoleculeAnnotationBuilder;

@RunWith(JMock.class)
public class MacromoleculeAnnotationBuilderTest {
    AnnotationBuilder builder;
    Mockery mockery = new JUnit4Mockery();
	final String GOTERM="GOTERM";
	final String UNIPROT="UNIPROT";
    final IMacromolecule macromol = mockery.mock(IMacromolecule.class);
	@Before
	public void setUp() throws Exception {
		builder = new MacromoleculeAnnotationBuilder(macromol, true);
		
	}

	@After
	public void tearDown() throws Exception {
	}
    
	@Test
	public void testBuildNotes() {
//		String s = builder.buildNotes();
//	    assertNotNull(s);
	}

	@Test
	public void testBuildAnnotationDoesntWriteAnnotationsIfAreEmpty() {
		mockery.checking(new Expectations () {
			{atLeast(1).of(macromol).getUniProt();}
			{will(returnValue(""));}
			{atLeast(1).of(macromol).getGOTerm();}
			{will(returnValue(""));}
			{atLeast(1).of(macromol).getId();}
			{will(returnValue("ID"));}
			{allowing(macromol).getCompoundList();will(returnValue(Collections.EMPTY_LIST));}
			{allowing(macromol).getSubunitList();will(returnValue(Collections.EMPTY_LIST));}
	
		});
		String s = builder.buildAnnotation();
		assertFalse(s.contains(UNIPROT));
		assertFalse(s.contains(GOTERM));
	}
	
	@Test
	public void testBuildAnnotationDoesWriteAnnotationsIfAreSet() {
		mockery.checking(new Expectations () {
			{atLeast(1).of(macromol).getUniProt();}
			{will(returnValue(UNIPROT));}
			{atLeast(1).of(macromol).getGOTerm();}
			{will(returnValue(GOTERM));}
			{atLeast(1).of(macromol).getId();}
			{will(returnValue("ID"));}
			{allowing(macromol).getCompoundList();will(returnValue(Collections.EMPTY_LIST));}
			{allowing(macromol).getSubunitList();will(returnValue(Collections.EMPTY_LIST));}
		
		});
		String s = builder.buildAnnotation();
		assertTrue(s.contains(UNIPROT));
		assertTrue(s.contains(GOTERM));
		assertFalse(s.contains("hasPart"));
		System.out.println(System.getProperty("java.version"));
	}
	
	
	


}
