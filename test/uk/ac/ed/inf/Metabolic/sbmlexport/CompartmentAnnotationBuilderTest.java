package uk.ac.ed.inf.Metabolic.sbmlexport;

import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;

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
