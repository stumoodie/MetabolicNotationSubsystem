package uk.ac.ed.inf.Metabolic.sbmlexport;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;

 class CompoundAnnotationBuilder extends AnnotationBuilder {
    ICompound compound;
    
	CompoundAnnotationBuilder(ICompound compound) {
		super(compound);
		this.compound = compound;
	}


	@Override
	String getRDFResources() {
		String resource="<rdf:li rdf:resource=\"";
		StringBuffer rc = new StringBuffer();
		rc.append(RDF_IS_ST).append(getBagStart());
		if(!compound.getChEBIId().isEmpty())
			rc.append(resource).append(AnnotationURLs.ChEBIID.getURL()).append(compound.getChEBIId()).append(RDFListEnd);
		if(!compound.getPubChemId().isEmpty())
		  rc.append(resource).append(AnnotationURLs.PUBCHEM).append(compound.getPubChemId()).append(RDFListEnd);
		if(!compound.getInChI().isEmpty())
		  rc.append(resource).append(AnnotationURLs.IUPAC).append(compound.getInChI()).append(RDFListEnd);
		if(!compound.getCID().isEmpty())
	      rc.append(resource).append(AnnotationURLs.KEGG.getURL()).append(compound.getCID()).append(RDFListEnd);
		rc.append(getBagEnd()).append(RDF_IS_END);
		return rc.toString();
	}

	@Override
	String buildNotes() {
		String supernotes = super.buildNotes();
		StringBuffer  notes = new StringBuffer(supernotes).append("<h2 xmlns='http://www.w3.org/1999/xhtml'>")
		  .append("Smiles")
		  .append("</h2>")
		  .append("<p xmlns='http://www.w3.org/1999/xhtml'>")
		  .append(compound.getSmiles())
		  .append("</p>");
		return notes.toString();
	}

}
