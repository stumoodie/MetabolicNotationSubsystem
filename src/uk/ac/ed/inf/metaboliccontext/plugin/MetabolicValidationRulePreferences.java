package uk.ac.ed.inf.metaboliccontext.plugin;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.pathwayeditor.application.contextadapter.uitoolkit.ValidationRulePreferences;
import org.pathwayeditor.contextadapter.publicapi.IDefaultValidationRuleConfigLoader;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleStore;

import uk.ac.ed.inf.Metabolic.DefaultRuleLoader;
import uk.ac.ed.inf.Metabolic.MetabolicContextAdapterServiceProvider;
import uk.ac.ed.inf.Metabolic.MetabolicContextValidationService;

public class MetabolicValidationRulePreferences extends ValidationRulePreferences implements IWorkbenchPreferencePage {
	
    /**
     * Default no-arg constructor needed for extension points
     */
	public MetabolicValidationRulePreferences() {

	}
	
 
	

    /**
     * 
     * @return
     */
	protected IDefaultValidationRuleConfigLoader createRuleLoader() {
		return new DefaultRuleLoader();
	}




	@Override
	protected IPreferenceStore createPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}




	@Override
	protected IValidationRuleStore getRuleStore() {
		return ((MetabolicContextValidationService)MetabolicContextAdapterServiceProvider
				         .getInstance()
				         .getValidationService())
				         .getRuleStore();
	}
	
	
	
	

}
