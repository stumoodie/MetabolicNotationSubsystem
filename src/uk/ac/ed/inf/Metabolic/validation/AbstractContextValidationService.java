package uk.ac.ed.inf.Metabolic.validation;

import java.util.List;
import java.util.Set;

import org.eclipse.ui.contexts.IContext;
import org.pathwayeditor.businessobjects.drawingprimitives.ICanvas;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationValidationService;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationReport;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationReportItem;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleConfig;
import org.pathwayeditor.businessobjects.repository.IMap;
import org.pathwayeditor.contextadapter.toolkit.ndom.AbstractNDOMParser;
import org.pathwayeditor.contextadapter.toolkit.ndom.NdomException;
import org.pathwayeditor.contextadapter.toolkit.validation.IDefaultValidationRuleConfigLoader;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;
import org.pathwayeditor.contextadapter.toolkit.validation.IValidationRuleStore;
import org.pathwayeditor.contextadapter.toolkit.validation.RuleValidationReportBuilder;


/**
 * @author asorokin
 *
 */
public abstract class AbstractContextValidationService implements INotationValidationService {
//public  class AbstractContextValidationService implements INotationValidationService {

	private INotation notation;
	private INotationSubsystem serviceProvider;
	private IValidationReport validationReport;
	List<IValidationReportItem> reportItems;
	private boolean beenValidated = false;
	private boolean readyToValidate = false;
	private ICanvas mapToValidate;
//	private IValidationRuleConfigurer configurer;
	
	private AbstractNDOMParser factory;
	private IRuleValidationReportBuilder reportBuilder;

	
	public AbstractContextValidationService(INotationSubsystem provider) {
		this.serviceProvider= provider;
		this.notation        = provider.getNotation();
	}

	public INotationSubsystem getNotationSubsystem() {
		return serviceProvider;
	}

    /**
     * Subclasses should not override this method
     */
	public ICanvas getMapBeingValidated() {
		return mapToValidate;
	}
    
	 /**
     * Subclasses should not override this method
     */
	public IValidationReport getValidationReport() {
		if(!(hasMapBeenValidated())){
			throw new IllegalStateException("Map has not been validated - no report available");
		}
		return validationReport;
	}
    
	 /**
     * Subclasses should not override this method
     */
	public boolean hasMapBeenValidated() {
		return beenValidated;
	}

	
    /**
     * Subclasses should override by returning <code>true</code>
     */
	public boolean isImplemented() {
		return false;
	}

	
    /**
     * Template pattern. Subclasses add specialized handling of:
     * <ul>
     * <li> creating an Ndom factory
     * <li> Generating an NDom
     * <li> Handling exceptions thrown by the parser
     * @throw {@link IllegalStateException} if service isReadyToValidate == false
     */
	public final void validateMap() {
		if(!isReadyToValidate())
			throw new IllegalStateException("Service not ready to validate");
//		 configureRulesFromUserPreferences();
		factory = createNdomFactory();
		reportBuilder = new RuleValidationReportBuilder(getRuleStore(), mapToValidate);
		factory.setRmo(mapToValidate.getModel().getRootNode());
		try {
			factory.parse();
			generateNdom();
		} catch (NdomException e) {
			handleNdomException(e);
		} finally{
			beenValidated=true;
			reportBuilder.createValidationReport();
		    validationReport = reportBuilder.getValidationReport();
		}
		
		
	}
	/**
	 * Should add specialised rule as failed.
	 * @param reportBuilder Provide access
	 */
	protected abstract void handleNdomException(NdomException e);

	 /**
     * Subclasses should not override this method
     */
/*	public void setRuleConfigurer(IValidationRuleConfigurer configurer) {
		if(configurer == null){
			throw new IllegalArgumentException("Rule configurer cannot be null!");
		}
		this.configurer = configurer;
	}
	
*/
	/**
	 * Subclasses should implement and provide an {@link AbstractNDOMParser} which performs
	 * validation. 
	 * @param reportBuilder. An {@link IRuleValidationReportBuilder} for use by the created parser.
	 * @return A non-null {@link AbstractNDOMParser}.
	 */
	protected abstract AbstractNDOMParser createNdomFactory();
	
	/**
	 * Subclasses should implement the generattion of a validationService domain object model.
	 * @throws NdomException If Ndom generation cannot proceed.
	 */
	protected abstract void generateNdom() throws NdomException;
	
/*	private void configureRulesFromUserPreferences() {
		// configurer is optional - no config = default settings
		if(configurer != null){
			configurer.configureRules(getRuleStore().getConfigurableRules());
		}
		
	}
    
*/	
	/**
     * Subclasses should not override this method
     */
	public INotation getNotation() {
		return notation;
	}
    
	/**
     * Subclasses should not override this method
     */
	public boolean isReadyToValidate() {
		return readyToValidate && getRuleStore().isInitialized();
	}

	public void setMapToValidate(ICanvas mapToValidate) {
		if(mapToValidate==null) throw new IllegalArgumentException("Map to be validated should not be null");
		this.mapToValidate = mapToValidate;
		beenValidated=false;
		readyToValidate=(this.mapToValidate!=null); 
	}


	/**
	 * Must be called before validation to load up the rule store.
	 * Typically implementations will implement an {@link IDefaultValidationRuleConfigLoader}
	 * and call it as follows:<br>
	 * <pre>
	 *    getRuleStore().initializeStore(new MyDefaultRuleLoader());
	 * </pre>
	 */
	public abstract void initRuleStore(); 

	public Set<IValidationRuleConfig> getDefaultRuleConfigurations() {
		return getRuleStore().getDefaultRuleConfigurations();
	}

	public Set<IValidationRuleConfig> getRuleConfigurations() {
		return getRuleStore().getAllRuleConfigurations();
	}

	/**
     * Subclasses should not override this method.
     * @return The {@link IValidationRuleStore} for this export service.
     */
	public abstract IValidationRuleStore getRuleStore();

	/**
     * Provides access for subclasses to the parser factory. Guranteed to be the same parser as the 
     * last invocation of createNdomFactory()
     * @return An {@link AbstractNDOMParser}
     */
	protected AbstractNDOMParser getFactory() {
		return factory;
	}

	/**
     * Provides access for subclasses to the report builder.
     * @return The {@link IRuleValidationReportBuilder} used to build the validation report.
     */
	protected IRuleValidationReportBuilder getReportBuilder() {
		return reportBuilder;
	}

}
