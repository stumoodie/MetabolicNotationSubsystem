/**
 * 
 */
package uk.ac.ed.inf.Metabolic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pathwayeditor.contextadapter.toolkit.ndom.NdomException;

import uk.ac.ed.inf.Metabolic.ndomAPI.ERelType;

/**
 * $Id$
 * @author Anatoly Sorokin
 * @date 26 May 2008
 * 
 */
public class RelationTest {

	private MetabolicReaction r,r1;
	private MetabolicMolecule m,m1;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		r=new MetabolicReaction("r1","Reaction 1","reaction_1");
		r1=new MetabolicReaction("r2","Reaction 2","reaction_2");
		m=new MetabolicCompound("c1","Compound 1","compound_1");
		m1=new MetabolicCompound("c2","Compound 2","compound_2");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		r=null;
		m=null;
	}
	
	@Test
	public void testSetMolecule() throws NdomException{
		MetabolicRelation rel=new MetabolicRelation("l1","","",ERelType.Activation);
		m.addActivatoryRelation(rel);
		assertNotNull(rel.getMolecule());
		assertEquals(m,rel.getMolecule());
		m1.addActivatoryRelation(rel);
		assertEquals(m1,rel.getMolecule());
		assertEquals(0, m.getActivatoryRelationList().size());
	}
	
	@Test
	public void testSetReaction() throws NdomException{
		MetabolicRelation rel=new MetabolicRelation("l1","","",ERelType.Activation);
		r.addActivator(rel);
		assertNotNull(rel.getReaction());
		assertEquals(r,rel.getReaction());
		r1.addActivator(rel);
		assertEquals(r1,rel.getReaction());
		assertEquals(0, r.getActivatorList().size());
	}
}


/*
 * $Log: RelationTest.java,v $
 * Revision 1.2  2008/07/15 11:14:32  smoodie
 * Refactored so code compiles with new Toolkit framework.
 *
 * Revision 1.1  2008/06/02 10:32:56  asorokin
 * NDOM facility
 *
 */