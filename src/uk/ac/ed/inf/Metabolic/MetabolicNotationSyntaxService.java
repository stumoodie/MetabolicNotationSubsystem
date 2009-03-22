package uk.ac.ed.inf.Metabolic;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

import org.pathwayeditor.businessobjects.drawingprimitives.attributes.LineStyle;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.LinkEndDecoratorShape;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.PrimitiveShapeType;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.RGB;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.Size;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IPropertyDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSyntaxService;
import org.pathwayeditor.businessobjects.typedefn.ILinkObjectType;
import org.pathwayeditor.businessobjects.typedefn.IObjectType;
import org.pathwayeditor.businessobjects.typedefn.IRootObjectType;
import org.pathwayeditor.businessobjects.typedefn.IShapeObjectType;
import org.pathwayeditor.businessobjects.typedefn.ILinkObjectType.LinkEditableAttributes;
import org.pathwayeditor.businessobjects.typedefn.ILinkTerminusDefinition.LinkTermEditableAttributes;
import org.pathwayeditor.businessobjects.typedefn.IShapeObjectType.EditableShapeAttributes;
import org.pathwayeditor.notationsubsystem.toolkit.definition.FormattedTextPropertyDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.definition.LinkObjectType;
import org.pathwayeditor.notationsubsystem.toolkit.definition.LinkTerminusDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.definition.NumberPropertyDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.definition.PlainTextPropertyDefinition;
import org.pathwayeditor.notationsubsystem.toolkit.definition.RootObjectType;
import org.pathwayeditor.notationsubsystem.toolkit.definition.ShapeObjectType;
import org.pathwayeditor.notationsubsystem.toolkit.definition.TextPropertyDefinition;

public class MetabolicNotationSyntaxService implements INotationSyntaxService {
  private static final int NUM_ROOT_OTS = 1;
  private final INotation context;
  private final Map <Integer, IShapeObjectType> shapes = new HashMap<Integer, IShapeObjectType>(); 
  private final Map <Integer, ILinkObjectType> links = new HashMap<Integer, ILinkObjectType>();
//  private final Set <IPropertyDefinition> propSet=new HashSet<IPropertyDefinition>();
  
  private RootObjectType rmo;
  //shapes
  private ShapeObjectType Compartment;
  private ShapeObjectType Process;
  private ShapeObjectType Compound;
  private ShapeObjectType Macromolecule;

  //links
  private LinkObjectType Consume;
  private LinkObjectType Produce;
  private LinkObjectType Activation;
  private LinkObjectType Catalysis;
  private LinkObjectType Inhibition;

  
  
  private INotationSubsystem serviceProvider;

	private static int[] getRGB(String hex) {
		hex = hex.replace("#", "");
		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4), 16);
		return new int[] { r, g, b };
	}

	private static IPropertyDefinition reassignVal(IPropertyDefinition prop,String val,boolean isEdit,boolean isVis){
		if( prop instanceof TextPropertyDefinition) return reassignVal((TextPropertyDefinition) prop,val,isEdit,isVis);
		if( prop instanceof FormattedTextPropertyDefinition) return reassignVal((FormattedTextPropertyDefinition) prop,val,isEdit,isVis);
		if( prop instanceof NumberPropertyDefinition) return reassignVal((NumberPropertyDefinition) prop,val,isEdit,isVis);
		return prop;
	}
	
	private static PlainTextPropertyDefinition reassignVal(TextPropertyDefinition prop,String val,boolean isEdit,boolean isVis){
		PlainTextPropertyDefinition newP=new PlainTextPropertyDefinition(prop.getName(),val,(prop.isVisualisable() | isVis),(prop.isEditable()&isEdit));
  //  if(newP.isVisualisable())newP.setAppearance(prop.getAppearance());
		return newP;
	}
	
	private static FormattedTextPropertyDefinition reassignVal(FormattedTextPropertyDefinition prop,String val,boolean isEdit,boolean isVis){
		FormattedTextPropertyDefinition newP=new FormattedTextPropertyDefinition(prop.getName(),val,(prop.isVisualisable() | isVis),(prop.isEditable()&isEdit));
//		if(newP.isVisualisable())newP.setAppearance(prop.getAppearance());
		return newP;
	}
	
	private static NumberPropertyDefinition reassignVal(NumberPropertyDefinition prop,String val,boolean isEdit,boolean isVis){
		NumberPropertyDefinition newP=new NumberPropertyDefinition(prop.getName(), new BigDecimal(val),(prop.isVisualisable() | isVis),(prop.isEditable()&isEdit));
 //   if(newP.isVisualisable())newP.setAppearance(prop.getAppearance());
		return newP;
	}
	

	public MetabolicNotationSyntaxService(INotationSubsystem serviceProvider) {
		this.serviceProvider=serviceProvider;
		this.context = serviceProvider.getNotation();
		//"Metabolic context"
		//"Basic biochemical context"
		//1_1_0
		createRMO();
	//shapes
	this.Compartment= new ShapeObjectType(this,10, "Compartment");
	createCompartment();
	this.Process= new ShapeObjectType(this,11, "Process");
	createProcess();
	this.Compound= new ShapeObjectType(this,12, "Compound");
	createCompound();
	this.Macromolecule= new ShapeObjectType(this,13, "Macromolecule");
	createMacromolecule();

		defineParentingRMO();
	//shapes parenting
		defineParentingCompartment();
		defineParentingProcess();
		defineParentingCompound();
		defineParentingMacromolecule();

	//links
	this.Consume = new LinkObjectType(this, 20, "Consume");
	createConsume();
	this.Produce = new LinkObjectType(this, 21, "Produce");
	createProduce();
	this.Activation = new LinkObjectType(this, 22, "Activation");
	createActivation();
	this.Catalysis = new LinkObjectType(this, 23, "Catalysis");
	createCatalysis();
	this.Inhibition = new LinkObjectType(this, 24, "Inhibition");
	createInhibition();

	//shape set
		this.shapes.put(this.Compartment.getUniqueId(), this.Compartment);
		this.shapes.put(this.Process.getUniqueId(), this.Process);
		this.shapes.put(this.Compound.getUniqueId(), this.Compound);
		this.shapes.put(this.Macromolecule.getUniqueId(), this.Macromolecule);

	//link set
		this.links.put(this.Consume.getUniqueId(), this.Consume);
		this.links.put(this.Produce.getUniqueId(), this.Produce);
		this.links.put(this.Activation.getUniqueId(), this.Activation);
		this.links.put(this.Catalysis.getUniqueId(), this.Catalysis);
		this.links.put(this.Inhibition.getUniqueId(), this.Inhibition);
		
	}

  public INotationSubsystem getNotationSubsystem() {
    return serviceProvider;
  }
  
  public INotation getNotation() {
    return this.context;
  }

  public Iterator<ILinkObjectType> linkTypeIterator() {
    return this.links.values().iterator();
  }

  public IRootObjectType getRootObjectType() {
    return this.rmo;
  }

  public Iterator<IShapeObjectType> shapeTypeIterator() {
    return this.shapes.values().iterator();
  }
  
  
  public Iterator<IObjectType> objectTypeIterator(){
    Set<IObjectType> retVal = new HashSet<IObjectType>(this.shapes.values());
    retVal.addAll(this.links.values());
    retVal.add(this.rmo);
    return retVal.iterator();
  }
  
  public boolean containsLinkObjectType(int uniqueId) {
    return this.links.containsKey(uniqueId);
  }

  public boolean containsObjectType(int uniqueId) {
    boolean retVal = this.links.containsKey(uniqueId);
    if(!retVal){
      retVal = this.shapes.containsKey(uniqueId);
    }
    if(!retVal){
      retVal = this.rmo.getUniqueId() == uniqueId;
    }
    return retVal;
  }

  public boolean containsShapeObjectType(int uniqueId) {
    return this.shapes.containsKey(uniqueId);
  }

  public ILinkObjectType getLinkObjectType(int uniqueId) {
    return this.links.get(uniqueId);
  }

  public IObjectType getObjectType(int uniqueId) {
    IObjectType retVal = this.links.get(uniqueId);
    if(retVal == null){
      retVal = this.shapes.get(uniqueId);
    }
    if(retVal == null){
      retVal = (this.rmo.getUniqueId() == uniqueId) ? this.rmo : null;
    }
    if(retVal == null){
      throw new IllegalArgumentException("The unique Id was not present in this notation subsystem");
    }
    return retVal;
  }

  public IShapeObjectType getShapeObjectType(int uniqueId) {
    return this.shapes.get(uniqueId);
  }

  private <T extends IObjectType> T findObjectTypeByName(Collection<? extends T> otSet, String name){
    T retVal = null;
    for(T val : otSet){
      if(val.getName().equals(name)){
        retVal = val;
        break;
      }
    }
    return retVal;
  }
  
  public ILinkObjectType findLinkObjectTypeByName(String name) {
    return findObjectTypeByName(this.links.values(), name);
  }

  public IShapeObjectType findShapeObjectTypeByName(String name) {
    return findObjectTypeByName(this.shapes.values(), name);
  }

  public int numLinkObjectTypes() {
    return this.links.size();
  }

  public int numShapeObjectTypes() {
    return this.shapes.size();
  }

  public int numObjectTypes(){
    return this.numLinkObjectTypes() + this.numShapeObjectTypes() + NUM_ROOT_OTS;
  }

		private void createRMO(){
			this.rmo= new RootObjectType(0, "Root Object", "ROOT_OBJECT", this);
		}
		private void defineParentingRMO(){
			HashSet<IShapeObjectType> set=new HashSet<IShapeObjectType>();
			set.addAll(Arrays.asList(new IShapeObjectType[]{this.Process, this.Compound, this.Macromolecule, this.Compartment}));
			for (IShapeObjectType child : set) {
			  this.rmo.getParentingRules().addChild(child);
			}

		}

	private void createCompartment(){
	this.Compartment.setDescription("Functional compartment");//ment to be TypeDescription rather
	this.Compartment.getDefaultAttributes().setName("Compartment");
	this.Compartment.getDefaultAttributes().setShapeType(PrimitiveShapeType.RECTANGLE);
	this.Compartment.getDefaultAttributes().setFillColour(new RGB(255,255,255));
	this.Compartment.getDefaultAttributes().setSize(new Size(20,20));
	int[] lc=new int[]{0,0,0};
	this.Compartment.getDefaultAttributes().setLineWidth(1);
	this.Compartment.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
	this.Compartment.getDefaultAttributes().setLineColour(new RGB(lc[0],lc[1],lc[2]));
	this.Compartment.getDefaultAttributes().setShapeType(PrimitiveShapeType.ROUNDED_RECTANGLE);		int[] s=new int[]{200,200};
			this.Compartment.getDefaultAttributes().setSize(new Size(s[0],s[1]));

	EnumSet<EditableShapeAttributes> editableAttributes = EnumSet.noneOf(EditableShapeAttributes.class);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
	}
	//this.Compartment.getDefaultAttributes().setFillEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
	}
	//this.Compartment.setPrimitiveShapeTypeEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
	}
	//this.Compartment.setSizeEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_STYLE);
	}
	//this.Compartment.getDefaultAttributes().setLineStyleEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
	}
	//this.Compartment.getDefaultAttributes().setLineWidthEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
	}
	//this.Compartment.getDefaultAttributes().setLineColourEditable(true);
	this.Compartment.setEditableAttributes(editableAttributes);
	this.Compartment.getDefaultAttributes().setUrl("");
	IPropertyDefinition GOTerm=reassignVal(getPropGOTerm()," ",true,false);
	Compartment.getDefaultAttributes().addPropertyDefinition(GOTerm);
	 	IPropertyDefinition Volume= new NumberPropertyDefinition(
	 "volume","1.0",false,true);
		this.Compartment.getDefaultAttributes().addPropertyDefinition(Volume); 	
	 	this.Compartment.getDefaultAttributes().setUrl("http://www.proteinatlas.org");
	 
	}

		private void defineParentingCompartment(){
			HashSet<IShapeObjectType> set=new HashSet<IShapeObjectType>();
			set.addAll(Arrays.asList(new IShapeObjectType[]{this.Compartment, this.Process, this.Compound, this.Macromolecule}));
			for (IShapeObjectType child : set) {
			  this.Compartment.getParentingRules().addChild(child);
			}

		}

		public ShapeObjectType getCompartment(){
			return this.Compartment;
		}
	private void createProcess(){
	this.Process.setDescription("chemical conversion of compounds");//ment to be TypeDescription rather
	this.Process.getDefaultAttributes().setName("Reaction");
	this.Process.getDefaultAttributes().setShapeType(PrimitiveShapeType.RECTANGLE);
	this.Process.getDefaultAttributes().setFillColour(new RGB(255,255,255));
	this.Process.getDefaultAttributes().setSize(new Size(20,20));
	int[] lc=new int[]{0,0,0};
	this.Process.getDefaultAttributes().setLineWidth(1);
	this.Process.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
	this.Process.getDefaultAttributes().setLineColour(new RGB(lc[0],lc[1],lc[2]));
	this.Process.getDefaultAttributes().setShapeType(PrimitiveShapeType.RECTANGLE);		int[] s=new int[]{10,10};
			this.Process.getDefaultAttributes().setSize(new Size(s[0],s[1]));int[] c=new int[]{255,255,255};
	this.Process.getDefaultAttributes().setFillColour(new RGB(c[0],c[1],c[2]));

	EnumSet<EditableShapeAttributes> editableAttributes = EnumSet.noneOf(EditableShapeAttributes.class);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
	}
	//this.Process.getDefaultAttributes().setFillEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
	}
	//this.Process.setPrimitiveShapeTypeEditable(true);
	if(false){
	    editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
	}
	//this.Process.setSizeEditable(false);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_STYLE);
	}
	//this.Process.getDefaultAttributes().setLineStyleEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
	}
	//this.Process.getDefaultAttributes().setLineWidthEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
	}
	//this.Process.getDefaultAttributes().setLineColourEditable(true);
	this.Process.setEditableAttributes(editableAttributes);
	this.Process.getDefaultAttributes().setUrl("");
	IPropertyDefinition ECnum=reassignVal(getPropECnum(),"-.-.-.-",true,false);
	Process.getDefaultAttributes().addPropertyDefinition(ECnum);
	 IPropertyDefinition KineticLaw=reassignVal(getPropKineticLaw()," ",true,false);
	Process.getDefaultAttributes().addPropertyDefinition(KineticLaw);
	 	IPropertyDefinition Reversibility=new PlainTextPropertyDefinition("Reversibility","irreversible",false,true);
		this.Process.getDefaultAttributes().addPropertyDefinition(Reversibility);	IPropertyDefinition Parameters=new PlainTextPropertyDefinition("Parameters"," ",false,true);
		this.Process.getDefaultAttributes().addPropertyDefinition(Parameters);
	}

		private void defineParentingProcess(){
			this.Process.getParentingRules().clear();
		}

		public ShapeObjectType getProcess(){
			return this.Process;
		}
	private void createCompound(){
	this.Compound.setDescription("chemical entity");//ment to be TypeDescription rather
	this.Compound.getDefaultAttributes().setName("Compound");
	this.Compound.getDefaultAttributes().setShapeType(PrimitiveShapeType.RECTANGLE);
	this.Compound.getDefaultAttributes().setFillColour(new RGB(255,255,255));
	this.Compound.getDefaultAttributes().setSize(new Size(20,20));
	int[] lc=new int[]{255,0,0};
	this.Compound.getDefaultAttributes().setLineWidth(1);
	this.Compound.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
	this.Compound.getDefaultAttributes().setLineColour(new RGB(lc[0],lc[1],lc[2]));
	this.Compound.getDefaultAttributes().setShapeType(PrimitiveShapeType.ELLIPSE);		int[] s=new int[]{20,20};
			this.Compound.getDefaultAttributes().setSize(new Size(s[0],s[1]));int[] c=new int[]{255,255,255};
	this.Compound.getDefaultAttributes().setFillColour(new RGB(c[0],c[1],c[2]));

	EnumSet<EditableShapeAttributes> editableAttributes = EnumSet.noneOf(EditableShapeAttributes.class);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
	}
	//this.Compound.getDefaultAttributes().setFillEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
	}
	//this.Compound.setPrimitiveShapeTypeEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
	}
	//this.Compound.setSizeEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_STYLE);
	}
	//this.Compound.getDefaultAttributes().setLineStyleEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
	}
	//this.Compound.getDefaultAttributes().setLineWidthEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
	}
	//this.Compound.getDefaultAttributes().setLineColourEditable(true);
	this.Compound.setEditableAttributes(editableAttributes);
	this.Compound.getDefaultAttributes().setUrl("");
	IPropertyDefinition CID=reassignVal(getPropCID()," ",true,false);
	Compound.getDefaultAttributes().addPropertyDefinition(CID);
	 IPropertyDefinition ChEBI=reassignVal(getPropChEBI()," ",true,false);
	Compound.getDefaultAttributes().addPropertyDefinition(ChEBI);
	 IPropertyDefinition PubChem=reassignVal(getPropPubChem()," ",true,false);
	Compound.getDefaultAttributes().addPropertyDefinition(PubChem);
	 IPropertyDefinition InChI=reassignVal(getPropInChI()," ",true,false);
	Compound.getDefaultAttributes().addPropertyDefinition(InChI);
	 IPropertyDefinition Smiles=reassignVal(getPropSmiles()," ",true,false);
	Compound.getDefaultAttributes().addPropertyDefinition(Smiles);
	 IPropertyDefinition IC=reassignVal(getPropIC(),"1.0",true,false);
	Compound.getDefaultAttributes().addPropertyDefinition(IC);
	 
	}

		private void defineParentingCompound(){
			this.Compound.getParentingRules().clear();
		}

		public ShapeObjectType getCompound(){
			return this.Compound;
		}
	private void createMacromolecule(){
	this.Macromolecule.setDescription("polymer");//ment to be TypeDescription rather
	this.Macromolecule.getDefaultAttributes().setName("Macromolecule");
	this.Macromolecule.getDefaultAttributes().setShapeType(PrimitiveShapeType.RECTANGLE);
	this.Macromolecule.getDefaultAttributes().setFillColour(new RGB(255,255,255));
	this.Macromolecule.getDefaultAttributes().setSize(new Size(20,20));
	int[] lc=new int[]{0,0,0};
	this.Macromolecule.getDefaultAttributes().setLineWidth(1);
	this.Macromolecule.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
	this.Macromolecule.getDefaultAttributes().setLineColour(new RGB(lc[0],lc[1],lc[2]));
	this.Macromolecule.getDefaultAttributes().setShapeType(PrimitiveShapeType.ROUNDED_RECTANGLE);		int[] s=new int[]{20,20};
			this.Macromolecule.getDefaultAttributes().setSize(new Size(s[0],s[1]));int[] c=new int[]{255,255,255};
	this.Macromolecule.getDefaultAttributes().setFillColour(new RGB(c[0],c[1],c[2]));

	EnumSet<EditableShapeAttributes> editableAttributes = EnumSet.noneOf(EditableShapeAttributes.class);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.FILL_COLOUR);
	}
	//this.Macromolecule.getDefaultAttributes().setFillEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.SHAPE_TYPE);
	}
	//this.Macromolecule.setPrimitiveShapeTypeEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.SHAPE_SIZE);
	}
	//this.Macromolecule.setSizeEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_STYLE);
	}
	//this.Macromolecule.getDefaultAttributes().setLineStyleEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_WIDTH);
	}
	//this.Macromolecule.getDefaultAttributes().setLineWidthEditable(true);
	if(true){
	    editableAttributes.add(EditableShapeAttributes.LINE_COLOUR);
	}
	//this.Macromolecule.getDefaultAttributes().setLineColourEditable(true);
	this.Macromolecule.setEditableAttributes(editableAttributes);
	this.Macromolecule.getDefaultAttributes().setUrl("");
	IPropertyDefinition GOTerm=reassignVal(getPropGOTerm()," ",true,false);
	Macromolecule.getDefaultAttributes().addPropertyDefinition(GOTerm);
	 	IPropertyDefinition UniProt=new PlainTextPropertyDefinition("UniProt"," ",false,true);
		this.Macromolecule.getDefaultAttributes().addPropertyDefinition(UniProt);
	}

		private void defineParentingMacromolecule(){
			HashSet<IShapeObjectType> set=new HashSet<IShapeObjectType>();
			set.addAll(Arrays.asList(new IShapeObjectType[]{this.Macromolecule, this.Compound}));
			for (IShapeObjectType child : set) {
			  this.Macromolecule.getParentingRules().addChild(child);
			}

		}

		public ShapeObjectType getMacromolecule(){
			return this.Macromolecule;
		}

	
	private void createConsume(){
	Set<IShapeObjectType> set=null;
	int[] lc=new int[]{0,0,0};
	this.Consume.getDefaultAttributes().setLineWidth(1);
	this.Consume.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
	this.Consume.getDefaultAttributes().setLineColour(new RGB(lc[0],lc[1],lc[2]));
	this.Consume.getDefaultAttributes().setName("Consumption Link");
	this.Consume.getDefaultAttributes().setDescription("");
	this.Consume.getDefaultAttributes().setDetailedDescription("");
	EnumSet<LinkEditableAttributes> editableAttributes = EnumSet.noneOf(LinkEditableAttributes.class);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.COLOUR);
	}
	//this.Consume.getDefaultAttributes().setLineColourEditable(true);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
	}
	//this.Consume.getDefaultAttributes().setLineStyleEditable(true);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
	}
	//this.Consume.getDefaultAttributes().setLineWidthEditable(true);
	this.Consume.setEditableAttributes(editableAttributes);

	this.Consume.getDefaultAttributes().setUrl("");
	IPropertyDefinition VarName=reassignVal(getPropVarName(),"v1",true,false);
	Consume.getDefaultAttributes().addPropertyDefinition(VarName);
	 
	//LinkEndDefinition sport=this.Consume.getLinkSource();
	//LinkEndDefinition tport=this.Consume.getLinkTarget();
	LinkTerminusDefinition sport=this.Consume.getSourceTerminusDefinition();
	LinkTerminusDefinition tport=this.Consume.getTargetTerminusDefinition();
	sport.getDefaultAttributes().setGap((short)0);//to set default offset value
	sport.getDefaultAttributes().setEndDecoratorType(LinkEndDecoratorShape.NONE);//, 8,8);
	sport.getDefaultAttributes().setEndSize(new Size(8,8));
	sport.getDefaultAttributes().setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
	sport.getDefaultAttributes().setTermSize(new Size(0,0));
	int[] csport=new int[]{255,255,255};
	sport.getDefaultAttributes().setTermColour(new RGB(csport[0],csport[1],csport[2]));
	//sport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
	EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet.of(LinkTermEditableAttributes.END_SIZE, LinkTermEditableAttributes.OFFSET,
	                  LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_SIZE);
	if(true){
	  editablesportAttributes.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
	}
	//sport.getDefaultAttributes().setShapeTypeEditable(true);
	if(true){
	  editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
	}
	//sport.getDefaultAttributes().setColourEditable(true);
	sport.setEditableAttributes(editablesportAttributes);
	IPropertyDefinition Role=reassignVal(getPropRole(),"substrate",true,true);
	sport.getDefaultAttributes().addPropertyDefinition(Role);
	 tport.getDefaultAttributes().setGap((short)0);
	tport.getDefaultAttributes().setEndDecoratorType(LinkEndDecoratorShape.NONE);//, 8,8);
	tport.getDefaultAttributes().setEndSize(new Size(8,8));
	tport.getDefaultAttributes().setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
	tport.getDefaultAttributes().setTermSize(new Size(0,0));
	int[] ctport=new int[]{255,255,255};
	tport.getDefaultAttributes().setTermColour(new RGB(ctport[0],ctport[1],ctport[2]));
	//tport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
	EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet.of(LinkTermEditableAttributes.END_SIZE, LinkTermEditableAttributes.OFFSET,
	                  LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_SIZE);
	if(true){
	  editabletportAttributes.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
	}
	//tport.getDefaultAttributes().setShapeTypeEditable(true);
	if(true){
	  editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
	}
	//tport.getDefaultAttributes().setColourEditable(true);
	tport.setEditableAttributes(editabletportAttributes);
	IPropertyDefinition Stoich=reassignVal(getPropStoich(),"1",true,false);
	tport.getDefaultAttributes().addPropertyDefinition(Stoich);
	 
	set=new HashSet<IShapeObjectType>();
	set.addAll(Arrays.asList(new IShapeObjectType[]{this.Process}));
	for (IShapeObjectType tgt : set) {
	  this.Consume.getLinkConnectionRules().addConnection(this.Compound, tgt);
	}

	}

	public LinkObjectType getConsume(){
		return this.Consume;
	}
	private void createProduce(){
	Set<IShapeObjectType> set=null;
	int[] lc=new int[]{0,0,0};
	this.Produce.getDefaultAttributes().setLineWidth(1);
	this.Produce.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
	this.Produce.getDefaultAttributes().setLineColour(new RGB(lc[0],lc[1],lc[2]));
	this.Produce.getDefaultAttributes().setName("Production Link");
	this.Produce.getDefaultAttributes().setDescription("");
	this.Produce.getDefaultAttributes().setDetailedDescription("");
	EnumSet<LinkEditableAttributes> editableAttributes = EnumSet.noneOf(LinkEditableAttributes.class);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.COLOUR);
	}
	//this.Produce.getDefaultAttributes().setLineColourEditable(true);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
	}
	//this.Produce.getDefaultAttributes().setLineStyleEditable(true);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
	}
	//this.Produce.getDefaultAttributes().setLineWidthEditable(true);
	this.Produce.setEditableAttributes(editableAttributes);

	this.Produce.getDefaultAttributes().setUrl("");
	IPropertyDefinition VarName=reassignVal(getPropVarName(),"v1",true,false);
	Produce.getDefaultAttributes().addPropertyDefinition(VarName);
	 
	//LinkEndDefinition sport=this.Produce.getLinkSource();
	//LinkEndDefinition tport=this.Produce.getLinkTarget();
	LinkTerminusDefinition sport=this.Produce.getSourceTerminusDefinition();
	LinkTerminusDefinition tport=this.Produce.getTargetTerminusDefinition();
	sport.getDefaultAttributes().setGap((short)2);
	sport.getDefaultAttributes().setEndDecoratorType(LinkEndDecoratorShape.NONE);//, 8,8);
	sport.getDefaultAttributes().setEndSize(new Size(8,8));
	sport.getDefaultAttributes().setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
	sport.getDefaultAttributes().setTermSize(new Size(0,0));
	int[] csport=new int[]{255,255,255};
	sport.getDefaultAttributes().setTermColour(new RGB(csport[0],csport[1],csport[2]));
	//sport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
	EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet.of(LinkTermEditableAttributes.END_SIZE, LinkTermEditableAttributes.OFFSET,
	                  LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_SIZE);
	if(true){
	  editablesportAttributes.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
	}
	//sport.getDefaultAttributes().setShapeTypeEditable(true);
	if(true){
	  editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
	}
	//sport.getDefaultAttributes().setColourEditable(true);
	sport.setEditableAttributes(editablesportAttributes);
	IPropertyDefinition Stoich=reassignVal(getPropStoich(),"1",true,false);
	sport.getDefaultAttributes().addPropertyDefinition(Stoich);
	 tport.getDefaultAttributes().setGap((short)0);//to set default offset value
	tport.getDefaultAttributes().setEndDecoratorType(LinkEndDecoratorShape.TRIANGLE);//, 5,5);
	tport.getDefaultAttributes().setEndSize(new Size(5,5));
	tport.getDefaultAttributes().setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
	tport.getDefaultAttributes().setTermSize(new Size(0,0));
	int[] ctport=new int[]{255,255,255};
	tport.getDefaultAttributes().setTermColour(new RGB(ctport[0],ctport[1],ctport[2]));
	//tport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
	EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet.of(LinkTermEditableAttributes.END_SIZE, LinkTermEditableAttributes.OFFSET,
	                  LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_SIZE);
	if(true){
	  editabletportAttributes.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
	}
	//tport.getDefaultAttributes().setShapeTypeEditable(true);
	if(true){
	  editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
	}
	//tport.getDefaultAttributes().setColourEditable(true);
	tport.setEditableAttributes(editabletportAttributes);
	IPropertyDefinition Role=reassignVal(getPropRole(),"product",true,false);
	tport.getDefaultAttributes().addPropertyDefinition(Role);
	 
	set=new HashSet<IShapeObjectType>();
	set.addAll(Arrays.asList(new IShapeObjectType[]{this.Compound}));
	for (IShapeObjectType tgt : set) {
	  this.Produce.getLinkConnectionRules().addConnection(this.Process, tgt);
	}

	}

	public LinkObjectType getProduce(){
		return this.Produce;
	}
	private void createActivation(){
	Set<IShapeObjectType> set=null;
	int[] lc=new int[]{0,0,0};
	this.Activation.getDefaultAttributes().setLineWidth(1);
	this.Activation.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
	this.Activation.getDefaultAttributes().setLineColour(new RGB(lc[0],lc[1],lc[2]));
	this.Activation.getDefaultAttributes().setName("Activation Link");
	this.Activation.getDefaultAttributes().setDescription("");
	this.Activation.getDefaultAttributes().setDetailedDescription("");
	EnumSet<LinkEditableAttributes> editableAttributes = EnumSet.noneOf(LinkEditableAttributes.class);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.COLOUR);
	}
	//this.Activation.getDefaultAttributes().setLineColourEditable(true);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
	}
	//this.Activation.getDefaultAttributes().setLineStyleEditable(true);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
	}
	//this.Activation.getDefaultAttributes().setLineWidthEditable(true);
	this.Activation.setEditableAttributes(editableAttributes);

	this.Activation.getDefaultAttributes().setUrl("");
	IPropertyDefinition VarName=reassignVal(getPropVarName(),"v1",true,false);
	Activation.getDefaultAttributes().addPropertyDefinition(VarName);
	 
	//LinkEndDefinition sport=this.Activation.getLinkSource();
	//LinkEndDefinition tport=this.Activation.getLinkTarget();
	LinkTerminusDefinition sport=this.Activation.getSourceTerminusDefinition();
	LinkTerminusDefinition tport=this.Activation.getTargetTerminusDefinition();
	sport.getDefaultAttributes().setGap((short)2);
	sport.getDefaultAttributes().setEndDecoratorType(LinkEndDecoratorShape.NONE);//, 8,8);
	sport.getDefaultAttributes().setEndSize(new Size(8,8));
	sport.getDefaultAttributes().setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
	sport.getDefaultAttributes().setTermSize(new Size(0,0));
	int[] csport=new int[]{255,255,255};
	sport.getDefaultAttributes().setTermColour(new RGB(csport[0],csport[1],csport[2]));
	//sport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
	EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet.of(LinkTermEditableAttributes.END_SIZE, LinkTermEditableAttributes.OFFSET,
	                  LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_SIZE);
	if(true){
	  editablesportAttributes.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
	}
	//sport.getDefaultAttributes().setShapeTypeEditable(true);
	if(true){
	  editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
	}
	//sport.getDefaultAttributes().setColourEditable(true);
	sport.setEditableAttributes(editablesportAttributes);
	IPropertyDefinition Stoich=reassignVal(getPropStoich(),"1",true,false);
	sport.getDefaultAttributes().addPropertyDefinition(Stoich);
	 tport.getDefaultAttributes().setGap((short)0);//to set default offset value
	tport.getDefaultAttributes().setEndDecoratorType(LinkEndDecoratorShape.ARROW);//, 8,8);
	tport.getDefaultAttributes().setEndSize(new Size(8,8));
	tport.getDefaultAttributes().setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
	tport.getDefaultAttributes().setTermSize(new Size(0,0));
	int[] ctport=new int[]{255,255,255};
	tport.getDefaultAttributes().setTermColour(new RGB(ctport[0],ctport[1],ctport[2]));
	//tport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
	EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet.of(LinkTermEditableAttributes.END_SIZE, LinkTermEditableAttributes.OFFSET,
	                  LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_SIZE);
	if(true){
	  editabletportAttributes.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
	}
	//tport.getDefaultAttributes().setShapeTypeEditable(true);
	if(true){
	  editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
	}
	//tport.getDefaultAttributes().setColourEditable(true);
	tport.setEditableAttributes(editabletportAttributes);
	IPropertyDefinition Role=reassignVal(getPropRole(),"activator",true,false);
	tport.getDefaultAttributes().addPropertyDefinition(Role);
	 
	set=new HashSet<IShapeObjectType>();
	set.addAll(Arrays.asList(new IShapeObjectType[]{this.Process}));
	for (IShapeObjectType tgt : set) {
	  this.Activation.getLinkConnectionRules().addConnection(this.Compound, tgt);
	}
	set=new HashSet<IShapeObjectType>();
	set.addAll(Arrays.asList(new IShapeObjectType[]{this.Process}));
	for (IShapeObjectType tgt : set) {
	  this.Activation.getLinkConnectionRules().addConnection(this.Macromolecule, tgt);
	}

	}

	public LinkObjectType getActivation(){
		return this.Activation;
	}
	private void createCatalysis(){
	Set<IShapeObjectType> set=null;
	int[] lc=new int[]{0,0,0};
	this.Catalysis.getDefaultAttributes().setLineWidth(1);
	this.Catalysis.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
	this.Catalysis.getDefaultAttributes().setLineColour(new RGB(lc[0],lc[1],lc[2]));
	this.Catalysis.getDefaultAttributes().setName("Catalysis Link");
	this.Catalysis.getDefaultAttributes().setDescription("");
	this.Catalysis.getDefaultAttributes().setDetailedDescription("");
	EnumSet<LinkEditableAttributes> editableAttributes = EnumSet.noneOf(LinkEditableAttributes.class);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.COLOUR);
	}
	//this.Catalysis.getDefaultAttributes().setLineColourEditable(true);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
	}
	//this.Catalysis.getDefaultAttributes().setLineStyleEditable(true);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
	}
	//this.Catalysis.getDefaultAttributes().setLineWidthEditable(true);
	this.Catalysis.setEditableAttributes(editableAttributes);

	this.Catalysis.getDefaultAttributes().setUrl("");
	//LinkEndDefinition sport=this.Catalysis.getLinkSource();
	//LinkEndDefinition tport=this.Catalysis.getLinkTarget();
	LinkTerminusDefinition sport=this.Catalysis.getSourceTerminusDefinition();
	LinkTerminusDefinition tport=this.Catalysis.getTargetTerminusDefinition();
	sport.getDefaultAttributes().setGap((short)2);
	sport.getDefaultAttributes().setEndDecoratorType(LinkEndDecoratorShape.NONE);//, 8,8);
	sport.getDefaultAttributes().setEndSize(new Size(8,8));
	sport.getDefaultAttributes().setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
	sport.getDefaultAttributes().setTermSize(new Size(0,0));
	int[] csport=new int[]{255,255,255};
	sport.getDefaultAttributes().setTermColour(new RGB(csport[0],csport[1],csport[2]));
	//sport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
	EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet.of(LinkTermEditableAttributes.END_SIZE, LinkTermEditableAttributes.OFFSET,
	                  LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_SIZE);
	if(true){
	  editablesportAttributes.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
	}
	//sport.getDefaultAttributes().setShapeTypeEditable(true);
	if(true){
	  editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
	}
	//sport.getDefaultAttributes().setColourEditable(true);
	sport.setEditableAttributes(editablesportAttributes);
	IPropertyDefinition Stoich=reassignVal(getPropStoich(),"1",true,false);
	sport.getDefaultAttributes().addPropertyDefinition(Stoich);
	 tport.getDefaultAttributes().setGap((short)0);//to set default offset value
	tport.getDefaultAttributes().setEndDecoratorType(LinkEndDecoratorShape.DIAMOND);//, 8,8);
	tport.getDefaultAttributes().setEndSize(new Size(8,8));
	tport.getDefaultAttributes().setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
	tport.getDefaultAttributes().setTermSize(new Size(0,0));
	int[] ctport=new int[]{255,255,255};
	tport.getDefaultAttributes().setTermColour(new RGB(ctport[0],ctport[1],ctport[2]));
	//tport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
	EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet.of(LinkTermEditableAttributes.END_SIZE, LinkTermEditableAttributes.OFFSET,
	                  LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_SIZE);
	if(true){
	  editabletportAttributes.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
	}
	//tport.getDefaultAttributes().setShapeTypeEditable(true);
	if(true){
	  editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
	}
	//tport.getDefaultAttributes().setColourEditable(true);
	tport.setEditableAttributes(editabletportAttributes);
	IPropertyDefinition Role=reassignVal(getPropRole(),"activator",true,false);
	tport.getDefaultAttributes().addPropertyDefinition(Role);
	 
	set=new HashSet<IShapeObjectType>();
	set.addAll(Arrays.asList(new IShapeObjectType[]{this.Process}));
	for (IShapeObjectType tgt : set) {
	  this.Catalysis.getLinkConnectionRules().addConnection(this.Compound, tgt);
	}
	set=new HashSet<IShapeObjectType>();
	set.addAll(Arrays.asList(new IShapeObjectType[]{this.Process}));
	for (IShapeObjectType tgt : set) {
	  this.Catalysis.getLinkConnectionRules().addConnection(this.Macromolecule, tgt);
	}

	}

	public LinkObjectType getCatalysis(){
		return this.Catalysis;
	}
	private void createInhibition(){
	Set<IShapeObjectType> set=null;
	int[] lc=new int[]{0,0,0};
	this.Inhibition.getDefaultAttributes().setLineWidth(1);
	this.Inhibition.getDefaultAttributes().setLineStyle(LineStyle.SOLID);
	this.Inhibition.getDefaultAttributes().setLineColour(new RGB(lc[0],lc[1],lc[2]));
	this.Inhibition.getDefaultAttributes().setName("Inhibition Link");
	this.Inhibition.getDefaultAttributes().setDescription("");
	this.Inhibition.getDefaultAttributes().setDetailedDescription("");
	EnumSet<LinkEditableAttributes> editableAttributes = EnumSet.noneOf(LinkEditableAttributes.class);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.COLOUR);
	}
	//this.Inhibition.getDefaultAttributes().setLineColourEditable(true);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.LINE_STYLE);
	}
	//this.Inhibition.getDefaultAttributes().setLineStyleEditable(true);
	if(true){
	  editableAttributes.add(LinkEditableAttributes.LINE_WIDTH);
	}
	//this.Inhibition.getDefaultAttributes().setLineWidthEditable(true);
	this.Inhibition.setEditableAttributes(editableAttributes);

	this.Inhibition.getDefaultAttributes().setUrl("");
	IPropertyDefinition VarName=reassignVal(getPropVarName(),"v1",true,false);
	Inhibition.getDefaultAttributes().addPropertyDefinition(VarName);
	 
	//LinkEndDefinition sport=this.Inhibition.getLinkSource();
	//LinkEndDefinition tport=this.Inhibition.getLinkTarget();
	LinkTerminusDefinition sport=this.Inhibition.getSourceTerminusDefinition();
	LinkTerminusDefinition tport=this.Inhibition.getTargetTerminusDefinition();
	sport.getDefaultAttributes().setGap((short)2);
	sport.getDefaultAttributes().setEndDecoratorType(LinkEndDecoratorShape.NONE);//, 8,8);
	sport.getDefaultAttributes().setEndSize(new Size(8,8));
	sport.getDefaultAttributes().setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
	sport.getDefaultAttributes().setTermSize(new Size(0,0));
	int[] csport=new int[]{255,255,255};
	sport.getDefaultAttributes().setTermColour(new RGB(csport[0],csport[1],csport[2]));
	//sport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
	EnumSet<LinkTermEditableAttributes> editablesportAttributes = EnumSet.of(LinkTermEditableAttributes.END_SIZE, LinkTermEditableAttributes.OFFSET,
	                  LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_SIZE);
	if(true){
	  editablesportAttributes.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
	}
	//sport.getDefaultAttributes().setShapeTypeEditable(true);
	if(true){
	  editablesportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
	}
	//sport.getDefaultAttributes().setColourEditable(true);
	sport.setEditableAttributes(editablesportAttributes);
	IPropertyDefinition Stoich=reassignVal(getPropStoich(),"1",true,false);
	sport.getDefaultAttributes().addPropertyDefinition(Stoich);
	 tport.getDefaultAttributes().setGap((short)0);//to set default offset value
	tport.getDefaultAttributes().setEndDecoratorType(LinkEndDecoratorShape.BAR);//, 8,8);
	tport.getDefaultAttributes().setEndSize(new Size(8,8));
	tport.getDefaultAttributes().setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
	tport.getDefaultAttributes().setTermSize(new Size(0,0));
	int[] ctport=new int[]{255,255,255};
	tport.getDefaultAttributes().setTermColour(new RGB(ctport[0],ctport[1],ctport[2]));
	//tport.getDefaultAttributes().setLineProperties(0, LineStyle.SOLID);
	EnumSet<LinkTermEditableAttributes> editabletportAttributes = EnumSet.of(LinkTermEditableAttributes.END_SIZE, LinkTermEditableAttributes.OFFSET,
	                  LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_SIZE);
	if(true){
	  editabletportAttributes.add(LinkTermEditableAttributes.END_DECORATOR_TYPE);
	}
	//tport.getDefaultAttributes().setShapeTypeEditable(true);
	if(true){
	  editabletportAttributes.add(LinkTermEditableAttributes.TERM_COLOUR);
	}
	//tport.getDefaultAttributes().setColourEditable(true);
	tport.setEditableAttributes(editabletportAttributes);
	IPropertyDefinition Role=reassignVal(getPropRole(),"inhibitor",true,false);
	tport.getDefaultAttributes().addPropertyDefinition(Role);
	 
	set=new HashSet<IShapeObjectType>();
	set.addAll(Arrays.asList(new IShapeObjectType[]{this.Process}));
	for (IShapeObjectType tgt : set) {
	  this.Inhibition.getLinkConnectionRules().addConnection(this.Compound, tgt);
	}
	set=new HashSet<IShapeObjectType>();
	set.addAll(Arrays.asList(new IShapeObjectType[]{this.Process}));
	for (IShapeObjectType tgt : set) {
	  this.Inhibition.getLinkConnectionRules().addConnection(this.Macromolecule, tgt);
	}

	}

	public LinkObjectType getInhibition(){
		return this.Inhibition;
	}
	

	private IPropertyDefinition getPropECnum(){
		IPropertyDefinition ECnum=new PlainTextPropertyDefinition("EC","-",true,true);
		return ECnum;
	}
	private IPropertyDefinition getPropSmiles(){
		IPropertyDefinition Smiles=new PlainTextPropertyDefinition("SMILES"," ",false,true);
		return Smiles;
	}
	private IPropertyDefinition getPropStoich(){
		IPropertyDefinition Stoich=new PlainTextPropertyDefinition("STOICH"," ",true,true);
		return Stoich;
	}
	private IPropertyDefinition getPropRole(){
		IPropertyDefinition Role=new PlainTextPropertyDefinition("ROLE"," ",false,true);
		return Role;
	}
	private IPropertyDefinition getPropIC(){
		IPropertyDefinition IC= new NumberPropertyDefinition(
	 "IC","0.0",false,true);
		return IC;
	}
	private IPropertyDefinition getPropKineticLaw(){
		IPropertyDefinition KineticLaw=new PlainTextPropertyDefinition("KineticLaw"," ",false,true);
		return KineticLaw;
	}
	private IPropertyDefinition getPropCID(){
		IPropertyDefinition CID=new PlainTextPropertyDefinition("CID"," ",false,true);
		return CID;
	}
	private IPropertyDefinition getPropChEBI(){
		IPropertyDefinition ChEBI=new PlainTextPropertyDefinition("ChEBI"," ",false,true);
		return ChEBI;
	}
	private IPropertyDefinition getPropPubChem(){
		IPropertyDefinition PubChem=new PlainTextPropertyDefinition("PubChem"," ",false,true);
		return PubChem;
	}
	private IPropertyDefinition getPropInChI(){
		IPropertyDefinition InChI=new PlainTextPropertyDefinition("InChI"," ",false,true);
		return InChI;
	}
	private IPropertyDefinition getPropGOTerm(){
		IPropertyDefinition GOTerm=new PlainTextPropertyDefinition("GO term"," ",false,true);
		return GOTerm;
	}
	private IPropertyDefinition getPropVarName(){
		IPropertyDefinition VarName=new PlainTextPropertyDefinition("VarName"," ",false,true);
		return VarName;
	}


}
