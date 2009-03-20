package uk.ac.ed.inf.Metabolic.excelexport;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ed.inf.Metabolic.ndomAPI.ERelType;
import uk.ac.ed.inf.Metabolic.parser.CompoundTestDouble;
import uk.ac.ed.inf.Metabolic.parser.MacromoleculeTestDouble;
import uk.ac.ed.inf.Metabolic.parser.MetabolicCompartment;
import uk.ac.ed.inf.Metabolic.parser.MetabolicCompartmentTestStub;
import uk.ac.ed.inf.Metabolic.parser.MetabolicRelation;
import uk.ac.ed.inf.Metabolic.parser.ModelTestDouble;
import uk.ac.ed.inf.Metabolic.parser.ReactionTestDouble;



public class ModelProcessingTest {
	
	ModelProcessor modelProcessor ;
	
	@Before
	public void setUp() throws Exception {
		
		ModelTestDouble NDOModel = new ModelTestDouble ("id", "name", null) ;
		
		MetabolicCompartmentTestStub RMO = new MetabolicCompartmentTestStub ( "ROOT_MAP_OBJECT1", "compartment_name", "compartment_asciiName",
				NDOModel) ; 
		
		NDOModel.addCompartment(RMO);
		RMO.addCompound( new CompoundTestDouble ("childCompound1", "compound1", "asciiName") ) ;
		RMO.addCompound( new CompoundTestDouble ("childCompound2", "compound2", "asciiName") ) ;
		RMO.addChildCompartment(new MetabolicCompartment ( "Compartment2", "compartment_name2", "compartment_asciiName",
				NDOModel) );
		

		MetabolicCompartmentTestStub childCompartment = new MetabolicCompartmentTestStub ( "childComparment", "compartment_name", "compartment_asciiName",
				NDOModel) ;
		
		childCompartment.addCompound( new CompoundTestDouble ("childCompound3", "compound3", "asciiName") ) ;
		childCompartment.addCompound( new CompoundTestDouble ("childCompound4", "compound4", "asciiName") ) ;
		childCompartment.addChildCompartment(new MetabolicCompartment ( "Compartment3", "compartment_name3", "compartment_asciiName",
				NDOModel) );
		childCompartment.addMacromolecule(new MacromoleculeTestDouble (
											"Macromolecule1", "macromolecule1", "asciiName")) ;						
		
		RMO.addChildCompartment(childCompartment) ;
		
		MacromoleculeTestDouble childMacromolecule = new MacromoleculeTestDouble (
				"Macromolecule1", "macromolecule1", "asciiName") ;
		
		CompoundTestDouble compound5 = new CompoundTestDouble ("childCompound5", "compound5", "asciiName") ;
		CompoundTestDouble compound6 = new CompoundTestDouble ("childCompound6", "compound6", "asciiName") ;
		
		childMacromolecule.addCompound(compound5) ;
		childMacromolecule.addCompound(compound6) ;
		
		RMO.addMacromolecule(childMacromolecule) ;
		
		
		
		MetabolicRelation relation1 = new MetabolicRelation ("relation1", "relation1", "asciiName" , ERelType.Activation) ;
		compound5.addActivatoryRelation(relation1) ;
		
		MetabolicRelation relation2 = new MetabolicRelation ("relation2", "relation2", "asciiName" , ERelType.Production) ;
		compound6.addSsource(relation2) ;
		
		ReactionTestDouble reaction1 = new ReactionTestDouble ("reaction1", "reaction1", "asciiName") ;
		
		
		reaction1.addActivator(relation1) ;
		reaction1.addProduct(relation2) ;
		
		modelProcessor = new ModelProcessor ( NDOModel ) ;
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGiveCompounds () {
		assertEquals( "giveCompounds",6 , modelProcessor.giveCompounds().size() ) ;
	}
	
	@Test
	public void testGiveCompartments () {
		assertEquals ( "giveCompartments",3 , modelProcessor.giveCompartments().size() ) ;
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
