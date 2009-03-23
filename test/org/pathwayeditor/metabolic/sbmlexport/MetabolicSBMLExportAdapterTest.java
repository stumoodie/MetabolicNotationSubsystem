package org.pathwayeditor.metabolic.sbmlexport;

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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.metabolic.ExportAdapterCreationException;
import org.pathwayeditor.metabolic.IExportAdapter;
import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.pathwayeditor.metabolic.sbmlexport.IEntityFactory;
import org.pathwayeditor.metabolic.sbmlexport.IModelFactory;
import org.pathwayeditor.metabolic.sbmlexport.IReactionBuilder;
import org.pathwayeditor.metabolic.sbmlexport.MetabolicSBMLExportAdapter;
import org.sbml.libsbml.Compartment;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.libsbml;


@RunWith(JMock.class)
public class MetabolicSBMLExportAdapterTest {
	Mockery mockery = new JUnit4Mockery();
//	static boolean isLibSBMLLoaded;
	static File aFile;
	
	/**
	 * Shortcut to be able to add test doubles.
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
		void setEntityBuilder(IEntityFactory entityFactory) {
			this.entityFactory = entityFactory;
		}

		void setModelFactory(IModelFactory modelFactory) {
			this.modelFactory = modelFactory;
		}


		void setReactionFactory(IReactionBuilder reactionFactory) {
			this.reactionFactory = reactionFactory;
		}
	}
	
//	@BeforeClass 
//    public static void loadNativeLibraries () throws Exception {
//    	isLibSBMLLoaded = LibSBMLConfigManager.configure();
//    	//overrides application code
//    	SBMLLoaderTestStub.overrideSingleton( new LibSBMLLoader(){
//    		public boolean loadLibrary() {
//    			return true;
//    		}
//    	});
//    	
//    }
    IExportAdapter<IModel> exportAdapter;
    MetabolicSBMLExportAdapterTestStub stubexportAdapter;
    
    
    
	@Before
	public void setUp() throws Exception {
	//	loadNativeLibraries();
		stubexportAdapter=new MetabolicSBMLExportAdapterTestStub();
	    exportAdapter = new MetabolicSBMLExportAdapter<IModel>();
//		SBMLLoaderTestStub.overrideSingleton( new LibSBMLLoader(){
//    		public boolean loadLibrary() {
//    			return true;
//    		}
//    	});
	    aFile = new File("file");
		
	}

	@After
	public void tearDown() throws Exception {
		
	}
	@AfterClass
	 public static void deleteFiles () {
		 if(aFile.exists()) {
			 aFile.delete();
		 }
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
	  final IModelFactory moeckModelFactory = mockery.mock(IModelFactory.class, "mockModelFactory");
	  final IModel mockModel = mockery.mock(IModel.class, "mockModel");
	  final IEntityFactory mockentityFac = mockery.mock(IEntityFactory.class, "mockentityFac");
	  final IReactionBuilder mockReactionFactory = mockery.mock(IReactionBuilder.class, "mockReactionFactory");
	  final SBMLDocument doc = createSBMLDocument();
	  mockery.checking(new Expectations() {
		{one(moeckModelFactory).createSBMLModel(with(any(SBMLDocument.class)), with(any(IModel.class)));}
		{will(returnValue(doc.getModel()));}
		{one(mockentityFac).buildSpeciesAndCompartments(with(any(Model.class)), with(any(IModel.class)));}
		{will(returnValue(doc.getModel()));}
		{one(mockReactionFactory).buildReactions(with(any(Model.class)), with(any(IModel.class)));}
		
	});
	  addDependencies(moeckModelFactory, mockentityFac, doc,mockReactionFactory);
	  
	  
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
	  final IReactionBuilder mockReactionFactory = mockery.mock(IReactionBuilder.class);
	  final SBMLDocument doc = createSBMLDocument();
	  mockery.checking(new Expectations() {
		{one(moeckModelFactory).createSBMLModel(with(any(SBMLDocument.class)), with(any(IModel.class)));}
		{will(returnValue(doc.getModel()));}
		{one(mockentityFac).buildSpeciesAndCompartments(with(any(Model.class)), with(any(IModel.class)));}
		{will(returnValue(doc.getModel()));}
		{one(mockReactionFactory).buildReactions(with(any(Model.class)), with(any(IModel.class)));}
		
	});
	  exportAdapter = stubexportAdapter;
	  addDependencies(moeckModelFactory, mockentityFac, doc,mockReactionFactory);
	 
	  // use SUT
	    // invalidates model - reaction with no specuies
		doc.getModel().addReaction(new Reaction());
	   exportAdapter.createTarget(mockModel);
	 
	  //validate
	  mockery.assertIsSatisfied();
	  assertFalse(exportAdapter.isTargetCreated());
	  
	  
	}

	private void addDependencies(final IModelFactory moeckModelFactory, final IEntityFactory mockentityFac,
			final SBMLDocument doc, final IReactionBuilder mockReactionFactory) {
		  stubexportAdapter.setEntityBuilder(mockentityFac);
		  stubexportAdapter.setModelFactory(moeckModelFactory);
		  stubexportAdapter.setDocument(doc);
		  stubexportAdapter.setReactionFactory(mockReactionFactory);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWriteTargetPrecondition1() throws Exception{
		exportAdapter.writeTarget(null);
		assertFalse(exportAdapter.isTargetCreated());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testWriteTargetFailsIfTargetNotCreated() throws Exception{
		
		exportAdapter.writeTarget(new FileOutputStream(aFile));
	}
	
	
	@Test
	public void testWriteTargetWritesStringrepresentation () throws Exception {
		MetabolicSBMLExportAdapterTestStub stub = new MetabolicSBMLExportAdapterTestStub ();
		SBMLDocument toWrite = createSBMLDocument();
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
