package uk.ac.ed.inf.Metabolic.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingElement;
import org.pathwayeditor.businessobjects.drawingprimitives.IShapeAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotationProperty;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleEnforcement;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IRuleValidationReportBuilder;
import org.pathwayeditor.notationsubsystem.toolkit.validation.ValidationRuleDefinition;

//@RunWith(JMock.class)
public class DoublePropertyRuleTest {
//	private static final double ASSERT_DOUBLE_DELTA = 0.0001;

	Mockery mockery = new JUnit4Mockery();

	DoublePropertyRule rule;
	IValidationRuleDefinition ruleDef;

	private IShapeAttribute imo;
	protected IDrawingElement ref;

	private IAnnotationProperty prop;

	private IRuleValidationReportBuilder report;
	@Before
	public void setUp() throws Exception {
		ruleDef=new ValidationRuleDefinition("IntString conversion rule","Properties",-12,RuleLevel.OPTIONAL, RuleEnforcement.ERROR);
		rule=new DoublePropertyRule(ruleDef, "Prop");
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
			{one(report).setRulePassed(ruleDef.getRuleNumber());}
			
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
		rule=new DoublePropertyRule(null, "Prop");
		rule.setObject(imo);
		assertFalse(rule.validate(report));
	}

}


/*
 * $Log:$
 */