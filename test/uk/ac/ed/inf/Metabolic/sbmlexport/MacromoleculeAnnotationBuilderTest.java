package uk.ac.ed.inf.Metabolic.sbmlexport;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.ed.inf.Metabolic.ndomAPI.IMacromolecule;
@RunWith(JMock.class)
public class MacromoleculeAnnotationBuilderTest {
    AnnotationBuilder builder;
    Mockery mockery = new JUnit4Mockery();
	
    final IMacromolecule macromol = mockery.mock(IMacromolecule.class);
	@Before
	public void setUp() throws Exception {
		builder = new MacromoleculeAnnotationBuilder(macromol);
		
	}

	@After
	public void tearDown() throws Exception {
	}
    @Ignore
	@Test
	public void testBuildNotes() {
		fail("Not yet implemented"); // TODO
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
			{atLeast(1).of(macromol).getCompoundList();will(returnValue(Collections.EMPTY_LIST));}
			{atLeast(1).of(macromol).getSubunitList();will(returnValue(Collections.EMPTY_LIST));}
		});
		String s = builder.buildAnnotation();
		System.out.println(s);
	}
	
	@Test
	public void testBuildAnnotationDoesWriteAnnotationsIfAreSet() {
		mockery.checking(new Expectations () {
			{atLeast(1).of(macromol).getUniProt();}
			{will(returnValue("AAA"));}
			{atLeast(1).of(macromol).getGOTerm();}
			{will(returnValue("BBB"));}
			{atLeast(1).of(macromol).getId();}
			{will(returnValue("ID"));}
			{atLeast(1).of(macromol).getCompoundList();will(returnValue(Collections.EMPTY_LIST));}
			{atLeast(1).of(macromol).getSubunitList();will(returnValue(Collections.EMPTY_LIST));}
		});
		String s = builder.buildAnnotation();
		assertTrue(s.contains("AAA"));
		assertTrue(s.contains("BBB"));
	}

}
