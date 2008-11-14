package uk.ac.ed.inf.Metabolic.parser;

import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingNode;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotatedObject;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;

/**
 * <br>$Id:$
 * @author Anatoly Sorokin
 * @date 28 Jun 2008
 * 
 */
public class DoublePropertyRule implements IParserRule{

	private String propName;
	private IValidationRuleDefinition ruleDef;
	
	private IDrawingNode imo;
	private IDrawingNode ref;
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


	/**
	 * @return object to be tested
	 */
	public IDrawingNode getImo() {
		return imo;
	}


	/* (non-Javadoc)
	 * @see uk.ac.ed.inf.Metabolic.parser.IParserRule#setRuleDef(org.pathwayeditor.contextadapter.publicapi.IValidationRuleDefinition)
	 */
	public void setRuleDef(IValidationRuleDefinition ruleDef) {
		this.ruleDef = ruleDef;
	}


	public void setObject(IDrawingNode imo) {
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
		String st=((IAnnotatedObject) imo.getAttribute()).getProperty(propName).getValue().toString();
		try {
			if (st != null && st.trim().length() > 0) {
				value = Double.parseDouble(st);
			}
		} catch (NumberFormatException nfe) {
			report.setRuleFailed(imo, ruleDef, "Illegal double value for "+propName+": "+st );
			return false;
		}
		report.setRulePassed(ruleDef);
		return true;
	}

	public void setRefObject(IDrawingNode imo) {
		ref=imo;
	}

	public IDrawingNode getRefObject() {
		return ref;
	}

	
	
}


/*
 * $Log:$
 */