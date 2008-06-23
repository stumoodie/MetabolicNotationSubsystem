package uk.ac.ed.inf.metaboliccontext.plugin;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleStore;

import uk.ac.ed.inf.Metabolic.MetabolicContextAdapterServiceProvider;
import uk.ac.ed.inf.Metabolic.MetabolicContextValidationService;

public class MetabolicValidationRulePreferencesTest {
	private class MetabolicValidationRulePreferencesExt extends MetabolicValidationRulePreferences {

		protected void init() {}
	}

	public MetabolicValidationRulePreferencesExt prefsTSS;
	public MetabolicValidationRulePreferences prefs;
	MetabolicContextValidationService validationService;

	@Before
	public void setUp() throws Exception {
		validationService = (MetabolicContextValidationService) MetabolicContextAdapterServiceProvider
				.getInstance().getValidationService();
		validationService.initRuleStore();
		prefsTSS = new MetabolicValidationRulePreferencesExt();
		prefs=prefsTSS;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRuleStoreReturnsSameRuleStoreInstanceAsValidationService() {
		IValidationRuleStore store = prefs.getRuleStore();
		assertNotNull(store);
		assertTrue(store == validationService.getRuleStore());
	}

}
