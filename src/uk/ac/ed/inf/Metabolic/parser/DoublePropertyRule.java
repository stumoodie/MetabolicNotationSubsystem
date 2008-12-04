package uk.ac.ed.inf.Metabolic.parser;

import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;

/**
 * <br>$Id:$
 * @author Anatoly Sorokin
 * @date 28 Jun 2008
 * 
 */
public class DoublePropertyRule extends AbstractPropertyRule implements IParserRule{

	private String propName;
	private double value;
	
	/**
	 * Returns double value value of property defined by {@link #propName}.
	 * <br>Preconditions:
	 * <ul><li>{@link #validate(IRuleValidationReportBuilder)} returns <code>TRUE</code></li>
	 * </ul>
	 * @return
	 */
	public double getValue() {
		return value;
	}

	/**e validation rule for conversion of <code>String</code> value to <code>int</code>.
	 * Creat
	 * @param propName
	 */
	public DoublePropertyRule(String propName) {
		this.propName=propName;
	}


	/**
	 * @return rule definition object
	 */
	public IValidationRuleDefinition getRuleDef() {
		return ruleDef;
	}



	/* (non-Javadoc)
	 * @see uk.ac.ed.inf.Metabolic.parser.IParserRule#validate(org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder)
	 */
	public boolean validate(IRuleValidationReportBuilder report) {
		checkState();
		if(report==null) throw new NullPointerException("Report builder is not set");
		String st= imo.getProperty(propName).getValue().toString();
		try {
			if (st != null && st.trim().length() > 0) {
				value = Double.parseDouble(st);
			}
		} catch (NumberFormatException nfe) {
			report.setRuleFailed(ref, ruleDef, "Illegal double value for "+propName+": "+st );
			return false;
		}
		report.setRulePassed(ruleDef);
		return true;
	}


	
	
}


/*
 * $Log:$
 */