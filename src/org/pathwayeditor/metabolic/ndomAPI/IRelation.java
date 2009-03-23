package org.pathwayeditor.metabolic.ndomAPI;

import org.pathwayeditor.notationsubsystem.toolkit.ndom.INdomModel;

public interface IRelation extends INdomModel{
	public String getRole();

	public int getStoichiometry();

	public IMolecule getMolecule();

	public IReaction getReaction();

	public ERelType getType();

	// private final ERelType type;

}
