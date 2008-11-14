package uk.ac.ed.inf.metaboliccontext.plugin;


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
