package uk.ac.ed.inf.Metabolic.sbmlexport;

import static org.junit.Assert.*;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMacromolecule;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

@RunWith(JMock.class)
public class EntittyBuilderTest {
    IEntityFactory entityFactory;
    SBMLDocument doc1;
    Mockery mockery = new JUnit4Mockery();
    Model sbmlModel;
    SBMLDocument doc;

	static {
		System.loadLibrary("sbmlj");
	}
	
	@Before
	public void setUp() throws Exception {
		entityFactory = new EntityBuilder();
		createSBMLModel();
	}

	private void createSBMLModel() {
		doc = new SBMLDocument();
		sbmlModel = doc.createModel("ModelID");
		sbmlModel.setName("ModelName");
		
	}
	@After
	public void tearDown() throws Exception {
	
	}


	
	@Test
	public void testBuildSpeciesAndCompartmentsFor3compartments() throws Exception{
		final IModel model = mockery.mock(IModel.class);
	
		final List<ICompartment> compartments = setUpCompartmentExpectations(0,2);
		mockery.checking(new Expectations () {
			{one(model).getCompartmentList();will(returnValue(compartments));}
			{ignoring(compartments.get(0));}
			{ignoring(compartments.get(1));}
			
		});
		entityFactory.buildSpeciesAndCompartments(sbmlModel, model);
		System.out.println(sbmlModel.getListOfCompartments().size());
		assertEquals(2L, sbmlModel.getListOfCompartments().size());
		assertEquals(0L,doc.checkL2v3Compatibility());
		assertEquals(0L,doc.checkConsistency());
		
	}
	
	@Test
	public void testBuildSpeciesAndCompartmentsFor2compartmentsAnd1Child() throws Exception{
		final IModel model = mockery.mock(IModel.class);
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
			
			{ignoring(parentcompartments.get(1));}
			{ignoring(childCompartments.get(0));}
	
			
			{one(model).getCompartmentList();will(returnValue(parentcompartments));}
		});
		
		entityFactory.buildSpeciesAndCompartments(sbmlModel, model);
		assertEquals(3L, sbmlModel.getListOfCompartments().size());
		assertEquals(0L,doc.checkL2v3Compatibility());
		assertEquals(0L,doc.checkConsistency());
		
	}
	
	@Test
	public void testBuildSpeciesAndCompartmentsForSingleCompoundInSingleCompartment()throws Exception {
		final IModel model = mockery.mock(IModel.class);
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
		entityFactory.buildSpeciesAndCompartments(sbmlModel, model);
		assertEquals(1L, sbmlModel.getListOfSpecies().size());
		assertEquals(0L,doc.checkL2v3Compatibility());
		assertEquals(0L,doc.checkConsistency());
		
	}
	
	@Test
	public void testBuildSpeciesAndCompartmentsForSingleMacromoleculeInSingleCompartment()throws Exception {
		final IModel model = mockery.mock(IModel.class);
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
			
			{ignoring(macromols.get(0));}
			
		});
		entityFactory.buildSpeciesAndCompartments(sbmlModel, model);
		assertEquals(1L, sbmlModel.getListOfSpecies().size());
		assertEquals(0L,doc.checkL2v3Compatibility());
		assertEquals(0L,doc.checkConsistency());
		
	}
	
	@Test
	public void testBuildSpeciesAndCompartmentsForSingleMacromoleculeWithChildCompoundInSingleCompartment()throws Exception {
		final IModel model = mockery.mock(IModel.class);
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
		entityFactory.buildSpeciesAndCompartments(sbmlModel, model);
	assertEquals(1L, sbmlModel.getListOfSpecies().size());
	assertEquals(0L,doc.checkL2v3Compatibility());
	assertEquals(0L,doc.checkConsistency());
	
		
		
	}
	
	private List<ICompartment> setUpCompartmentExpectations(int startID, int numCompartments) {
	    List<ICompartment> rc = new ArrayList<ICompartment>();
	    for (int i=startID; i<startID +numCompartments; i++){
	    	ICompartment mock = getMockCompartment(i, "compartment" + i);
	    	rc.add(mock);
	    }
	    return rc;
	    
	}
	
	
	ICompartment getMockCompartment(final int i, String name) {
		final ICompartment compound = mockery.mock(ICompartment.class, name);
		mockery.checking(new Expectations () {
			{atLeast(1).of(compound).getId();will(returnValue("CompID" + i));}
			{atLeast(1).of(compound).getASCIIName();will(returnValue("CompAsciiName" +i));}
			{atLeast(1).of(compound).getDescription();will(returnValue("CompDescription"+i));}
			{atLeast(1).of(compound).getDetailedDescription();will(returnValue("CompDetailedDescription"+i));}
			{atLeast(1).of(compound).getVolume();will(returnValue(new Integer(i+1).doubleValue()));}
			{atLeast(1).of(compound).getGOTerm();will(returnValue("GOTerm"+i));}
		});
		return compound;
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
