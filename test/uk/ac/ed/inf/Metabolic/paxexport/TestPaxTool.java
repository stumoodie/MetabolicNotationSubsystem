/**
 * 
 */
package uk.ac.ed.inf.Metabolic.paxexport;


import java.io.FileOutputStream;

import org.biopax.paxtools.impl.level3.Level3FactoryImpl;
import org.biopax.paxtools.io.jena.JenaIOHandler;
import org.biopax.paxtools.io.simpleIO.SimpleExporter;
import org.biopax.paxtools.model.BioPAXFactory;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.model.level3.BiochemicalReaction;
import org.biopax.paxtools.model.level3.Level3Factory;
import org.biopax.paxtools.model.level3.SmallMolecule;
import org.biopax.paxtools.model.level3.SmallMoleculeReference;
import org.biopax.paxtools.model.level3.Stoichiometry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Anatoly Sorokin
 *
 */
public class TestPaxTool {
	static Level3Factory factory;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory=new Level3FactoryImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	Model model;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		model=factory.createModel();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		SimpleExporter exp=new SimpleExporter(factory.getLevel());
		FileOutputStream fos=new FileOutputStream("testModel"+System.currentTimeMillis()+".owl");
		exp.convertToOWL(model, fos);
		fos.flush();
		fos.close();
		
	}

	@Test
	public void testModelCreation(){
		SmallMolecule sm1=factory.createSmallMolecule();
		sm1.setDisplayName("H2O");
		sm1.addName("water");
		sm1.setRDFId("water");
		model.add(sm1);
		SmallMolecule sm2=factory.createSmallMolecule();
		sm2.setDisplayName("O2");
		sm2.addName("oxygen");
		sm2.setRDFId("oxygen");
		model.add(sm2);
		SmallMolecule sm3=factory.createSmallMolecule();
		sm3.setDisplayName("H2");
		sm3.addName("hydrogen");
		sm3.setRDFId("hydrogen");
		model.add(sm3);
		BiochemicalReaction r=factory.createBiochemicalReaction();
		r.setRDFId("reaction");
		Stoichiometry st1=factory.createStoichiometry();
		st1.setPhysicalEntity(sm1);
		st1.setStoichiometricCoefficient(2.0f);
		st1.setRDFId("st1");
		model.add(st1);
		r.addLeft(sm1);
		r.addParticipantStoichiometry(st1);
//		Stoichiometry st2=factory.createStoichiometry();
//		st2.setPhysicalEntity(sm2);
//		st2.setStoichiometricCoefficient(1.0f);
//		st2.setRDFId("st2");
//		model.add(st2);
//		r.addParticipantStoichiometry(st2);
		r.addRight(sm2);
		Stoichiometry st3=factory.createStoichiometry();
		st3.setPhysicalEntity(sm3);
		st3.setStoichiometricCoefficient(2.0f);
		st3.setRDFId("st3");
		model.add(st3);
		r.addRight(sm3);
		r.addParticipantStoichiometry(st3);
		model.add(r);
	}
}
