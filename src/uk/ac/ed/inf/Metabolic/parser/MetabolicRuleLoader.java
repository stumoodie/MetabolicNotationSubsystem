package uk.ac.ed.inf.Metabolic.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pathwayeditor.businessobjects.notationsubsystem.INotationValidationService;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleConfig;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleEnforcement;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IDefaultValidationRuleConfigLoader;
import org.pathwayeditor.notationsubsystem.toolkit.validation.ValidationRuleConfig;
import org.pathwayeditor.notationsubsystem.toolkit.validation.ValidationRuleDefinition;

import uk.ac.ed.inf.Metabolic.MetabolicNotationSubsystem;

/**
 * <br>
 * $Id:$
 * 
 * @author Anatoly Sorokin
 * @date 28 Jun 2008
 * 
 */
public class MetabolicRuleLoader implements IDefaultValidationRuleConfigLoader {

	private static MetabolicRuleLoader instance;

	static final int ERROR_ID = 1000;
	static final int STOICH_ERROR_ID = 1001;
	static final int IC_ERROR_ID = 1002;
	static final int RE_PARAM_ERROR_ID = 1003;
	static final int NOT_REGISTERED_ERROR_ID=1004;
	static final int CONSUMPTION_TO_REVERSIBLE_ERROR_ID=1005;
	//Compound definition discrepancy
	static final int COMP_DEF_ERROR_ID=1006;
	//Reaction definition discrepancy
	static final int RE_DEF_ERROR_ID=1007;
//	error("Reaction for Activation relation is not registered in the model");
//	error("Molecule for Cativation relation is not registered in the model");
//	error("Molecule for Catalysis relation is not registered in the model");
//	error("Reaction for Inhibiton relation is not registered in the model");
//	error("Molecule for Inhibiton relation is not registered in the model");
//	error("Molecule in Production relation is not registered in the model");
//	error("Molecule in Consumption relation is not registered in the model");
//	error("Consumption link to reversible reaction");

	static final int ORPHAN_COMPOUND_ERROR_ID=1008;
	static final int ORPHAN_PROCESS_ERROR_ID=1009;
	static IValidationRuleDefinition stoich, ic, reParam, notReg, consRev,compDef,reDef,ERROR,orphanCDef,orphanRDef;

	private static Map<IValidationRuleDefinition, IParserRule> rules = new HashMap<IValidationRuleDefinition, IParserRule>();
	static IValidationRuleConfig configStoich, configIC, configReparam,
			configNotReg, configConsRev,configCompDef,configReDef,ERROR_CONFIG,configOrphanCDef,configOrphanRDef;
	static INotationValidationService validationService;

	static {
		validationService = MetabolicNotationSubsystem.getInstance()
				.getValidationService();
		notReg = new ValidationRuleDefinition(validationService,
				"Object is not registered with the model", "NDOM parser", NOT_REGISTERED_ERROR_ID,
				RuleLevel.MANDATORY,RuleEnforcement.ERROR);
		consRev = new ValidationRuleDefinition(validationService,
				"Consumption link to reversible reaction", "NDOM parser", CONSUMPTION_TO_REVERSIBLE_ERROR_ID,
				RuleLevel.MANDATORY,RuleEnforcement.ERROR);
		compDef=new ValidationRuleDefinition(validationService,"Compound definition discrepancy","NDOM parser",COMP_DEF_ERROR_ID,
				RuleLevel.OPTIONAL,RuleEnforcement.WARNING);
		reDef=new ValidationRuleDefinition(validationService,"Reaction definition discrepancy","NDOM parser",RE_DEF_ERROR_ID,
				RuleLevel.OPTIONAL,RuleEnforcement.WARNING);
		orphanRDef=new ValidationRuleDefinition(validationService,"Orphan object","NDOM parser",ORPHAN_PROCESS_ERROR_ID,
				RuleLevel.MANDATORY,RuleEnforcement.ERROR);
		orphanCDef=new ValidationRuleDefinition(validationService,"Orphan object","NDOM parser",ORPHAN_COMPOUND_ERROR_ID,
				RuleLevel.OPTIONAL,RuleEnforcement.WARNING);
		ERROR = new ValidationRuleDefinition(validationService, "Exception!", "Graph",
				ERROR_ID, RuleLevel.MANDATORY,RuleEnforcement.ERROR);

		configNotReg = new ValidationRuleConfig(notReg); // not run,
		configConsRev = new ValidationRuleConfig(consRev); // not run,
		configOrphanRDef = new ValidationRuleConfig(orphanRDef); // not run,
		configOrphanCDef = new ValidationRuleConfig(orphanCDef); // not run,
		// error
		configCompDef = new ValidationRuleConfig(compDef); // not run,
		configReDef = new ValidationRuleConfig(reDef); // not run,
		// error
		ERROR_CONFIG = new ValidationRuleConfig(ERROR); // not run,
																	// error
		stoichiometryIntDefinition();
		initConcentrDoubleDefinition();
		reParametersRegexDefinition();
	}

	private static void stoichiometryIntDefinition() {
		IntPropertyRule r = new IntPropertyRule("STOICH");
		stoich = new ValidationRuleDefinition(validationService,"Stoichiometry",  "NDOM parser",STOICH_ERROR_ID, RuleLevel.MANDATORY,RuleEnforcement.ERROR);
		configStoich = new ValidationRuleConfig(stoich);// must be
																	// run,
																	// error
		r.setRuleDef(stoich);
		rules.put(stoich, r);
	}

	private static void initConcentrDoubleDefinition() {
		DoublePropertyRule r = new DoublePropertyRule("IC");
		ic = new ValidationRuleDefinition(
				validationService, "Initial concentration", "NDOM parser",IC_ERROR_ID, RuleLevel.MANDATORY,RuleEnforcement.ERROR);
		configIC = new ValidationRuleConfig(ic); // must be run,
																// error
		r.setRuleDef(ic);
		rules.put(ic, r);
	}

	private static void reParametersRegexDefinition() {
		RegexpPropertyRule r = new RegexpPropertyRule("Parameters",
				"^(\\s*\\w+\\s*=\\s*[0-9eE\\-+.]+\\s*;)+$");
		r.setEmptyValid(true);
		reParam = new ValidationRuleDefinition(
				validationService,
				"Reaction parameters",  "NDOM parser",RE_PARAM_ERROR_ID, RuleLevel.MANDATORY,RuleEnforcement.ERROR);
		configReparam = new ValidationRuleConfig(reParam);
		r.setRuleDef(reParam);
		rules.put(reParam, r);
	}

	private MetabolicRuleLoader() {

	}

	public Set<IValidationRuleConfig> loadDefaultRuleConfigurations() {
		Set<IValidationRuleConfig> rc = new HashSet<IValidationRuleConfig>(
				Arrays.asList(new IValidationRuleConfig[] { configStoich,
						configIC, configReparam, configNotReg, configConsRev,configCompDef,configReDef,configOrphanCDef,configOrphanRDef,ERROR_CONFIG }));
		return rc;
	}

	public static IDefaultValidationRuleConfigLoader getInstance() {
		if (instance == null) {
			instance = new MetabolicRuleLoader();
		}
		return instance;
	}

	public IParserRule getRuleByDef(IValidationRuleDefinition def) {
		if (rules.containsKey(def)) {
			return rules.get(def);
		} else {
			IParserRule res = null;
			for (IValidationRuleDefinition d : rules.keySet()) {
				if (d.getRuleNumber() == def.getRuleNumber()) {
					res = rules.get(d);
					break;
				}
			}
			return res;
		}

	}
}

/*
 * $Log:$
 */