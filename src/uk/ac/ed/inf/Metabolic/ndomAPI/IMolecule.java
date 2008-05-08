
package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

public interface IMolecule extends IModelObject {
  List<IRelation> getSinkList() ;
  List<IRelation> getSourceList() ;
  List<IRelation> getActivatoryRelationList() ;
  List<IRelation> getInhibitoryRelationList() ;
  List<IRelation> getCatalyticRelationList() ;
}
