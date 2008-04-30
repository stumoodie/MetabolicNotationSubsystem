package uk.ac.ed.inf.Metabolic;

import org.pathwayeditor.businessobjectsAPI.BusinessObjectFactory;
import org.pathwayeditor.businessobjectsAPI.ILink;
import org.pathwayeditor.businessobjectsAPI.IRootMapObject;
import org.pathwayeditor.businessobjectsAPI.IShape;
import org.pathwayeditor.contextadapter.publicapi.IShapeObjectType;

public class TestDiagramGenerator {
    MetabolicContextAdapterSyntaxService service;
	
	public TestDiagramGenerator() {
		service = new MetabolicContextAdapterServiceProvider().getSyntaxService();
	}

	public IRootMapObject createRootMapObject () {
		return BusinessObjectFactory.getInstance().createIRootMapObject(service.getRootMapObjectType());
	}
	
	public IShape createShape(MetabolicContextAdapterSyntaxService.ObjectTypes type) {
		
		if(type.equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Compartment))
		  return BusinessObjectFactory.getInstance().createChildShape(service.getCompartment());
		if(type.equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Process))
			  return BusinessObjectFactory.getInstance().createChildShape(service.getProcess());
		if(type.equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Compound))
			  return BusinessObjectFactory.getInstance().createChildShape(service.getCompound());
		if(type.equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Compartment))
			  return BusinessObjectFactory.getInstance().createChildShape(service.getCompartment());
		if(type.equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Macromolecule))
			  return BusinessObjectFactory.getInstance().createChildShape(service.getMacromolecule());
		throw new IllegalArgumentException();
	}
	
	public ILink createLink (MetabolicContextAdapterSyntaxService.ObjectTypes type) {
		if(type.equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Activation))
			  return BusinessObjectFactory.getInstance().createLink(service.getActivation());
			if(type.equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Catalusis))
				  return BusinessObjectFactory.getInstance().createLink(service.getCatalusis());
			if(type.equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Consume))
				  return BusinessObjectFactory.getInstance().createLink(service.getConsume());
			if(type.equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Produce))
				  return BusinessObjectFactory.getInstance().createLink(service.getProduce());
			if(type.equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Inhibition))
				  return BusinessObjectFactory.getInstance().createLink(service.getInhibition());
			throw new IllegalArgumentException();
		
	}
}
