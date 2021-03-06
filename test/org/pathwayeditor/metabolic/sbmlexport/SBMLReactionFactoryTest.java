/*
  Licensed to the Court of the University of Edinburgh (UofE) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The UofE licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
*/
package org.pathwayeditor.metabolic.sbmlexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.pathwayeditor.metabolic.ndomAPI.IMolecule;
import org.pathwayeditor.metabolic.ndomAPI.IReaction;
import org.pathwayeditor.metabolic.ndomAPI.IRelation;
import org.pathwayeditor.metabolic.sbmlexport.IReactionBuilder;
import org.pathwayeditor.metabolic.sbmlexport.SBMLReactionFactory;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.Species;
import org.sbml.libsbml.libsbml;

@RunWith(JMock.class)
public class SBMLReactionFactoryTest {
    Mockery mockery = new JUnit4Mockery();
    IReactionBuilder reactionBuilder;
    SBMLDocument doc;
    Model sbmlModel;

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
		System.out.println(libsbml.writeSBMLToString(doc));
	}
	
	final String SUB_1_ID="sub1";
	final String PROD_1_ID="prod11";
	final String ACTIVATOR_1_ID="prod11";

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
		assertEquals(0L,sbmlModel.getListOfReactions().size());
		assertEquals(0L,doc.checkL2v3Compatibility());
		
	}
	
	@Test
	public void testBuildReversibleReactionAddsSBMLREactionFor1ReactantAnd1Product() {
		final IModel model = mockery.mock(IModel.class);
		sbmlModel.addSpecies(new Species(SUB_1_ID));
		sbmlModel.addSpecies(new Species(PROD_1_ID));
		final List<IReaction> reactions = getMockReactions(1);
		
		setUpExpectationsForReactionWithNoModifiers(model, reactions);
		
		reactionBuilder.buildReactions(sbmlModel, model);
		assertEquals(1L,sbmlModel.getListOfReactions().size());
		assertTrue(SBMLVerificationUtilities.getSubstrateByIndex(sbmlModel,0,0).getSpecies().contains(SUB_1_ID));
		assertTrue(SBMLVerificationUtilities.getProductByIndex(sbmlModel,0,0).getSpecies().contains(PROD_1_ID));
		assertTrue(SBMLVerificationUtilities.getProductByIndex(sbmlModel,0,0).getSpecies().contains(PROD_1_ID));
		assertTrue(SBMLVerificationUtilities.getReactionByIndex(sbmlModel,0).getReversible());
		assertEquals(0L,((Reaction)sbmlModel.getListOfReactions().get(0)).getListOfModifiers().size());
		assertEquals(0L,doc.checkL2v3Compatibility());
		
	}
	
	@Test
	public void testBuildIrrReversibleReactionAddsSBMLREactionFor1ReactantAnd1Product() {
		final IModel model = mockery.mock(IModel.class);
		sbmlModel.addSpecies(new Species(SUB_1_ID));
		sbmlModel.addSpecies(new Species(PROD_1_ID));
		final List<IReaction> reactions = Arrays.asList(new IReaction[]{createMockIrreversibleReaction(0)});
		setUpExpectationsForReactionWithNoModifiers(model, reactions);
		
		reactionBuilder.buildReactions(sbmlModel, model);
		assertFalse(SBMLVerificationUtilities.getReactionByIndex(sbmlModel, 0).getReversible());
		assertEquals(0L,doc.checkL2v3Compatibility());
		
		
	}
	
	@Test
	public void testBuildReactionWithActivator() {
		final IModel model = mockery.mock(IModel.class);
		sbmlModel.addSpecies(new Species(SUB_1_ID));
		sbmlModel.addSpecies(new Species(PROD_1_ID));
		sbmlModel.addSpecies(new Species(ACTIVATOR_1_ID));
		final List<IReaction> reactions = Arrays.asList(new IReaction[]{createMockIrreversibleReaction(0)});
		setUpExpectationsForReactionWithModifiers(model, reactions);
		
		reactionBuilder.buildReactions(sbmlModel, model);
		Reaction r = SBMLVerificationUtilities.getReactionByIndex(sbmlModel, 0);
		assertFalse(r.getReversible());
		assertEquals(1L, r.getListOfModifiers().size());
		assertEquals(0L,doc.checkL2v3Compatibility());
		
		
	}
	
	private void setUpExpectationsForReactionWithNoModifiers(final IModel model, final List<IReaction> reactions) {
		final List<IRelation> substrates = getMockSubstrateRelations(1);
		final List<IRelation> products = getMockProductRelations(1);
		mockery.checking(new Expectations () {
			{one(model).getReactionList();will(returnValue(reactions));}
			{atLeast(1).of(reactions.get(0)).getSubstrateList();will(returnValue(substrates));}
			{atLeast(1).of(reactions.get(0)).getProductList();will(returnValue(products));}
			{atLeast(1).of(reactions.get(0)).getInhibitorList();will(returnValue(Collections.EMPTY_LIST));}
			{atLeast(1).of(reactions.get(0)).getActivatorList();will(returnValue(Collections.EMPTY_LIST));}
			{atLeast(1).of(reactions.get(0)).getCatalystList();will(returnValue(Collections.EMPTY_LIST));}
		});
		
	}
	
	private void setUpExpectationsForReactionWithModifiers(final IModel model, final List<IReaction> reactions) {
		final List<IRelation> substrates = getMockSubstrateRelations(1);
		final List<IRelation> products = getMockProductRelations(1);
		final List<IRelation> activators = getMockActivatorRelations(1);
		mockery.checking(new Expectations () {
			{one(model).getReactionList();will(returnValue(reactions));}
			{atLeast(1).of(reactions.get(0)).getSubstrateList();will(returnValue(substrates));}
			{atLeast(1).of(reactions.get(0)).getProductList();will(returnValue(products));}
			{atLeast(1).of(reactions.get(0)).getInhibitorList();will(returnValue(Collections.EMPTY_LIST));}
			{atLeast(1).of(reactions.get(0)).getActivatorList();will(returnValue(activators));}
			{atLeast(1).of(reactions.get(0)).getCatalystList();will(returnValue(Collections.EMPTY_LIST));}
		});
		
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
		final IMolecule compound=mockery.mock(IMolecule.class);
		mockery.checking(new Expectations () {
			{one(substrate).getStoichiometry();will(returnValue(1));}
			{one(substrate).getMolecule();will(returnValue(compound));}
			{one(compound).getId();will(returnValue(SUB_1_ID+i));}
			{one(substrate).getRole();will(returnValue("SUBSTRATE"));}
		});
		return substrate;
	}
	
	private List<IRelation> getMockProductRelations (int num) {
		List<IRelation> rc = new ArrayList<IRelation>();
		for (int i = 0; i< num; i++) {
			rc.add(createMockProductRelation(i, "product" + i, "compound" + i));
		}
		return rc;
	}
	
	private IRelation createMockActivatorRelation(final int i, String activatorName, String compoundName) {
		final IRelation  activator = mockery.mock(IRelation.class, activatorName);
		final IMolecule compound=mockery.mock(IMolecule.class, compoundName);
		mockery.checking(new Expectations () {
			{never(activator).getStoichiometry();will(returnValue(1));}
			{one(activator).getMolecule();will(returnValue(compound));}
			{one(compound).getId();will(returnValue(ACTIVATOR_1_ID+i));}
			{one(activator).getRole();will(returnValue("ACTIVATOR"));}
		});
		return activator;
	}
	
	private List<IRelation> getMockActivatorRelations (int num) {
		List<IRelation> rc = new ArrayList<IRelation>();
		for (int i = 0; i< num; i++) {
			rc.add(createMockActivatorRelation(i, "activator" + i, "activatorCompound" + i));
		}
		return rc;
	}
	
	private IRelation createMockProductRelation(final int i, String relationName, String compoundName) {
		final IRelation  product = mockery.mock(IRelation.class, "relationName");
		final IMolecule compound=mockery.mock(IMolecule.class, compoundName);
		mockery.checking(new Expectations () {
			{one(product).getStoichiometry();will(returnValue(1));}
			{one(product).getMolecule();will(returnValue(compound));}
			{one(compound).getId();will(returnValue(PROD_1_ID +i));}
			{one(product).getRole();will(returnValue("PRODUCT"));}
		});
		return product;
	}
	private List<IReaction> getMockReactions(int num) {
		List<IReaction> rc = new ArrayList<IReaction>();
		for (int i = 0; i< num; i++) {
			rc.add(createMockReversibleReaction(i));
		}
		return rc;
		
	}
	private IReaction createMockReversibleReaction(final int i) {
		final IReaction mockReaction = createBasicReaction(i);
		mockery.checking(new Expectations () {
			{allowing(mockReaction).isReversible();will(returnValue(true));}
	
		});
		return mockReaction;
	}
	
	private IReaction createMockIrreversibleReaction(final int i) {
		final IReaction mockReaction = createBasicReaction(i);
		mockery.checking(new Expectations () {
			{allowing(mockReaction).isReversible();will(returnValue(false));}
		});
		return mockReaction;
	}
	
	private IReaction createBasicReaction (final int i) {
		final IReaction mockReaction = mockery.mock(IReaction.class);
		mockery.checking(new Expectations () {
			{allowing(mockReaction).getId();will(returnValue("ReacID" + i));}
			{allowing(mockReaction).getASCIIName();will(returnValue("ReacName" +i));}
			{allowing(mockReaction).getDescription();will(returnValue("ReacDescription"+i));}
			{allowing(mockReaction).getDetailedDescription();will(returnValue("ReacDetailedDescription"+i));}
			{allowing(mockReaction).getECNumber();will(returnValue("1.1.1.1"));}
//			{allowing(mockReaction).getKineticLaw();will(returnValue("kinetic law"));}
//			{allowing(mockReaction).getKineticLaw();will(returnValue("<?xml version=\"1.0\" encoding=\"UTF-8\"?><kineticLaw><math xmlns=\"http://www.w3.org/1998/Math/MathML\"> <semantics definitionURL=\"http://biomodels.net/SBO/#SBO:0000062\"> <apply> <times/> <ci>k</ci> <ci>R1</ci> <ci>R2</ci> </apply> </semantics> </math> <listOfParameters> <parameter id=\"k\" value=\"0.1\"/> </listOfParameters></kineticLaw>"));}
			{allowing(mockReaction).getKineticLaw();will(returnValue("<math xmlns=\"http://www.w3.org/1998/Math/MathML\"> <semantics definitionURL=\"http://biomodels.net/SBO/#SBO:0000062\"> <apply> <times/> <ci>k</ci> <ci>R1</ci> <ci>R2</ci> </apply> </semantics> </math>"));}
			{allowing(mockReaction).getParameters();will(returnValue("k=0.1;f=1e-6;"));}
		});
		return mockReaction;
	}
		

}
