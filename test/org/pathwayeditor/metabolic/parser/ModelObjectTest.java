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
package org.pathwayeditor.metabolic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingNode;
import org.pathwayeditor.businessobjects.drawingprimitives.IShapeAttribute;
import org.pathwayeditor.metabolic.parser.ModelObject;


public class ModelObjectTest {

	private static final String ASCII_NAME = "name";

	private static final String NAME = "<name>";
	private static final String DESCRIPTION = "description>";
	private static final String DETAILED_DESCRIPTION = "detailed description";

	private Mockery mockery = new JUnit4Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	private String goodId = "c001";

	private String badId = "!goodId";

//	private ModelObject mObject = mockery.mock(ModelObject.class);

	// private
	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		ModelObject stObject = new ModelObject(goodId, NAME, ASCII_NAME) {
		};
		assertEquals(goodId.hashCode(), stObject.hashCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testModelObjectStringStringStringBadId() {
		new ModelObject(badId, NAME, ASCII_NAME) {
		};
	}

	@Test(expected = IllegalArgumentException.class)
	public void testModelObjectStringNullString() {
			new ModelObject(goodId, null, ASCII_NAME) {
			};	
	}

	@Test(expected = IllegalArgumentException.class)
	public void testModelObjectStringStringNull() {
		new ModelObject(goodId, NAME, null) {
		};
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testModelObjectWithEmptyStringNotAllowed() {
		String EMPTY_STRING_ID="";
		new ModelObject(EMPTY_STRING_ID, NAME, null) {
		};
	}

	@Test
	public void testModelObjectStringStringString() {
		ModelObject stObject = new ModelObject(goodId, NAME, ASCII_NAME) {
		};
		assertEquals(goodId, stObject.getId());
		assertEquals(NAME, stObject.getName());
		assertEquals(ASCII_NAME, stObject.getASCIIName());
	}

	@Test
	public void testModelObjectStringIMapObject() {
		final IDrawingNode mo = mockery.mock(IDrawingNode.class);
		final IShapeAttribute isa = mockery.mock(IShapeAttribute.class);
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(mo).getAttribute();
				will(returnValue(isa));
			}
			{
				atLeast(2).of(isa).getName();
				will(returnValue(NAME));
			}
			{
				one(isa).getDescription();
				will(returnValue(DESCRIPTION));
			}
			{
				one(isa).getDetailedDescription();
				will(returnValue(DETAILED_DESCRIPTION));
			}
		});
		ModelObject stObject = new ModelObject(goodId, mo) {
		};
		assertEquals(goodId, stObject.getId());
		assertEquals(NAME, stObject.getName());
//FIXME		assertEquals(ASCII_NAME, stObject.getASCIIName());
		assertEquals(DESCRIPTION, stObject.getDescription());
		assertEquals(DETAILED_DESCRIPTION, stObject.getDetailedDescription());
	}

	@Test
	public void testToString() {
		ModelObject stObject = new ModelObject(goodId, NAME, ASCII_NAME) {
		};
		stObject.setDescription(DESCRIPTION);
		stObject.setDetailedDescription(DETAILED_DESCRIPTION);
		assertEquals("ModelObject:[\nID:" + goodId + "\nname:" + ASCII_NAME
				+ "\nDescription:" + DESCRIPTION + "\nDetailedDescription:"
				+ DETAILED_DESCRIPTION + "\n]\n", stObject.toString());
	}

	@Test
	public void testEqualsObject() {
		ModelObject stObject = new ModelObject(goodId, NAME, ASCII_NAME) {
		};
		assertTrue(stObject.equals(stObject));
		assertTrue(stObject.equals(new ModelObject(goodId, NAME, ASCII_NAME) {
		}));
		assertTrue((new ModelObject(goodId, NAME, ASCII_NAME) {
		}).equals(stObject));
		assertFalse(stObject.equals(new ModelObject("id1", NAME, ASCII_NAME) {
		}));
	}

}

/*
 * $Log$
 * Revision 1.2  2008/07/12 15:50:18  smoodie
 * eliminated warnings and resurrected a couple of Ignored tests.
 *
 * Revision 1.1  2008/07/10 12:06:37  nhanlon
 * extraction of Toolkit project
 *
 * Revision 1.3  2008/06/27 13:18:46  radams
 * *** empty log message ***
 *
 * Revision 1.2  2008/06/26 13:09:07  radams
 * *** empty log message ***
 *
 * Revision 1.1  2008/06/02 10:27:40  asorokin
 * NDOM facility
 *
 */