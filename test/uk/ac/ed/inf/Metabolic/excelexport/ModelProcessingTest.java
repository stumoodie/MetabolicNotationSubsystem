package uk.ac.ed.inf.Metabolic.excelexport;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ed.inf.Metabolic.excelexport.ModelProcessor;
import uk.ac.ed.inf.Metabolic.ndomAPI.ERelType;
import uk.ac.ed.inf.Metabolic.parser.MetabolicCompartment;
import uk.ac.ed.inf.Metabolic.parser.MetabolicRelation;
import uk.ac.ed.inf.Metabolic.parser.TestCompartment;
import uk.ac.ed.inf.Metabolic.parser.TestCompound;
import uk.ac.ed.inf.Metabolic.parser.TestMacromolecule;
import uk.ac.ed.inf.Metabolic.parser.TestModel;
import uk.ac.ed.inf.Metabolic.parser.TestReaction;


public class ModelProcessingTest {
	
	ModelProcessor modelProcessor ;
	
	@Before
	public void setUp() throws Exception {
		
		TestModel NDOModel = new TestModel ("id", "name", null) ;
		
		TestCompartment RMO = new TestCompartment ( "ROOT_MAP_OBJECT1", "compartment_name", "compartment_asciiName",
				NDOModel) ; 
		
		NDOModel.addCompartment(RMO);
		RMO.addCompound( new TestCompound ("childCompound1", "compound1", "asciiName") ) ;
		RMO.addCompound( new TestCompound ("childCompound2", "compound2", "asciiName") ) ;
		RMO.addChildCompartment(new MetabolicCompartment ( "Compartment2", "compartment_name2", "compartment_asciiName",
				NDOModel) );
		

		TestCompartment childCompartment = new TestCompartment ( "childComparment", "compartment_name", "compartment_asciiName",
				NDOModel) ;
		
		childCompartment.addCompound( new TestCompound ("childCompound3", "compound3", "asciiName") ) ;
		childCompartment.addCompound( new TestCompound ("childCompound4", "compound4", "asciiName") ) ;
		childCompartment.addChildCompartment(new MetabolicCompartment ( "Compartment3", "compartment_name3", "compartment_asciiName",
				NDOModel) );
		childCompartment.addMacromolecule(new TestMacromolecule (
											"Macromolecule1", "macromolecule1", "asciiName")) ;						
		
		RMO.addChildCompartment(childCompartment) ;
		
		TestMacromolecule childMacromolecule = new TestMacromolecule (
				"Macromolecule1", "macromolecule1", "asciiName") ;
		
		TestCompound compound5 = new TestCompound ("childCompound5", "compound5", "asciiName") ;
		TestCompound compound6 = new TestCompound ("childCompound6", "compound6", "asciiName") ;
		
		childMacromolecule.addCompound(compound5) ;
		childMacromolecule.addCompound(compound6) ;
		
		RMO.addMacromolecule(childMacromolecule) ;
		
		
		
		MetabolicRelation relation1 = new MetabolicRelation ("relation1", "relation1", "asciiName" , ERelType.Activation) ;
		compound5.addActivatoryRelation(relation1) ;
		
		MetabolicRelation relation2 = new MetabolicRelation ("relation2", "relation2", "asciiName" , ERelType.Production) ;
		compound6.addSsource(relation2) ;
		
		TestReaction reaction1 = new TestReaction ("reaction1", "reaction1", "asciiName") ;
		
		
		reaction1.addActivator(relation1) ;
		reaction1.addProduct(relation2) ;
		
		modelProcessor = new ModelProcessor ( NDOModel ) ;
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGiveCompounds () {
		assertEquals ( 6 , modelProcessor.giveCompounds().size() ) ;
	}
	
	@Test
	public void testGiveCompartments () {
		assertEquals ( 3 , modelProcessor.giveCompartments().size() ) ;
	}

	@Test
	public void testGiveMacromolecules () {
		assertEquals ( 2 , modelProcessor.giveMacromolecules().size() ) ;
	}
	
	@Test
	public void testGiveReactions () {
		assertEquals ( 1 , modelProcessor.giveReactions().size() ) ;
	}	
	
}
