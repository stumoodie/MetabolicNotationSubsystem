package org.pathwayeditor.metabolic.sbmlexport;

import org.pathwayeditor.metabolic.ndomAPI.IReaction;

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
		if(reaction.getECNumber().length() != 0)
			rc.append(RDFList).append(AnnotationURLs.EC.getURL()).append(reaction.getECNumber()).append(RDFListEnd);
		rc.append(getBagEnd()).append(RDF_ISVERSIONOF_END);
		return rc.toString();
	}

}
