package uk.ac.ed.inf.Metabolic.sbmlexport;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.Species;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMacromolecule;
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
	}
	
	
	


}
