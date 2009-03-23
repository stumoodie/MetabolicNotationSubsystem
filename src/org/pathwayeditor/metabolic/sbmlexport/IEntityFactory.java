package org.pathwayeditor.metabolic.sbmlexport;

import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.sbml.libsbml.Model;


 interface IEntityFactory {
     Model buildSpeciesAndCompartments (Model sbmlModel, IModel model);
}
