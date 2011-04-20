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
package org.pathwayeditor.metabolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.businessobjects.drawingprimitives.ICanvas;
import org.pathwayeditor.businessobjects.drawingprimitives.IModel;
import org.pathwayeditor.businessobjects.drawingprimitives.IRootNode;
import org.pathwayeditor.metabolic.MetabolicNDOMValidationService;
import org.pathwayeditor.metabolic.validation.MetabolicRuleStore;


/**
 * <br>$Id:$
 * @author Anatoly Sorokin
 * @date 5 Aug 2008
 * 
 */
@RunWith(JMock.class)
public class MetabolicNDOMValidationServiceTest {

	private Mockery mockery = new JUnit4Mockery();
	private ICanvas map;
	
	private IRootNode rmo;
	private IModel model;
	
	private MetabolicNDOMValidationService s;
	@Before
	public void setUp() throws Exception {
		s = new MetabolicNDOMValidationService();
		rmo=mockery.mock(IRootNode.class);
		map=mockery.mock(ICanvas.class);
		model=mockery.mock(IModel.class);
		mockery.checking(new Expectations(){{
			allowing(map).getModel();will(returnValue(model));
			allowing(model).getRootNode();will(returnValue(rmo));
		}});

	}

	@After
	public void tearDown() throws Exception {
		s = null;
	}

	@Test
	public void testGetRuleStore() {
		assertEquals(MetabolicRuleStore.getInstance(), s.getRuleStore());
	}

	@Test
	public void testSetMap(){
		assertFalse("ready before set", s.isReadyToValidate());
		s.setMapToValidate(map);
		assertTrue("ready after set", s.isReadyToValidate());
		assertFalse("validated after set", s.hasBeenValidated());
		assertFalse("ndomCreated after set", s.ndomWasCreated());
		assertNull("report after set", s.getValidationReport());
		
	}

	@Test(expected=NullPointerException.class)
	public void testSetNullMap(){
		s.setMapToValidate(null);
	
	}

}


/*
 * $Log:$
 */