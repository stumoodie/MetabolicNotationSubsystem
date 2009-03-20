package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

import org.pathwayeditor.notationsubsystem.toolkit.ndom.INdomModel;

public interface IReaction extends INdomModel {
	public List<IRelation> getSubstrateList();

	public List<IRelation> getProductList();

	public List<IRelation> getActivatorList();

	public List<IRelation> getInhibitorList();

	public List<IRelation> getCatalystList();

	public String getECNumber();

	public String getKineticLaw();
	
	public String getParameters();

	public boolean isReversible();
}
