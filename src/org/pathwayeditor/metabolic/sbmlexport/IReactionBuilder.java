package org.pathwayeditor.metabolic.sbmlexport;

import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.sbml.libsbml.Model;


 interface IReactionBuilder {
  void buildReactions(Model sbmlModel, IModel model);
}
