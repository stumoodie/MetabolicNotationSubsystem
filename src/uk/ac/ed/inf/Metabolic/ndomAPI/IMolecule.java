package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

import org.pathwayeditor.contextadapter.toolkit.ndom.IModelObject;

public interface IMolecule extends IModelObject {
	public List<IRelation> getSinkList();

	public List<IRelation> getSourceList();

	public List<IRelation> getActivatoryRelationList();

	public List<IRelation> getInhibitoryRelationList();

	public List<IRelation> getCatalyticRelationList();
	
	public IModelObject    getParent () ;
}
