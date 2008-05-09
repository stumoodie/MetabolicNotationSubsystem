package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sbml.libsbml.Compartment;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.SBase;
import org.sbml.libsbml.Species;
import org.sbml.libsbml.libsbml;

import uk.ac.ed.inf.Metabolic.ExportAdapterCreationException;
import uk.ac.ed.inf.Metabolic.IExportAdapter;
import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMacromolecule;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

class MetabolicSBMLExportAdapter<N extends IModel> implements IExportAdapter<N> {
	
	final long SPATIAL_DIMENSIONS = 3;
	private boolean isTargetCreated = false;
	
	private SBMLDocument document;
	
	private Set<IMacromolecule> seenMacromols = new HashSet<IMacromolecule>();
	
	

	public void createTarget(IModel model) throws ExportAdapterCreationException {
		
		if (model == null) {
			throw new IllegalArgumentException("model is null");
		}
		document = new SBMLDocument();
		Model sbmlModel = createSBMLModel(model);
		addCompartments(sbmlModel, model.getCompartmentList());
		
		System.out.println(libsbml.writeSBMLToString(document));
		if(document.checkL2v3Compatibility() == 0)
			isTargetCreated = true;
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
			seenMacromols.add(macromol);
		}
		
	}


	private void addAnnotationAndNotes(SBase sbmlObject, AnnotationBuilder builder) {
		String annotation = builder.buildAnnotation();
		sbmlObject.appendAnnotation(annotation);
		String notes = builder.buildNotes();
		sbmlObject.appendNotes(notes);
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

	private Model createSBMLModel(IModel model) {
		Model sbmlmodel = document.createModel(model.getId());
		sbmlmodel.setName(model.getASCIIName());
		AnnotationBuilder modelAnnotationBuilder = new ModelAnnotationBuilder(model);
		String notes = modelAnnotationBuilder.buildNotes();
		sbmlmodel.appendNotes(notes);
		return sbmlmodel;
	}


	


	public boolean isTargetCreated() {
		return isTargetCreated;
	}

	public void writeTarget(OutputStream stream) throws IOException {
		if (stream == null) {
			throw new IllegalArgumentException("output stream is null");
		}
		if (!isTargetCreated()) {
			throw new IllegalStateException("Target not created");
		}
		

	}

}
