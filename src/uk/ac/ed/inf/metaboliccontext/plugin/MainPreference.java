package uk.ac.ed.inf.metaboliccontext.plugin;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import uk.ac.ed.inf.Metabolic.MetabolicContextAdapterServiceProvider;

public class MainPreference extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public MainPreference (){
		super(GRID);
		setDescription("Main  preference page for " + MetabolicContextAdapterServiceProvider.DISPLAY_NAME);
		setTitle("Metabolic Context Adapter  Preferences");
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
	}
	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

}
