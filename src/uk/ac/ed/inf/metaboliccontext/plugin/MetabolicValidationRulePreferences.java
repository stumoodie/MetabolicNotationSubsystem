package uk.ac.ed.inf.metaboliccontext.plugin;

import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleConfig;

import uk.ac.ed.inf.Metabolic.MetabolicNotationSubsystem;

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
		return MetabolicNotationSubsystem
				         .getInstance()
				         .getValidationService()
				         .getRuleConfigurations();
	}


	@Override
	protected Set<IValidationRuleConfig> getDefaultConfigurableRules() {
		return    MetabolicNotationSubsystem
		         .getInstance()
		         .getValidationService()
		         .getDefaultRuleConfigurations();
	}
	
	
	
	

}
