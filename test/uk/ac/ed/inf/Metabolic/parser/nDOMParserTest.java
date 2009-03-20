/**
 * 
 */
package uk.ac.ed.inf.Metabolic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pathwayeditor.businessobjects.drawingprimitives.ICanvasAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingElement;
import org.pathwayeditor.businessobjects.drawingprimitives.IRootNode;
import org.pathwayeditor.businessobjects.typedefn.INodeObjectType;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.NdomException;


/**
 * $Id: nDOMParserTest.java 4646 2009-02-05 17:20:50Z smoodie $
 * @author Anatoly Sorokin
 * @date 28 May 2008
 * 
 */
public class nDOMParserTest {

	static class ParserStub extends AbstractNDOMParser{

		public ParserStub() {
			super(null);
		}

		@Override
		protected void connectivity() {
		}

		@Override
		protected void ndom() {
		}

		@Override
		protected void rmo() {
		}

		@Override
		protected void semanticValidation() {
		}

		
	}
	private AbstractNDOMParser parser;
	
	private Mockery mockery = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		parser=new ParserStub();
		IRootNode rmo=mockery.mock(IRootNode.class);
		parser.setRmo(rmo);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.pathwayeditor.notationsubsystem.toolkit.ndom.AbstractNDOMParser#getId(org.pathwayeditor.businessobjectsAPI.IMapObject)}.
	 */
	@Test
	public void testGetIdString() {
		final String name = "ObjecType";
		final IDrawingElement s=mockery.mock(IDrawingElement.class);
		final INodeObjectType otString=mockery.mock(INodeObjectType.class);
		final ICanvasAttribute a=mockery.mock(ICanvasAttribute.class);
		mockery.checking(new Expectations(){{
			atLeast(1).of(s).getAttribute();
			will(returnValue(a));
			atLeast(1).of(a).getObjectType();
			will(returnValue(otString));
			atLeast(1).of(otString).getName();
			will(returnValue(name));
		}});
		String id=parser.getId(s);
		assertEquals(name+"0", id);
		id=parser.getId(s);
		assertEquals(name+"1", id);
		mockery.assertIsSatisfied();
	}

	/**
	 * Test method for {@link org.pathwayeditor.notationsubsystem.toolkit.ndom.AbstractNDOMParser#getId(org.pathwayeditor.businessobjectsAPI.IMapObject)}.
	 */
	@Test
	public void testGetIdWrong() {
		final String name = "Objec Type";
		String nameOk = "ObjecType0";
		final IDrawingElement s=mockery.mock(IDrawingElement.class);
		final INodeObjectType otString=mockery.mock(INodeObjectType.class);
		final ICanvasAttribute a=mockery.mock(ICanvasAttribute.class);
		mockery.checking(new Expectations(){{
			atLeast(1).of(s).getAttribute();
			will(returnValue(a));
			atLeast(1).of(a).getObjectType();
			will(returnValue(otString));
			one(otString).getName();
			will(returnValue(name));
		}});
		String id=parser.getId(s);
		assertEquals(nameOk, id);
		mockery.assertIsSatisfied();
	}

	/**
	 * Test method for {@link org.pathwayeditor.notationsubsystem.toolkit.ndom.AbstractNDOMParser#getId(org.pathwayeditor.businessobjectsAPI.IMapObject)}.
	 */
	@Test
	public void testGetIdWeird() {
		final String name = "£Objec%$ <Type>=*&";
		String nameOk = "ObjecType0";
		final IDrawingElement s=mockery.mock(IDrawingElement.class);
		final INodeObjectType otString=mockery.mock(INodeObjectType.class);
		final ICanvasAttribute a=mockery.mock(ICanvasAttribute.class);
		mockery.checking(new Expectations(){{
			atLeast(1).of(s).getAttribute();
			will(returnValue(a));
			atLeast(1).of(a).getObjectType();
			will(returnValue(otString));
			one(otString).getName();
			will(returnValue(name));
		}});
		String id=parser.getId(s);
		assertEquals(nameOk, id);
		mockery.assertIsSatisfied();
	}


	@Test
	public void testWarning(){
		try {
			parser.parse();
		} catch (NdomException e) {
			fail("Exception is thrown");
		}
		assertTrue("Valid before warning",parser.isValid());
		assertEquals("Before warning",0, parser.getReport().size());
//		String message = "message";
		//parser.warning(message);
		assertEquals("After warning", 0, parser.getReport().size());
	//	assertTrue("contains message",parser.getReport().get(0).contains(message));
		//assertTrue("Starts with warning",parser.getReport().get(0).startsWith("WARNING"));	
		assertTrue("Valid warning",parser.isValid());
		assertFalse("Valid warning",parser.hasWarnings());
	}
    
	@Test
	public void testError(){
		try {
			parser.parse();
		} catch (NdomException e) {
			fail("Exception is thrown");
		}
		assertTrue("Valid before error",parser.isValid());
		assertEquals("Before error",0, parser.getReport().size());
//		String message = "message";
	//	parser.error(message);
		assertEquals("After warning", 0, parser.getReport().size());
	//	assertTrue("contains message",parser.getReport().get(0).contains(message));
	//	assertTrue("Starts with error",parser.getReport().get(0).startsWith("ERROR"));	
		assertTrue("After warning",parser.isValid());
	}
	

	
	@Test
	public void testParse() throws NdomException {
		final String name = "ObjecType";
		final IDrawingElement s=mockery.mock(IDrawingElement.class);
		final INodeObjectType otString=mockery.mock(INodeObjectType.class);
		final ICanvasAttribute a=mockery.mock(ICanvasAttribute.class);
		mockery.checking(new Expectations(){{
			atLeast(1).of(s).getAttribute();
			will(returnValue(a));
			atLeast(1).of(a).getObjectType();
			will(returnValue(otString));
			atLeast(1).of(otString).getName();
			will(returnValue(name));
		}});
		String id=parser.getId(s);
		id=parser.getId(s);
		id=parser.getId(s);
		assertFalse(parser.isParsed());
		parser.parse();
		assertTrue(parser.isParsed());
		assertTrue(parser.isValid());
		assertFalse(parser.hasWarnings());
		//parser set report to empty
		assertEquals(0, parser.getReport().size());
		//parser should reset counter to 0
		id=parser.getId(s);
		assertEquals(name+"0", id);
		mockery.assertIsSatisfied();
	}
	
//	@Test(expected=IllegalArgumentException.class)
//	public void testReportBuilderCantBeNull() {
//		parser.setReportBuilder(null);
//	}

}


/*
 * $Log$
 * Revision 1.2  2008/07/12 15:50:18  smoodie
 * eliminated warnings and resurrected a couple of Ignored tests.
 *
 * Revision 1.1  2008/07/10 12:06:37  nhanlon
 * extraction of Toolkit project
 *
 * Revision 1.7  2008/07/07 08:48:09  nhanlon
 * interface changes
 *
 * Revision 1.6  2008/06/27 13:18:46  radams
 * *** empty log message ***
 *
 * Revision 1.5  2008/06/26 13:09:07  radams
 * *** empty log message ***
 *
 * Revision 1.4  2008/06/23 14:42:09  radams
 * *** empty log message ***
 *
 * Revision 1.3  2008/06/13 10:40:59  radams
 * ignore tests while failing
 *
 * Revision 1.2  2008/06/02 12:24:15  asorokin
 * SetUp is fixed
 *
 * Revision 1.1  2008/06/02 10:27:40  asorokin
 * NDOM facility
 *
 */