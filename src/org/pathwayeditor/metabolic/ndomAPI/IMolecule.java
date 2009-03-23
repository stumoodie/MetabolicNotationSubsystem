package org.pathwayeditor.metabolic.ndomAPI;

import java.util.List;

import org.pathwayeditor.notationsubsystem.toolkit.ndom.INdomModel;


public interface IMolecule extends INdomModel {
	public List<IRelation> getSinkList();

	public List<IRelation> getSourceList();

	public List<IRelation> getActivatoryRelationList();

	public List<IRelation> getInhibitoryRelationList();

	public List<IRelation> getCatalyticRelationList();
	
	public INdomModel    getParent () ;
}
