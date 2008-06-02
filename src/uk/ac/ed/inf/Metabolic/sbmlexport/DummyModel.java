package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;

public class DummyModel implements IModel {

	public Set<String> getCIDList() {
		return Collections.EMPTY_SET;
	}

	public Set<String> getChEBIList() {
		return Collections.EMPTY_SET;
	}

	public List<ICompartment> getCompartmentList() {
		return Collections.EMPTY_LIST;
	}

	public Set<String> getECnumberList() {
		return Collections.EMPTY_SET;
	}

	public Set<String> getGOTermList() {
		return Collections.EMPTY_SET;
	}

	public Set<String> getInChIList() {
		return Collections.EMPTY_SET;
	}

	public Set<String> getPubChemList() {
		return Collections.EMPTY_SET;
	}

	public List<IReaction> getReactionList() {
		return Collections.EMPTY_LIST;
	}

	public Set<String> getRoleList() {
		return Collections.EMPTY_SET;
	}

	public Set<String> getSmilesList() {
		return Collections.EMPTY_SET;
	}

	public String getASCIIName() {
		return "Dummy name";
	}

	public String getDescription() {
		return "";
	}

	public String getDetailedDescription() {
		return "";
	}

	public String getId() {
		return "modelID";
	}

	public String getName() {
		return "dummy name";
	}

}
