package uk.ac.ed.inf.Metabolic.sbmlexport;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModelObject;

 class CompartmentAnnotationBuilder extends AnnotationBuilder {
	private ICompartment compartment;
	CompartmentAnnotationBuilder(ICompartment comp) {
		super((IModelObject)comp);
		this.compartment=comp;
	}
	String BQBIOLst =  "<bqbiol:isVersionOf>";
	String BQBiolEnd = "</bqbiol:isVersionOf>";
	@Override
	String getBQBiolSt() {
		return BQBIOLst;
	}

	@Override
	String getBQBiolEnd() {
		return BQBiolEnd;
	}

	@Override
	String getRDFResources(){
		StringBuffer sb = new StringBuffer();
		sb.append(RDFList).append("http://www.geneontology.org/#")
		                  .append( ((ICompartment)compartment).getGOTerm())
		                  .append(RDFListEnd);
		return sb.toString();
	}

	

}
