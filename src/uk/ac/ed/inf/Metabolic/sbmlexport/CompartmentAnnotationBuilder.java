package uk.ac.ed.inf.Metabolic.sbmlexport;

import org.pathwayeditor.contextadapter.toolkit.ndom.IModelObject;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;

 class CompartmentAnnotationBuilder extends AnnotationBuilder {
	private ICompartment compartment;
	CompartmentAnnotationBuilder(ICompartment comp) {
		super((IModelObject)comp);
		this.compartment=comp;
	}
	
	

	@Override
	String getRDFResources(){
		StringBuffer sb = new StringBuffer();
		sb.append(RDF_ISVERSIONOF_ST).append(getBagStart())
		.append(RDFList).append(AnnotationURLs.GO.getURL())
		                  .append( ((ICompartment)compartment).getGOTerm())
		                  .append(RDFListEnd)
		.append(getBagEnd())
		.append(RDF_ISVERSIONOF_END);
		return sb.toString();
	}

	

}
