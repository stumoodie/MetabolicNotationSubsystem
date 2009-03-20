package uk.ac.ed.inf.Metabolic.parser;

import java.math.BigDecimal;

import org.pathwayeditor.businessobjects.drawingprimitives.properties.INumberAnnotationProperty;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IRuleValidationReportBuilder;

/**
 * <br>$Id:$
 * @author Anatoly Sorokin
 * @date 28 Jun 2008
 * 
 */
public class IntPropertyRule extends AbstractPropertyRule implements IParserRule{
	/**e validation rule for conversion of <code>String</code> value to <code>int</code>.
	 * Create
	 * @param propName
	 */
	public IntPropertyRule(IValidationRuleDefinition ruleDefn, String propName) {
		super(ruleDefn, propName);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ed.inf.Metabolic.parser.IParserRule#validate(org.pathwayeditor.notationsubsystem.toolkit.validation.IRuleValidationReportBuilder)
	 */
	public boolean validate(IRuleValidationReportBuilder report) {
		checkState();
		if(report==null) throw new NullPointerException("Report builder is not set");
		INumberAnnotationProperty prop = (INumberAnnotationProperty)this.getCurrentProperty();  
		BigDecimal st = prop.getValue();
		if(st.remainder(BigDecimal.ONE) != BigDecimal.ZERO){
			// if there is a remainder then it is not an integer 
			report.setRuleFailed(this.getRefObject(), this.getRuleDef().getRuleNumber(), "Illegal integer value for "+this.getPropertyName()+": "+st );
			return false;
		}
		report.setRulePassed(this.getRuleDef().getRuleNumber());
		return true;
	}

	
	
}


/*
 * $Log:$
 */