package uk.ac.ed.inf.Metabolic.sbmlexport;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMacromolecule;

public class MacromoleculeAnnotationBuilder extends AnnotationBuilder {

	private IMacromolecule macromolecule;

	MacromoleculeAnnotationBuilder(IMacromolecule object) {
		super(object);
		this.macromolecule=object;
		// TODO Auto-generated constructor stub
	}

	@Override
	String getBQBiolEnd() {
		if(macromolecule.getCompoundList().isEmpty() && macromolecule.getSubunitList().isEmpty()) {
			return "</bqbiol:is>";
		} else {
			return"</bqbiol:hasPart>";
		}
	}

	@Override
	String getBQBiolSt() {
		if(macromolecule.getCompoundList().isEmpty() && macromolecule.getSubunitList().isEmpty()) {
			return "<bqbiol:is>";
		} else {
			return"<bqbiol:hasPart>";
		}
	}

	@Override
	String getRDFResources() {
		StringBuffer sb = new StringBuffer();
		if(!macromolecule.getGOTerm().isEmpty())	{
			sb.append(RDFList).append("http://www.geneontology.org/#")
		                  .append( macromolecule.getGOTerm())
		                  .append(RDFListEnd);
		}
		
		if(!macromolecule.getUniProt().isEmpty())	{
			sb.append(RDFList).append("http://www.geneontology.org/#")
		                  .append( macromolecule.getUniProt())
		                  .append(RDFListEnd);
		}
		return sb.toString();
	}

}
