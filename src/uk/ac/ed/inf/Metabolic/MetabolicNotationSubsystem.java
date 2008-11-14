package uk.ac.ed.inf.Metabolic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.pathwayeditor.businessobjects.drawingprimitives.attributes.Version;
import org.pathwayeditor.businessobjects.hibernate.pojos.HibNotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationAutolayoutService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationConversionService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationExportService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationImportService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationPluginService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationValidationService;
import org.pathwayeditor.contextadapter.toolkit.validation.NotationValidationService;

import uk.ac.ed.inf.Metabolic.excelexport.ExcelExportService;
import uk.ac.ed.inf.Metabolic.paxexport.BioPAXExportService;
import uk.ac.ed.inf.Metabolic.sbmlexport.SBMLExportService;

public class MetabolicNotationSubsystem implements INotationSubsystem {
    
	// made public for access within plugin
	public static final String GLOBAL_ID = "uk.ac.ed.inf.Metabolic.Metabolic";
	//private static final String GLOBAL_ID = "12635452516346262546";
	public static final String DISPLAY_NAME = "Metabolic Notation";
	private static final String NAME = "Metabolic validationService";
	public static final Version version= new Version(1,0,0);
	
	
	private MetabolicNotationSyntaxService syntaxService;
	private INotation notation;
	private Set<INotationExportService> exportServices = new HashSet<INotationExportService>();
	private INotationValidationService validationService;
	private static INotationSubsystem instance;
	
	/**
	 * Service providers should be instantiated but accessed through getInstance
	 */
	private MetabolicNotationSubsystem() {
	   
		this.notation = new HibNotation(GLOBAL_ID,DISPLAY_NAME,NAME,version);
		this.syntaxService = new MetabolicNotationSyntaxService(this);
		exportServices.add(new SBMLExportService(this));
		exportServices.add(new ExcelExportService(this));
		exportServices.add(new BioPAXExportService(this));
		MetabolicNDOMValidationService ndomVal = MetabolicNDOMValidationService.getInstance(this);
		this.validationService=new NotationValidationService(this,ndomVal);
	}
	
	public static INotationSubsystem getInstance() {
		if(instance == null){
			instance= new MetabolicNotationSubsystem();
		}
		return instance;
	}

	 public INotation getNotation() {
		return this.notation;
	}
	
    /**
     * Returns an unmodifiable collection of export services
     */
	public Set <INotationExportService>getExportServices() {
		return Collections.unmodifiableSet (exportServices);
	}

	public Set<INotationImportService> getImportServices() {
		return Collections.emptySet();
	}

	public Set<INotationPluginService> getPluginServices() {
		return Collections.emptySet();
	}

	public MetabolicNotationSyntaxService getSyntaxService() {
		return this.syntaxService;
	}


	public INotationValidationService getValidationService() {
		return validationService;
	}

	public Set<INotationConversionService> getConversionServices() {
		return Collections.emptySet();
	}

	public INotationAutolayoutService getAutolayoutService() {
		return new DefaultAutolayoutService();
	}

	private class DefaultAutolayoutService implements INotationAutolayoutService {

		public INotation getNotation() {
			return notation;
		}

		public boolean isImplemented() {
			return false;
		}
		public INotationSubsystem getNotationSubsystem() {
			return MetabolicNotationSubsystem.this;
		}

	}

	public boolean isFallback() {
		return false;
	}
}
