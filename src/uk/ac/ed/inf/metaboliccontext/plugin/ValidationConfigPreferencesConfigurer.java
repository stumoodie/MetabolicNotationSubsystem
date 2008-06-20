package uk.ac.ed.inf.metaboliccontext.plugin;

import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleConfig;

import uk.ac.ed.inf.Metabolic.IRuleConfigurer;

public class ValidationConfigPreferencesConfigurer implements IRuleConfigurer {

	public void configureRules(Set<IValidationRuleConfig> rulesToConfigure) {
		IPreferenceStore store = getPreferenceStore();
		for (IValidationRuleConfig config : rulesToConfigure) {
			String value = store.getString(Integer.toString(config.getValidationRuleDefinition()
					.getRuleNumber()));

			if (value.equals(ValidationRulePreferences.IGNORE)) {
				config.setMustBeRun(false);
			} else if (value.equals(ValidationRulePreferences.WARNING)) {
				config.setMustBeRun(true);
				config.promoteToError(false);
			} else if (value.equals(ValidationRulePreferences.ERROR)) {
				config.setMustBeRun(true);
				config.promoteToError(true);
			}
		}

	}

	private IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

}
