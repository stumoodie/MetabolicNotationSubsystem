package uk.ac.ed.inf.metaboliccontext.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleConfig;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleDefinition;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.contextadapter.toolkit.validation.IDefaultValidationRuleConfigLoader;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleStore;
import org.pathwayeditor.contextadapter.toolkit.validation.RuleStore;

import uk.ac.ed.inf.Metabolic.DefaultRuleLoader;
import uk.ac.ed.inf.metaboliccontext.plugin.Activator;

public class ValidationRulePreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	final static String ERROR="Error";
	final static String WARNING="Warning";
	final static String IGNORE="Ignore";
	final String[][] ERROR_WARNING_IGNORE = new String[][] { { ERROR, ERROR }, {WARNING, WARNING },
			{ IGNORE, IGNORE } }; // guidelines
	final String[][] ERROR_IGNORE = new String[][] { { ERROR, ERROR }, { IGNORE, IGNORE } }; // optional

	//IPreferenceStore store;
	IRuleStore ruleStore;

	public ValidationRulePreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		initRuleStore();
		setDescription("Rule Configuration  preference page for validation");
		setTitle("Rule Configuration Preferences");
	    initializeDefaults();
	}
	
    


	private void initializeDefaults() {
		 Set<IValidationRuleConfig> defaults = ruleStore.getDefaultRuleConfigurations();
	      for(IValidationRuleConfig config:defaults) {
	    	  if ((config.getValidationRuleDefinition().getRuleLevel().equals(RuleLevel.MANDATORY))) {
	    		  continue;
	    	  }
	    	  IValidationRuleDefinition defn = config.getValidationRuleDefinition();
	    	  String toSet;
	    	  if(!config.mustBeRun()){
	    		  toSet=IGNORE;
	    	  } else {
	    		  if(config.isErrorRule()){
	    			  toSet=ERROR;
	    		  } else if(config.isWarningRule()) {
	    			  toSet=WARNING;
	    		  }
	    		  else {
	    			  throw new IllegalStateException("Default Config not set properly");
	    		  }
	    	  }
	    	  getPreferenceStore().setDefault(Integer.toString(defn.getRuleNumber()), toSet);
	      }
		
	}


	@Override
	protected void createFieldEditors() {
		Set<IValidationRuleConfig> configs = getRuleConfigSet();
		Map<String, Set<IValidationRuleConfig>> configsByCategory = sortConfigsByCategory(configs);
		Composite parent = createContainerForGroups();
		for (String category : configsByCategory.keySet()) {
			if (!categoryHasConfigurableRules(configsByCategory, category))
				continue;
			Group group = createGroupForCategory(parent, category);

			for (IValidationRuleConfig config : configsByCategory.get(category)) {
				IValidationRuleDefinition defn = config.getValidationRuleDefinition();
				if (!(defn.getRuleLevel().equals(RuleLevel.MANDATORY))) {
					String[][] content;
					if (defn.getRuleLevel().equals(RuleLevel.OPTIONAL)) {
						content = ERROR_IGNORE;
					} else {
						content = ERROR_WARNING_IGNORE;
					}
					createAndAddFieldEditor(group, defn, content);
				}
			}

		}

	}

	private Composite createContainerForGroups() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		Composite parent = new Composite(getFieldEditorParent(), SWT.NULL);
		parent.setLayout(layout);
		return parent;
	}
    /**
     * Different CAs will provide different subclasses of config. This will be substitutable.
     * @return
     */
	protected Set<IValidationRuleConfig> getRuleConfigSet() {
		return ruleStore.getConfigurableRules();
	}
     /*
      * PAckage for test overriding
      */
	 Group createGroupForCategory(Composite parent, String category) {
		Group group = new Group(parent, SWT.SHADOW_OUT);
		group.setText("Rule Category:  " + category);
		return group;
	}
    /*
     * Package to sub out in tests - uses widgets
     */
	void createAndAddFieldEditor(Group group, IValidationRuleDefinition defn, String[][] content) {
		ComboFieldEditor editor = new ComboFieldEditor(Integer.toString(defn.getRuleNumber()),
				defn.getName(), content, group);
		addField(editor);
	}

	private boolean categoryHasConfigurableRules(Map<String, Set<IValidationRuleConfig>> configsByCategory, String category) {
	  
		if(configsByCategory.get(category).isEmpty()){
			return false;
		}
		return true;
	}

	private Map<String, Set<IValidationRuleConfig>> sortConfigsByCategory(Set<IValidationRuleConfig> configs) {
		Map<String, Set<IValidationRuleConfig>> rc = new HashMap<String, Set<IValidationRuleConfig>>();
		for (IValidationRuleConfig config : configs) {
			String category = config.getValidationRuleDefinition().getRuleCategory();
			if (!rc.keySet().contains(category)) {
				rc.put(category, new HashSet<IValidationRuleConfig>());
			}
			rc.get(category).add(config);
		}

		return rc;
	}

	public void init(IWorkbench workbench) {
     
	}

	private IRuleStore initRuleStore() {
		ruleStore = RuleStore.instance();
		if(!ruleStore.isInitialized()){
			ruleStore.initializeStore(createRuleLoader());
		}
		return ruleStore;
	}



    /**
     * can be overridden in subclasses, will be abstract when put in application
     * @return
     */
	protected IDefaultValidationRuleConfigLoader createRuleLoader() {
		return new DefaultRuleLoader();
	}
	
	
	
	

}
