package uk.ac.ed.inf.Metabolic.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pathwayeditor.contextadapter.toolkit.ndom.INdomModel;
import org.pathwayeditor.contextadapter.toolkit.ndom.ModelObject;
import org.pathwayeditor.contextadapter.toolkit.ndom.NdomException;

import uk.ac.ed.inf.Metabolic.ndomAPI.ERelType;
import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMolecule;
import uk.ac.ed.inf.Metabolic.ndomAPI.IRelation;

public abstract class MetabolicMolecule  extends ModelObject implements IMolecule {

	private INdomModel parent;
	protected List<IRelation> activatoryRelationList = new ArrayList<IRelation>();
	protected List<IRelation> inhibitoryRelationList = new ArrayList<IRelation>();
	protected List<IRelation> catalyticRelationList = new ArrayList<IRelation>();
	protected List<IRelation> sinkList = new ArrayList<IRelation>();
	protected List<IRelation> sourceList = new ArrayList<IRelation>();

	public MetabolicMolecule(String id, String name, String name2) {
		super(id,name,name2);
	}

	public List<IRelation> getActivatoryRelationList() {
		return Collections.unmodifiableList(activatoryRelationList);
	}

	public List<IRelation> getInhibitoryRelationList() {
		return Collections.unmodifiableList(inhibitoryRelationList);
	}

	public List<IRelation> getCatalyticRelationList() {
		return Collections.unmodifiableList(catalyticRelationList);
	}

	public List<IRelation> getSinkList() {
		return Collections.unmodifiableList(sinkList);
	}

	public List<IRelation> getSourceList() {
		return Collections.unmodifiableList(sourceList);
	}

	boolean addActivatoryRelation(MetabolicRelation rel) throws NdomException {
		if (rel.getType() != ERelType.Activation)
			throw new NdomException("Expected activation, got: "
					+ rel.getType().toString());
		rel.setMolecule(this);
		return activatoryRelationList.add(rel);
	}

	boolean removeActivatoryRelation(MetabolicRelation rel) {
		return activatoryRelationList.remove(rel);
	}

	boolean addCatalyticRelation(MetabolicRelation rel) throws NdomException {
		if (rel.getType() != ERelType.Catalysis)
			throw new NdomException("Expected catalysis, got: "
					+ rel.getType().toString());
		rel.setMolecule(this);
		return catalyticRelationList.add(rel);
	}

	boolean removeCatalyticRelation(MetabolicRelation rel) {
		return catalyticRelationList.remove(rel);
	}

	boolean addInhibitoryRelation(MetabolicRelation rel) throws NdomException {
		if (rel.getType() != ERelType.Inhibition)
			throw new NdomException("Expected inhibition, got: "
					+ rel.getType().toString());
		rel.setMolecule(this);
		return inhibitoryRelationList.add(rel);
	}

	boolean removeInhibitoryRelation(MetabolicRelation rel) {
		return inhibitoryRelationList.remove(rel);
	}

	boolean addSink(MetabolicRelation rel) throws NdomException {
		if (rel.getType() != ERelType.Consumption)
			throw new NdomException("Expected consumption, got: "
					+ rel.getType().toString());
		rel.setMolecule(this);
		return sinkList.add(rel);
	}

	boolean removeSink(MetabolicRelation rel) {
		return sinkList.remove(rel);
	}

	boolean addSource(MetabolicRelation rel) throws NdomException {
		if (rel.getType() != ERelType.Production)
			throw new NdomException("Expected Production, got: "
					+ rel.getType().toString());
		rel.setMolecule(this);
		return sourceList.add(rel);
	}

	boolean removeSource(MetabolicRelation rel) {
		return sourceList.remove(rel);
	}

	public INdomModel getParent() {
		return parent;
	}

	public void setParent(INdomModel parent) {
		this.parent = parent;
	}

	/**
	 * Is molecule independent in the model. Any molecule could exists as it is
	 * in the model, or it could be part of bigger macromolecule. This method
	 * returns <code>true</code> if molecule is part of bigger molecule.
	 * 
	 * @return true if contains within another molecule
	 */
	public boolean isSubunit() {
		return !(getParent() instanceof ICompartment);
	}

}

/*
 * $Log: MetabolicMolecule.java,v $
 * Revision 1.2  2008/07/15 11:14:32  smoodie
 * Refactored so code compiles with new Toolkit framework.
 *
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */