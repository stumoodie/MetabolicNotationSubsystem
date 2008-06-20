package uk.ac.ed.inf.Metabolic.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pathwayeditor.contextadapter.toolkit.ndom.ModelObject;

import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;
import uk.ac.ed.inf.Metabolic.ndomAPI.IRelation;

public class MetabolicReaction  extends ModelObject implements IReaction {

	//Properties
	private String eCNumber;
	private String kineticLaw;
	private String parameters;
	private boolean reversible;
	
	//Lists
	private List<IRelation> activatorList=new ArrayList<IRelation>();
	private List<IRelation> inhibitorList=new ArrayList<IRelation>();
	private List<IRelation> catalystList=new ArrayList<IRelation>();
	private List<IRelation> productList=new ArrayList<IRelation>();
	private List<IRelation> substrateList=new ArrayList<IRelation>();
	
	
	public MetabolicReaction(String id, String name, String asciiName) {
		super(id,name,asciiName);
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
 * $Log$
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