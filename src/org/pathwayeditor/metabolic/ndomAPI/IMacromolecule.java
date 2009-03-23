package org.pathwayeditor.metabolic.ndomAPI;

import java.util.List;

public interface IMacromolecule extends IMolecule {
	public String getGOTerm();

	public String getUniProt();

	public List<ICompound> getCompoundList();

	public List<IMacromolecule> getSubunitList();
}
