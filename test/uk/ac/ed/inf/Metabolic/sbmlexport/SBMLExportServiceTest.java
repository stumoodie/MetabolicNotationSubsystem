package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.File;

import org.eclipse.ui.contexts.IContext;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pathwayeditor.businessobjects.notationsubsystem.ExportServiceException;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationReport;
import org.pathwayeditor.businessobjects.repository.IMap;

import uk.ac.ed.inf.Metabolic.IExportAdapter;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

@RunWith(JMock.class)
public class SBMLExportServiceTest {

	private Mockery mockery = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};

	final IContext context = mockery.mock(IContext.class);
	final IMap map = mockery.mock(IMap.class);
	final IRootMapObject rmo = mockery.mock(IRootMapObject.class);
	final IMapObject child = mockery.mock(IMapObject.class);
	final IContextAdapterServiceProvider provider=mockery.mock(IContextAdapterServiceProvider.class);
	final IModel model = mockery.mock(IModel.class);
	SBMLExportService service;
	SBMLExportServiceTSS serviceTSS;
    File NONEXISTENT = new File ("/xyz/fhdg/dfhsgsf.shydfg/ABC/dgdsg");
    File EXISTENT;
//    static boolean canRun = false; // check this is true b4 running export tests.

	private IExportAdapter<IModel> adapter;
	
	class SBMLExportServiceTSS extends SBMLExportService {

		public SBMLExportServiceTSS(IContextAdapterServiceProvider provider) {
			super(provider);
		}
		IModel getModel(IContextAdapterValidationService validator) {
			return model;
		}
		IExportAdapter<IModel> getGenerator() {
			return adapter;
		}
		
	}
   
    
   //uk.ac.ed.inf.metabolic
//    @BeforeClass 
//    public static void loadNativeLibraries () throws Exception {
//    	canRun = LibSBMLConfigManager.configure();
//    }
 
    @Before
	public void setUp() throws Exception {
    	mockery.checking(new Expectations(){{atLeast(1).of(provider).getContext(); will(returnValue(validationService));}});
		serviceTSS = new SBMLExportServiceTSS(provider);
		service = serviceTSS;
		 EXISTENT = new File ("SBMLoutput");
		 mockery.checking(new Expectations() {
			{one(rmo).addChild(child);}
		});
		 rmo.addChild(child);
	
	}

	@After
	public void tearDown() throws Exception {
		EXISTENT.delete();
	}

	@Test(expected=IllegalArgumentException.class) 
	public void testExportMapThrowsIllegalArgumentIfmapisNull() throws Throwable {
        service.exportMap(null, new File("anything"));
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void testExportMapThrowsIllegalArgumentIffileisNull() throws Throwable {
        service.exportMap(map, null);
	}
	
	@Test(expected=ExportServiceException.class) 
	public void testExportMapThrowsRuntimeExeptionIffileDoesnotExist() throws Throwable {
		mockery.checking(new Expectations () {
			{one(map).getTheSingleRootMapObject();}
			{will(returnValue(rmo));}
		});
        service.exportMap(map, NONEXISTENT);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testExportValidMap() throws Throwable {
//		if(!canRun){
//			fail("LibSBML not loaded");
//		}
		final IContextAdapterValidationService validator= mockery.mock(IContextAdapterValidationService.class);
		final IValidationReport report = mockery.mock(IValidationReport.class);
		adapter = mockery.mock(IExportAdapter.class);
		mockery.checking(new Expectations () {
			{one(validator).setMapToValidate(map);}
			{one(validator).isReadyToValidate();will(returnValue(true));}
			{one(validator).validateMap();}
			{one(validator).getValidationReport();will(returnValue(report));}
			{one(provider).getValidationService();will(returnValue(validator));}
			{one(report).isMapValid();will(returnValue(true));}
			{atLeast(1).of(map).getTheSingleRootMapObject();}
			{will(returnValue(rmo));}
			{ignoring(rmo);}
		    //{ignoring(validator);}
			{ignoring(adapter);}
		});
		EXISTENT.createNewFile();
        service.exportMap(map, EXISTENT);
	}
 
	@SuppressWarnings("unchecked")
	@Test(expected=ExportServiceException.class)
	public void testExportInvalidMap() throws Throwable {
//		if(!canRun){
//			fail("LibSBML not loaded");
//		}
		final IContextAdapterValidationService validator= mockery.mock(IContextAdapterValidationService.class);
		final IValidationReport report = mockery.mock(IValidationReport.class);
		adapter = mockery.mock(MetabolicSBMLExportAdapter.class);
		mockery.checking(new Expectations () {
			{one(provider).getValidationService();will(returnValue(validator));}
			{one(validator).setMapToValidate(map);}
			{one(validator).validateMap();}
			{one(validator).isReadyToValidate();will(returnValue(true));}
			{one(validator).getValidationReport();will(returnValue(report));}
			{one(report).isMapValid();will(returnValue(false));}
			{atLeast(1).of(map).getTheSingleRootMapObject();}
			{will(returnValue(rmo));}
			{ignoring(rmo);}
			{ignoring(validator);}
//			{ignoring(adapter);}
		});
		EXISTENT.createNewFile();
        service.exportMap(map, EXISTENT);
	}
 

	@Test
	public void testGetCode() {
		assertEquals(service.TYPECODE, service.getCode());
	}

	@Test
	public void testGetContext() {
		assertEquals(context, service.getContext());
	}

	@Test
	public void testGetDisplayName() {
		assertEquals(service.DISPLAY_NAME, service.getDisplayName());
	}

	@Test
	public void testGetRecommendedSuffix() {
		assertEquals("sbml", service.getRecommendedSuffix());
	}

	@Test
	public void testToString() {
		assertTrue(service.toString().contains(service.DISPLAY_NAME));
		assertTrue(service.toString().contains(service.TYPECODE));
	}

}
