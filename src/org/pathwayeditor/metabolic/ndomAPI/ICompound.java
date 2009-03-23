package org.pathwayeditor.metabolic.ndomAPI;

public interface ICompound extends IMolecule {
	public double getIC();

	public String getInChI();

	public String getCID();

	public String getChEBIId();

	public String getPubChemId();

	public String getSmiles();
}
