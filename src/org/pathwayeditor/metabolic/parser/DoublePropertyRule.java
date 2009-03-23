package org.pathwayeditor.metabolic.parser;

import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IRuleValidationReportBuilder;

/**
 * <br>$Id:$
 * @author Anatoly Sorokin
 * @date 28 Jun 2008
 * 
 */
public class DoublePropertyRule extends AbstractPropertyRule implements IParserRule{
	
	/**e validation rule for conversion of <code>String</code> value to <code>int</code>.
	 * Creat
	 * @param propName
	 */
	public DoublePropertyRule(IValidationRuleDefinition rule, String propName) {
		super(rule, propName);
	}


	/* (non-Javadoc)
	 * @see org.pathwayeditor.metabolic.parser.IParserRule#validate(org.pathwayeditor.notationsubsystem.toolkit.validation.IRuleValidationReportBuilder)
	 */
	public boolean validate(IRuleValidationReportBuilder report) {
		checkState();
		if(report==null) throw new NullPointerException("Report builder is not set");
		//TODO: do we need this since the property type guarantees the number type.
		report.setRulePassed(this.getRuleDef().getRuleNumber());
		return true;
	}


	
	
}


/*
 * $Log:$
 */