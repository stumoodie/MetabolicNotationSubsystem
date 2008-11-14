package uk.ac.ed.inf.metaboliccontext.plugin;

import java.util.ArrayList;
import java.util.List;

import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleConfig;

import uk.ac.ed.inf.Metabolic.MetabolicNotationSubsystem;

public class MetabolicValidationRulePreferencesTest {
	private class MetabolicValidationRulePreferencesExt extends MetabolicValidationRulePreferences {

		protected void init() {}
	}

	public MetabolicValidationRulePreferencesExt prefsTSS;
	public MetabolicValidationRulePreferences prefs;
	ContextValidationService validationService;

	@Before
	public void setUp() throws Exception {
		validationService = (ContextValidationService) MetabolicNotationSubsystem
				.getInstance().getValidationService();
//		validationService.initRuleStore();
		prefsTSS = new MetabolicValidationRulePreferencesExt();
		prefs=prefsTSS;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRuleConfigsReturnsSameRuleStoreInstanceAsValidationService() {
		List<IValidationRuleConfig> store = new ArrayList<IValidationRuleConfig>(prefs.getConfigurableRules());
		//Collections.sort(store);
		assertNotNull(store);
		List<IValidationRuleConfig> serviceConfigs = new ArrayList<IValidationRuleConfig>(validationService.getRuleConfigurations());
		//Collections.sort(serviceConfigs);
		for(int i = 0; i< store.size(); i++) {
			assertEquals(store.get(i), serviceConfigs.get(i));
		}
	}

}
