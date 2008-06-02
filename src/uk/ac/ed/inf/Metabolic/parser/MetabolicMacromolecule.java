package uk.ac.ed.inf.Metabolic.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMacromolecule;
import uk.ac.ed.inf.Metabolic.ndomAPI.IRelation;

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
 * $Log$
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */