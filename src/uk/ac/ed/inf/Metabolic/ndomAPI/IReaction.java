package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

public interface IReaction extends IModelObject {
	public List<IRelation> getSubstrateList();

	public List<IRelation> getProductList();

	public List<IRelation> getActovatorList();

	public List<IRelation> getInhibitorList();

	public List<IRelation> getCatalystList();

	public String getECNumber();

	public String getKineticLaw();

	public boolean isReversible();
}
