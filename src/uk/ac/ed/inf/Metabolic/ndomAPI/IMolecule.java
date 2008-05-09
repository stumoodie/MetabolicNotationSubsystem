package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

public interface IMolecule extends IModelObject {
	public List<IRelation> getSinkList();

	public List<IRelation> getSourceList();

	public List<IRelation> getActivatoryRelationList();

	public List<IRelation> getInhibitoryRelationList();

	public List<IRelation> getCatalyticRelationList();
}
