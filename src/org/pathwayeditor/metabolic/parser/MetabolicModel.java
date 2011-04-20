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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pathwayeditor.metabolic.ndomAPI.ICompartment;
import org.pathwayeditor.metabolic.ndomAPI.ICompound;
import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.pathwayeditor.metabolic.ndomAPI.IReaction;
import org.pathwayeditor.metabolic.ndomAPI.IRelation;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.NdomException;


public class MetabolicModel implements IModel {

	private String description="";
	private String detailedDescription="";
	// Object identity
	private final String id;
	private final String name;
	private final String aSCIIName;

	private List<ICompartment> compartmentList = new ArrayList<ICompartment>();
	private List<IReaction> reactionList = new ArrayList<IReaction>();
	// Dictionaries
	// Compound
	private Map<String, List<ICompound>> compoundsByCID = new HashMap<String, List<ICompound>>();
	private Map<String, List<ICompound>> compoundsByChEBI = new HashMap<String, List<ICompound>>();
	private Map<String, List<ICompound>> compoundsByInChI = new HashMap<String, List<ICompound>>();
	private Map<String, List<ICompound>> compoundsBySmiles = new HashMap<String, List<ICompound>>();
	private Map<String, List<ICompound>> compoundsByPubChem = new HashMap<String, List<ICompound>>();

	// Realtions
	private Map<String, IRelation> relationsByRole = new HashMap<String, IRelation>();

	// Compartment
	private Map<String, ICompartment> compartmentByGO = new HashMap<String, ICompartment>();

	// Reactions
	private Map<String, List<IReaction>> reactionByEC = new HashMap<String, List<IReaction>>();

	// Macromolecules
//	private Map<String, IMacromolecule> macromoleculeByGO = new HashMap<String, IMacromolecule>();

	public MetabolicModel(String id, String name, String asciiName) {
		this.id = id;
		this.name = name;
		aSCIIName = asciiName;
	}

	// Macromolecule,Compartment,reaction
	public Set<String> getGOTermList() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getECnumberList() {
		// TODO Auto-generated method stub
		return null;
	}

	// Relations
	public Set<String> getRoleList() {
		return Collections.unmodifiableSet(relationsByRole.keySet());
	}

	// Compound lists
	public Set<String> getCIDList() {
		return Collections.unmodifiableSet(compoundsByCID.keySet());
	}

	public Set<String> getChEBIList() {
		return Collections.unmodifiableSet(compoundsByChEBI.keySet());
	}

	public Set<String> getInChIList() {
		return Collections.unmodifiableSet(compoundsByInChI.keySet());
	}

	public Set<String> getPubChemList() {
		return Collections.unmodifiableSet(compoundsByPubChem.keySet());
	}

	public Set<String> getSmilesList() {
		return Collections.unmodifiableSet(compoundsBySmiles.keySet());
	}

	public String getDescription() {
		return description;
	}

	public String getDetailedDescription() {
		return detailedDescription;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getASCIIName() {
		return aSCIIName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDetailedDescription(String detailedDescription) {
		this.detailedDescription = detailedDescription;
	}

	public List<ICompartment> getCompartmentList() {
		return Collections.unmodifiableList(compartmentList);
	}

	public List<IReaction> getReactionList() {
		return Collections.unmodifiableList(reactionList);
	}

	boolean addCompartment(MetabolicCompartment e) throws NdomException {
		registerCompartment(e);
		return compartmentList.add(e);
	}

	void registerCompartment(MetabolicCompartment e) throws NdomException {
		String go = e.getGOTerm();

		if (null != go && go.trim().length() > 0) {
			ICompartment l = compartmentByGO.get(go);
			if (null != l) {
				if (!l.equals(e))
					throw new NdomException(
							"Multiple Compartments with the same Identity: "
									+ go);
			}
			compartmentByGO.put(go, e);
		}
	}

	boolean removeCompartment(MetabolicCompartment o) {
		unregisterCompartment(o);
		return compartmentList.remove(o);
	}

	void unregisterCompartment(MetabolicCompartment o) {
		String go=o.getGOTerm();
		if(null!=go && go.trim().length()>0){
		compartmentByGO.remove(go);
		}
	}

	boolean addReaction(MetabolicReaction e) {
		String ec=e.getECNumber();
		if(ec!=null && ec.trim().length()>0){
			List<IReaction> l=reactionByEC.get(ec);
			if(l==null){
				l=new ArrayList<IReaction>();
				reactionByEC.put(ec,l);
			}
			l.add(e);
		}
		return reactionList.add(e);
	}

	boolean removeReaction(IReaction o) {
		return reactionList.remove(o);
	}

	void registerCompound(MetabolicCompound c) {
		registerCID(c);
		registerChEBI(c);
		registerInChI(c);
		registerPubChem(c);
		registerSmiles(c);
	}

	private void populateCompoundMap(MetabolicCompound c, String cid,
			Map<String, List<ICompound>> map) {
		if (null != cid && cid.trim().length() > 0) {
			List<ICompound> l = map.get(cid);
			if (null == l) {
				l = new ArrayList<ICompound>();
				map.put(cid, l);
			}
			l.add(c);
		}
	}

	private void registerCID(MetabolicCompound c) {
		String cid = c.getCID();
		Map<String, List<ICompound>> map = compoundsByCID;
		populateCompoundMap(c, cid, map);
	}

	private void registerChEBI(MetabolicCompound c) {
		String cid = c.getChEBIId();
		Map<String, List<ICompound>> map = compoundsByChEBI;
		populateCompoundMap(c, cid, map);
	}

	private void registerInChI(MetabolicCompound c) {
		String cid = c.getInChI();
		Map<String, List<ICompound>> map = compoundsByInChI;
		populateCompoundMap(c, cid, map);
	}

	private void registerPubChem(MetabolicCompound c) {
		String cid = c.getPubChemId();
		Map<String, List<ICompound>> map = compoundsByPubChem;
		populateCompoundMap(c, cid, map);
	}

	private void registerSmiles(MetabolicCompound c) {
		String cid = c.getSmiles();
		Map<String, List<ICompound>> map = compoundsBySmiles;
		populateCompoundMap(c, cid, map);
	}

	void unregisterCompound(MetabolicCompound c) {
		unregisterCID(c);
		unregisterChEBI(c);
		unregisterInChI(c);
		unregisterPubChem(c);
		unregisterSmiles(c);
	}

	private void unregisterCID(MetabolicCompound c) {
		String cid = c.getCID();
		Map<String, List<ICompound>> map = compoundsByCID;
		removeCompoundFromMap(c, cid, map);
	}

	private void unregisterChEBI(MetabolicCompound c) {
		String cid = c.getChEBIId();
		Map<String, List<ICompound>> map = compoundsByChEBI;
		removeCompoundFromMap(c, cid, map);
	}

	private void unregisterInChI(MetabolicCompound c) {
		String cid = c.getInChI();
		Map<String, List<ICompound>> map = compoundsByInChI;
		removeCompoundFromMap(c, cid, map);
	}

	private void unregisterPubChem(MetabolicCompound c) {
		String cid = c.getPubChemId();
		Map<String, List<ICompound>> map = compoundsByPubChem;
		removeCompoundFromMap(c, cid, map);
	}

	private void unregisterSmiles(MetabolicCompound c) {
		String cid = c.getSmiles();
		Map<String, List<ICompound>> map = compoundsBySmiles;
		removeCompoundFromMap(c, cid, map);
	}

	private void removeCompoundFromMap(MetabolicMolecule c, String cid,
			Map<String, List<ICompound>> map) {
		if (null != cid && cid.trim().length() > 0) {
			List<ICompound> l = map.get(cid);
			if (l != null) {
				l.remove(c);
			}
			if (l.size() == 0) {
				map.remove(cid);
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer("MetabolicModel:[");
		sb.append("\nname:").append(getASCIIName());
		sb.append("\nDescription:").append(getDescription());
		sb.append("\nDetailedDescription:").append(getDetailedDescription());
		sb.append("\nCompartments:").append(compartmentList.size());
		sb.append("\nReactions:").append(reactionList.size());
		sb.append("\n\tCompartments:{");
		for(ICompartment c:compartmentList){
			sb.append(c.toString());
		}
		sb.append("\n\t}\n\tReactions:{");
		for(IReaction r:reactionList){
			sb.append(r.toString());
		}
		sb.append("\n]\n");
		return sb.toString();
	}

	
}

/*
 * $Log: MetabolicModel.java,v $
 * Revision 1.2  2008/07/15 11:14:32  smoodie
 * Refactored so code compiles with new Toolkit framework.
 *
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */