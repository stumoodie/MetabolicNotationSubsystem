package org.pathwayeditor.metabolic.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleConfig;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleEnforcement;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IValidationRuleStore;
import org.pathwayeditor.notationsubsystem.toolkit.validation.ValidationRuleConfig;
import org.pathwayeditor.notationsubsystem.toolkit.validation.ValidationRuleDefinition;

public class MetabolicRuleStore implements IValidationRuleStore {
	private static MetabolicRuleStore anInstance = null;

	public static final int ERROR_ID = 1000;
	public static final int STOICH_ERROR_ID = 1001;
	public static final int IC_ERROR_ID = 1002;
	public static final int RE_PARAM_ERROR_ID = 1003;
	public static final int NOT_REGISTERED_ERROR_ID = 1004;
	public static final int CONSUMPTION_TO_REVERSIBLE_ERROR_ID = 1005;
	// Compound definition discrepancy
	public static final int COMP_DEF_ERROR_ID = 1006;
	// Reaction definition discrepancy
	public static final int RE_DEF_ERROR_ID = 1007;
	public static final int ORPHAN_COMPOUND_ERROR_ID = 1008;
	public static final int ORPHAN_PROCESS_ERROR_ID = 1009;
	private final Map<Integer, IValidationRuleConfig> ruleLookup;

	public static IValidationRuleStore getInstance(){
		if(anInstance == null){
			anInstance = new MetabolicRuleStore();
		}
		return anInstance;
	}
	
	private MetabolicRuleStore() {
		IValidationRuleConfig rules[] = {
				new ValidationRuleConfig(new ValidationRuleDefinition(
				"Object is not registered with the model", "NDOM parser",
				NOT_REGISTERED_ERROR_ID, RuleLevel.MANDATORY,
				RuleEnforcement.ERROR)),
				new ValidationRuleConfig(new ValidationRuleDefinition(
				"Consumption link to reversible reaction", "NDOM parser",
				CONSUMPTION_TO_REVERSIBLE_ERROR_ID, RuleLevel.MANDATORY,
				RuleEnforcement.ERROR)),
				new ValidationRuleConfig(new ValidationRuleDefinition(
				"Compound definition discrepancy", "NDOM parser",
				COMP_DEF_ERROR_ID, RuleLevel.OPTIONAL, RuleEnforcement.WARNING)),
				new ValidationRuleConfig(new ValidationRuleDefinition("Reaction definition discrepancy",
				"NDOM parser", RE_DEF_ERROR_ID, RuleLevel.OPTIONAL,
				RuleEnforcement.WARNING)),
				new ValidationRuleConfig(new ValidationRuleDefinition("Orphan object",
				"NDOM parser", ORPHAN_PROCESS_ERROR_ID, RuleLevel.MANDATORY,
				RuleEnforcement.ERROR)),
				new ValidationRuleConfig(new ValidationRuleDefinition("Orphan object",
				"NDOM parser", ORPHAN_COMPOUND_ERROR_ID, RuleLevel.OPTIONAL,
				RuleEnforcement.WARNING)),
				new ValidationRuleConfig(new ValidationRuleDefinition("Exception!", "Graph", ERROR_ID,
				RuleLevel.MANDATORY, RuleEnforcement.ERROR)),
				new ValidationRuleConfig(new ValidationRuleDefinition("Stoichiometry", "NDOM parser",
				STOICH_ERROR_ID, RuleLevel.MANDATORY, RuleEnforcement.ERROR)),
				new ValidationRuleConfig(new ValidationRuleDefinition("Initial concentration",
				"NDOM parser", IC_ERROR_ID, RuleLevel.MANDATORY,
				RuleEnforcement.ERROR)),
				new ValidationRuleConfig(new ValidationRuleDefinition(
						"Reaction parameters", "NDOM parser", RE_PARAM_ERROR_ID,
						RuleLevel.MANDATORY, RuleEnforcement.ERROR))
		};
		this.ruleLookup = new HashMap<Integer, IValidationRuleConfig>();
		for(IValidationRuleConfig rule : rules){
			this.ruleLookup.put(rule.getValidationRuleDefinition().getRuleNumber(), rule);
		}
	}

	public boolean containsRule(int ruleNumber) {
		return this.ruleLookup.containsKey(ruleNumber);
	}

	public Set<IValidationRuleConfig> getAllRuleConfigurations() {
		return new HashSet<IValidationRuleConfig>(this.ruleLookup.values());
	}

	public Set<IValidationRuleDefinition> getAllRuleDefinitions() {
		Set<IValidationRuleDefinition> retVal = new HashSet<IValidationRuleDefinition>();
		for(IValidationRuleConfig configRule : this.ruleLookup.values()){
			retVal.add(configRule.getValidationRuleDefinition());
		}
		return retVal;
	}

	public Set<IValidationRuleConfig> getConfigurableRules() {
		Set<IValidationRuleConfig> retVal = new HashSet<IValidationRuleConfig>();
		for(IValidationRuleConfig configRule : this.ruleLookup.values()){
			if(configRule.getValidationRuleDefinition().getRuleLevel().equals(RuleLevel.OPTIONAL)){
				retVal.add(configRule);
			}
		}
		return retVal;
	}

	public IValidationRuleDefinition getRuleById(int ruleID) {
		IValidationRuleConfig config = this.ruleLookup.get(ruleID);
		return config != null ? config.getValidationRuleDefinition() : null; 
	}

	public IValidationRuleConfig getRuleConfigByID(int id) {
		return this.ruleLookup.get(id);
	}

}
