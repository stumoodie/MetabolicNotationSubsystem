package uk.ac.ed.inf.metaboliccontext.plugin;

import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.pathwayeditor.application.contextadapter.uitoolkit.ValidationRulePreferences;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleConfig;

import uk.ac.ed.inf.Metabolic.MetabolicContextAdapterServiceProvider;

public class MetabolicValidationRulePreferences extends ValidationRulePreferences implements IWorkbenchPreferencePage {
	
    /**
     * Default no-arg constructor needed for extension points
     */
	public MetabolicValidationRulePreferences() {

	}
	
	
	@Override
	protected IPreferenceStore createPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}




	@Override
	protected Set<IValidationRuleConfig> getConfigurableRules() {
		return MetabolicContextAdapterServiceProvider
				         .getInstance()
				         .getValidationService()
				         .getRuleConfigurations();
	}


	@Override
	protected Set<IValidationRuleConfig> getDefaultConfigurableRules() {
		return    MetabolicContextAdapterServiceProvider
		         .getInstance()
		         .getValidationService()
		         .getDefaultRuleConfigurations();
	}
	
	
	
	

}
