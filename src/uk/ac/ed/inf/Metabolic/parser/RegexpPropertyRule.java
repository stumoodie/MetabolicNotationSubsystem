package uk.ac.ed.inf.Metabolic.parser;

import java.util.regex.Pattern;

import org.pathwayeditor.businessobjectsAPI.IMapObject;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleDefinition;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;

/**
 * Check regexp against property value. Designed to validate string values in
 * properties. <br>
 * $Id:$
 * 
 * @author Anatoly Sorokin
 * @date 5 Aug 2008
 * 
 */
public class RegexpPropertyRule implements IParserRule {

	private String propName;
	private IValidationRuleDefinition ruleDef;
	private Pattern pattern;

	private IMapObject imo;
	private IMapObject ref;
	private String value;
	private boolean isEmptyValid = false;

	public RegexpPropertyRule(String propName, Pattern pattern) {
		this.propName = propName;
		this.pattern = pattern;
	}

	public RegexpPropertyRule(String propName, String regexp) {
		this.propName = propName;
		this.pattern = Pattern.compile(regexp);
	}

	public void setObject(IMapObject imo) {
		this.imo = imo;
		ref=imo;
	}

	public void setRuleDef(IValidationRuleDefinition ruleDef) {
		this.ruleDef = ruleDef;
	}

	public boolean validate(IRuleValidationReportBuilder report) {
		if (ruleDef == null)
			throw new NullPointerException("Rule definition is not set");
		if (imo == null)
			throw new NullPointerException("IMapObject is not set");
		if (report == null)
			throw new NullPointerException("Report builder is not set");
		if (pattern == null)
			throw new NullPointerException("Regex is not set");
		String st = imo.getPropertyByName(propName).getValue();
		if ((st == null || st.trim().length() == 0)) {
			if (isEmptyValid) {
				value = "";
				report.setRulePassed(ruleDef);
				return true;
			} else {
				report.setRuleFailed(imo, ruleDef,
						"Empty string is not valid value for " + propName);
				return false;
			}
		}
		if (pattern.matcher(st).matches()) {
			value = st;
			report.setRulePassed(ruleDef);
			return true;
		}
		report.setRuleFailed(imo, ruleDef, "Regex not match value for "
				+ propName + ": " + st + " <> (" + pattern + ")");
		return false;
	}

	protected String getPropName() {
		return propName;
	}

	protected IValidationRuleDefinition getRuleDef() {
		return ruleDef;
	}

	protected String getValue() {
		return value;
	}

	protected IMapObject getImo() {
		return imo;
	}

	/**
	 * Is empty string acceptable by the rule.
	 * 
	 * @return
	 */
	public boolean isEmptyValid() {
		return isEmptyValid;
	}

	/**
	 * Set acceptability of empty string. By default empty string is not valid.
	 * 
	 * @param isEmptyValid
	 */
	public void setEmptyValid(boolean isEmptyValid) {
		this.isEmptyValid = isEmptyValid;
	}


	public void setRefObject(IMapObject imo) {
		ref=imo;
	}

	public IMapObject getRefObject() {
		return ref;
	}
}

/*
 * $Log:$
 */