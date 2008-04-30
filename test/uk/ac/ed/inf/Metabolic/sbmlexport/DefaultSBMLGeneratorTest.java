package uk.ac.ed.inf.Metabolic.sbmlexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pathwayeditor.businessobjectsAPI.ILink;
import org.pathwayeditor.businessobjectsAPI.IRootMapObject;
import org.pathwayeditor.businessobjectsAPI.IShape;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.SpeciesReference;
import org.sbml.libsbml.libsbml;

import uk.ac.ed.inf.Metabolic.MetabolicContextAdapterSyntaxService;
import uk.ac.ed.inf.Metabolic.TestDiagramGenerator;

public class DefaultSBMLGeneratorTest {
    DefaultSBMLGenerator generator;
    TestDiagramGenerator diagramGenerator;
    IShape s1, pro, s3;
    ILink consume, produce;
    static boolean canRun;
  //uk.ac.ed.inf.Metabolic
    @BeforeClass 
    public static void loadNativeLibraries () throws Exception {
    	canRun = LibSBMLConfigManager.configure();
    	
    }
	@Before
	public void setUp() throws Exception {
		generator = new DefaultSBMLGenerator();
		diagramGenerator = new TestDiagramGenerator();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateEmptySBMLModelContainsOneCompartment() {
		
		IRootMapObject rmo =createEmptyDiagram(); 
		SBMLDocument doc = generator.generateSBMLModel(rmo);
		Model m = doc.getModel();
		assertEquals(1, m.getListOfCompartments().size());
		assertEquals(rmo.getId(), m.getListOfCompartments().get(0).getId());
	}
	
	@Test
	public void testGenerateSBMLWithTwoCompoundsAndAProcess() {
		
		IRootMapObject rmo =createDiagramWithUnconnectedTwoCompoundsAndAProcess(); 
		SBMLDocument doc = generator.generateSBMLModel(rmo);
		Model m = doc.getModel();
		assertEquals(2, m.getListOfSpecies().size());
		assertEquals(1, m.getListOfReactions().size());
		for (int i = 0; i < m.getListOfSpecies().size(); i++) {
			assertNotNull(m.getListOfSpecies().get(i).getId());
			assertNotNull(m.getListOfSpecies().get(i).getName());
		}
		System.out.println(libsbml.writeSBMLToString(doc));

	}
	
	
	@Test
	public void testReactantsBasicProcessWithOneSubstrateAndOneProduct() {
		IRootMapObject rmo =createDiagramWithTwoCompoundsAndAProcessLinkedByConsumeAndProduceLinks(); 
		SBMLDocument doc = generator.generateSBMLModel(rmo);
		Model m = doc.getModel();
		Reaction r = (Reaction)m.getListOfReactions().get(0);
		assertEquals(1, r.getListOfReactants().size());
		SpeciesReference sr = (SpeciesReference)r.getListOfReactants().get(0);
		assertEquals(s1.getId(),sr.getSpecies());
		

	}
	
	@Test
	public void testProductsOfBasicProcessWithOneSubstrateAndOneProduct() {
		IRootMapObject rmo =createDiagramWithTwoCompoundsAndAProcessLinkedByConsumeAndProduceLinks(); 
		SBMLDocument doc = generator.generateSBMLModel(rmo);
		Model m = doc.getModel();
		Reaction r = (Reaction)m.getListOfReactions().get(0);
		assertEquals(1, r.getListOfProducts().size());
		SpeciesReference sr = (SpeciesReference)r.getListOfProducts().get(0);
		assertEquals(s3.getId(),sr.getSpecies());


	}
	
	
	// e.g., S Pro P
	private IRootMapObject createDiagramWithUnconnectedTwoCompoundsAndAProcess() {
		IRootMapObject rmo = createEmptyDiagram();
		addSubsrate_PRocess_Product(rmo);
		return rmo;
	}
	
	// e.g., S->Pro->P
	private IRootMapObject createDiagramWithTwoCompoundsAndAProcessLinkedByConsumeAndProduceLinks() {
		IRootMapObject rmo = createEmptyDiagram();
		addSubsrate_PRocess_Product(rmo);
		addConnections();
		return rmo;
	}
	
	
	
	private void addConnections() {
		consume = diagramGenerator.createLink(MetabolicContextAdapterSyntaxService.ObjectTypes.Consume);
		produce = diagramGenerator.createLink(MetabolicContextAdapterSyntaxService.ObjectTypes.Produce);
		consume.reconnect(s1, pro);
		produce.reconnect(pro, s3);
		
	}
	private void addSubsrate_PRocess_Product(IRootMapObject rmo) {
		 s1 = diagramGenerator.createShape(MetabolicContextAdapterSyntaxService.ObjectTypes.Compound);
		s1.setName("substrate");
		 pro = diagramGenerator.createShape(MetabolicContextAdapterSyntaxService.ObjectTypes.Process);
		pro.setName("process");
		 s3 = diagramGenerator.createShape(MetabolicContextAdapterSyntaxService.ObjectTypes.Compound);
		s3.setName("product");
		rmo.addChild(s1);
		rmo.addChild(pro);
		rmo.addChild(s3);
	}
	private IRootMapObject createEmptyDiagram() {
		return diagramGenerator.createRootMapObject();
	}

}
