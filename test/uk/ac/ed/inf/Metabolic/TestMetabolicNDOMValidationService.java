package uk.ac.ed.inf.Metabolic;

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
import org.pathwayeditor.businessobjects.drawingprimitives.IRootNode;

/**
 * <br>$Id:$
 * @author Anatoly Sorokin
 * @date 5 Aug 2008
 * 
 */
@RunWith(JMock.class)
public class TestMetabolicNDOMValidationService {

	private Mockery mockery = new JUnit4Mockery();
	private ICanvas map;
	
	private IRootNode rmo;
	
	private MetabolicNDOMValidationService s;
	
	@Before
	public void setUp() throws Exception {
		s=MetabolicNDOMValidationService.getInstance();
		rmo=mockery.mock(IRootNode.class);
		map=mockery.mock(ICanvas.class);
		mockery.checking(new Expectations(){{
			allowing(map).getModel().getRootNode();will(returnValue(rmo));
		}});

	}

	@After
	public void tearDown() throws Exception {
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