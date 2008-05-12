package uk.ac.ed.inf.Metabolic.sbmlexport;

import org.sbml.libsbml.Model;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

 interface IReactionBuilder {
  void buildReactions(Model sbmlModel, IModel model);
}
