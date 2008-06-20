package uk.ac.ed.inf.metaboliccontext.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterServiceProvider;

import uk.ac.ed.inf.Metabolic.MetabolicContextAdapterServiceProvider;
import uk.ac.ed.inf.Metabolic.MetabolicContextValidationService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "uk.ac.ed.inf.csb.Metabolic";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;		
		MetabolicContextValidationService service = (MetabolicContextValidationService) MetabolicContextAdapterServiceProvider.getInstance().getValidationService();
	    service.setRuleConfigurer(new ValidationConfigPreferencesConfigurer());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
