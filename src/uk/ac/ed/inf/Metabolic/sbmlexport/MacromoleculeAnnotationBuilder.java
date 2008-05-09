package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.util.List;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMacromolecule;

public class MacromoleculeAnnotationBuilder extends AnnotationBuilder {

	private IMacromolecule macromolecule;
    private boolean isTopLevel;
	MacromoleculeAnnotationBuilder(IMacromolecule object, boolean isTopLevel) {
		super(object);
		this.macromolecule=object;
		this.isTopLevel = isTopLevel;
		// TODO Auto-generated constructor stub
	}

	@Override
	String getBQBiolEnd() {
		return"";
	}

	@Override
	String getBQBiolSt() {
		return "";
	}

	@Override
	String getRDFResources() {
		StringBuffer sb = new StringBuffer();
		if(isTopLevel){
			sb.append(getRDFIsData());
			sb.append(getHasPartData());
		} else {
			sb.append(getRDFIsData());
		}
		return sb.toString();
	}
	  private String getHasPartData() {
		  if(macromolecule.getCompoundList().isEmpty() && macromolecule.getSubunitList().isEmpty()){
			  return "";
		  }
		  StringBuffer sb = new StringBuffer();
		  sb.append(RDF_HAS_ST).append(getBagStart());
		  addChildElementData(sb, macromolecule);
		  sb.append(getBagEnd()).append(RDF_HAS_END);
		  return sb.toString();
		  
	}

	private void addChildElementData(StringBuffer sb, IMacromolecule macromolecule2) {
		List<ICompound> compounds = macromolecule2.getCompoundList();
		  for (ICompound c: compounds) {
			  sb.append(RDFList).append(AnnotationURLs.ChEBIID.getURL())
            .append( c.getChEBIId())
            .append(RDFListEnd);
		  }
		  
		  List<IMacromolecule> macromolChildren = macromolecule2.getSubunitList();
		  for (IMacromolecule child: macromolChildren) {
			  sb.append(RDFList).append(AnnotationURLs.UNIPROT.getURL())
            .append( child.getUniProt())
            .append(RDFListEnd);
			  addChildElementData(sb, child);
		  }
		 
		
	}

	private String getRDFIsData() {
		 
		StringBuffer sb = new StringBuffer();
		
		sb.append(RDF_IS_ST).append(getBagStart());
		if(!macromolecule.getUniProt().isEmpty())	{
			sb.append(RDFList).append(AnnotationURLs.UNIPROT.getURL())
		                  .append( macromolecule.getUniProt())
		                  .append(RDFListEnd);
		}
		sb.append(getBagEnd()).append(RDF_IS_END);
		
		sb.append(RDF_ISVERSIONOF_ST).append(getBagStart());
		if(!macromolecule.getGOTerm().isEmpty())	{
			sb.append(RDFList).append(AnnotationURLs.GO.getURL())
		                  .append( macromolecule.getGOTerm())
		                  .append(RDFListEnd);
		}
		sb.append(getBagEnd()).append(RDF_ISVERSIONOF_END);
		return sb.toString();
	}

}
