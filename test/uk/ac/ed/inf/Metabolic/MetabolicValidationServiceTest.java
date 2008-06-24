package uk.ac.ed.inf.Metabolic;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.contextadapter.publicapi.IContext;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterServiceProvider;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterValidationService;
import org.pathwayeditor.contextadapter.toolkit.ndom.AbstractNDOMParser.NdomException;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;

import uk.ac.ed.inf.Metabolic.parser.NDOMFactory;

@RunWith(JMock.class)
public class MetabolicValidationServiceTest {
   private IContextAdapterValidationService serviceAPI;
   private MetabolicContextValidationServiceExt serviceTSS;
   Mockery mockery = new JUnit4Mockery();
   final IContext context = mockery.mock(IContext.class);
   final IContextAdapterServiceProvider provider = mockery.mock(IContextAdapterServiceProvider.class);
   final IRuleValidationReportBuilder builder = mockery.mock(IRuleValidationReportBuilder.class);
   class MetabolicContextValidationServiceExt extends MetabolicContextValidationService {

	public MetabolicContextValidationServiceExt(IContextAdapterServiceProvider provider) {
		super(provider);
	}
	public IRuleValidationReportBuilder getReportBuilder (){
		return builder;
	}
	   
   }
	@Before
	public void setUp() throws Exception {
		mockery.checking(new Expectations() {
			{one(provider).getContext();will(returnValue(context))	;}
		});
		serviceTSS = new MetabolicContextValidationServiceExt(provider);
		serviceAPI=serviceTSS;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsImplemented() {
		assertTrue(serviceAPI.isImplemented());
	}
    @Ignore
	@Test
	public void testGenerateNdom() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testInitRuleStore() {
		serviceTSS.initRuleStore();
		assertTrue(serviceTSS.getRuleStore().isInitialized());
	}
    @Ignore
	@Test
	public void testGetModel() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCreateNdomFactory() {
		assertNotNull(serviceTSS.createNdomFactory());
	}

	@Test
	public void testHandleNdomExceptionNdomException() {
		serviceTSS.initRuleStore();
		mockery.checking(new Expectations() {
			{ atLeast(1).of(builder).setRuleFailed(null, DefaultRuleLoader.ERROR, MetabolicContextValidationService.ERROR_MESSAGE);
				;
			}
		});
		serviceTSS.handleNdomException(new NdomException ());
		mockery.assertIsSatisfied();
	}

}
