package uk.ac.ed.inf.Metabolic.sbmlexport;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import org.sbml.libsbml.Species;

import uk.ac.ed.inf.Metabolic.IExportAdapter;
import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMacromolecule;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

@RunWith(JMock.class)
public class MetabolicSBMLExportAdapterTest {
	Mockery mockery = new JUnit4Mockery();
	static boolean canRun;
	@BeforeClass 
    public static void loadNativeLibraries () throws Exception {
    	canRun = LibSBMLConfigManager.configure();
    	
    }
    IExportAdapter<IModel> exportAdapter;
    MetabolicSBMLExportAdapterExtension stubexportAdapter;
    
    // allows access to created SBML for verification
    private class MetabolicSBMLExportAdapterExtension extends MetabolicSBMLExportAdapter<IModel>{
    	Model getSBMLModel () {
			return document.getModel();
		}
    }
    
	@Before
	public void setUp() throws Exception {
		stubexportAdapter=new MetabolicSBMLExportAdapterExtension();
	    exportAdapter = stubexportAdapter;
		
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

	@Test(expected=IllegalArgumentException.class)
	public void testWriteTargetPrecondition1() throws Exception{
		exportAdapter.writeTarget(null);
		assertFalse(exportAdapter.isTargetCreated());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testWriteTargetFailsIfTargetNotCreated() throws Exception{
		exportAdapter.writeTarget(new FileOutputStream("file"));
	}
	
	




	
}
