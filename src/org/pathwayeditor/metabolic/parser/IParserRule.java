package org.pathwayeditor.metabolic.parser;

import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingElement;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotatedObject;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IRuleValidationReportBuilder;

/**
 * <br>$Id:$
 * @author Anatoly Sorokin
 * @date 27 Jun 2008
 * 
 */
public interface IParserRule {

	IValidationRuleDefinition getRuleDef();
	
	/**
	 * set object to be analyse. 
	 * @param imo
	 */
	void setObject(IAnnotatedObject imo);

	IAnnotatedObject getObject();
	
	/**
	 * set reference object. set object to be selected in the case of error. By default {@link #setObject(IMapObject)} set  reference object as well.
	 * @param imo
	 */
	void setRefObject(IDrawingElement imo);

	IDrawingElement getRefObject();
	
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