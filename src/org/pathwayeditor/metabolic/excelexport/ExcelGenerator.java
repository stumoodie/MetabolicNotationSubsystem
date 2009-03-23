package org.pathwayeditor.metabolic.excelexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.pathwayeditor.metabolic.ndomAPI.ICompartment;
import org.pathwayeditor.metabolic.ndomAPI.ICompound;
import org.pathwayeditor.metabolic.ndomAPI.IMacromolecule;
import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.pathwayeditor.metabolic.ndomAPI.IReaction;
import org.pathwayeditor.metabolic.ndomAPI.IRelation;


public class ExcelGenerator implements IExcelFileGenerator {
	
	private HSSFWorkbook workbook ;
	private ModelProcessor modelProcessor ;
	protected String templatePath ;
	private CellManipulator cellManipulator ;
	
	public ExcelGenerator(IModel model , String templatePath ) throws IllegalArgumentException  {
		if ( templatePath == null )
			throw new IllegalArgumentException () ;
	
		this.modelProcessor = new ModelProcessor ( model );
		this.templatePath = templatePath ;
		
	}


	public void populateCompoundPage() {
		
		ArrayList<ICompound> compounds = new ArrayList <ICompound> ( modelProcessor.giveCompounds ( ) );
		
		HSSFSheet sheet = workbook.getSheet("Compounds") ;	
		
		for ( int a = 0 ; a < compounds.size() ; a ++)
		{			
			ICompound tempCompound = compounds.get(a) ;
			
			HSSFRow row = sheet.createRow((short) 1+ a ) ;
			
			cellManipulator.putStringInNormalRow ( row , 0 , tempCompound.getId() );
			cellManipulator.putStringInNormalRow ( row , 1 , tempCompound.getName() );
			cellManipulator.putStringInNormalRow ( row , 2 , tempCompound.getDescription() );
			cellManipulator.putStringInNormalRow ( row , 3 , tempCompound.getDetailedDescription() );
			
			if ( tempCompound.getParent().getId().equals("ROOT_MAP_OBJECT1") )
			{
				cellManipulator.putStringInNormalRow ( row , 4 , "");
			}
			else
			{
				cellManipulator.putStringInNormalRow ( row , 4 ,  tempCompound.getParent().getId() );
			}
			
			cellManipulator.putDoubleInRow ( row , 5 , tempCompound.getIC() );
			cellManipulator.putStringInNormalRow ( row , 6 , tempCompound.getCID() );
			cellManipulator.putStringInNormalRow ( row , 7 , tempCompound.getChEBIId() );
			cellManipulator.putStringInNormalRow ( row , 8 , tempCompound.getPubChemId() );
			cellManipulator.putStringInNormalRow ( row , 9 , tempCompound.getInChI() );
			cellManipulator.putStringInNormalRow ( row , 10 , tempCompound.getSmiles() );
			cellManipulator.putIntegerInRow ( row , 11 , 100 );
			
		}
	}


	public void populateMacromoleculePage(){

		
		ArrayList<IMacromolecule> macromolecules = new ArrayList<IMacromolecule> ( modelProcessor.giveMacromolecules ( ) );
		
		HSSFSheet sheet = workbook.getSheet("Macromolecules") ;	
		
		for ( int a = 0 ; a < macromolecules.size() ; a ++)
		{			
			IMacromolecule tempMacromolecule = macromolecules.get(a) ;
			
			HSSFRow row = sheet.createRow((short) 1+ a ) ;
			
			
			
			if ( tempMacromolecule.getParent().getId().equals("ROOT_MAP_OBJECT1") )
			{
				cellManipulator.putStringInNormalRow ( row , 0 , tempMacromolecule.getId() );
				cellManipulator.putStringInNormalRow ( row , 1 , "" );
				cellManipulator.putStringInNormalRow ( row , 5 , tempMacromolecule.getParent().getId() );
			}
			else
			{
				cellManipulator.putStringInNormalRow ( row , 1 , tempMacromolecule.getId() );
				cellManipulator.putStringInNormalRow ( row , 0 , tempMacromolecule.getParent().getId() );
				cellManipulator.putStringInNormalRow ( row , 5 , "" );
			}			
			
			cellManipulator.putStringInNormalRow ( row , 2 , tempMacromolecule.getName() );
			cellManipulator.putStringInNormalRow ( row , 3 , tempMacromolecule.getDescription() );
			cellManipulator.putStringInNormalRow ( row , 4 , tempMacromolecule.getDetailedDescription() );
			
			cellManipulator.putStringInNormalRow ( row , 6 , tempMacromolecule.getGOTerm() );
			cellManipulator.putStringInNormalRow ( row , 7 , tempMacromolecule.getUniProt() );
			
			StringBuffer heterogroups = new StringBuffer () ;
			
			for ( int b = 0 ; b < tempMacromolecule.getCompoundList().size() ; b++ )
			{
				heterogroups.append( ", " +  tempMacromolecule.getCompoundList().get(b).getId() );
			}
			
			if ( heterogroups.length() > 0 )
			{
				cellManipulator.putStringInNormalRow ( row , 8 , heterogroups.substring(2) ) ;
			}
			else
				cellManipulator.putStringInNormalRow ( row , 8 , "") ;
				
		}

	}

	public void populateModelPage() {

		
		ArrayList<ICompartment> compartments = new ArrayList<ICompartment> ( modelProcessor.giveCompartments() );
		
		HSSFSheet sheet = workbook.getSheet("Model") ;
		
		HSSFRow titleRow = sheet.createRow((short)1);
		
		cellManipulator.putStringInHeaderRow ( titleRow , 1 , modelProcessor.getModel().getName() );
		cellManipulator.putStringInHeaderRow ( titleRow , 2 , modelProcessor.getModel().getDescription() );
		cellManipulator.putStringInHeaderRow ( titleRow , 3 , modelProcessor.getModel().getDetailedDescription() );
		
		
		for ( int a = 0 ; a < compartments.size() ; a ++)
		{
			ICompartment temp = compartments.get(a) ;
			
			HSSFRow row = sheet.createRow((short) 5 + a);
			
			cellManipulator.putStringInNormalRow ( row , 0 , temp.getId() );
			cellManipulator.putStringInNormalRow ( row , 1 , temp.getName() );
			cellManipulator.putStringInNormalRow ( row , 2 , temp.getDescription() );
			cellManipulator.putStringInNormalRow ( row , 3 , temp.getDetailedDescription() );
			cellManipulator.putStringInNormalRow ( row , 4 , temp.getGOTerm() );
			
		}

	}
	
	public void populateReactionsPage() {

		
		ArrayList<IReaction> reactions = new ArrayList<IReaction> ( modelProcessor.giveReactions() );
		
		HSSFSheet sheet = workbook.getSheet("Reactions") ;
		
		for ( int a = 0 ; a < reactions.size() ; a++ )
		{
			IReaction tempReaction = reactions.get(a) ;
			
			HSSFRow row = sheet.createRow((short) 1 + a);
			
			cellManipulator.putStringInNormalRow ( row , 0 , tempReaction.getId() );
			cellManipulator.putStringInNormalRow ( row , 1 , tempReaction.getDescription() );
			cellManipulator.putStringInNormalRow ( row , 2 , tempReaction.getDetailedDescription() );
			
			
			StringBuffer subratePartOfReactionIds = new StringBuffer () ;
			StringBuffer subratePartOfReaction = new StringBuffer () ;
			
			ArrayList<IRelation> subrates = new ArrayList<IRelation> ( tempReaction.getSubstrateList() ) ;
			
			for ( int b = 0 ; b < subrates.size() ; b++ )
			{
				subratePartOfReactionIds.append( " + " +  subrates.get(b).getMolecule().getId());
				subratePartOfReaction.append( " + " +  cellManipulator.convertToRTF(subrates.get(b).getMolecule().getDescription()));
			}
			
			ArrayList<IRelation> activators = new ArrayList<IRelation> ( tempReaction.getActivatorList()) ;
			
			for ( int b = 0 ; b < activators.size() ; b++ )
			{
				subratePartOfReactionIds.append( " + " +  activators.get(b).getMolecule().getId() );
				subratePartOfReaction.append( " + " +  cellManipulator.convertToRTF(activators.get(b).getMolecule().getDescription() ));
			}
			
			ArrayList<IRelation> catalysts = new ArrayList<IRelation> ( tempReaction.getCatalystList() ) ;
			
			for ( int b = 0 ; b < catalysts.size() ; b++ )
			{
				subratePartOfReactionIds.append( " + " +  catalysts.get(b).getMolecule().getId() );
				subratePartOfReaction.append( " + " +  cellManipulator.convertToRTF(catalysts.get(b).getMolecule().getDescription()) );
			}
			
			ArrayList<IRelation> inhibitors = new ArrayList<IRelation> ( tempReaction.getInhibitorList() ) ;
			
			for ( int b = 0 ; b < inhibitors.size() ; b++ )
			{
				subratePartOfReactionIds.append( " + " +  inhibitors.get(b).getMolecule().getId() );
				subratePartOfReaction.append( " + " +  cellManipulator.convertToRTF(inhibitors.get(b).getMolecule().getDescription()) );
			}
			
			StringBuffer productsPartOfReactionIds = new StringBuffer () ;
			StringBuffer productsPartOfReaction = new StringBuffer () ;
			
			ArrayList<IRelation> products = new ArrayList<IRelation> ( tempReaction.getProductList() ) ;
			
			for ( int b = 0 ; b < products.size() ; b++ )
			{
				productsPartOfReactionIds.append( " + " +  products.get(b).getMolecule().getId() );
				productsPartOfReaction.append( " + " +  cellManipulator.convertToRTF(products.get(b).getMolecule().getDescription() ));
			}			
			
			String subratePartOfReactionStringIds ="" , productsPartOfReactionStringIds = "" ,
				   subratePartOfReactionString ="" , productsPartOfReactionString = "" 		;
			
			if ( subratePartOfReactionIds.length() > 0 )
				subratePartOfReactionStringIds = subratePartOfReactionIds.substring(3) ;
			if ( subratePartOfReaction.length() > 0 )
				subratePartOfReactionString = subratePartOfReaction.substring(3) ;
			
			if ( productsPartOfReactionIds.length() > 0 )
				productsPartOfReactionStringIds = productsPartOfReactionIds.substring(3) ;
			if ( productsPartOfReaction.length() > 0 )
				productsPartOfReactionString = productsPartOfReaction.substring(3) ;

			
			String connector = " => " ;
			if ( tempReaction.isReversible()  )
				connector = " <=> " ;

			cellManipulator.putStringInNormalRow ( row , 3 , subratePartOfReactionString + connector +  productsPartOfReactionString );
			cellManipulator.putStringInNormalRow ( row , 4 , subratePartOfReactionStringIds + connector +  productsPartOfReactionStringIds );
			
			cellManipulator.putStringInNormalRow ( row , 5 , tempReaction.getECNumber() );
			cellManipulator.putStringInNormalRow ( row , 6 , tempReaction.getKineticLaw() );
			cellManipulator.putStringInNormalRow ( row , 7 , tempReaction.getParameters() );
			
			StringBuffer activatorString = new StringBuffer () ;
			
			if (tempReaction.getActivatorList().size() > 0 )
			{
				for ( int b = 0 ; b < tempReaction.getActivatorList().size() ; b ++ )
				{
					activatorString.append( ", " + tempReaction.getActivatorList().get(b).getMolecule().getId() );
				}
				
				activatorString = activatorString.delete(0, 2) ;
			}
			
			cellManipulator.putStringInNormalRow ( row , 8 ,activatorString.toString() );

			StringBuffer inhibitorString = new StringBuffer () ;
			
			if (tempReaction.getInhibitorList().size() > 0 )
			{
				for ( int b = 0 ; b < tempReaction.getInhibitorList().size() ; b ++ )
				{
					inhibitorString.append(", " + tempReaction.getInhibitorList().get(b).getMolecule().getId() );
				}
				
				inhibitorString = inhibitorString.delete(0 , 2) ;
			}
			
			cellManipulator.putStringInNormalRow ( row , 9 , inhibitorString.toString() );

			StringBuffer catalystString = new StringBuffer () ;
			
			if (tempReaction.getCatalystList().size() > 0 )
			{
				for ( int b = 0 ; b < tempReaction.getCatalystList().size() ; b ++ )
				{
					catalystString.append (", " + tempReaction.getCatalystList().get(b).getMolecule().getId() ) ;
				}
				
				catalystString = catalystString.delete(0 , 2) ;
			}			
			
			cellManipulator.putStringInNormalRow ( row , 10 , catalystString.toString() );
			
			
		}
		
	}


	public void createNewWorkbook() throws IllegalStateException ,IOException {
		if ( !isTemplateValid () )
			throw new IllegalStateException () ;
		
		workbook = loadTemplateFromPath() ;
		
		this.cellManipulator = new CellManipulator ( workbook) ;
		
	}


	public IModel getMetabolicNDOM() {
		return modelProcessor.getModel();
	}


	public String getTemplateLocation() {
		return templatePath;
	}


	public boolean isTemplateValid() {
		
		HSSFWorkbook loadedTemplate ;
		
		try {
		 loadedTemplate = loadTemplateFromPath () ;
		} catch (IOException e) {
			return false ;
		}
		
		
		if ( loadedTemplate == null )
			return false;
		
		if ( loadedTemplate.getSheet("Model") == null )
			return false;

		if ( loadedTemplate.getSheet("Compounds") == null )
			return false;

		if ( loadedTemplate.getSheet("Reactions") == null )
			return false;
		
		if ( loadedTemplate.getSheet("Macromolecules") == null )
			return false;
		
		return true ;

	}





	public void saveWorkBook(File target) throws IllegalArgumentException,
			IOException {
		if ( target == null )
			throw new IllegalArgumentException () ;
		
		FileOutputStream fos = new FileOutputStream ( target ) ;
		
		try{
		workbook.write(fos) ;
		}
		finally 
		{
			fos.close() ;
		}
	}


	public boolean wasWorkBookCreated() {
		if ( workbook == null )
		return false;
		
		return true ;
	}

	protected HSSFWorkbook loadTemplateFromPath () throws IOException
	{
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream( templatePath ) ;
		
		try {
        POIFSFileSystem fs;
		fs = new POIFSFileSystem( inputStream );
		return new HSSFWorkbook(fs);
		}
		finally {
			inputStream.close() ;
		}

	}

}
