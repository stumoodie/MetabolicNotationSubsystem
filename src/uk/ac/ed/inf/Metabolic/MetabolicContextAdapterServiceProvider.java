package uk.ac.ed.inf.Metabolic;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pathwayeditor.businessobjectsAPI.IMap;
import org.pathwayeditor.contextadapter.publicapi.IContext;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterAutolayoutService;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterExportService;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterServiceProvider;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterValidationService;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.GeneralContext;

import uk.ac.ed.inf.Metabolic.sbmlexport.SBMLExportService;



public class MetabolicContextAdapterServiceProvider implements IContextAdapterServiceProvider {
    
	private static final String GLOBAL_ID = "uk.ac.ed.inf.Metabolic.Metabolic";
	//private static final String GLOBAL_ID = "12635452516346262546";
	private static final String DISPLAY_NAME = "Basic biochemical context";
	private static final String NAME = "Metabolic context";
	private static final int[] VERS = getVersion("1_0_0");
	
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
	private MetabolicContextValidationService validationService;
	
	public MetabolicContextAdapterServiceProvider() {
	
		this.context = new GeneralContext(GLOBAL_ID, DISPLAY_NAME, NAME,
				VERS[0], VERS[1], VERS[2]);
		this.syntaxService = new MetabolicContextAdapterSyntaxService(this);
		exportServices.add(new SBMLExportService(this));
		this.validationService=new MetabolicContextValidationService(this);
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

	public Set getImportServices() {
		return Collections.emptySet();
	}

	public Set getPluginServices() {
		return Collections.emptySet();
	}

	public MetabolicContextAdapterSyntaxService getSyntaxService() {
		return this.syntaxService;
	}


	public IContextAdapterValidationService getValidationService() {
		return validationService;
	}

	public Set getConversionServices() {
		return Collections.emptySet();
	}

	public IContextAdapterAutolayoutService getAutolayoutService() {
		return new DefaultAutolayoutService();
	}

	private class DefaultValidationService implements IContextAdapterValidationService {

		public IContext getContext() {
			return context;
		}

		public IMap getMapBeingValidated() {
			throw new UnsupportedOperationException("Validation service has not been implemented for this context adapter");
		}

		public List getValidationReport() {
			throw new UnsupportedOperationException("Validation service has not been implemented for this context adapter");
		}

		public boolean hasMapBeenValidated() {
			throw new UnsupportedOperationException("Validation service has not been implemented for this context adapter");
		}

		public boolean hasWarnings() {
			throw new UnsupportedOperationException("Validation service has not been implemented for this context adapter");
		}

		public boolean isImplemented() {
			return false;
		}

		public boolean isMapValid() {
			throw new UnsupportedOperationException("Validation service has not been implemented for this context adapter");
		}

		public boolean isReadyToValidate() {
			throw new UnsupportedOperationException("Validation service has not been implemented for this context adapter");
		}

		public void setMapToValidate(IMap mapToValidate) {
			throw new UnsupportedOperationException("Validation service has not been implemented for this context adapter");
		}

		public void validateMap() {
			throw new UnsupportedOperationException("Validation service has not been implemented for this context adapter");
		}
		public IContextAdapterServiceProvider getServiceProvider() {
			return MetabolicContextAdapterServiceProvider.this;
		}
		
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
