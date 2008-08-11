package uk.ac.ed.inf.metaboliccontext.plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleConfig;
import org.pathwayeditor.contextadapter.toolkit.validation.ContextValidationService;

import uk.ac.ed.inf.Metabolic.MetabolicContextAdapterServiceProvider;

public class MetabolicValidationRulePreferencesTest {
	private class MetabolicValidationRulePreferencesExt extends MetabolicValidationRulePreferences {

		protected void init() {}
	}

	public MetabolicValidationRulePreferencesExt prefsTSS;
	public MetabolicValidationRulePreferences prefs;
	ContextValidationService validationService;

	@Before
	public void setUp() throws Exception {
		validationService = (ContextValidationService) MetabolicContextAdapterServiceProvider
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
