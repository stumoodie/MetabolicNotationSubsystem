package uk.ac.ed.inf.Metabolic;

import java.util.Set;

import org.pathwayeditor.contextadapter.publicapi.IValidationRuleConfig;

public interface IRuleConfigurer {

	public void configureRules(Set<IValidationRuleConfig> rulesToConfigure);
}
