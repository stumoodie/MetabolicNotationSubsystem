package uk.ac.ed.inf.Metabolic.ndomAPI;

public interface IRelation extends IModelObject {
	public String getRole();

	public int getStoichiometry();

	public IMolecule getMolecule();

	public IReaction getReaction();

	public ERelType getType();

	// private final ERelType type;

}
