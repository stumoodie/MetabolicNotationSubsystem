package uk.ac.ed.inf.Metabolic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.pathwayeditor.businessobjects.notationsubsystem.INotationValidationService;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleConfig;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleEnforcement;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.contextadapter.toolkit.validation.IDefaultValidationRuleConfigLoader;
import org.pathwayeditor.contextadapter.toolkit.validation.ValidationRuleConfig;
import org.pathwayeditor.contextadapter.toolkit.validation.ValidationRuleDefinition;

/**
 * Supplies the default Rules and their definitions.
 * Just mocked rules at the moment but these should be replaced with real rules.
 * 
 * @author Richard Adams
 *
 */
public class DefaultRuleLoader implements IDefaultValidationRuleConfigLoader {
	static final int ERROR_ID = 1000;
	static IValidationRuleDefinition def1, def2, def3, def4,ERROR;
	
	static IValidationRuleConfig config1 ,config2, config3, config4, ERROR_CONFIG;
	static INotationValidationService validationService;
	static {
		validationService = MetabolicNotationSubsystem.getInstance().getValidationService();
		def1 = new ValidationRuleDefinition(validationService, "Rule 1", "Layout", 1, RuleLevel.MANDATORY, RuleEnforcement.ERROR);
		def2 = new ValidationRuleDefinition(validationService, "Rule 2", "Type", 2, RuleLevel.MANDATORY,RuleEnforcement.ERROR);
		def3 = new ValidationRuleDefinition(validationService, "Rule 3", "Graph", 3, RuleLevel.OPTIONAL,RuleEnforcement.WARNING);
		def4 = new ValidationRuleDefinition(validationService, "Rule 4 with quite a long name", "Graph", 4, RuleLevel.OPTIONAL,RuleEnforcement.ERROR);
		ERROR = new ValidationRuleDefinition(validationService, "Exception!", "Graph", ERROR_ID, RuleLevel.OPTIONAL,RuleEnforcement.ERROR);
		
		config1 = new ValidationRuleConfig(def1);// must be run, error
		config2 = new ValidationRuleConfig(def2); // must be run, error
		config3 = new ValidationRuleConfig(def3); // must be run, warning
		config4 = new ValidationRuleConfig(def4); // not run, error
		ERROR_CONFIG = new ValidationRuleConfig(ERROR); // not run, error
		
	}

	public Set<IValidationRuleConfig> loadDefaultRuleConfigurations() {
		Set <IValidationRuleConfig> rc =  new HashSet<IValidationRuleConfig>(Arrays.asList(new IValidationRuleConfig[]{config1, config2, config3,config4, ERROR_CONFIG}));
		return rc;
	}

	

}
