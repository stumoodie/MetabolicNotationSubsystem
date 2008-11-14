package uk.ac.ed.inf.metaboliccontext.plugin;

import uk.ac.ed.inf.Metabolic.MetabolicNotationSubsystem;

public class MetabolicContextServiceProviderLoader implements IContextAdapterServiceProviderLoader {
    
	/**
	 * Public no-args for extension point mechanism
	 */
	public MetabolicContextServiceProviderLoader (){
		super();
	}
	public IContextAdapterServiceProvider getContextAdapterServiceProvider() {
		return MetabolicNotationSubsystem.getInstance();
	}

}
