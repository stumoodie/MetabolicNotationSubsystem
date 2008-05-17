package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.util.Collections;
import java.util.List;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;

public class DummyModel implements IModel {

	public List<String> getCIDList() {
		return Collections.EMPTY_LIST;
	}

	public List<String> getChEBIList() {
		return Collections.EMPTY_LIST;
	}

	public List<ICompartment> getCompartmentList() {
		return Collections.EMPTY_LIST;
	}

	public List<String> getECnumberList() {
		return Collections.EMPTY_LIST;
	}

	public List<String> getGOTermList() {
		return Collections.EMPTY_LIST;
	}

	public List<String> getInChIList() {
		return Collections.EMPTY_LIST;
	}

	public List<String> getPubChemList() {
		return Collections.EMPTY_LIST;
	}

	public List<IReaction> getReactionList() {
		return Collections.EMPTY_LIST;
	}

	public List<String> getRoleList() {
		return Collections.EMPTY_LIST;
	}

	public List<String> getSmilesList() {
		return Collections.EMPTY_LIST;
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
