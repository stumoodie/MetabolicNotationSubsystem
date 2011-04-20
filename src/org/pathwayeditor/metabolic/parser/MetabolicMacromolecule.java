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
import org.pathwayeditor.metabolic.ndomAPI.ICompound;
import org.pathwayeditor.metabolic.ndomAPI.IMacromolecule;
import org.pathwayeditor.metabolic.ndomAPI.IRelation;


public class MetabolicMacromolecule extends MetabolicMolecule implements IMacromolecule {

	//specific properties
	private String gOTerm;
	private String uniProt;
	//Lists
	List<IMacromolecule> subunitList=new ArrayList<IMacromolecule>();
	List<ICompound> compoundList=new ArrayList<ICompound>();
	public MetabolicMacromolecule(String id, String name, String asciiName) {
		super(id,name,asciiName);
	}


	public MetabolicMacromolecule(String id, IDrawingNode mapObject,
			MetabolicModel m) throws IllegalArgumentException {
		super(id, mapObject, m);
	}


	public MetabolicMacromolecule(String description,
			String detailedDescription, String id, String name, String asciiName,
			String term, String uniProt, List<IMacromolecule> subunitList,
			List<ICompound> compoundList,
			List<IRelation> activatoryRelationList,
			List<IRelation> inhibitoryRelationList,
			List<IRelation> catalyticRelationList, List<IRelation> sinkList,
			List<IRelation> sourceList) {
		super(id,name,asciiName);
		setDescription(description);
		setDetailedDescription(detailedDescription);
		gOTerm = term;
		this.uniProt = uniProt;
		this.subunitList = subunitList;
		this.compoundList = compoundList;
		this.activatoryRelationList = activatoryRelationList;
		this.inhibitoryRelationList = inhibitoryRelationList;
		this.catalyticRelationList = catalyticRelationList;
		this.sinkList = sinkList;
		this.sourceList = sourceList;
	}


	public String getGOTerm() {
		return gOTerm;
	}


	public String getUniProt() {
		return uniProt;
	}


	public void setGOTerm(String term) {
		gOTerm = term;
	}


	public void setUniProt(String uniProt) {
		this.uniProt = uniProt;
	}

	boolean addSubunit(MetabolicMacromolecule e) {
		e.setParent(this);
		return subunitList.add(e);
	}


	boolean removeSubunit(MetabolicMolecule o) {
		o.setParent(null);
		return subunitList.remove(o);
	}
	
	boolean addCompound(MetabolicCompound e) {
		e.setParent(this);
		return compoundList.add(e);
	}


	boolean removeCompound(MetabolicMolecule o) {
		o.setParent(null);
		return compoundList.remove(o);
	}


	public List<IMacromolecule> getSubunitList() {
		return subunitList;
	}


	public List<ICompound> getCompoundList() {
		return Collections.unmodifiableList(compoundList);
	}
	


}


/*
 * $Log: MetabolicMacromolecule.java,v $
 * Revision 1.3  2008/07/15 11:14:32  smoodie
 * Refactored so code compiles with new Toolkit framework.
 *
 * Revision 1.2  2008/06/25 10:41:01  ntsorman
 * Excel Export service for the Metabolic Context
 *
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */