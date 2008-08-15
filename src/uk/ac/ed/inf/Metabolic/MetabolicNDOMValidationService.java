package uk.ac.ed.inf.Metabolic;

import org.pathwayeditor.businessobjectsAPI.IMap;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterServiceProvider;
import org.pathwayeditor.contextadapter.publicapi.IValidationReport;
import org.pathwayeditor.contextadapter.toolkit.ndom.INDOMValidationService;
import org.pathwayeditor.contextadapter.toolkit.ndom.INdomModel;
import org.pathwayeditor.contextadapter.toolkit.ndom.NdomException;
import org.pathwayeditor.contextadapter.toolkit.validation.IDefaultValidationRuleConfigLoader;
import org.pathwayeditor.contextadapter.toolkit.validation.IValidationRuleStore;
import org.pathwayeditor.contextadapter.toolkit.validation.RuleStore;
import org.pathwayeditor.contextadapter.toolkit.validation.RuleValidationReportBuilder;

import uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory;
import uk.ac.ed.inf.Metabolic.parser.MetabolicRuleLoader;
import uk.ac.ed.inf.Metabolic.parser.NDOMFactory;

public class MetabolicNDOMValidationService implements INDOMValidationService {

	private static MetabolicNDOMValidationService instance;

	public static MetabolicNDOMValidationService getInstance(
			IContextAdapterServiceProvider provider) {
		if (instance == null) {
			instance = new MetabolicNDOMValidationService(provider);
		}
		return instance;
	}

	private NDOMFactory factory;
	private RuleValidationReportBuilder reportBuilder;
//	private IContextAdapterServiceProvider provider;
//	private IContext context;
	private IMap map;
	private boolean readyToValidate;
	private boolean validated;
	private IValidationReport validationReport;
//	private IValidationRuleStore ruleStore;
	private boolean ndomCreated;
	
	private MetabolicNDOMValidationService(IContextAdapterServiceProvider provider) {
//		this.provider=provider;
//		this.context        = provider.getContext();
	}

	public IMap getMapBeingValidated() {
		return map;
	}

	public INdomModel getNDOM() {
		return factory.getNdom();
	}

	public IValidationRuleStore getRuleStore() {
		return RuleStore.getInstance(getRuleLoader());
	}

	protected IDefaultValidationRuleConfigLoader getRuleLoader() {
		return MetabolicRuleLoader.getInstance();
	}

	public IValidationReport getValidationReport() {
		return validationReport;
	}

	public boolean hasBeenValidated() {
		return validated;
	}

	public boolean isReadyToValidate() {
		
		return readyToValidate;
	}

	public boolean ndomWasCreated() {
		return ndomCreated;
	}

	public void setMapToValidate(IMap map) {
		if(map==null) throw new NullPointerException("Map to validate set to null");
		this.map=map;
		readyToValidate=true;
		validated=false;
		validationReport=null;
		ndomCreated=false;
	}

	public void validateMap() {
		factory = createNdomFactory();
		reportBuilder = new RuleValidationReportBuilder(getRuleStore(), map);
		factory.setReportBuilder(reportBuilder);
		factory.setRmo(map.getTheSingleRootMapObject());
		try {
			generateNdom();
		} catch (NdomException e) {
			handleNdomException(e);
		} finally{
			validated=true;
			reportBuilder.createValidationReport();
		    validationReport = reportBuilder.getValidationReport();
		}
	}

	protected void generateNdom() throws NdomException{
		ndomCreated=false;
		validated=false;
		getFactory().parse();
	}

	protected NDOMFactory createNdomFactory() {
		NDOMFactory ret=new MetabolicNDOMFactory(getMapBeingValidated().getTheSingleRootMapObject());
		return ret;
	}

	protected void handleNdomException(NdomException e) {
		throw new RuntimeException(e);//no ndomExceptions should ever be thrown in this basic context
	}

	protected NDOMFactory getFactory() {
		return factory;
	}

}

/*
 * $Log:$
 */