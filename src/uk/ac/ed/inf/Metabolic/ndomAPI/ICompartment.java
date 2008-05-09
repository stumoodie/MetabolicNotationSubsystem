package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

public interface ICompartment extends IModelObject {
	public List<ICompound> getCompoundList();

	public List<IMacromolecule> getMacromoleculeList();

	public String getGOTerm();

	public double getVolume();
  	public List<ICompartment>getChildCompartments();
  	public ICompartment getParentCompartment();
}
