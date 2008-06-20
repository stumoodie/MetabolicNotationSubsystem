package uk.ac.ed.inf.metaboliccontext.plugin;

import org.eclipse.jface.preference.IPreferenceStore;
import org.pathwayeditor.application.contextadapter.uitoolkit.ValidationConfigPreferencesConfigurer;



/**
 * Implementation of {@link IRuleConfigurer} which reads from a Preference Store
 * @author Richard Adams
 *
 */
 class MetabolicValidationConfigPreferencesConfigurer extends ValidationConfigPreferencesConfigurer {

	protected  IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

}
