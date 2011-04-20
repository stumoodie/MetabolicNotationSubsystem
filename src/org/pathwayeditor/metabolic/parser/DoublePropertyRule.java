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