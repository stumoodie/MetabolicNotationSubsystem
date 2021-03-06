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


import org.pathwayeditor.businessobjects.drawingprimitives.ILinkEdge;
import org.pathwayeditor.metabolic.ndomAPI.ERelType;
import org.pathwayeditor.metabolic.ndomAPI.IMolecule;
import org.pathwayeditor.metabolic.ndomAPI.IReaction;
import org.pathwayeditor.metabolic.ndomAPI.IRelation;


public class MetabolicRelation extends ModelObject implements IRelation {

	private final ERelType type;

	private MetabolicReaction reaction;
	private String role;
	private String varName;
	private int stoichiometry;
	private MetabolicMolecule molecule;

	public MetabolicRelation(String id, String name, String asciiName,
			ERelType type) {
		super(id,name,asciiName);
		this.type = type;
	}
	
	

	public MetabolicRelation(String id, ILinkEdge mapObject,
			ERelType type)
			throws IllegalArgumentException {
		super(id, mapObject);
		this.type = type;
	}



	public MetabolicRelation(String description, String detailedDescription,
			String id, String name, String asciiName, ERelType type,
			MetabolicReaction reaction, String role, int stoichiometry,
			MetabolicMolecule molecule) {
		super(id,name,asciiName);
		this.type = type;
		this.reaction = reaction;
		this.role = role;
		this.stoichiometry = stoichiometry;
		this.molecule = molecule;
	}

	public IMolecule getMolecule() {
		return molecule;
	}

	public IReaction getReaction() {
		return reaction;
	}

	public String getRole() {
		return role;
	}

	public int getStoichiometry() {
		return stoichiometry;
	}

	public ERelType getType() {
		return type;
	}

	void setReaction(MetabolicReaction reaction) {
		if (this.reaction != null) {
			switch (type) {
			case Catalysis:
				this.reaction.removeCatalyst(this);
				break;
			case Activation:
				this.reaction.removeActivator(this);
				break;
			case Inhibition:
				this.reaction.removeInhibitor(this);
				break;
			case Consumption:
				this.reaction.removeSubstrate(this);
				break;
			case Production:
				this.reaction.removeProduct(this);
			default: // throw new NDOMFactory.NdomException("Unknown realtion
				// type");
			}

		}
		this.reaction = reaction;
	}

	void setRole(String role) {
		this.role = role;
	}

	void setStoichiometry(int stoichiometry) {
		this.stoichiometry = stoichiometry;
	}

	void setMolecule(MetabolicMolecule molecule) {
		if (this.molecule != null) {
			switch (type) {
			case Catalysis:
				this.molecule.removeCatalyticRelation(this);
				break;
			case Activation:
				this.molecule.removeActivatoryRelation(this);
				break;
			case Inhibition:
				this.molecule.removeInhibitoryRelation(this);
				break;
			case Consumption:
				this.molecule.removeSink(this);
				break;
			case Production:
				this.molecule.removeSource(this);
			default: // throw new NDOMFactory.NdomException("Unknown realtion
				// type");
			}

		}
		this.molecule = molecule;
	}
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer("MetabolicRelation:[");
		sb.append("\nID:").append(getId());
		sb.append("\nname:").append(getASCIIName());
		sb.append("\nDescription:").append(getDescription());
		sb.append("\nDetailedDescription:").append(getDetailedDescription());
		sb.append("\nStoichiometry:").append(getStoichiometry());
		sb.append("\nRole:").append(getRole());
		sb.append("\nMolecule:").append(getMolecule().getId());
		sb.append("\nReaction:").append(getReaction().getId());
		sb.append("\n]\n");
		return sb.toString();
	}

	String getVarName() {
		return varName;
	}

	void setVarName(String varName) {
		this.varName = varName;
	}

}

/*
 * $Log: MetabolicRelation.java,v $
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */