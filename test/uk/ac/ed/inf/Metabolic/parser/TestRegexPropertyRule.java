package uk.ac.ed.inf.Metabolic.parser;

import java.util.regex.Pattern;

import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition.RuleLevel;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;
import org.pathwayeditor.contextadapter.toolkit.validation.ValidationRuleDefinition;

import uk.ac.ed.inf.Metabolic.MetabolicNotationSubsystem;

@RunWith(JMock.class)
public class TestRegexPropertyRule {
	Mockery mockery = new JUnit4Mockery();

	RegexpPropertyRule rule;
	IValidationRuleDefinition ruleDef;

	private IMapObject imo;

	private IContextProperty prop;

	private IRuleValidationReportBuilder report;
	@Before
	public void setUp() throws Exception {
		rule=new RegexpPropertyRule("Prop","^[0-9].*");
		ruleDef=new ValidationRuleDefinition(MetabolicNotationSubsystem.getInstance().getContext(),"RegexString conversion rule","Properties",-12,RuleLevel.OPTIONAL);
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
			{one(prop).getValue();will(returnValue("1  "));}
			{one(report).setRulePassed(ruleDef);}
			
		});
		rule.setObject(imo);
		assertTrue(rule.validate(report));
		assertEquals("String value","1  ", rule.getValue());
	}

	@Test
	public void testValidateWrongStringSet() {
		rule.setObject(imo);
		mockery.checking(new Expectations(){
			{atLeast(1).of(imo).getPropertyByName("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("One"));}
			{one(report).setRuleFailed(imo, ruleDef, "Regex not match value for Prop: One <> (^[0-9].*)");}
		});
		assertFalse(rule.validate(report));
		assertNull("String value",rule.getValue());
	}

	@Test
	public void testValidateEmptyStringIsEmptyValidSetFalse() {
		rule.setObject(imo);
		rule.setEmptyValid(false);
		mockery.checking(new Expectations(){
			{atLeast(1).of(imo).getPropertyByName("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(""));}
			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		assertFalse(rule.validate(report));
		assertNull("String value",rule.getValue());
	}
	@Test
	public void testValidateSpacesStringIsEmptyValidSetFalse() {
		rule.setObject(imo);
		rule.setEmptyValid(false);
		mockery.checking(new Expectations(){
			{atLeast(1).of(imo).getPropertyByName("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("   "));}
			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		assertFalse(rule.validate(report));
		assertNull("String value",rule.getValue());
	}
	
	@Test
	public void testValidateNullStringIsEmptyValidSetFalse() {
		rule.setObject(imo);
		rule.setEmptyValid(false);
		mockery.checking(new Expectations(){
			{atLeast(1).of(imo).getPropertyByName("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(null));}
			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		assertFalse(rule.validate(report));
		assertNull("String value",rule.getValue());
	}

	@Test
	public void testValidateEmptyStringIsEmptyValidSetTrue() {
		rule.setObject(imo);
		rule.setEmptyValid(true);
		mockery.checking(new Expectations(){
			{atLeast(1).of(imo).getPropertyByName("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(""));}
			{one(report).setRulePassed(ruleDef);}
//			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		assertTrue(rule.validate(report));
		assertEquals("String value","",rule.getValue());
	}
	
	@Test
	public void testValidateSpacesStringIsEmptyValidSetTrue() {
		rule.setObject(imo);
		rule.setEmptyValid(true);
		mockery.checking(new Expectations(){
			{atLeast(1).of(imo).getPropertyByName("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue("\t"));}
			{one(report).setRulePassed(ruleDef);}
//			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
		assertTrue(rule.validate(report));
		assertEquals("String value","",rule.getValue());
	}
	
	@Test
	public void testValidateNullStringIsEmptyValidSetTrue() {
		rule.setObject(imo);
		rule.setEmptyValid(true);
		mockery.checking(new Expectations(){
			{atLeast(1).of(imo).getPropertyByName("Prop");will(returnValue(prop));}
			{one(prop).getValue();will(returnValue(null));}
			{one(report).setRulePassed(ruleDef);}
//			{one(report).setRuleFailed(imo, ruleDef, "Empty string is not valid value for Prop");}
		});
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