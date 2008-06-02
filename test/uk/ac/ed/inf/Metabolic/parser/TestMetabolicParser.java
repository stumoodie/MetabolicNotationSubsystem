/**
 * 
 */
package uk.ac.ed.inf.Metabolic.parser;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.pathwayeditor.businessobjectsAPI.IMapObject;
import org.pathwayeditor.businessobjectsAPI.IRootMapObject;
import org.pathwayeditor.contextadapter.toolkit.ndom.AbstractNDOMParser;
import org.pathwayeditor.contextadapter.toolkit.ndom.ModelObject;

/**
 * $Id$
 * @author Anatoly Sorokin
 * @date 2 Jun 2008
 * 
 */
public class TestMetabolicParser {

	private MetabolicNDOMFactory parser;
	
	private Mockery mockery = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		parser=new MetabolicNDOMFactory();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#connectivity()}.
	 */
	@Ignore
	@Test
	public void testConnectivity() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#rmo()}.
	 */
	@Ignore
	@Test
	public void testRmo() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#compartment(uk.ac.ed.inf.Metabolic.parser.MetabolicCompartment, org.pathwayeditor.businessobjectsAPI.IMapObject)}.
	 */
	@Ignore
	@Test
	public void testCompartmentMetabolicCompartmentIMapObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#compound(uk.ac.ed.inf.Metabolic.parser.MetabolicCompartment, org.pathwayeditor.businessobjectsAPI.IMapObject)}.
	 */
	@Ignore
	@Test
	public void testCompoundMetabolicCompartmentIMapObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#compound(uk.ac.ed.inf.Metabolic.parser.MetabolicMacromolecule, org.pathwayeditor.businessobjectsAPI.IMapObject)}.
	 */
	@Ignore
	@Test
	public void testCompoundMetabolicMacromoleculeIMapObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#macromolecule(uk.ac.ed.inf.Metabolic.parser.MetabolicCompartment, org.pathwayeditor.businessobjectsAPI.IMapObject)}.
	 */
	@Ignore
	@Test
	public void testMacromoleculeMetabolicCompartmentIMapObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#macromolecule(uk.ac.ed.inf.Metabolic.parser.MetabolicMacromolecule, org.pathwayeditor.businessobjectsAPI.IMapObject)}.
	 */
	@Ignore
	@Test
	public void testMacromoleculeMetabolicMacromoleculeIMapObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#checkParameters(MetabolicReaction)}.
	 */
	@Test
	public void testCheckParameters() {
		final IRootMapObject parent=mockery.mock(IRootMapObject.class);
		final MetabolicReaction goodRe=mockery.mock(MetabolicReaction.class);
		final MetabolicReaction badRe=mockery.mock(MetabolicReaction.class);
		mockery.checking(new Expectations(){{
			allowing(goodRe).getParameters();will(returnValue("k=12;   p=0.0345;f=1.0e-6;"));
			allowing(badRe).getParameters();will(returnValue("f"));
		}});
		parser.setRmo(parent);
		assertTrue(parser.getReport().size()==0);
		parser.checkParameters(goodRe);
		assertTrue(parser.getReport().size()==0);
		parser.checkParameters(badRe);
		
		assertFalse(parser.getReport().size()==0);
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#activate(org.pathwayeditor.businessobjectsAPI.ILink, uk.ac.ed.inf.Metabolic.parser.MetabolicReaction)}.
	 */
	@Ignore
	@Test
	public void testActivate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#catalysis(org.pathwayeditor.businessobjectsAPI.ILink, uk.ac.ed.inf.Metabolic.parser.MetabolicReaction)}.
	 */
	@Ignore
	@Test
	public void testCatalysisILinkMetabolicReaction() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#inhibit(org.pathwayeditor.businessobjectsAPI.ILink, uk.ac.ed.inf.Metabolic.parser.MetabolicReaction)}.
	 */
	@Ignore
	@Test
	public void testInhibit() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#products(org.pathwayeditor.businessobjectsAPI.ILink, uk.ac.ed.inf.Metabolic.parser.MetabolicReaction)}.
	 */
	@Ignore
	@Test
	public void testProducts() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#substrate(org.pathwayeditor.businessobjectsAPI.ILink, uk.ac.ed.inf.Metabolic.parser.MetabolicReaction)}.
	 */
	@Ignore
	@Test
	public void testSubstrate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory#updateKL(uk.ac.ed.inf.Metabolic.parser.MetabolicReaction, java.lang.String, java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testUpdateKL() {
		fail("Not yet implemented"); // TODO
	}

}


/*
 * $Log$
 * Revision 1.1  2008/06/02 15:14:00  asorokin
 * *** empty log message ***
 *
 */