package uk.ac.ed.inf.Metabolic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.pathwayeditor.contextadapter.publicapi.IContext;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterAutolayoutService;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterExportService;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterImportService;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterPluginService;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterConversionService;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterServiceProvider;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterValidationService;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.GeneralContext;
import org.pathwayeditor.contextadapter.toolkit.validation.ContextValidationService;

import uk.ac.ed.inf.Metabolic.excelexport.ExcelExportService;
import uk.ac.ed.inf.Metabolic.sbmlexport.SBMLExportService;



public class MetabolicContextAdapterServiceProvider implements IContextAdapterServiceProvider {
    
	// made public for access within plugin
	public static final String GLOBAL_ID = "uk.ac.ed.inf.Metabolic.Metabolic";
	//private static final String GLOBAL_ID = "12635452516346262546";
	public static final String DISPLAY_NAME = "Metabolic Context";
	private static final String NAME = "Metabolic context";
	public static final int[] VERS = getVersion("1_0_0");
	
	private static int[] getVersion(String ver) {
		String[] l = ver.split("_");
		int majorVersion = Integer.parseInt(l[0]);
		int minorVersion = Integer.parseInt(l[1]);
		int patchVersion = Integer.parseInt(l[2]);

		return new int[] { majorVersion, minorVersion, patchVersion };
	}
	private MetabolicContextAdapterSyntaxService syntaxService;
	private IContext context;
	private Set<IContextAdapterExportService> exportServices = new HashSet<IContextAdapterExportService>();
	private IContextAdapterValidationService validationService;
	private static IContextAdapterServiceProvider instance;
	
	/**
	 * Service providers should be instantiated but accessed through getInstance
	 */
	private MetabolicContextAdapterServiceProvider() {
	   
		this.context = new GeneralContext(GLOBAL_ID, DISPLAY_NAME, NAME,
				VERS[0], VERS[1], VERS[2]);
		this.syntaxService = new MetabolicContextAdapterSyntaxService(this);
		exportServices.add(new SBMLExportService(this));
		exportServices.add(new ExcelExportService(this));
		MetabolicNDOMValidationService ndomVal = MetabolicNDOMValidationService.getInstance(this);
		this.validationService=new ContextValidationService(this,ndomVal);;
	}
	
	public static IContextAdapterServiceProvider getInstance() {
		if(instance == null){
			instance= new MetabolicContextAdapterServiceProvider();
		}
		return instance;
	}
	

	 public IContext getContext() {
		return this.context;
	}
	
	
    /**
     * Returns an unmodifiable collection of export services
     */
	public Set <IContextAdapterExportService>getExportServices() {
		return Collections.unmodifiableSet (exportServices);
	}

	public Set<IContextAdapterImportService> getImportServices() {
		return Collections.emptySet();
	}

	public Set<IContextAdapterPluginService> getPluginServices() {
		return Collections.emptySet();
	}

	public MetabolicContextAdapterSyntaxService getSyntaxService() {
		return this.syntaxService;
	}


	public IContextAdapterValidationService getValidationService() {
		return validationService;
	}

	public Set<IContextAdapterConversionService> getConversionServices() {
		return Collections.emptySet();
	}

	public IContextAdapterAutolayoutService getAutolayoutService() {
		return new DefaultAutolayoutService();
	}

	private class DefaultAutolayoutService implements IContextAdapterAutolayoutService {

		public IContext getContext() {
			return context;
		}

		public boolean isImplemented() {
			return false;
		}
		public IContextAdapterServiceProvider getServiceProvider() {
			return MetabolicContextAdapterServiceProvider.this;
		}
		
	}
}
