package uk.ac.ed.inf.Metabolic.sbmlexport;

import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;

public class ReactionAnnotationBuilder extends AnnotationBuilder {
    private IReaction reaction;
	ReactionAnnotationBuilder(IReaction reaction) {
		super(reaction);
		this.reaction=reaction;
		
	}

	@Override
	String getRDFResources() {
		StringBuffer rc = new StringBuffer();
		rc.append(RDF_ISVERSIONOF_ST).append(getBagStart());
		if(!reaction.getECNumber().isEmpty())
			rc.append(RDFList).append(AnnotationURLs.EC.getURL()).append(reaction.getECNumber()).append(RDFListEnd);
		rc.append(getBagEnd()).append(RDF_ISVERSIONOF_END);
		return rc.toString();
	}

}
