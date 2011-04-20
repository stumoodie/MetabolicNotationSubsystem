/*
  Licensed to the Court of the University of Edinburgh (UofE) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The UofE licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
*/
package org.pathwayeditor.metabolic.sbmlexport;

import java.util.List;

import org.pathwayeditor.metabolic.ndomAPI.ICompartment;
import org.pathwayeditor.metabolic.ndomAPI.ICompound;
import org.pathwayeditor.metabolic.ndomAPI.IMacromolecule;
import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.sbml.libsbml.Compartment;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBase;
import org.sbml.libsbml.Species;


class EntityBuilder implements IEntityFactory {

	public Model buildSpeciesAndCompartments(Model sbmlModel, IModel model) {
		addCompartments(sbmlModel, model.getCompartmentList());
		return sbmlModel;

	}
    
	private void addCompartments(Model sbmlModel, List<ICompartment> comps) {
		for (ICompartment comp: comps) {
			Compartment sbmlCompartment = sbmlModel.createCompartment();
			sbmlCompartment.setId(comp.getId());
			sbmlCompartment.setName(comp.getASCIIName());
			sbmlCompartment.setSize(comp.getVolume());
			sbmlCompartment.setSpatialDimensions(3);
			
			if(comp.getParentCompartment()!=null){
				sbmlCompartment.setOutside(comp.getParentCompartment().getId());
			}
			AnnotationBuilder builder = new CompartmentAnnotationBuilder(comp);
			addAnnotationAndNotes(sbmlCompartment, builder);
			addCompounds(sbmlCompartment, comp);
		    addMacromolecules(sbmlCompartment, comp);
			addCompartments(sbmlModel, comp.getChildCompartments());
		}
		
	}
	private void addMacromolecules(Compartment sbmlCompartment, ICompartment comp) {
		List<IMacromolecule> macromolecules = comp.getMacromoleculeList();
		for (IMacromolecule macromol: macromolecules) {
			Species s = sbmlCompartment.getModel().createSpecies();
			
			s.setId(macromol.getId());
			s.setName(macromol.getASCIIName());
			s.setCompartment(sbmlCompartment.getId());
			AnnotationBuilder builder = new MacromoleculeAnnotationBuilder(macromol,true);
			addAnnotationAndNotes(s, builder);
		
		}
		
	}
	
	private void addCompounds(Compartment sbmlCompartment, ICompartment comp) {
		List<ICompound> compounds = comp.getCompoundList();
		addCompoundsFromList(sbmlCompartment, compounds);
		
	}
	
	private void addCompoundsFromList(Compartment sbmlCompartment, List<ICompound> compounds) {
		for (ICompound compound: compounds) {
			Species s = sbmlCompartment.getModel().createSpecies();
			s.setId(compound.getId());
			s.setName(compound.getASCIIName());
			s.setCompartment(sbmlCompartment.getId());
			s.setInitialConcentration(compound.getIC());
			AnnotationBuilder builder = new CompoundAnnotationBuilder(compound);
			addAnnotationAndNotes(s, builder);
		}
	}
	
	private void addAnnotationAndNotes(SBase sbmlObject, AnnotationBuilder builder) {
		String annotation = builder.buildAnnotation();
		sbmlObject.appendAnnotation(annotation);
		String notes = builder.buildNotes();
		sbmlObject.appendNotes(notes);
	}
}
