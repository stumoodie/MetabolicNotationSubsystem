
package uk.ac.ed.inf.Metabolic.ndomAPI;

import java.util.List;

public interface IModel extends IModelObject {
  List<IComparment> getCompartmentList() ;
  List<IReaction> getReactionList() ;
}