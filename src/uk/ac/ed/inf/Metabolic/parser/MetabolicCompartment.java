package uk.ac.ed.inf.Metabolic.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pathwayeditor.contextadapter.toolkit.ndom.ModelObject;
import org.pathwayeditor.contextadapter.toolkit.ndom.NdomException;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMacromolecule;

public class MetabolicCompartment extends ModelObject implements ICompartment {

	private ICompartment parentCompartment;
	//specific properties
	private String gOTerm="";
	private double volume;
	
	//Lists
	private List<IMacromolecule> macromoleculeList=new ArrayList<IMacromolecule>();
	private List<ICompartment> childCompartments=new ArrayList<ICompartment>();
	private List<ICompound> compoundList=new ArrayList<ICompound>();
	private final MetabolicModel parentModel;

	
	public MetabolicCompartment(String id, String name, String asciiName,
			MetabolicModel m) {
		super(id,name,asciiName);
		parentModel=m;
	}
	
	
	public MetabolicCompartment(String description, String detailedDescription,
			String id, String name, String asciiName,
			ICompartment parentCompartment,MetabolicModel m, String term, double volume,
			List<IMacromolecule> macromoleculeList,
			List<ICompartment> childCompartments, List<ICompound> compoundList) {
		this(id,name,asciiName,m);
		this.parentCompartment = parentCompartment;
		setDescription(description);
		setDetailedDescription(detailedDescription);
		gOTerm = term;
		this.volume = volume;
		this.macromoleculeList = macromoleculeList;
		this.childCompartments = childCompartments;
		this.compoundList = compoundList;
	}
	
	public ICompartment getParentCompartment() {
		return parentCompartment;
	}
	public String getGOTerm() {
		return gOTerm;
	}
	public double getVolume() {
		return volume;
	}
	public List<ICompartment> getChildCompartments() {
		return Collections.unmodifiableList(childCompartments);
	}
	public List<ICompound> getCompoundList() {
		return Collections.unmodifiableList(compoundList);
	}
	public void setGOTerm(String term) {
		gOTerm = term;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}

	public List<IMacromolecule> getMacromoleculeList() {
		return Collections.unmodifiableList(macromoleculeList);
	}


	boolean addChildCompartment(MetabolicCompartment e) throws NdomException {
		parentModel.registerCompartment(e);
		e.setParentCompartment(this);
		return childCompartments.add(e);
	}


	boolean removeChildCompartment(MetabolicCompartment o) {
		getParentModel().unregisterCompartment(o);
		setParentCompartment(null);
		return childCompartments.remove(o);
	}

	boolean addMacromolecule(MetabolicMacromolecule e) {
		e.setParent(this);
		return macromoleculeList.add(e);
	}


	boolean removeMacromolecule(MetabolicMacromolecule o) {
		o.setParent(null);
		return macromoleculeList.remove(o);
	}
	
	boolean addCompound(MetabolicCompound e) {
		parentModel.registerCompound(e);
		e.setParent(this);
		return compoundList.add(e);
	}


	boolean removeCompound(MetabolicCompound o) {
		parentModel.unregisterCompound(o);
		o.setParent(null);
		return compoundList.remove(o);
	}


	public void setParentCompartment(ICompartment parentCompartment) {
		this.parentCompartment = parentCompartment;
	}


	public MetabolicModel getParentModel() {
		return parentModel;
	}

	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer("MetabolicCompartment:[");
		sb.append("\nID:").append(getId());
		sb.append("\nname:").append(getASCIIName());
		sb.append("\nDescription:").append(getDescription());
		sb.append("\nDetailedDescription:").append(getDetailedDescription());
		for(ICompound c:compoundList ){
			sb.append(c.toString());
		}
		sb.append("\n]\n");
		return sb.toString();
	}
}


/*
 * $Log: MetabolicCompartment.java,v $
 * Revision 1.2  2008/07/15 11:14:32  smoodie
 * Refactored so code compiles with new Toolkit framework.
 *
 * Revision 1.1  2008/06/02 10:31:41  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */