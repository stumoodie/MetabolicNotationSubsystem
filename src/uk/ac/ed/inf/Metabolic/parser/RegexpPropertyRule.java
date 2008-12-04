package uk.ac.ed.inf.Metabolic.parser;

import java.util.regex.Pattern;

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
public class RegexpPropertyRule extends AbstractPropertyRule implements
		IParserRule {

	private String propName;
	private Pattern pattern;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.ed.inf.Metabolic.parser.IParserRule#validate(org.pathwayeditor.
	 * contextadapter.toolkit.validation.IRuleValidationReportBuilder)
	 */
	public boolean validate(IRuleValidationReportBuilder report) {
		checkState();
		if (report == null)
			throw new NullPointerException("Report builder is not set");
		if (pattern == null)
			throw new NullPointerException("Regex is not set");
		Object val = imo.getProperty(
				propName).getValue();
		String st=null;
		if(val!=null){
			st = val.toString();
		}
		if ((st == null || st.trim().length() == 0)) {
			if (isEmptyValid) {
				value = "";
				report.setRulePassed(ruleDef);
				return true;
			} else {
				report.setRuleFailed(ref, ruleDef,
						"Empty string is not valid value for " + propName);
				return false;
			}
		}
		if (pattern.matcher(st).matches()) {
			value = st;
			report.setRulePassed(ruleDef);
			return true;
		}
		report.setRuleFailed(ref, ruleDef, "Regex not match value for "
				+ propName + ": " + st + " <> (" + pattern + ")");
		return false;
	}

	protected String getPropName() {
		return propName;
	}

	/**
	 * @return
	 */
	protected String getValue() {
		return value;
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

}

/*
 * $Log:$
 */