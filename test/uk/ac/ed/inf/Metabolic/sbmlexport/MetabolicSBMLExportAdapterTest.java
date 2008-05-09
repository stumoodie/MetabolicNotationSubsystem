package uk.ac.ed.inf.Metabolic.sbmlexport;

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
	@Before
	public void setUp() throws Exception {
		exportAdapter=new MetabolicSBMLExportAdapter<IModel>();
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
	
	@Test
	public void testCreateTargetPassesIfTargetCreatedFor3compartments() throws Exception{
		final IModel model = mockery.mock(IModel.class);
		setUpmodelExpectations(model);
		final List<ICompartment> compartments = setUpCompartmentExpectations(0,2);
		mockery.checking(new Expectations () {
			{one(model).getCompartmentList();will(returnValue(compartments));}
			
			{allowing(compartments.get(0)).getChildCompartments();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{one(compartments.get(0)).getParentCompartment();will(returnValue(null));}
			{allowing(compartments.get(0)).getCompoundList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(compartments.get(0)).getMacromoleculeList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			
			{allowing(compartments.get(1)).getChildCompartments();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{one(compartments.get(1)).getParentCompartment();will(returnValue(null));}
			{allowing(compartments.get(1)).getCompoundList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(compartments.get(1)).getMacromoleculeList();}
			{will(returnValue(Collections.EMPTY_LIST));}
		});
		exportAdapter.createTarget(model);
		assertTrue(exportAdapter.isTargetCreated());
		exportAdapter.writeTarget(new FileOutputStream("file"));
	}
	
	@Test
	public void testTargetCreatedFor2compartmentsAnd1Child() throws Exception{
		final IModel model = mockery.mock(IModel.class);
		setUpmodelExpectations(model);
		final List<ICompartment> parentcompartments = setUpCompartmentExpectations(0,2);
		final List<ICompartment> childCompartments = setUpCompartmentExpectations(3,1);
		mockery.checking(new Expectations () {
			{allowing(parentcompartments.get(0)).getChildCompartments();}
			{will(returnValue(childCompartments));}
			{allowing(parentcompartments.get(0)).getParentCompartment();will(returnValue(null));}
			{allowing(parentcompartments.get(0)).getCompoundList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(parentcompartments.get(0)).getMacromoleculeList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			
			{allowing(parentcompartments.get(1)).getChildCompartments();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(parentcompartments.get(1)).getParentCompartment();will(returnValue(null));}
			{allowing(parentcompartments.get(1)).getCompoundList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(parentcompartments.get(1)).getMacromoleculeList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			
			{allowing(childCompartments.get(0)).getChildCompartments();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(childCompartments.get(0)).getParentCompartment();}
			{will(returnValue(parentcompartments.get(0)));}
			{allowing(childCompartments.get(0)).getCompoundList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(childCompartments.get(0)).getMacromoleculeList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			
			{one(model).getCompartmentList();will(returnValue(parentcompartments));}
		});
		exportAdapter.createTarget(model);
		assertTrue(exportAdapter.isTargetCreated());
		
	}
	
	@Test
	public void testTargetCreatedForSingleCompoundInSingleCompartment()throws Exception {
		final IModel model = mockery.mock(IModel.class);
		setUpmodelExpectations(model);
		final List<ICompartment> parentcompartments = setUpCompartmentExpectations(0,1);
		final List<ICompound> compounds = Arrays.asList(new ICompound[]{createMockCompound(1)});
		
		mockery.checking(new Expectations() {
			{allowing(parentcompartments.get(0)).getChildCompartments();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(parentcompartments.get(0)).getParentCompartment();will(returnValue(null));}
			{allowing(parentcompartments.get(0)).getCompoundList();}
			{will(returnValue(compounds));}
			{allowing(parentcompartments.get(0)).getMacromoleculeList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{one(model).getCompartmentList();will(returnValue(parentcompartments));}
			
		});
		exportAdapter.createTarget(model);
		assertTrue(exportAdapter.isTargetCreated());
		
	}
	
	@Test
	public void testTargetCreatedForSingleMacromoleculeInSingleCompartment()throws Exception {
		final IModel model = mockery.mock(IModel.class);
		setUpmodelExpectations(model);
		final List<ICompartment> parentcompartments = setUpCompartmentExpectations(0,1);
		final List<IMacromolecule> macromols = Arrays.asList(new IMacromolecule[]{createMockMacromolecule(1)});
		
		mockery.checking(new Expectations() {
			{allowing(parentcompartments.get(0)).getChildCompartments();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(parentcompartments.get(0)).getParentCompartment();will(returnValue(null));}
			{allowing(parentcompartments.get(0)).getCompoundList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(parentcompartments.get(0)).getMacromoleculeList();}
			{will(returnValue(macromols));}
			{one(model).getCompartmentList();will(returnValue(parentcompartments));}
			{allowing(macromols.get(0)).getCompoundList();will(returnValue(Collections.EMPTY_LIST));}
			{allowing(macromols.get(0)).getSubunitList();will(returnValue(Collections.EMPTY_LIST));}
			
		});
		exportAdapter.createTarget(model);
		assertTrue(exportAdapter.isTargetCreated());
		
	}
	
	@Test
	public void testTargetCreatedForSingleMacromoleculeWithChildCompoundInSingleCompartment()throws Exception {
		final IModel model = mockery.mock(IModel.class);
		setUpmodelExpectations(model);
		final List<ICompartment> parentcompartments = setUpCompartmentExpectations(0,1);
		final List<IMacromolecule> macromols = Arrays.asList(new IMacromolecule[]{createMockMacromolecule(1)});
		final List<ICompound> childcompounds = Arrays.asList(new ICompound[]{createMockCompoundForHasPArtAnnotation(2)});
		
		mockery.checking(new Expectations() {
			{allowing(parentcompartments.get(0)).getChildCompartments();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(parentcompartments.get(0)).getParentCompartment();will(returnValue(null));}
			{allowing(parentcompartments.get(0)).getCompoundList();}
			{will(returnValue(Collections.EMPTY_LIST));}
			{allowing(parentcompartments.get(0)).getMacromoleculeList();}
			{will(returnValue(macromols));}
			{one(model).getCompartmentList();will(returnValue(parentcompartments));}
			{allowing(macromols.get(0)).getCompoundList();will(returnValue(childcompounds));}
			{allowing(macromols.get(0)).getSubunitList();will(returnValue(Collections.EMPTY_LIST));}
			
		});
		exportAdapter.createTarget(model);
		assertTrue(exportAdapter.isTargetCreated());
		
	}

	private void setUpmodelExpectations(final IModel model) {
		
		mockery.checking(new Expectations () {
			{one(model).getId();will(returnValue("ModelID"));}
			{one(model).getASCIIName();will(returnValue("ModelAsciiName"));}
			{one(model).getDescription();will(returnValue("ModelDescription"));}
			{one(model).getDetailedDescription();will(returnValue("ModelDetailedDescription"));}
			
			
		});
		
	}

	private List<ICompartment> setUpCompartmentExpectations(int startID, int numCompartments) {
	    List<ICompartment> rc = new ArrayList<ICompartment>();
	    for (int i=startID; i<startID +numCompartments; i++){
	    	ICompartment mock = getMockCompartment(i);
	    	rc.add(mock);
	    }
	    return rc;
	    
	}
	
	ICompartment getMockCompartment(final int i) {
		final ICompartment comp = mockery.mock(ICompartment.class);
		mockery.checking(new Expectations () {
			{atLeast(1).of(comp).getId();will(returnValue("CompID" + i));}
			{one(comp).getASCIIName();will(returnValue("CompAsciiName" +i));}
			{one(comp).getDescription();will(returnValue("CompDescription"+i));}
			{one(comp).getDetailedDescription();will(returnValue("CompDetailedDescription"+i));}
			{one(comp).getVolume();will(returnValue(new Integer(i+1).doubleValue()));}
			{one(comp).getGOTerm();will(returnValue("GOTerm"+i));}
		});
		return comp;
	}
	
	ICompound createMockCompound(final int i) {
		final ICompound compound = mockery.mock(ICompound.class);
		mockery.checking(new Expectations () {
			{atLeast(1).of(compound).getId();will(returnValue("CompoundID" + i));}
			{atLeast(1).of(compound).getASCIIName();will(returnValue("CompoundAsciiName" +i));}
			{atLeast(1).of(compound).getDescription();will(returnValue("CompoundDescription"+i));}
			{atLeast(1).of(compound).getDetailedDescription();will(returnValue("CompoundDetailedDescription"+i));}
			{atLeast(1).of(compound).getInChI();will(returnValue("INCHI"));}
			{atLeast(1).of(compound).getCID();will(returnValue("CID"+i));}
			{atLeast(1).of(compound).getPubChemId();will(returnValue("PubchemID"+i));}
			{atLeast(1).of(compound).getChEBIId();will(returnValue("ChEBIID"+i));}
			{atLeast(1).of(compound).getSmiles();will(returnValue("Smiles"+i));}
			{atLeast(1).of(compound).getIC();will(returnValue(new Integer(i).doubleValue()));}
		});
		return compound;
	}
	
	ICompound createMockCompoundForHasPArtAnnotation(final int i) {
		final ICompound compound = mockery.mock(ICompound.class);
		mockery.checking(new Expectations () {
			{allowing(compound).getInChI();will(returnValue("INCHI"));}
			{allowing(compound).getCID();will(returnValue("CID"+i));}
			{allowing(compound).getPubChemId();will(returnValue("PubchemID"+i));}
			{allowing(compound).getChEBIId();will(returnValue("ChEBIID"+i));}
			
		});
		return compound;
	}
	
	IMacromolecule createMockMacromolecule(final int i) {
		final IMacromolecule macro = mockery.mock(IMacromolecule.class);
		mockery.checking(new Expectations () {
			{atLeast(1).of(macro).getId();will(returnValue("macroID" + i));}
			{atLeast(1).of(macro).getASCIIName();will(returnValue("macroAsciiName" +i));}
			{atLeast(1).of(macro).getDescription();will(returnValue("macroDescription"+i));}
			{atLeast(1).of(macro).getDetailedDescription();will(returnValue("macroDetailedDescription"+i));}
			{atLeast(1).of(macro).getGOTerm();will(returnValue("macromolecule GO"+i));}
			{atLeast(1).of(macro).getUniProt();will(returnValue("macromoleculeUniprot"+i));}
			
		});
		return macro;
	}
}
