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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingElement;
import org.pathwayeditor.businessobjects.drawingprimitives.IShapeAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.INumberAnnotationProperty;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleEnforcement;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.metabolic.parser.IntPropertyRule;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IRuleValidationReportBuilder;
import org.pathwayeditor.notationsubsystem.toolkit.validation.ValidationRuleDefinition;

@RunWith(JMock.class)
public class IntPropertyRuleTest {
	private static final int RULE_NUMBER = -12;

	Mockery mockery = new JUnit4Mockery();

	IntPropertyRule rule;
	IValidationRuleDefinition ruleDef;

	private IShapeAttribute imo;
	protected IDrawingElement ref;

	private INumberAnnotationProperty prop;

	private IRuleValidationReportBuilder report;
	@Before
	public void setUp() throws Exception {
		ruleDef=new ValidationRuleDefinition("IntString conversion rule","Properties",RULE_NUMBER,RuleLevel.OPTIONAL, RuleEnforcement.ERROR);
		rule=new IntPropertyRule(ruleDef, "Prop");
		imo = mockery.mock(IShapeAttribute.class);
		ref=mockery.mock(IDrawingElement.class);
		prop = mockery.mock(INumberAnnotationProperty.class);
		report = mockery.mock(IRuleValidationReportBuilder.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateEverythingSet() {
		mockery.checking(new Expectations(){
			{allowing(ref).getAttribute();will(returnValue(imo));}
			{allowing(imo).getProperty("Prop");will(returnValue(prop));}
			{allowing(prop).getValue();will(returnValue(BigDecimal.ONE));}
			{allowing(report).setRulePassed(ruleDef.getRuleNumber());}
			
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
	}

	@Test
	public void testValidateWrongStringSet() {
		mockery.checking(new Expectations(){
			{allowing(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{allowing(prop).getValue();will(returnValue(new BigDecimal(1.1)));}
			{allowing(report).setRuleFailed(with(same(ref)), with(same(RULE_NUMBER)), with(any(String.class)));}
		});
		rule.setRefObject(ref);
		assertFalse(rule.validate(report));
	}

	@Test(expected=NullPointerException.class)
	public void testValidateReportNotSet() {
		rule.setObject(imo);
		assertFalse(rule.validate(null));
	}

	@Test(expected=NullPointerException.class)
	public void testValidateIMONotSet() {
		rule.setObject(imo);
		assertFalse(rule.validate(null));
	}

	
	@Test(expected=NullPointerException.class)
	public void testValidateRuleDefNotSet() {
		mockery.checking(new Expectations(){
			{allowing(ref).getAttribute();will(returnValue(imo));}
			{allowing(imo).getProperty("Prop");will(returnValue(prop));}
			{allowing(prop).getValue();will(returnValue(BigDecimal.ONE));}
			{allowing(report).setRulePassed(ruleDef.getRuleNumber());}
			
		});
		rule=new IntPropertyRule(null, "Prop");
		rule.setObject(imo);
		assertFalse(rule.validate(report));
	}

}


/*
 * $Log:$
 */