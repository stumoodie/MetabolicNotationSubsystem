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
package org.pathwayeditor.metabolic.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingNode;
import org.pathwayeditor.metabolic.ndomAPI.ERelType;
import org.pathwayeditor.metabolic.ndomAPI.ICompartment;
import org.pathwayeditor.metabolic.ndomAPI.IMolecule;
import org.pathwayeditor.metabolic.ndomAPI.IRelation;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.INdomModel;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.NdomException;


public abstract class MetabolicMolecule  extends ModelObject implements IMolecule {

	private INdomModel parent;
	protected List<IRelation> activatoryRelationList = new ArrayList<IRelation>();
	protected List<IRelation> inhibitoryRelationList = new ArrayList<IRelation>();
	protected List<IRelation> catalyticRelationList = new ArrayList<IRelation>();
	protected List<IRelation> sinkList = new ArrayList<IRelation>();
	protected List<IRelation> sourceList = new ArrayList<IRelation>();
	private MetabolicModel parentModel;

	public MetabolicModel getParentModel() {
		return parentModel;
	}

	public void setParentModel(MetabolicModel parentModel) {
		this.parentModel = parentModel;
	}

	public MetabolicMolecule(String id, String name, String name2) {
		super(id,name,name2);
	}

	public MetabolicMolecule(String id, IDrawingNode mapObject,MetabolicModel m)
			throws IllegalArgumentException {
		super(id, mapObject);
		parentModel=m;
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