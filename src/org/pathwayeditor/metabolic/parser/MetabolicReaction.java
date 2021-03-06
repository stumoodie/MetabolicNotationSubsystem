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
import org.pathwayeditor.metabolic.ndomAPI.IReaction;
import org.pathwayeditor.metabolic.ndomAPI.IRelation;


public class MetabolicReaction  extends ModelObject implements IReaction {

	//Properties
	private String eCNumber;
	private String kineticLaw;
	private String parameters;
	private boolean reversible;
//	private MetabolicModel parentModel;
	
	//Lists
	private List<IRelation> activatorList=new ArrayList<IRelation>();
	private List<IRelation> inhibitorList=new ArrayList<IRelation>();
	private List<IRelation> catalystList=new ArrayList<IRelation>();
	private List<IRelation> productList=new ArrayList<IRelation>();
	private List<IRelation> substrateList=new ArrayList<IRelation>();
	
	
	public MetabolicReaction(String id, String name, String asciiName) {
		super(id,name,asciiName);
	}

	

	public MetabolicReaction(String id, IDrawingNode mapObject, MetabolicModel m)
			throws IllegalArgumentException {
		super(id, mapObject);
//		parentModel=m;
	}



	public MetabolicReaction(String description, String detailedDescription,
			String id, String name, String asciiName, String number,
			String kineticLaw, boolean reversible,
			List<IRelation> activatorList, List<IRelation> inhibitorList,
			List<IRelation> catalystList, List<IRelation> productList,
			List<IRelation> substrateList) {
		super(id,name,asciiName);
		setDescription(description);
		setDetailedDescription(detailedDescription);
		eCNumber = number;
		this.kineticLaw = kineticLaw;
		this.reversible = reversible;
		this.activatorList = activatorList;
		this.inhibitorList = inhibitorList;
		this.catalystList = catalystList;
		this.productList = productList;
		this.substrateList = substrateList;
	}


	public String getECNumber() {
		return eCNumber;
	}


	public String getKineticLaw() {
		return kineticLaw;
	}


	public boolean isReversible() {
		return reversible;
	}


	public List<IRelation> getActivatorList() {
		return Collections.unmodifiableList(activatorList);
	}


	public List<IRelation> getInhibitorList() {
		return Collections.unmodifiableList(inhibitorList);
	}


	public List<IRelation> getCatalystList() {
		return Collections.unmodifiableList(catalystList);
	}


	public List<IRelation> getProductList() {
		return Collections.unmodifiableList(productList);
	}


	public List<IRelation> getSubstrateList() {
		return Collections.unmodifiableList(substrateList);
	}


	public void setECNumber(String number) {
		eCNumber = number;
	}


	public void setKineticLaw(String kineticLaw) {
		this.kineticLaw = kineticLaw;
	}


	public void setReversible(String reversible) {
		this.reversible = ("reversible".equalsIgnoreCase(reversible));
	}
	
	//List mutation methods
	boolean addActivator(MetabolicRelation rel) {
		rel.setReaction(this);
		return activatorList.add(rel);
	}

	boolean removeActivator(MetabolicRelation rel) {
		return activatorList.remove(rel);
	}
	
	boolean addCatalyst(MetabolicRelation rel) {
		rel.setReaction(this);
		return catalystList.add(rel);
	}

	boolean removeCatalyst(MetabolicRelation rel) {
		return catalystList.remove(rel);
	}
	
	
	boolean addInhibitor(MetabolicRelation rel) {
		rel.setReaction(this);
		return inhibitorList.add(rel);
	}

	boolean removeInhibitor(MetabolicRelation rel) {
		return inhibitorList.remove(rel);
	}
	
	
	boolean addSubstrate(MetabolicRelation rel) {
		rel.setReaction(this);
		return substrateList.add(rel);
	}

	boolean removeSubstrate(MetabolicRelation rel) {
		return substrateList.remove(rel);
	}

	boolean addProduct(MetabolicRelation rel) {
		rel.setReaction(this);
		return productList.add(rel);
	}

	boolean removeProduct(MetabolicRelation rel) {
		return productList.remove(rel);
	}
	
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer("MetabolicReaction:[");
		sb.append("\nID:").append(getId());
		sb.append("\nname:").append(getASCIIName());
		sb.append("\nDescription:").append(getDescription());
		sb.append("\nDetailedDescription:").append(getDetailedDescription());
		sb.append("\nSubstrates:{\n");
		for(IRelation c:substrateList ){
			sb.append(c.toString());
		}
		sb.append("\n}\nProducts:{\n");
		for(IRelation c:productList ){
			sb.append(c.toString());
		}
		sb.append("\n}\n]\n");
		return sb.toString();
	}


	public String getParameters() {
		return parameters;
	}


	void setParameters(String parameters) {
		this.parameters = parameters;
	}

}


/*
 * $Log: MetabolicReaction.java,v $
 * Revision 1.3  2008/06/20 22:48:19  radams
 * imports
 *
 * Revision 1.2  2008/06/02 15:15:13  asorokin
 * KineticLaw parameters parsing and validation
 *
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */