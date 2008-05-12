package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

public interface IModel extends IModelObject {
	public List<ICompartment> getCompartmentList();

	public List<IReaction> getReactionList();
	
	//Defined properties dictionaries
	public List<String> getECnumberList();
	
	public List<String> getSmilesList();
	
	public List<String> getRoleList();
	
	public List<String> getCIDList();

	public List<String> getChEBIList();

	public List<String> getPubChemList();

	public List<String> getInChIList();

	public List<String> getGOTermList();

/*
//getters for object from property 
 	public List<String> get();

	public List<String> get();

	public List<String> get();

	public List<String> get();

*/
}
