package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

public interface IModel extends IModelObject {
	public List<ICompartment> getCompartmentList();

	public List<IReaction> getReactionList();
}
