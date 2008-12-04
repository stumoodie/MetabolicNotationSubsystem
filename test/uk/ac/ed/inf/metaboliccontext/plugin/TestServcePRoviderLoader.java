package uk.ac.ed.inf.metaboliccontext.plugin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestServcePRoviderLoader {
    IContextAdapterServiceProviderLoader loader;
	@Before
	public void setUp() throws Exception {
		loader = new MetabolicContextServiceProviderLoader();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetContextAdapterServiceProviderIsSingleInstance() {
		IContextAdapterServiceProvider sp = loader.getContextAdapterServiceProvider();
		assertNotNull(sp);
		assertTrue(sp == loader.getContextAdapterServiceProvider());
	}

}
