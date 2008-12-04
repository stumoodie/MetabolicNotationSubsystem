package uk.ac.ed.inf.Metabolic.parser;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingElement;
import org.pathwayeditor.businessobjects.drawingprimitives.IShapeAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotatedObject;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotationProperty;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleEnforcement;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;
import org.pathwayeditor.contextadapter.toolkit.validation.ValidationRuleDefinition;

import uk.ac.ed.inf.Metabolic.MetabolicNotationSubsystem;

//@RunWith(JMock.class)
public class DoublePropertyRuleTest {
	private static final double ASSERT_DOUBLE_DELTA = 0.0001;

	Mockery mockery = new JUnit4Mockery();

	DoublePropertyRule rule;
	IValidationRuleDefinition ruleDef;

	private IShapeAttribute imo;
	protected IDrawingElement ref;

	private IAnnotationProperty prop;

	private IRuleValidationReportBuilder report;
	@Before
	public void setUp() throws Exception {
		rule=new DoublePropertyRule("Prop");
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
			{one(prop).getValue();will(returnValue("1.0"));}
			{one(report).setRulePassed(ruleDef);}
			
		});
		rule.setRefObject(ref);
		assertTrue(rule.validate(report));
		assertEquals("Double value",1.0, rule.getValue(), ASSERT_DOUBLE_DELTA);
	}

	@Test
	public void testValidateWrongStringSet() {
		
		mockery.checking(new Expectations(){
			{one(ref).getAttribute();will(returnValue(imo));}
			{one(imo).getProperty("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("One"));}
			{allowing(report).setRuleFailed(ref, ruleDef, "Illegal double value for Prop: One");}
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
		rule.setRuleDef(null);
		rule.setObject(imo);
		assertFalse(rule.validate(report));
	}

}


/*
 * $Log:$
 */