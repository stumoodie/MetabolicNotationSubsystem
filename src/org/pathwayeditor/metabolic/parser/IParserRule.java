/*
  Licensed to the Court of the University of Edinburgh (UofE) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The UofE licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
*/
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