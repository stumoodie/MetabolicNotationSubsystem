package org.pathwayeditor.metabolic.sbmlexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pathwayeditor.businessobjects.drawingprimitives.ICanvas;
import org.pathwayeditor.businessobjects.drawingprimitives.IShapeNodeFactory;
import org.pathwayeditor.businessobjects.management.NonPersistentCanvasFactory;
import org.pathwayeditor.businessobjects.notationsubsystem.ExportServiceException;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.typedefn.IShapeObjectType;
import org.pathwayeditor.metabolic.MetabolicNotationSubsystem;
import org.pathwayeditor.metabolic.sbmlexport.SBMLExportService;


//@RunWith(JMock.class)
public class SBMLExportServiceTest {
//	private Mockery mockery = new JUnit4Mockery();

	private INotation context;
	private ICanvas map; // = mockery.mock(ICanvas.class);
//	private IRootNode rmo = mockery.mock(IRootNode.class);
//	private IShapeNode child = mockery.mock(IShapeNode.class);
	private INotationSubsystem provider;
//	private IModel model = mockery.mock(IModel.class);
//	private INotationSyntaxService mockSyntaxService;
//	private IRootObjectType mockRootObjectType;
//	private IShapeObjectType mockShapeObjectType;
	private SBMLExportService service;

    private static final File NONEXISTENT = new File ("/xyz/fhdg/dfhsgsf.shydfg/ABC/dgdsg");
    private static final File EXISTENT = new File ("SBMLoutput");
	private static final String TEST_NAME = "Test Canvas Name";
//	private static final int ROOT_IDX = 0;
//	private static final int SHAPE_IDX = 1;
	private static final String SHAPE_TYPE_UID = "Compound";

//	private static final Object SBML_EXPORT_CODE = null;

    @Before
	public void setUp() throws Exception {
    	this.provider = MetabolicNotationSubsystem.getInstance();
//    	this.mockSyntaxService = this.mockery.mock(INotationSyntaxService.class);
//    	this.mockShapeObjectType = mockery.mock(IShapeObjectType.class, "mockShapeObjectType");
//    	this.mockRootObjectType = mockery.mock(IRootObjectType.class, "mockRootObjectType");
//    	mockery.checking(new Expectations(){{
//    		allowing(context).getQualifiedName(); will(returnValue(QUALIFIED_NAME));
//    		
//    		allowing(provider).getNotation(); will(returnValue(context));
//    		allowing(provider).isFallback(); will(returnValue(false));
//    		allowing(provider).getExportServices(); will(returnValue(service));
//    		allowing(provider).getSyntaxService(); will(returnValue(mockSyntaxService));
//    		
//    		allowing(mockSyntaxService).getRootObjectType(); will(returnValue(mockRootObjectType));
//    		
//    		allowing(mockSyntaxService).getShapeObjectType(SHAPE_IDX); will(returnValue(mockShapeObjectType));
//    		
//    		allowing(mockRootObjectType).getUniqueId(); will(returnValue(ROOT_IDX));
//    		
//    	}});
    	this.context = this.provider.getNotation();
		NonPersistentCanvasFactory fact = NonPersistentCanvasFactory.getInstance();
		fact.setNotationSubsystem(this.provider);
		fact.setCanvasName(TEST_NAME);
		this.map = fact.createNewCanvas();
		IShapeNodeFactory rootNodeFact = this.map.getModel().getRootNode().getSubModel().shapeNodeFactory();
		IShapeObjectType testType = (IShapeObjectType)this.provider.getSyntaxService().findShapeObjectTypeByName(SHAPE_TYPE_UID); 
		rootNodeFact.setObjectType(testType);
		rootNodeFact.createShapeNode();
    	this.service = new SBMLExportService(this.provider);
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
//		mockery.checking(new Expectations () {
//			{one(map).getModel().getRootNode();}
//			{will(returnValue(rmo));}
//		});
        service.exportMap(map, NONEXISTENT);
	}
	
	@Test
	public void testExportValidMap() throws Throwable {
//		if(!canRun){
//			fail("LibSBML not loaded");
//		}
//		final INotationValidationService validator= mockery.mock(INotationValidationService.class);
//		final IValidationReport report = mockery.mock(IValidationReport.class);
//		adapter = mockery.mock(IExportAdapter.class);
//		mockery.checking(new Expectations () {
//			{one(validator).setCanvasToValidate(map);}
//			{one(validator).isReadyToValidate();will(returnValue(true));}
//			{one(validator).validate();}
//			{one(validator).getValidationReport();will(returnValue(report));}
//			{one(provider).getValidationService();will(returnValue(validator));}
//			{one(report).isValid();will(returnValue(true));}
//			{atLeast(1).of(map).getModel().getRootNode();}
//			{will(returnValue(rmo));}
//			{ignoring(rmo);}
//		    //{ignoring(validator);}
//			{ignoring(adapter);}
//		});
		EXISTENT.createNewFile();
        service.exportMap(map, EXISTENT);
	}
 
	@Test(expected=ExportServiceException.class)
	public void testExportInvalidMap() throws Throwable {
//		if(!canRun){
//			fail("LibSBML not loaded");
//		}
//		final INotationValidationService validator= mockery.mock(INotationValidationService.class);
//		final IValidationReport report = mockery.mock(IValidationReport.class);
//		adapter = mockery.mock(MetabolicSBMLExportAdapter.class);
//		mockery.checking(new Expectations () {
//			{one(provider).getValidationService();will(returnValue(validator));}
//			{one(validator).setCanvasToValidate(map);}
//			{one(validator).validate();}
//			{one(validator).isReadyToValidate();will(returnValue(true));}
//			{one(validator).getValidationReport();will(returnValue(report));}
//			{one(report).isValid();will(returnValue(false));}
//			{atLeast(1).of(map).getModel().getRootNode();}
//			{will(returnValue(rmo));}
//			{ignoring(rmo);}
//			{ignoring(validator);}
////			{ignoring(adapter);}
//		});
		EXISTENT.createNewFile();
        service.exportMap(map, NONEXISTENT);
	}
 

	@Test
	public void testGetCode() {
		assertEquals(service.TYPECODE, service.getCode());
	}

	@Test
	public void testGetContext() {
		assertEquals(context, service.getNotation());
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
