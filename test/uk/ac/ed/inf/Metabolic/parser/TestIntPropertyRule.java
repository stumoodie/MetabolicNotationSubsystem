package uk.ac.ed.inf.Metabolic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.businessobjectsAPI.IContextProperty;
import org.pathwayeditor.businessobjectsAPI.IMapObject;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleDefinition;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;
import org.pathwayeditor.contextadapter.toolkit.validation.ValidationRuleDefinition;

import uk.ac.ed.inf.Metabolic.MetabolicContextAdapterServiceProvider;

@RunWith(JMock.class)
public class TestIntPropertyRule {
	Mockery mockery = new JUnit4Mockery();

	IntPropertyRule rule;
	IValidationRuleDefinition ruleDef;

	private IMapObject imo;

	private IContextProperty prop;

	private IRuleValidationReportBuilder report;
	@Before
	public void setUp() throws Exception {
		rule=new IntPropertyRule("Prop");
		ruleDef=new ValidationRuleDefinition(MetabolicContextAdapterServiceProvider.getInstance().getContext(),"IntString conversion rule","Properties",-12,RuleLevel.OPTIONAL);
		rule.setRuleDef(ruleDef);
		imo = mockery.mock(IMapObject.class);
		prop = mockery.mock(IContextProperty.class);
		report = mockery.mock(IRuleValidationReportBuilder.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateEverythingSet() {
		mockery.checking(new Expectations(){
			{one(imo).getPropertyByName("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("1"));}
			{one(report).setRulePassed(ruleDef);}
			
		});
		rule.setObject(imo);
		assertTrue(rule.validate(report));
		assertEquals("Integer value",1, rule.getValue());
	}

	@Test
	public void testValidateWrongStringSet() {
		rule.setObject(imo);
		mockery.checking(new Expectations(){
			{atLeast(1).of(imo).getPropertyByName("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("One"));}
			{one(report).setRuleFailed(imo, ruleDef, "Illegal integer value for Prop: One");}
		});
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