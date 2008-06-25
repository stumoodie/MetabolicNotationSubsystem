package uk.ac.ed.inf.Metabolic.parser;

import java.util.Collections;
import java.util.List;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;
import uk.ac.ed.inf.Metabolic.ndomAPI.IRelation;

/**
 * $Id$
 * @author Anatoly Sorokin
 * @date 16 May 2008
 * 
 */
public class MetabolicCompound extends MetabolicMolecule  implements ICompound {

	private String cID;
	private String chEBIId;
	private double iC;
	private String inChI;
	private String pubChemId;
	private String smiles;
	private int cloneNumber=0;
	public MetabolicCompound(String id, String name, String asciiName) {
		super(id,name,asciiName);
	}

	public MetabolicCompound(String cid, String chEBIId, double ic,
			String inChI, String pubChemId, String smiles, String description,
			String detailedDescription, String id, String name, String asciiName) {
		super(id,name,asciiName);
		cID = cid;
		this.chEBIId = chEBIId;
		iC = ic;
		this.inChI = inChI;
		this.pubChemId = pubChemId;
		this.smiles = smiles;
		setDescription(description);
		setDetailedDescription(detailedDescription);
	}

	public String getCID() {
		return cID;
	}

	public void setCID(String cid) {
		cID = cid;
	}

	public String getChEBIId() {
		return chEBIId;
	}

	public void setChEBIId(String chEBIId) {
		this.chEBIId = chEBIId;
	}

	public double getIC() {
		return iC;
	}

	public void setIC(double ic) {
		iC = ic;
	}

	public String getInChI() {
		return inChI;
	}

	public void setInChI(String inChI) {
		this.inChI = inChI;
	}

	public String getPubChemId() {
		return pubChemId;
	}

	public void setPubChemId(String pubChemId) {
		this.pubChemId = pubChemId;
	}

	public String getSmiles() {
		return smiles;
	}

	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}

	public List<IRelation> getActivatoryRelationList() {
		return Collections.unmodifiableList(activatoryRelationList);
	}

	public List<IRelation> getCatalyticRelationList() {
		return Collections.unmodifiableList(catalyticRelationList);
	}

	public List<IRelation> getInhibitoryRelationList() {
		return Collections.unmodifiableList(inhibitoryRelationList);
	}

	public List<IRelation> getSinkList() {
		return Collections.unmodifiableList(sinkList);
	}

	public List<IRelation> getSourceList() {
		return Collections.unmodifiableList(sourceList);
	}

	//List mutation methods
	boolean addActivatoryRelation(IRelation rel) {
		return activatoryRelationList.add(rel);
	}

	boolean removeActivatoryRelation(IRelation rel) {
		return activatoryRelationList.remove(rel);
	}

	boolean addCatalyticRelation(IRelation rel) {
		return catalyticRelationList.add(rel);
	}

	boolean removeCatalyticRelation(IRelation rel) {
		return catalyticRelationList.remove(rel);
	}

	boolean addInhibitoryRelation(IRelation rel) {
		return inhibitoryRelationList.add(rel);
	}

	boolean removeInhibitoryRelation(IRelation rel) {
		return inhibitoryRelationList.remove(rel);
	}

	boolean addSink(IRelation rel) {
		return sinkList.add(rel);
	}

	boolean removeSink(IRelation rel) {
		return sinkList.remove(rel);
	}

	boolean addSsource(IRelation rel) {
		return sourceList.add(rel);
	}

	boolean removeSource(IRelation rel) {
		return sourceList.remove(rel);
	}

	public int getCloneNumber() {
		return cloneNumber;
	}

	public void setCloneNumber(int cloneNumber) {
		this.cloneNumber = cloneNumber;
	}
	
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer("MetabolicCompound:[");
		sb.append("\nID:").append(getId());
		sb.append("\nname:").append(getASCIIName());
		sb.append("\nDescription:").append(getDescription());
		sb.append("\nDetailedDescription:").append(getDetailedDescription());
		sb.append("\n]\n");
		return sb.toString();
	}

}


/*
 * $Log$
 * Revision 1.3  2008/06/25 10:41:01  ntsorman
 * Excel Export service for the Metabolic Context
 *
 * Revision 1.2  2008/06/20 22:48:19  radams
 * imports
 *
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */