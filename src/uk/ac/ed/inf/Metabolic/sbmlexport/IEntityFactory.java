package uk.ac.ed.inf.Metabolic.sbmlexport;

import org.sbml.libsbml.Model;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

 interface IEntityFactory {
     Model buildSpeciesAndCompartments (Model sbmlModel, IModel model);
}
