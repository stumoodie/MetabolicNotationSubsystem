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
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotationProperty;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;

public abstract class AbstractPropertyRule implements IParserRule {
	private final IValidationRuleDefinition ruleDef;
	private final String propertyName;
	private IAnnotatedObject imo;
	private IDrawingElement ref;

	protected AbstractPropertyRule(IValidationRuleDefinition ruleDefn, String propertyName){
		this.ruleDef = ruleDefn;
		this.propertyName = propertyName;
	}
	
	public String getPropertyName(){
		return this.propertyName;
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
	public IAnnotatedObject getObject() {
		return imo;
	}

	public void setObject(IAnnotatedObject imo) {
		this.imo = imo;
	}

	protected IAnnotationProperty getCurrentProperty(){
		return this.imo.getProperty(this.propertyName);
	}
	
	public void setRefObject(IDrawingElement r) {
		ref=r;
		imo=(IAnnotatedObject) ref.getAttribute();
	}

	public IDrawingElement getRefObject() {
		return ref;
	}

	protected void checkState() {
		if(imo==null) throw new IllegalStateException("IAnnotatedObject is not set");
	}

}
