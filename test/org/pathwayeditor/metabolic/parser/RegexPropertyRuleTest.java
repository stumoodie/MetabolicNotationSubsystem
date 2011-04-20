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

import java.util.regex.Pattern;

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
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IPlainTextAnnotationProperty;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleEnforcement;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.metabolic.parser.RegexpPropertyRule;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IRuleValidationReportBuilder;
import org.pathwayeditor.notationsubsystem.toolkit.validation.ValidationRuleDefinition;

@RunWith(JMock.class)
public class RegexPropertyRuleTest {
	Mockery mockery = new JUnit4Mockery();

	RegexpPropertyRule rule;
	IValidationRuleDefinition ruleDef;

	private IShapeAttribute imo;
	protected IDrawingElement ref;

	private IPlainTextAnnotationProperty prop;

	private IRuleValidationReportBuilder report;

	@Before
	public void setUp() throws Exception {
		ruleDef=new ValidationRuleDefinition("RegexString conversion rule","Properties",-12,RuleLevel.OPTIONAL, RuleEnforcement.ERROR);
		rule=new RegexpPropertyRule(ruleDef, "Prop","^[0-9].*");
		imo = mockery.mock(IShapeAttribute.class);
		ref=mockery.mock(IDrawingElement.class);
		prop = mockery.mock(IPlainTextAnnotationProperty.class);
		report = mockery.mock(IRuleValidationReportBuilder.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateEverythingSet() {
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{one(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("1  "));}
			{one(report).setRulePassed(ruleDef.getRuleNumber());}
			
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
	}

	@Test
	public void testValidateWrongStringSet() {
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("One"));}
			{one(report).setRuleFailed(ref, ruleDef.getRuleNumber(), "Regex not match value for Prop: One <> (^[0-9].*)");}
		});
		rule.setRefObject(ref);
		assertFalse(rule.validate(report));
	}

	@Test
	public void testValidateEmptyStringIsEmptyValidSetFalse() {
		rule.setEmptyValid(false);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(""));}
			{one(report).setRuleFailed(ref, ruleDef.getRuleNumber(), "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertFalse(rule.validate(report));
	}
	@Test
	public void testValidateSpacesStringIsEmptyValidSetFalse() {
		rule.setEmptyValid(false);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("   "));}
			{one(report).setRuleFailed(ref, ruleDef.getRuleNumber(), "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertFalse(rule.validate(report));
	}
	
	@Test
	public void testValidateNullStringIsEmptyValidSetFalse() {
		rule.setEmptyValid(false);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(null));}
			{one(report).setRuleFailed(ref, ruleDef.getRuleNumber(), "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertFalse(rule.validate(report));
	}

	@Test
	public void testValidateEmptyStringIsEmptyValidSetTrue() {
		rule.setEmptyValid(true);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(""));}
			{one(report).setRulePassed(ruleDef.getRuleNumber());}
//			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
	}
	
	@Test
	public void testValidateSpacesStringIsEmptyValidSetTrue() {
		rule.setEmptyValid(true);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("\t"));}
			{one(report).setRulePassed(ruleDef.getRuleNumber());}
//			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
	}
	
	@Test
	public void testValidateNullStringIsEmptyValidSetTrue() {
		rule.setEmptyValid(true);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(null));}
			{one(report).setRulePassed(ruleDef.getRuleNumber());}
//			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
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
			{allowing(prop).getValue();will(returnValue(null));}
			{allowing(report).setRuleFailed(ref, ruleDef.getRuleNumber(), "Empty string is not valid value for Prop");}
		});
		rule=new RegexpPropertyRule(null, "Prop","^[0-9].*");
		rule.setObject(imo);
		assertFalse(rule.validate(report));
	}

	@Test(expected=NullPointerException.class)
	public void testValidatePatternNotSet() {
		rule=new RegexpPropertyRule(ruleDef, "Prop",(Pattern)null);
		rule.setObject(imo);
		assertFalse(rule.validate(report));
	}

}


/*
 * $Log:$
 */