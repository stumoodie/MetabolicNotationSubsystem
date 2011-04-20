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

import java.util.regex.Pattern;

import org.pathwayeditor.businessobjects.drawingprimitives.properties.IPlainTextAnnotationProperty;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IRuleValidationReportBuilder;

/**
 * Check regexp against property value. Designed to validate string values in
 * properties. <br>
 * $Id:$
 * 
 * @author Anatoly Sorokin
 * @date 5 Aug 2008
 * 
 */
public class RegexpPropertyRule extends AbstractPropertyRule implements	IParserRule {
	private final Pattern pattern;
	private boolean isEmptyValid = false;

	public RegexpPropertyRule(IValidationRuleDefinition rule, String propName, Pattern pattern) {
		super(rule, propName);
		this.pattern = pattern;
	}

	public RegexpPropertyRule(IValidationRuleDefinition rule, String propName, String regexp) {
		this(rule, propName, Pattern.compile(regexp));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pathwayeditor.metabolic.parser.IParserRule#validate(org.pathwayeditor.
	 * contextadapter.toolkit.validation.IRuleValidationReportBuilder)
	 */
	public boolean validate(IRuleValidationReportBuilder report) {
		checkState();
		if (report == null)
			throw new NullPointerException("Report builder is not set");
		if (pattern == null)
			throw new NullPointerException("Regex is not set");
		IPlainTextAnnotationProperty prop = (IPlainTextAnnotationProperty)this.getCurrentProperty();
		String val = prop.getValue();
		String st=null;
		if(val!=null){
			st = val.toString();
		}
		if ((st == null || st.trim().length() == 0)) {
			if (isEmptyValid) {
				report.setRulePassed(this.getRuleDef().getRuleNumber());
				return true;
			} else {
				report.setRuleFailed(this.getRefObject(), getRuleDef().getRuleNumber(),
						"Empty string is not valid value for " + this.getPropertyName());
				return false;
			}
		}
		if (pattern.matcher(st).matches()) {
			report.setRulePassed(this.getRuleDef().getRuleNumber());
			return true;
		}
		report.setRuleFailed(this.getRefObject(), this.getRuleDef().getRuleNumber(), "Regex not match value for "
				+ this.getPropertyName() + ": " + st + " <> (" + pattern + ")");
		return false;
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