package uk.ac.ed.inf.Metabolic.sbmlexport;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.SimpleSpeciesReference;
import org.sbml.libsbml.Species;
import org.sbml.libsbml.SpeciesReference;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;
import uk.ac.ed.inf.Metabolic.ndomAPI.IRelation;
@RunWith(JMock.class)
public class SBMLReactionFactoryTest {
    Mockery mockery = new JUnit4Mockery();
    IReactionBuilder reactionBuilder;
    static boolean canRun;
    SBMLDocument doc;
    Model sbmlModel;
	@BeforeClass 
    public static void loadNativeLibraries () throws Exception {
    	canRun = LibSBMLConfigManager.configure();
    	
    }
	@Before
	public void setUp() throws Exception {
		reactionBuilder= new SBMLReactionFactory();
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
	
	final String SUB_1_ID="sub1";
	final String PROD_1_ID="prod11";

	@Test
	public void testBuildReactionDoesNotAddSBMLREactionIfNoSubstrateOrProductDefined() {
		final IModel model = mockery.mock(IModel.class);
		sbmlModel.addSpecies(new Species(SUB_1_ID));
		sbmlModel.addSpecies(new Species(PROD_1_ID));
		final List<IReaction> reactions = getMockReactions(1);
		mockery.checking(new Expectations () {
			{one(model).getReactionList();will(returnValue(reactions));}
			{atLeast(1).of(reactions.get(0)).getSubstrateList();will(returnValue(Collections.EMPTY_LIST));}
			{atLeast(1).of(reactions.get(0)).getProductList();will(returnValue(Collections.EMPTY_LIST));}
			
		});
		
		reactionBuilder.buildReactions(sbmlModel, model);
		assertEquals(0,sbmlModel.getListOfReactions().size());
		
	}
	
	@Test
	public void testBuildReversibleReactionAddsSBMLREactionFor1ReactantAnd1Product() {
		final IModel model = mockery.mock(IModel.class);
		sbmlModel.addSpecies(new Species(SUB_1_ID));
		sbmlModel.addSpecies(new Species(PROD_1_ID));
		final List<IReaction> reactions = getMockReactions(1);
		
		setUpExpecations(model, reactions);
		
		reactionBuilder.buildReactions(sbmlModel, model);
		assertEquals(1,sbmlModel.getListOfReactions().size());
		assertTrue(getSubstrate(0).getSpecies().contains(SUB_1_ID));
		assertTrue(getProduct(0).getSpecies().contains(PROD_1_ID));
		assertTrue(getProduct(0).getSpecies().contains(PROD_1_ID));
		assertTrue(getReaction().getReversible());
		assertEquals(0,((Reaction)sbmlModel.getListOfReactions().get(0)).getListOfModifiers().size());
		
	}
	
	@Test
	public void testBuildIrrReversibleReactionAddsSBMLREactionFor1ReactantAnd1Product() {
		final IModel model = mockery.mock(IModel.class);
		sbmlModel.addSpecies(new Species(SUB_1_ID));
		sbmlModel.addSpecies(new Species(PROD_1_ID));
		final List<IReaction> reactions = Arrays.asList(new IReaction[]{createMockIrreversibleReaction(0)});
		setUpExpecations(model, reactions);
		
		
		reactionBuilder.buildReactions(sbmlModel, model);
	
		assertFalse(getReaction().getReversible());
		
		
	}
	
	private void setUpExpecations(final IModel model, final List<IReaction> reactions) {
		final List<IRelation> substrates = getMockSubstrateRelations(1);
		final List<IRelation> products = getMockProductRelations(1);
		mockery.checking(new Expectations () {
			{one(model).getReactionList();will(returnValue(reactions));}
			{atLeast(1).of(reactions.get(0)).getSubstrateList();will(returnValue(substrates));}
			{atLeast(1).of(reactions.get(0)).getProductList();will(returnValue(products));}
			{atLeast(1).of(reactions.get(0)).getInhibitorList();will(returnValue(Collections.EMPTY_LIST));}
			{atLeast(1).of(reactions.get(0)).getActovatorList();will(returnValue(Collections.EMPTY_LIST));}
			{atLeast(1).of(reactions.get(0)).getCatalystList();will(returnValue(Collections.EMPTY_LIST));}
		});
		
	}
	private Reaction getReaction() {
		return (Reaction)sbmlModel.getListOfReactions().get(0);
	}
	private SimpleSpeciesReference getProduct(int i) {
		Reaction r = ((Reaction)sbmlModel.getListOfReactions().get(0));
		return (SpeciesReference)r.getListOfProducts().get(i);
	}
	private SpeciesReference getSubstrate(int index) {
		Reaction r = ((Reaction)sbmlModel.getListOfReactions().get(0));
		return (SpeciesReference)r.getListOfReactants().get(index);
	}
	
	
	private List<IRelation> getMockSubstrateRelations (int num) {
		List<IRelation> rc = new ArrayList<IRelation>();
		for (int i = 0; i< num; i++) {
			rc.add(createMockSubstrateRelation(i));
		}
		return rc;
	}
	
	private IRelation createMockSubstrateRelation(final int i) {
		final IRelation  substrate = mockery.mock(IRelation.class);
		mockery.checking(new Expectations () {
			{one(substrate).getStoichiometry();will(returnValue(1));}
			{one(substrate).getId();will(returnValue(SUB_1_ID+i));}
		});
		return substrate;
	}
	
	private List<IRelation> getMockProductRelations (int num) {
		List<IRelation> rc = new ArrayList<IRelation>();
		for (int i = 0; i< num; i++) {
			rc.add(createMockProductRelation(i));
		}
		return rc;
	}
	
	private IRelation createMockProductRelation(final int i) {
		final IRelation  substrate = mockery.mock(IRelation.class);
		mockery.checking(new Expectations () {
			{one(substrate).getStoichiometry();will(returnValue(1));}
			{one(substrate).getId();will(returnValue(PROD_1_ID +i));}
		});
		return substrate;
	}
	private List<IReaction> getMockReactions(int num) {
		List<IReaction> rc = new ArrayList<IReaction>();
		for (int i = 0; i< num; i++) {
			rc.add(createMockReversibleReaction(i));
		}
		return rc;
		
	}
	private IReaction createMockReversibleReaction(final int i) {
		final IReaction mockReaction = mockery.mock(IReaction.class);
		mockery.checking(new Expectations () {
			{allowing(mockReaction).getId();will(returnValue("ReacID" + i));}
			{allowing(mockReaction).getASCIIName();will(returnValue("ReacName" +i));}
			{allowing(mockReaction).getDescription();will(returnValue("ReacDescription"+i));}
			{allowing(mockReaction).getDetailedDescription();will(returnValue("ReacDetailedDescription"+i));}
			{allowing(mockReaction).isReversible();will(returnValue(true));}
	
		});
		return mockReaction;
	}
	
	private IReaction createMockIrreversibleReaction(final int i) {
		final IReaction mockReaction = mockery.mock(IReaction.class);
		mockery.checking(new Expectations () {
			{allowing(mockReaction).getId();will(returnValue("ReacID" + i));}
			{allowing(mockReaction).getASCIIName();will(returnValue("ReacName" +i));}
			{allowing(mockReaction).getDescription();will(returnValue("ReacDescription"+i));}
			{allowing(mockReaction).getDetailedDescription();will(returnValue("ReacDetailedDescription"+i));}
			{allowing(mockReaction).isReversible();will(returnValue(false));}
	
		});
		return mockReaction;
	}

}