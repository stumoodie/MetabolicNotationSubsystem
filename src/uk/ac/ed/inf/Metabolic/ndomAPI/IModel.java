package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;
import java.util.Set;

import org.pathwayeditor.contextadapter.toolkit.ndom.IModelObject;

public interface IModel extends IModelObject {
	public List<ICompartment> getCompartmentList();

	public List<IReaction> getReactionList();
	
	//Defined properties dictionaries
	public Set<String> getECnumberList();
	
	public Set<String> getSmilesList();
	
	public Set<String> getRoleList();
	
	public Set<String> getCIDList();

	public Set<String> getChEBIList();

	public Set<String> getPubChemList();

	public Set<String> getInChIList();

	public Set<String> getGOTermList();

/*
//getters for object from property 
 	public List<String> get();

	public List<String> get();

	public List<String> get();

	public List<String> get();

*/
}
