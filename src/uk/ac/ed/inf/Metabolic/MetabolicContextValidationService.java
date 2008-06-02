package uk.ac.ed.inf.Metabolic;

import java.util.ArrayList;
import java.util.List;

import org.pathwayeditor.businessobjectsAPI.IMap;
import org.pathwayeditor.contextadapter.publicapi.IContext;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterServiceProvider;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterValidationService;
import org.pathwayeditor.contextadapter.toolkit.ndom.AbstractNDOMParser.NdomException;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory;

public class MetabolicContextValidationService implements
		IContextAdapterValidationService {
	private IContext context;
	private List<String> validationReport=new ArrayList<String>();
	private boolean beenValidated = false;
	private boolean mapValid = true;
	private boolean hasWarnings = false;
	private boolean readyToValidate = false;
	private IMap mapToValidate;
	private IModel ndom;
	private MetabolicNDOMFactory factory;
	
	public MetabolicContextValidationService(IContextAdapterServiceProvider provider) {
		this.serviceProvider=provider;
		context=provider.getContext();
	}
	private IContextAdapterServiceProvider serviceProvider;

	public IContextAdapterServiceProvider getServiceProvider() {
		return serviceProvider;
	}


	public IMap getMapBeingValidated() {
		return mapToValidate;
	}

	public List<String> getValidationReport() {
		return validationReport;
	}

	public boolean hasMapBeenValidated() {
		return beenValidated;
	}

	public boolean hasWarnings() {
		return hasWarnings;
	}

	public boolean isImplemented() {
		return true;
	}

	public boolean isMapValid() {
		return mapValid;
	}


	public void validateMap() {
		if(!isReadyToValidate()) return;
		mapValid=true;
		factory = new MetabolicNDOMFactory();
		factory.setRmo(mapToValidate.getTheSingleRootMapObject());
		try {
			factory.parse();
			ndom=factory.getNdom();
		} catch (NdomException e) {
			mapValid=false;
			validationReport.add("ERROR\t NdomException\t"+e.getMessage());
		}finally{
			copyReport();
		}
		beenValidated=true;
	}

	void copyReport() {
		mapValid&=factory.isValid();
		validationReport.addAll(factory.getReport());
		hasWarnings|=factory.hasWarnings();
	}

	public IContext getContext() {
		return context;
	}

	public boolean isReadyToValidate() {
		return readyToValidate;
	}

	public void setMapToValidate(IMap mapToValidate) {
		if(mapToValidate==null) throw new IllegalArgumentException("Map to be validated should not be null");
		this.mapToValidate = mapToValidate;
		beenValidated=false;
		validationReport=new ArrayList<String>();
		readyToValidate=(this.mapToValidate!=null); 
	}

	public IModel getModel(){
		return ndom;
	}
}

/*
 * $Log$
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */