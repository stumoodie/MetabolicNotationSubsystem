
package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

public interface IReaction extends IModelObject {
  List<IRelation> getSubstrateList() ;
  List<IRelation> getProductList() ;
  List<IRelation> getActovatorList() ;
  List<IRelation> getInhibitorList() ;
  List<IRelation> getCatalystList() ;
  String getECNumber() ;
  String getKineticLaw() ;
  boolean isReversible() ;
}
