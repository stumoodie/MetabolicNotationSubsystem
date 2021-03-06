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

import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.metabolic.ndomAPI.ICompartment;
import org.pathwayeditor.metabolic.sbmlexport.AnnotationBuilder;
import org.pathwayeditor.metabolic.sbmlexport.CompartmentAnnotationBuilder;


@RunWith(JMock.class)
public class CompartmentAnnotationBuilderTest {
    AnnotationBuilder builder;
    Mockery mockery = new JUnit4Mockery();
	final ICompartment comp = mockery.mock(ICompartment.class);
	
   
	@Before
	public void setUp() throws Exception {
		builder = new CompartmentAnnotationBuilder(comp);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildAnnotation() {
		mockery.checking(new Expectations() {
			{one(comp).getGOTerm();will(returnValue("GOTERM"));}
			{one(comp).getId();will(returnValue("Id"));}
		});
		String anno =builder.buildAnnotation();
		assertTrue(anno.contains("GOTERM"));
		
	}
	


}
