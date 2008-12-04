package uk.ac.ed.inf.Metabolic.parser;

import static org.junit.Assert.*;

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
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotationProperty;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleEnforcement;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;
import org.pathwayeditor.contextadapter.toolkit.validation.ValidationRuleDefinition;

import uk.ac.ed.inf.Metabolic.MetabolicNotationSubsystem;

@RunWith(JMock.class)
public class RegexPropertyRuleTest {
	Mockery mockery = new JUnit4Mockery();

	RegexpPropertyRule rule;
	IValidationRuleDefinition ruleDef;

	private IShapeAttribute imo;
	protected IDrawingElement ref;

	private IAnnotationProperty prop;

	private IRuleValidationReportBuilder report;

	@Before
	public void setUp() throws Exception {
		rule=new RegexpPropertyRule("Prop","^[0-9].*");
		ruleDef=new ValidationRuleDefinition(MetabolicNotationSubsystem.getInstance().getValidationService(),"RegexString conversion rule","Properties",-12,RuleLevel.OPTIONAL, RuleEnforcement.ERROR);
		rule.setRuleDef(ruleDef);
		imo = mockery.mock(IShapeAttribute.class);
		ref=mockery.mock(IDrawingElement.class);
		prop = mockery.mock(IAnnotationProperty.class);
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
			{one(report).setRulePassed(ruleDef);}
			
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
		assertEquals("String value","1  ", rule.getValue());
	}

	@Test
	public void testValidateWrongStringSet() {
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("One"));}
			{one(report).setRuleFailed(ref, ruleDef, "Regex not match value for Prop: One <> (^[0-9].*)");}
		});
		rule.setRefObject(ref);
		assertFalse(rule.validate(report));
		assertNull("String value",rule.getValue());
	}

	@Test
	public void testValidateEmptyStringIsEmptyValidSetFalse() {
		rule.setEmptyValid(false);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(""));}
			{one(report).setRuleFailed(ref, ruleDef, "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertFalse(rule.validate(report));
		assertNull("String value",rule.getValue());
	}
	@Test
	public void testValidateSpacesStringIsEmptyValidSetFalse() {
		rule.setEmptyValid(false);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("   "));}
			{one(report).setRuleFailed(ref, ruleDef, "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertFalse(rule.validate(report));
		assertNull("String value",rule.getValue());
	}
	
	@Test
	public void testValidateNullStringIsEmptyValidSetFalse() {
		rule.setEmptyValid(false);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(null));}
			{one(report).setRuleFailed(ref, ruleDef, "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertFalse(rule.validate(report));
		assertNull("String value",rule.getValue());
	}

	@Test
	public void testValidateEmptyStringIsEmptyValidSetTrue() {
		rule.setEmptyValid(true);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(""));}
			{one(report).setRulePassed(ruleDef);}
//			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
		assertEquals("String value","",rule.getValue());
	}
	
	@Test
	public void testValidateSpacesStringIsEmptyValidSetTrue() {
		rule.setEmptyValid(true);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("\t"));}
			{one(report).setRulePassed(ruleDef);}
//			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
		assertEquals("String value","",rule.getValue());
	}
	
	@Test
	public void testValidateNullStringIsEmptyValidSetTrue() {
		rule.setEmptyValid(true);
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(null));}
			{one(report).setRulePassed(ruleDef);}
//			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
		assertEquals("String value","",rule.getValue());
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
		rule.setRuleDef(null);
		rule.setObject(imo);
		assertFalse(rule.validate(report));
	}

	@Test(expected=NullPointerException.class)
	public void testValidatePatternNotSet() {
		rule=new RegexpPropertyRule("Prop",(Pattern)null);
		rule.setRuleDef(ruleDef);
		rule.setObject(imo);
		assertFalse(rule.validate(report));
	}

}


/*
 * $Log:$
 */