package uk.ac.ed.inf.metaboliccontext.plugin;

import org.pathwayeditor.contextadapter.publicapi.IContextAdapterServiceProvider;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterServiceProviderLoader;

import uk.ac.ed.inf.Metabolic.MetabolicContextAdapterServiceProvider;

public class MetabolicContextServiceProviderLoader implements IContextAdapterServiceProviderLoader {
    
	/**
	 * Public no-args for extension point mechanism
	 */
	public MetabolicContextServiceProviderLoader (){
		super();
	}
	public IContextAdapterServiceProvider getContextAdapterServiceProvider() {
		return MetabolicContextAdapterServiceProvider.getInstance();
	}

}
