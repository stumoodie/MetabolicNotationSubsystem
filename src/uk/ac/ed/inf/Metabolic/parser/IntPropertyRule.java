package uk.ac.ed.inf.Metabolic.parser;

import org.pathwayeditor.businessobjectsAPI.IMapObject;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleDefinition;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;
import org.pathwayeditor.contextadapter.toolkit.validation.ValidationRuleDefinition;

/**
 * <br>$Id:$
 * @author Anatoly Sorokin
 * @date 28 Jun 2008
 * 
 */
public class IntPropertyRule implements IParserRule{

	private String propName;
	private IValidationRuleDefinition ruleDef;
	
	private IMapObject imo;
	private IMapObject ref;
	private int value;
	
	/**
	 * Returns integer value of property defined by {@link #propName}.
	 * <br>Preconditions:
	 * <ul><li>{@link #validate(IRuleValidationReportBuilder)} returns <code>TRUE</code></li>
	 * </ul>
	 * @return
	 */
	public int getValue() {
		return value;
	}

	/**e validation rule for conversion of <code>String</code> value to <code>int</code>.
	 * Creat
	 * @param propName
	 */
	public IntPropertyRule(String propName) {
		this.propName=propName;
	}


	/**
	 * @return rule definition object
	 */
	public IValidationRuleDefinition getRuleDef() {
		return ruleDef;
	}


	/**
	 * @return object to be tested
	 */
	public IMapObject getImo() {
		return imo;
	}


	/* (non-Javadoc)
	 * @see uk.ac.ed.inf.Metabolic.parser.IParserRule#setRuleDef(org.pathwayeditor.contextadapter.publicapi.IValidationRuleDefinition)
	 */
	public void setRuleDef(IValidationRuleDefinition ruleDef) {
		this.ruleDef = ruleDef;
	}


	public void setObject(IMapObject imo) {
		this.imo = imo;
		ref=imo;
	}


	/* (non-Javadoc)
	 * @see uk.ac.ed.inf.Metabolic.parser.IParserRule#validate(org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder)
	 */
	public boolean validate(IRuleValidationReportBuilder report) {
		if(ruleDef==null) throw new NullPointerException("Rule definition is not set");
		if(imo==null) throw new NullPointerException("IMapObject is not set");
		if(report==null) throw new NullPointerException("Report builder is not set");
		String st=imo.getPropertyByName(propName).getValue();
		try {
			if (st != null && st.trim().length() > 0) {
				value = Integer.parseInt(st);
			}
		} catch (NumberFormatException nfe) {
			report.setRuleFailed(ref, ruleDef, "Illegal integer value for "+propName+": "+st );
			return false;
		}
		report.setRulePassed(ruleDef);
		return true;
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