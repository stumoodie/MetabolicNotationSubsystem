package uk.ac.ed.inf.Metabolic;

import org.pathwayeditor.contextadapter.publicapi.IContextAdapterServiceProvider;
import org.pathwayeditor.contextadapter.publicapi.IValidationRuleDefinition;
import org.pathwayeditor.contextadapter.toolkit.ndom.AbstractNDOMParser;
import org.pathwayeditor.contextadapter.toolkit.ndom.AbstractNDOMParser.NdomException;
import org.pathwayeditor.contextadapter.toolkit.validation.AbstractContextValidationService;
import org.pathwayeditor.contextadapter.toolkit.validation.IRuleValidationReportBuilder;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory;

public class MetabolicContextValidationService extends AbstractContextValidationService {

	private IModel ndom;
	
	public MetabolicContextValidationService(IContextAdapterServiceProvider provider) {
		super(provider);
	}

	public boolean isImplemented() {
		return true;
	}

	public IModel getModel(){
		return ndom;
	}
	/**
	 * Must be called before validation
	 */
	public void initRuleStore() {
	 getRuleStore().initializeStore(new DefaultRuleLoader());
	}

	@Override
	protected AbstractNDOMParser createNdomFactory(IRuleValidationReportBuilder reportBuilder) {
		return new MetabolicNDOMFactory(reportBuilder);
	}

	@Override
	protected void generateNdom() throws NdomException {
		ndom=((MetabolicNDOMFactory)getFactory()).getNdom();
		for(IValidationRuleDefinition rule: getRuleStore().getAllRuleDefinitions()){
			getReportBuilder().setRulePassed(rule);
		}
		System.out.println("dom generated");
		
	}

	@Override
	protected void handleNdomException() {
		getReportBuilder().setRuleFailed(null, getRuleStore().getRuleById(DefaultRuleLoader.ERROR_ID), "Major validation error");
		
	}

}
	
	

/*
 * $Log$
 * Revision 1.9  2008/06/23 14:22:19  radams
 * abstracted most of validation service to abstract class
 *
 * Revision 1.8  2008/06/23 07:52:04  radams
 * mad echanges to allow configuration
 *
 * Revision 1.7  2008/06/20 22:52:31  radams
 * initialisation removed from constructor
 *
 * Revision 1.6  2008/06/20 13:54:52  radams
 * 1st commit of preference handling mechanism
 *
 * Revision 1.5  2008/06/17 13:24:21  radams
 * added example method for new report generation
 *
 * Revision 1.4  2008/06/16 14:53:16  radams
 * add string message to error reporting
 *
 * Revision 1.3  2008/06/11 11:18:05  radams
 * put in placeholder report generation and altered validation service exception
 *
 * Revision 1.2  2008/06/05 22:15:49  radams
 * added validation method
 *
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */