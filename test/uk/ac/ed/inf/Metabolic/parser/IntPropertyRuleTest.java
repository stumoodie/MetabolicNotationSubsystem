package uk.ac.ed.inf.Metabolic.parser;

import static org.junit.Assert.*;

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
public class IntPropertyRuleTest {
	Mockery mockery = new JUnit4Mockery();

	IntPropertyRule rule;
	IValidationRuleDefinition ruleDef;

	private IShapeAttribute imo;
	protected IDrawingElement ref;

	private IAnnotationProperty prop;

	private IRuleValidationReportBuilder report;
	@Before
	public void setUp() throws Exception {
		rule=new IntPropertyRule("Prop");
		ruleDef=new ValidationRuleDefinition(MetabolicNotationSubsystem.getInstance().getValidationService(),"IntString conversion rule","Properties",-12,RuleLevel.OPTIONAL, RuleEnforcement.ERROR);
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
			{one(prop).getValue();will(returnValue("1"));}
			{one(report).setRulePassed(ruleDef);}
			
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
		assertEquals("Integer value",1, rule.getValue());
	}

	@Test
	public void testValidateWrongStringSet() {
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{atLeast(1).of(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("One"));}
			{one(report).setRuleFailed(ref, ruleDef, "Illegal integer value for Prop: One");}
		});
		rule.setRefObject(ref);
		assertFalse(rule.validate(report));
		assertEquals("Integer value",0, rule.getValue());
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

}


/*
 * $Log:$
 */