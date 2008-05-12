package uk.ac.ed.inf.Metabolic.sbmlexport;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

@RunWith(JMock.class)
public class ModelFactoryTest {
	Mockery mockery = new JUnit4Mockery();
	IModelFactory factory;
	SBMLDocument document;
	
	static boolean canRun;
	@BeforeClass 
    public static void loadNativeLibraries () throws Exception {
    	canRun = LibSBMLConfigManager.configure();
    	
    }
	
	@Before
	public void setUp() throws Exception {
		factory = new ModelFactory();
		document = new SBMLDocument();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	String ID = "ID";
	String NAME= "name";
	String DESC = "desc";
	String DETAILED_DESC = "detailed";

	@Test
	public void testCreateSBMLModel() {
		final IModel model = mockery.mock(IModel.class);
		setUpmodelExpectations(model);
		Model sbmlmodel = factory.createSBMLModel(document, model);
		mockery.assertIsSatisfied();
		assertGeneratedModelHasExpectedValues(sbmlmodel);
		assertEquals(0,document.checkL2v3Compatibility());
	
	}
	
	private void assertGeneratedModelHasExpectedValues(Model sbmlmodel) {
		assertEquals(ID, sbmlmodel.getId());
		assertEquals(NAME, sbmlmodel.getName());
		assertTrue(sbmlmodel.getNotesString().contains(DESC));
		assertTrue(sbmlmodel.getNotesString().contains(DETAILED_DESC));
	
		
	}

	private void setUpmodelExpectations(final IModel model) {
		
		mockery.checking(new Expectations () {
			{one(model).getId();will(returnValue(ID));}
			{one(model).getASCIIName();will(returnValue(NAME));}
			{one(model).getDescription();will(returnValue(DESC));}
			{one(model).getDetailedDescription();will(returnValue(DETAILED_DESC));}
			
			
		});
		
	}

}
