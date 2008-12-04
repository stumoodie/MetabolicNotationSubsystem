package uk.ac.ed.inf.Metabolic;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.businessobjects.drawingprimitives.ICanvas;
import org.pathwayeditor.businessobjects.drawingprimitives.IModel;
import org.pathwayeditor.businessobjects.drawingprimitives.IRootNode;
import org.pathwayeditor.contextadapter.toolkit.validation.RuleStore;

import uk.ac.ed.inf.Metabolic.parser.MetabolicRuleLoader;

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
		s=MetabolicNDOMValidationService.getInstance(MetabolicNotationSubsystem.getInstance());
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
	}

	@Test
	public void testGetRuleStore() {
		assertEquals(RuleStore.getInstance(s.getRuleLoader()), s.getRuleStore());
	}

	@Test
	public void testGetRuleLoader() {
		assertEquals(MetabolicRuleLoader.getInstance(), s.getRuleLoader()); // TODO
	}

	@Ignore
	@Test
	public void testGetValidationReport() {
		fail("Not yet implemented"); // TODO
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

	@Ignore
	@Test
	public void testValidateMap() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public void testGenerateNdom() {
		
	}

	@Ignore
	@Test
	public void testCreateNdomFactory() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public void testHandleNdomException() {
		fail("Not yet implemented"); // TODO
	}

}


/*
 * $Log:$
 */