package uk.ac.ed.inf.Metabolic.excelexport;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ed.inf.Metabolic.ndomAPI.ICompartment;
import uk.ac.ed.inf.Metabolic.ndomAPI.ICompound;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMacromolecule;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;
import uk.ac.ed.inf.Metabolic.ndomAPI.IRelation;

public class ModelProcessor {
	
	private IModel model ; 
	
	
	
	public ModelProcessor(IModel model) throws IllegalArgumentException {
		if ( model == null )
			throw new IllegalArgumentException () ;
		this.model = model;
	}
	
	public IModel getModel ()
	{
		return model ;
	}
	
	public List<ICompartment> giveCompartments ( ) throws NullPointerException 
	{
		if ( model == null )
			throw new NullPointerException () ;
		
		ArrayList<ICompartment> compartments = new ArrayList<ICompartment> () ;
		
		ICompartment RMO =  model.getCompartmentList().get(0) ;
		
		addChildsofCompartments ( RMO , compartments )  ;
		
		return compartments ;
	}
	
	public List<ICompound> giveCompounds ( ) throws NullPointerException
	{
		if ( model == null )
			throw new NullPointerException () ;
		
		ArrayList<ICompound> compounds = new ArrayList<ICompound> () ;
		
		ICompartment RMO = model.getCompartmentList().get(0) ;
		
		addChildCompounds ( RMO , compounds )  ;
		
		return compounds ;
	}
	
	public List<IMacromolecule> giveMacromolecules ( ) throws NullPointerException
	{
		if ( model == null )
			throw new NullPointerException () ;
		
		ArrayList<IMacromolecule> macromolecules = new ArrayList<IMacromolecule> () ;
		
		ICompartment RMO =  model.getCompartmentList().get(0) ;
		
		addChildMacromolecules ( RMO , macromolecules )  ;
		
		return macromolecules ;
	}
	
//	private List<IRelation> giveActivatoryRelations() {
//		if ( model == null )
//			throw new NullPointerException () ;
//		
//		ArrayList<ICompound> compounds = new ArrayList<ICompound> ( giveCompounds() );
//		
//		ArrayList<IRelation> activatoryReactions = new ArrayList<IRelation> () ;
//		
//		for ( int a = 0 ; a < compounds.size() ; a ++)
//		{
//			ICompound tempCompound = compounds.get(a) ;
//			
//			activatoryReactions.addAll(tempCompound.getActivatoryRelationList()) ;
//		}
//		
//		return activatoryReactions ;
//	}
//	
//	private List<IRelation> giveCatalyticRelations() {
//		if ( model == null )
//			throw new NullPointerException () ;
//		
//		ArrayList<ICompound> compounds = new ArrayList<ICompound> ( giveCompounds() );
//		
//		ArrayList<IRelation> catalyticRelation = new ArrayList<IRelation> () ;
//		
//		for ( int a = 0 ; a < compounds.size() ; a ++)
//		{	
//			catalyticRelation.addAll(compounds.get(a).getCatalyticRelationList()) ;
//		}
//		
//		return catalyticRelation ;
//	}
	
//	private List<IRelation> giveInhibitoryRelations() {
//		if ( model == null )
//			throw new NullPointerException () ;
//		
//		ArrayList<ICompound> compounds = new ArrayList<ICompound> ( giveCompounds() );
//		
//		ArrayList<IRelation> inhibitoryRelation = new ArrayList<IRelation> () ;
//		
//		for ( int a = 0 ; a < compounds.size() ; a ++)
//		{			
//			inhibitoryRelation.addAll(compounds.get(a).getInhibitoryRelationList()) ;
//		}
//		
//		return inhibitoryRelation ;
//	}
	
	private List<IRelation> giveSinkList() {
		if ( model == null )
			throw new NullPointerException () ;
		
		ArrayList<ICompound> compounds = new ArrayList<ICompound> ( giveCompounds() );
		
		ArrayList<IRelation> sinkList = new ArrayList<IRelation> () ;
		
		for ( int a = 0 ; a < compounds.size() ; a ++)
		{
			sinkList.addAll(compounds.get(a).getSinkList()) ;
		}
		
		return sinkList ;
	}
	
	private List<IRelation> giveSourceList() {
		if ( model == null )
			throw new NullPointerException () ;
		
		ArrayList<ICompound> compounds = new ArrayList<ICompound> ( giveCompounds() );
		
		ArrayList<IRelation> sourceList = new ArrayList<IRelation> () ;
		
		for ( int a = 0 ; a < compounds.size() ; a ++)
		{
			sourceList.addAll(compounds.get(a).getSourceList()) ;
		}
		
		return sourceList ;
	}
	
	public List<IReaction> giveReactions () {
		
		ArrayList <IReaction> reactions = new ArrayList <IReaction> () ;
		
		ArrayList<IRelation> relations = new ArrayList<IRelation> ( ) ;
		
		relations.addAll(giveSinkList()) ;
		relations.addAll(giveSourceList()) ;
//		relations.addAll(giveActivatoryRelations()) ;
//		relations.addAll(giveCatalyticRelations()) ;
//		relations.addAll(giveInhibitoryRelations()) ;

		
		for ( int a = 0 ; a < relations.size() ; a++ )
		{
			IRelation tempRelation = (IRelation)relations.get(a) ;
			
			if ( !reactions.contains(tempRelation.getReaction()))
			reactions.add( tempRelation.getReaction() ) ;
		}
		
		return reactions ;
	}
	
	private void addChildCompounds ( ICompartment compartment , List<ICompound > toAddIn)  
	{		
			toAddIn.addAll(compartment.getCompoundList()) ;

			ArrayList<ICompartment> childrenCompartments = new ArrayList<ICompartment> () ;
			
			childrenCompartments.addAll(compartment.getChildCompartments()) ;
			
			for ( int a = 0 ; a < childrenCompartments.size() ; a ++)
			{
				addChildCompounds ( childrenCompartments.get(a) , toAddIn ) ;
			}
			
			ArrayList<IMacromolecule> childrenMacromolecules = new ArrayList<IMacromolecule> () ;
			
			childrenMacromolecules.addAll(compartment.getMacromoleculeList()) ;
			
			
			for ( int a = 0 ; a < childrenMacromolecules.size() ; a ++ )
			{
				addChildCompounds ( childrenMacromolecules.get(a) , toAddIn ) ;
			}

	}
	
	private void addChildCompounds(IMacromolecule macromolecule,List<ICompound> toAddIn) {
		
		toAddIn.addAll(macromolecule.getCompoundList()) ;
		
		ArrayList<IMacromolecule> childrenMacromolecules = new ArrayList<IMacromolecule> () ;
		
		childrenMacromolecules.addAll(macromolecule.getSubunitList()) ;
		
		
		for ( int a = 0 ; a < childrenMacromolecules.size() ; a ++ )
		{
			addChildCompounds ( childrenMacromolecules.get(a) , toAddIn ) ;
		}

	}

	private void addChildsofCompartments ( ICompartment compartment , List<ICompartment> toAddIn) 
	{
		toAddIn.addAll(compartment.getChildCompartments()) ;
		
		ArrayList<ICompartment> children = new ArrayList<ICompartment> ( compartment.getChildCompartments() ) ;
		
		for ( int a = 0 ; a < children.size() ; a++  )
		{
			ICompartment temp = children.get(a ) ;
			
			addChildsofCompartments ( temp , toAddIn ) ;
		}
	}
	
	private void addChildMacromolecules ( ICompartment compartment , List<IMacromolecule> toAddIn)  
	{
			
			toAddIn.addAll(compartment.getMacromoleculeList()) ;
			
			ArrayList<ICompartment> childrenCompartments = new ArrayList<ICompartment> () ;
			
			childrenCompartments.addAll(compartment.getChildCompartments()) ;
		
			for ( int a = 0 ; a < childrenCompartments.size() ; a++)
			{
				addChildMacromolecules ( childrenCompartments.get(a) , toAddIn ) ;
			}
	
			ArrayList<IMacromolecule> childrenMacromolecules = new ArrayList<IMacromolecule> () ;
			
			childrenMacromolecules.addAll(compartment.getMacromoleculeList()) ;
			
			
			for ( int a = 0 ; a < childrenMacromolecules.size() ; a ++ )
			{
				addChildMacromolecules ( childrenMacromolecules.get(a) , toAddIn ) ;
			}		
			
	}
	
	private void addChildMacromolecules(IMacromolecule macromolecule, List<IMacromolecule> toAddIn) {
	
		toAddIn.addAll(macromolecule.getSubunitList()) ;
		
		ArrayList<IMacromolecule> childrenMacromolecules = new ArrayList<IMacromolecule> () ;
		
		childrenMacromolecules.addAll(macromolecule.getSubunitList()) ;
		
		
		for ( int a = 0 ; a < childrenMacromolecules.size() ; a ++ )
		{
			addChildMacromolecules ( childrenMacromolecules.get(a) , toAddIn ) ;
		}
	}

}
