package uk.ac.ed.inf.Metabolic.sbmlexport;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModelObject;

public abstract class AnnotationBuilder {
	protected IModelObject object;
	String RDFst = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:dcterms=\"http://purl.org/dc/terms/\" xmlns:vCard=\"http://www.w3.org/2001/vcard-rdf/3.0#\" xmlns:bqbiol=\"http://biomodels.net/biology-qualifiers/\" xmlns:bqmodel=\"http://biomodels.net/model-qualifiers/\">";
	String RDFend = "</rdf:RDF>";

	String RDFdescSt = "<rdf:Description rdf:about=\"#\">";

	
	String RDFBagSt = "<rdf:Bag>";

	String RDFList = "<rdf:li rdf:resource=\"";

	String RDFListEnd = "\"/>";
	String RDFbagEnd = "</rdf:Bag>";
	
	String RDFdescEnd = "</rdf:Description>";
	
	String RDF_IS_ST= "<bqbiol:is>";
	String RDF_IS_END="</bqbiol:is>";
	
	String RDF_HAS_ST= "<bqbiol:hasPart>";
	String RDF_HAS_END="</bqbiol:hasPart>";
	
	String RDF_ISVERSIONOF_ST= "<bqbiol:isVersionOf>";
	String RDF_ISVERSIONOF_END="</bqbiol:isVersionOf>";
	
	AnnotationBuilder(IModelObject object) {
		super();
		this.object = object;
	}
	
	 String buildNotes(){
		StringBuffer sb = new StringBuffer();
		 sb.append("<p xmlns='http://www.w3.org/1999/xhtml'>")
		  .append("Description :")
		  .append(object.getDescription())
		  .append("</p>")
		  .append("<p xmlns='http://www.w3.org/1999/xhtml'>")
		  .append("Detailed Description :")
		  .append(object.getDetailedDescription())
		  .append("</p>").toString();
		 return sb.toString();
	}

	final String buildAnnotation() {
		StringBuffer sb = new StringBuffer();
		sb.append(buildStart());
		
		sb.append(getRDFResources());
		
		sb.append(buildEnd());
		return sb.toString();

	}

	private Object buildEnd() {
		return new StringBuffer(RDFdescEnd)
		              .append("\n")
		              .append(RDFend).toString();
	}


	 String getBagEnd() {
		return RDFbagEnd+"\n";
	}
    /**
     * Fills in specific annotation for an element.
     * Only added for property values that aren't empty.
     * @return
     */
	abstract String getRDFResources();

	private String buildStart() {
		String desc = RDFdescSt.replace("#", object.getId());
		return new StringBuffer().append(RDFst)
		      .append("\n")
		      .append(desc)
		      .append("\n").toString();
	}

	

	 String getBagStart() {
		return RDFBagSt+"\n";
	}

	

}
