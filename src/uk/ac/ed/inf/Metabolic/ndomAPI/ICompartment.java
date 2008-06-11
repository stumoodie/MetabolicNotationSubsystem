package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

import org.pathwayeditor.contextadapter.toolkit.ndom.IModelObject;

public interface ICompartment extends IModelObject {
	public List<ICompound> getCompoundList();

	public List<IMacromolecule> getMacromoleculeList();

	public String getGOTerm();

	public double getVolume();
  	public List<ICompartment>getChildCompartments();
  	public ICompartment getParentCompartment();
}