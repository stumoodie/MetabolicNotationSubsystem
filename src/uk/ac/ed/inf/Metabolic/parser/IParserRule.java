package uk.ac.ed.inf.Metabolic.parser;

import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingElement;
import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingNode;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotatedObject;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;

/**
 * <br>$Id:$
 * @author Anatoly Sorokin
 * @date 27 Jun 2008
 * 
 */
public interface IParserRule {

	/**
	 * Assign rule definition object to the actual rule. should be invoked in constructor for particular rule definition;
	 * @param ruleDef
	 */
	void setRuleDef(IValidationRuleDefinition ruleDef);
	
	/**
	 * set object to be analyse. 
	 * @param imo
	 */
	void setObject(IAnnotatedObject imo);

	/**
	 * set reference object. set object to be selected in the case of error. By default {@link #setObject(IMapObject)} set  reference object as well.
	 * @param imo
	 */
	void setRefObject(IDrawingElement imo);

	
	/**
	 * Validate the rule. 
	 * <br> Preconditions:<ul><li>IMapObject is set</li>
	 * <li>Ruledefinition is set</li></ul>
	 * Postconditions:<ul><li>If object is valid returns true</li>
	 * <li>If object is not valid:<ul><li>Failure is reported to IRuleValidationReportBuilder</li>
	 * <li>return false</li></li></ul>
	 * @param report report builder to report problems.
	 * @return true if object is valid false otherwise.
	 */
	boolean validate(IRuleValidationReportBuilder report);

}


/*
 * $Log:$
 */