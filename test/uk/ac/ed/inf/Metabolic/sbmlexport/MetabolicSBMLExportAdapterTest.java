package uk.ac.ed.inf.Metabolic.sbmlexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbml.libsbml.Compartment;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.Species;
import org.sbml.libsbml.libsbml;

import uk.ac.ed.inf.Metabolic.ExportAdapterCreationException;
import uk.ac.ed.inf.Metabolic.IExportAdapter;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

@RunWith(JMock.class)
public class MetabolicSBMLExportAdapterTest {
	Mockery mockery = new JUnit4Mockery();
	static boolean canRun;
	
	/**
	 * Shortcut to be able to test writeTarget
	 * @author Richard Adams
	 *
	 */
	private class  MetabolicSBMLExportAdapterTestStub extends MetabolicSBMLExportAdapter<IModel> {
		
		public boolean isTargetCreated () {
			return true;
		}
		void setDocument(SBMLDocument doc) {
			this.document=doc;
		}
	}
	
	@BeforeClass 
    public static void loadNativeLibraries () throws Exception {
    	canRun = LibSBMLConfigManager.configure();
    	
    }
    IExportAdapter<IModel> exportAdapter;
    MetabolicSBMLExportAdapterTestStub stubexportAdapter;
    
    
    
	@Before
	public void setUp() throws Exception {
		stubexportAdapter=new MetabolicSBMLExportAdapterTestStub();
	    exportAdapter = new MetabolicSBMLExportAdapter<IModel>();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateTargetPreconditions() throws Exception {
		exportAdapter.createTarget(null);
	}

	@Test
	public void testIsTargetCreatedIsInitiallyFalse() {
	  assertFalse(exportAdapter.isTargetCreated()) ;
	}
	
	@Test
	public void testShouldCreateTargetForValidSBMLDocument()  {
	  assertFalse(exportAdapter.isTargetCreated()) ;
	  final IModelFactory moeckModelFactory = mockery.mock(IModelFactory.class);
	  final IModel mockModel = mockery.mock(IModel.class);
	  final IEntityFactory mockentityFac = mockery.mock(IEntityFactory.class);
	  final SBMLDocument doc = createSBMLDocument();
	  mockery.checking(new Expectations() {
		{one(moeckModelFactory).createSBMLModel(with(any(SBMLDocument.class)), with(any(IModel.class)));}
		{will(returnValue(doc.getModel()));}
		{one(mockentityFac).buildSpeciesAndCompartments(with(any(Model.class)), with(any(IModel.class)));}
		{will(returnValue(doc.getModel()));}
		
	});
	  addDependencies(moeckModelFactory, mockentityFac, doc);
	  
	  exportAdapter = stubexportAdapter;
	  
	  // use SUT
	  try {
	  exportAdapter.createTarget(mockModel);
	  }catch(ExportAdapterCreationException e) {
		  fail(e.getMessage());
	  }
	  //validate
	  mockery.assertIsSatisfied();
	  assertTrue(exportAdapter.isTargetCreated());
	  
	  
	}
	
	@Test(expected=ExportAdapterCreationException.class)
	public void testCreateTargetThrowsExceptionForInValidSBMLDocument() throws Exception {
	  assertFalse(exportAdapter.isTargetCreated()) ;
	  final IModelFactory moeckModelFactory = mockery.mock(IModelFactory.class);
	  final IModel mockModel = mockery.mock(IModel.class);
	  final IEntityFactory mockentityFac = mockery.mock(IEntityFactory.class);
	  final SBMLDocument doc = createSBMLDocument();
	  mockery.checking(new Expectations() {
		{one(moeckModelFactory).createSBMLModel(with(any(SBMLDocument.class)), with(any(IModel.class)));}
		{will(returnValue(doc.getModel()));}
		{one(mockentityFac).buildSpeciesAndCompartments(with(any(Model.class)), with(any(IModel.class)));}
		{will(returnValue(doc.getModel()));}
		
	});
	 // doc.getModel().appendNotes("</p>dsds</p>");
	  // add dependencies
	  addDependencies(moeckModelFactory, mockentityFac, doc);
	  
	  exportAdapter = stubexportAdapter;
	  
	  // use SUT
	
		doc.getModel().addReaction(new Reaction());
	  exportAdapter.createTarget(mockModel);
	 
	  //validate
	  mockery.assertIsSatisfied();
	  assertFalse(exportAdapter.isTargetCreated());
	  
	  
	}

	private void addDependencies(final IModelFactory moeckModelFactory, final IEntityFactory mockentityFac,
			final SBMLDocument doc) {
		stubexportAdapter.setEntityBuilder(mockentityFac);
		  stubexportAdapter.setModelFactory(moeckModelFactory);
		  stubexportAdapter.setDocument(doc);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWriteTargetPrecondition1() throws Exception{
		exportAdapter.writeTarget(null);
		assertFalse(exportAdapter.isTargetCreated());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testWriteTargetFailsIfTargetNotCreated() throws Exception{
		exportAdapter.writeTarget(new FileOutputStream("file"));
	}
	
	
	@Test
	public void testWriteTargetWritesStringrepresentation () throws Exception {
		MetabolicSBMLExportAdapterTestStub stub = new MetabolicSBMLExportAdapterTestStub ();
		SBMLDocument toWrite = createSBMLDocument();
		File f = new File ("./sbml");
		f.createNewFile();
		stub.setDocument(toWrite);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stub.writeTarget(baos);
		assertEquals(libsbml.writeSBMLToString(toWrite),baos.toString());
	}

	private SBMLDocument createSBMLDocument() {
		SBMLDocument doc = new SBMLDocument();
		Model m = doc.createModel();
		m.setId("model");
		Compartment c = m.createCompartment();
		c.setId("comp1");
		c.setSize(1.0);
		return doc;
	}
	
	




	
}
