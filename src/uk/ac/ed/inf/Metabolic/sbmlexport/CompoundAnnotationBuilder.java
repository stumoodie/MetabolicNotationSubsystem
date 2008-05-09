package uk.ac.ed.inf.Metabolic.sbmlexport;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;

 class CompoundAnnotationBuilder extends AnnotationBuilder {
    ICompound compound;
    
	CompoundAnnotationBuilder(ICompound compound) {
		super(compound);
		this.compound = compound;
	}

	@Override
	String getBQBiolEnd() {
		return "</bqbiol:is>";
	}

	@Override
	String getBQBiolSt() {
		return "<bqbiol:is>";
	}

	@Override
	String getRDFResources() {
		String resource="<rdf:li rdf:resource=\"";
		StringBuffer rc = new StringBuffer();
		rc.append(resource).append("http://www.ebi.ac.uk/chebi/#").append(compound.getChEBIId()).append(RDFListEnd)
		  .append(resource).append("http://www.pubchem.gov/substance/#").append(compound.getPubChemId()).append(RDFListEnd)
		  .append(resource).append("http://www.iupac.org/inchi/#").append(compound.getInChI()).append(RDFListEnd)
	      .append(resource).append("http://www.genome.jp/kegg/compound/#").append(compound.getCID()).append(RDFListEnd);
		
		return rc.toString();
	}

	@Override
	String buildNotes() {
		String supernotes = super.buildNotes();
		StringBuffer  notes = new StringBuffer(supernotes).append("<p xmlns='http://www.w3.org/1999/xhtml'>")
		  .append("Smiles :")
		  .append(compound.getSmiles())
		  .append("</p>");
		return notes.toString();
	}

}
