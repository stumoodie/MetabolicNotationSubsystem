
package uk.ac.ed.inf.Metabolic.ndomAPI;

public interface IRelation extends IModelObject {
  String getRole() ;
  int getStoichiometry() ;
  IMolecule getMolecule() ;
  IReaction getReaction() ;
}
