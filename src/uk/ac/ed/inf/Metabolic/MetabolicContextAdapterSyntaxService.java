package uk.ac.ed.inf.Metabolic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.pathwayeditor.businessobjects.constants.ArrowheadStyle;
import org.pathwayeditor.businessobjects.constants.LineStyle;
import org.pathwayeditor.businessobjects.constants.ShapeType;
import org.pathwayeditor.contextadapter.publicapi.IContext;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterServiceProvider;
import org.pathwayeditor.contextadapter.publicapi.IContextAdapterSyntaxService;
import org.pathwayeditor.contextadapter.publicapi.IPropertyDefinition;
import org.pathwayeditor.contextadapter.publicapi.IRootMapObjectType;
import org.pathwayeditor.contextadapter.publicapi.IShapeObjectType;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.FormattedTextPropertyDefinition;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.LinkEndDefinition;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.LinkObjectType;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.NumberPropertyDefinition;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.RootMapObjectType;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.ShapeObjectType;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.TextPropertyDefinition;

public class MetabolicContextAdapterSyntaxService implements IContextAdapterSyntaxService {
	static enum ObjectTypes {
		Compartment{public String toString(){return "10";}},
		Process{public String toString(){return "11";}},
		Compound{public String toString(){return "12";}},
		Macromolecule{public String toString(){return "13";}},
		Consume{public String toString(){return "20";}},
		Produce{public String toString(){return "21";}},
		Activation{public String toString(){return "22";}},
		Catalysis{public String toString(){return "23";}},
		Inhibition{public String toString(){return "24";}},

		ROOT_MAP_OBJECT{public String toString(){return "-10";}}
	}
	
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
	
	private static TextPropertyDefinition reassignVal(TextPropertyDefinition prop,String val,boolean isEdit,boolean isVis){
		TextPropertyDefinition newP=new TextPropertyDefinition(prop.getName(),val,(prop.isVisualisable() | isVis),(prop.isEditable()&isEdit));
		return newP;
	}
	
	private static FormattedTextPropertyDefinition reassignVal(FormattedTextPropertyDefinition prop,String val,boolean isEdit,boolean isVis){
		FormattedTextPropertyDefinition newP=new FormattedTextPropertyDefinition(prop.getName(),val,(prop.isVisualisable() | isVis),(prop.isEditable()&isEdit));
		return newP;
	}
	
	private static NumberPropertyDefinition reassignVal(NumberPropertyDefinition prop,String val,boolean isEdit,boolean isVis){
		NumberPropertyDefinition newP=new NumberPropertyDefinition(prop.getName(),val,(prop.isVisualisable() | isVis),(prop.isEditable()&isEdit));
		return newP;
	}
	
	private final IContext context;
	private final Set  shapeSet = new HashSet(); 
	private final Set  linkSet = new HashSet();
	private final Set  propSet=new HashSet();
	
	private RootMapObjectType rmo;
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

	
	
	private IContextAdapterServiceProvider serviceProvider;

	public IContextAdapterServiceProvider getServiceProvider() {
		return serviceProvider;
	}
	
	public MetabolicContextAdapterSyntaxService(IContextAdapterServiceProvider serviceProvider) {
		this.serviceProvider=serviceProvider;
		this.context = serviceProvider.getContext();
		//"Metabolic context"
		//"Basic biochemical context"
		//1_0_0
		createRMO();
	//shapes
		createCompartment();
		createProcess();
		createCompound();
		createMacromolecule();

		defineParentingRMO();
	//shapes parenting
		defineParentingCompartment();
		defineParentingProcess();
		defineParentingCompound();
		defineParentingMacromolecule();

	//links
		createConsume();createProduce();createActivation();createCatalysis();createInhibition();
	//shape set
		this.shapeSet.add(this.Compartment);
		this.shapeSet.add(this.Process);
		this.shapeSet.add(this.Compound);
		this.shapeSet.add(this.Macromolecule);

	//link set
		this.linkSet.add(this.Consume);this.linkSet.add(this.Produce);this.linkSet.add(this.Activation);this.linkSet.add(this.Catalysis);this.linkSet.add(this.Inhibition);		
	}

	public IContext getContext() {
		return this.context;
	}

	public Set getLinkTypes() {
		return new HashSet(this.linkSet);
	}

	public IRootMapObjectType getRootMapObjectType() {
		return this.rmo;
	}

	public Set getShapeTypes() {
		return new HashSet(this.shapeSet);
	}
		private void createRMO(){
			this.rmo = new RootMapObjectType(this.context, ObjectTypes.ROOT_MAP_OBJECT);
		}
		private void defineParentingRMO(){
			HashSet<IShapeObjectType> set=new HashSet<IShapeObjectType>();
			set.addAll(Arrays.asList(new IShapeObjectType[]{this.Process, this.Compound, this.Macromolecule, this.Compartment}));
			for (IShapeObjectType child : set) {
			  this.rmo.getParentingRules().addChild(child);
			}
	
		}

	private void createCompartment(){
	this.Compartment = new ShapeObjectType(this.context, ObjectTypes.Compartment);
	this.Compartment.setName("Compartment");
	//this.Compartment.setDescription("Functional compartment");
	this.Compartment.setDescription("Functional compartment");//ment to be TypeDescription rather
	this.Compartment.setShapeType(ShapeType.RECTANGLE);
	this.Compartment.setFillProperty(255,255,255);
	this.Compartment.setSize(20,20);
	int[] lc=new int[]{0,0,0};
	this.Compartment.setLineProperty(1, LineStyle.SOLID,lc[0],lc[1],lc[2]);
	this.Compartment.setShapeType(ShapeType.ROUNDED_RECTANGLE);		int[] s=new int[]{200,200};
			this.Compartment.setSize(s[0],s[1]);
	this.Compartment.setFillEditable(true);
	//this.Compartment.setShapeTypeEditable(true);
	//this.Compartment.setSizeEditable(true);
	this.Compartment.setLineStyleEditable(true);
	this.Compartment.setLineWidthEditable(true);
	this.Compartment.setLineColourEditable(true);
	this.Compartment.setURL("http://");
	 	IPropertyDefinition GOTerm=reassignVal(getPropGOTerm()," ",true,false);
	 	Compartment.addProperty(GOTerm);
	 	IPropertyDefinition Volume= new NumberPropertyDefinition(
	 "volume","1.0",false,true);
		this.Compartment.addProperty(Volume); 	
	 	this.Compartment.setURL("http://www.proteinatlas.org");
	 
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
	this.Process = new ShapeObjectType(this.context, ObjectTypes.Process);
	this.Process.setName("Reaction");
	//this.Process.setDescription("chemical conversion of compounds");
	this.Process.setDescription("chemical conversion of compounds");//ment to be TypeDescription rather
	this.Process.setShapeType(ShapeType.RECTANGLE);
	this.Process.setFillProperty(255,255,255);
	this.Process.setSize(20,20);
	int[] lc=new int[]{0,0,0};
	this.Process.setLineProperty(1, LineStyle.SOLID,lc[0],lc[1],lc[2]);
	this.Process.setShapeType(ShapeType.RECTANGLE);		int[] s=new int[]{10,10};
			this.Process.setSize(s[0],s[1]);int[] c=new int[]{255,255,255};
	this.Process.setFillProperty(c[0],c[1],c[2]);
	this.Process.setFillEditable(true);
	//this.Process.setShapeTypeEditable(true);
	//this.Process.setSizeEditable(false);
	this.Process.setLineStyleEditable(true);
	this.Process.setLineWidthEditable(true);
	this.Process.setLineColourEditable(true);
	this.Process.setURL("http://");
	 	IPropertyDefinition ECnum=reassignVal(getPropECnum(),"-.-.-.-",true,false);
	 	Process.addProperty(ECnum);
	  	IPropertyDefinition KineticLaw=reassignVal(getPropKineticLaw()," ",true,false);
	 	Process.addProperty(KineticLaw);
	 	IPropertyDefinition Reversibility=new TextPropertyDefinition("Reversibility","reversible",false,true);
		this.Process.addProperty(Reversibility);
	}
	
		private void defineParentingProcess(){
			this.Process.getParentingRules().clear();
		}
	
		public ShapeObjectType getProcess(){
			return this.Process;
		}
	private void createCompound(){
	this.Compound = new ShapeObjectType(this.context, ObjectTypes.Compound);
	this.Compound.setName("Compound");
	//this.Compound.setDescription("chemical entity");
	this.Compound.setDescription("chemical entity");//ment to be TypeDescription rather
	this.Compound.setShapeType(ShapeType.RECTANGLE);
	this.Compound.setFillProperty(255,255,255);
	this.Compound.setSize(20,20);
	int[] lc=new int[]{255,0,0};
	this.Compound.setLineProperty(1, LineStyle.SOLID,lc[0],lc[1],lc[2]);
	this.Compound.setShapeType(ShapeType.ELLIPSE);		int[] s=new int[]{20,20};
			this.Compound.setSize(s[0],s[1]);int[] c=new int[]{255,255,255};
	this.Compound.setFillProperty(c[0],c[1],c[2]);
	this.Compound.setFillEditable(true);
	//this.Compound.setShapeTypeEditable(true);
	//this.Compound.setSizeEditable(true);
	this.Compound.setLineStyleEditable(true);
	this.Compound.setLineWidthEditable(true);
	this.Compound.setLineColourEditable(true);
	this.Compound.setURL("http://");
	 	IPropertyDefinition CID=reassignVal(getPropCID()," ",true,false);
	 	Compound.addProperty(CID);
	  	IPropertyDefinition ChEBI=reassignVal(getPropChEBI()," ",true,false);
	 	Compound.addProperty(ChEBI);
	  	IPropertyDefinition PubChem=reassignVal(getPropPubChem()," ",true,false);
	 	Compound.addProperty(PubChem);
	  	IPropertyDefinition InChI=reassignVal(getPropInChI()," ",true,false);
	 	Compound.addProperty(InChI);
	  	IPropertyDefinition Smiles=reassignVal(getPropSmiles()," ",true,false);
	 	Compound.addProperty(Smiles);
	  	IPropertyDefinition IC=reassignVal(getPropIC(),"1.0",true,false);
	 	Compound.addProperty(IC);
	 
	}
	
		private void defineParentingCompound(){
			this.Compound.getParentingRules().clear();
		}
	
		public ShapeObjectType getCompound(){
			return this.Compound;
		}
	private void createMacromolecule(){
	this.Macromolecule = new ShapeObjectType(this.context, ObjectTypes.Macromolecule);
	this.Macromolecule.setName("Macromolecule");
	//this.Macromolecule.setDescription("polymer");
	this.Macromolecule.setDescription("polymer");//ment to be TypeDescription rather
	this.Macromolecule.setShapeType(ShapeType.RECTANGLE);
	this.Macromolecule.setFillProperty(255,255,255);
	this.Macromolecule.setSize(20,20);
	int[] lc=new int[]{0,0,0};
	this.Macromolecule.setLineProperty(1, LineStyle.SOLID,lc[0],lc[1],lc[2]);
	this.Macromolecule.setShapeType(ShapeType.ROUNDED_RECTANGLE);		int[] s=new int[]{20,20};
			this.Macromolecule.setSize(s[0],s[1]);int[] c=new int[]{255,255,255};
	this.Macromolecule.setFillProperty(c[0],c[1],c[2]);
	this.Macromolecule.setFillEditable(true);
	//this.Macromolecule.setShapeTypeEditable(true);
	//this.Macromolecule.setSizeEditable(true);
	this.Macromolecule.setLineStyleEditable(true);
	this.Macromolecule.setLineWidthEditable(true);
	this.Macromolecule.setLineColourEditable(true);
	this.Macromolecule.setURL("http://");
	 	IPropertyDefinition GOTerm=reassignVal(getPropGOTerm()," ",true,false);
	 	Macromolecule.addProperty(GOTerm);
	 	IPropertyDefinition UniProt=new TextPropertyDefinition("UniProt"," ",false,true);
		this.Macromolecule.addProperty(UniProt);
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
	HashSet<IShapeObjectType> set=null;
	this.Consume= new LinkObjectType(this.context, ObjectTypes.Consume);
	int[] lc=new int[]{0,0,0};
	this.Consume.setLineProperty(1, LineStyle.SOLID,lc[0],lc[1],lc[2]);
	this.Consume.setName("Consumption Link");
	this.Consume.setLineColourEditable(true);
	this.Consume.setLineStyleEditable(true);
	this.Consume.setLineWidthEditable(true);
	 	IPropertyDefinition VarName=reassignVal(getPropVarName(),"v1",true,false);
	 	Consume.addProperty(VarName);
	 
	LinkEndDefinition sport=this.Consume.getLinkSource();
	LinkEndDefinition tport=this.Consume.getLinkTarget();
	sport.setOffset(0);//to set default offset value
	sport.getLinkEndDecorator().setDecorator(ArrowheadStyle.NONE, 10,10);
	sport.getTerminusDecorator().setDecoratorType(ShapeType.RECTANGLE);
	sport.getTerminusDecorator().setSize(0,0);
	int[] csport=new int[]{255,255,255};
	sport.getTerminusDecorator().setColourProperties(csport[0],csport[1],csport[2]);
	sport.getTerminusDecorator().setLineProperties(0, LineStyle.SOLID);
	sport.getTerminusDecorator().setShapeTypeEditable(true);
	sport.getTerminusDecorator().setColourEditable(true);
	 	IPropertyDefinition Role=reassignVal(getPropRole(),"substrate",true,true);
	 	sport.addProperty(Role);
	 tport.setOffset(0);
	tport.getLinkEndDecorator().setDecorator(ArrowheadStyle.NONE, 10,10);
	tport.getTerminusDecorator().setDecoratorType(ShapeType.RECTANGLE);
	tport.getTerminusDecorator().setSize(0,0);
	int[] ctport=new int[]{255,255,255};
	tport.getTerminusDecorator().setColourProperties(ctport[0],ctport[1],ctport[2]);
	tport.getTerminusDecorator().setLineProperties(0, LineStyle.SOLID);
	tport.getTerminusDecorator().setShapeTypeEditable(true);
	tport.getTerminusDecorator().setColourEditable(true);
	 	IPropertyDefinition Stoich=reassignVal(getPropStoich(),"1",true,false);
	 	tport.addProperty(Stoich);
	 
	//this.Consume.setDetailedDescription(detailedDescription);
	this.Consume.setUrl("http://");
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
	HashSet<IShapeObjectType> set=null;
	this.Produce= new LinkObjectType(this.context, ObjectTypes.Produce);
	int[] lc=new int[]{0,0,0};
	this.Produce.setLineProperty(1, LineStyle.SOLID,lc[0],lc[1],lc[2]);
	this.Produce.setName("Production Link");
	this.Produce.setLineColourEditable(true);
	this.Produce.setLineStyleEditable(true);
	this.Produce.setLineWidthEditable(true);
	 	IPropertyDefinition VarName=reassignVal(getPropVarName(),"v1",true,false);
	 	Produce.addProperty(VarName);
	 
	LinkEndDefinition sport=this.Produce.getLinkSource();
	LinkEndDefinition tport=this.Produce.getLinkTarget();
	sport.setOffset(2);
	sport.getLinkEndDecorator().setDecorator(ArrowheadStyle.NONE, 10,10);
	sport.getTerminusDecorator().setDecoratorType(ShapeType.RECTANGLE);
	sport.getTerminusDecorator().setSize(0,0);
	int[] csport=new int[]{255,255,255};
	sport.getTerminusDecorator().setColourProperties(csport[0],csport[1],csport[2]);
	sport.getTerminusDecorator().setLineProperties(0, LineStyle.SOLID);
	sport.getTerminusDecorator().setShapeTypeEditable(true);
	sport.getTerminusDecorator().setColourEditable(true);
	 	IPropertyDefinition Stoich=reassignVal(getPropStoich(),"1",true,false);
	 	sport.addProperty(Stoich);
	 tport.setOffset(0);//to set default offset value
	tport.getLinkEndDecorator().setDecorator(ArrowheadStyle.TRIANGLE, 8,8);
	tport.getTerminusDecorator().setDecoratorType(ShapeType.RECTANGLE);
	tport.getTerminusDecorator().setSize(0,0);
	int[] ctport=new int[]{255,255,255};
	tport.getTerminusDecorator().setColourProperties(ctport[0],ctport[1],ctport[2]);
	tport.getTerminusDecorator().setLineProperties(0, LineStyle.SOLID);
	tport.getTerminusDecorator().setShapeTypeEditable(true);
	tport.getTerminusDecorator().setColourEditable(true);
	 	IPropertyDefinition Role=reassignVal(getPropRole(),"product",true,false);
	 	tport.addProperty(Role);
	 
	//this.Produce.setDetailedDescription(detailedDescription);
	this.Produce.setUrl("http://");
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
	HashSet<IShapeObjectType> set=null;
	this.Activation= new LinkObjectType(this.context, ObjectTypes.Activation);
	int[] lc=new int[]{0,0,0};
	this.Activation.setLineProperty(1, LineStyle.SOLID,lc[0],lc[1],lc[2]);
	this.Activation.setName("Activation Link");
	this.Activation.setLineColourEditable(true);
	this.Activation.setLineStyleEditable(true);
	this.Activation.setLineWidthEditable(true);
	 	IPropertyDefinition VarName=reassignVal(getPropVarName(),"v1",true,false);
	 	Activation.addProperty(VarName);
	 
	LinkEndDefinition sport=this.Activation.getLinkSource();
	LinkEndDefinition tport=this.Activation.getLinkTarget();
	sport.setOffset(2);
	sport.getLinkEndDecorator().setDecorator(ArrowheadStyle.NONE, 10,10);
	sport.getTerminusDecorator().setDecoratorType(ShapeType.RECTANGLE);
	sport.getTerminusDecorator().setSize(0,0);
	int[] csport=new int[]{255,255,255};
	sport.getTerminusDecorator().setColourProperties(csport[0],csport[1],csport[2]);
	sport.getTerminusDecorator().setLineProperties(0, LineStyle.SOLID);
	sport.getTerminusDecorator().setShapeTypeEditable(true);
	sport.getTerminusDecorator().setColourEditable(true);
	 	IPropertyDefinition Stoich=reassignVal(getPropStoich(),"1",true,false);
	 	sport.addProperty(Stoich);
	 tport.setOffset(0);//to set default offset value
	tport.getLinkEndDecorator().setDecorator(ArrowheadStyle.TRIANGLE, 10,10);
	tport.getTerminusDecorator().setDecoratorType(ShapeType.RECTANGLE);
	tport.getTerminusDecorator().setSize(0,0);
	int[] ctport=new int[]{255,255,255};
	tport.getTerminusDecorator().setColourProperties(ctport[0],ctport[1],ctport[2]);
	tport.getTerminusDecorator().setLineProperties(0, LineStyle.SOLID);
	tport.getTerminusDecorator().setShapeTypeEditable(true);
	tport.getTerminusDecorator().setColourEditable(true);
	 	IPropertyDefinition Role=reassignVal(getPropRole(),"activator",true,false);
	 	tport.addProperty(Role);
	 
	//this.Activation.setDetailedDescription(detailedDescription);
	this.Activation.setUrl("http://");
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
	HashSet<IShapeObjectType> set=null;
	this.Catalysis= new LinkObjectType(this.context, ObjectTypes.Catalysis);
	int[] lc=new int[]{0,0,0};
	this.Catalysis.setLineProperty(1, LineStyle.SOLID,lc[0],lc[1],lc[2]);
	this.Catalysis.setName("Catalysis Link");
	this.Catalysis.setLineColourEditable(true);
	this.Catalysis.setLineStyleEditable(true);
	this.Catalysis.setLineWidthEditable(true);
	LinkEndDefinition sport=this.Catalysis.getLinkSource();
	LinkEndDefinition tport=this.Catalysis.getLinkTarget();
	sport.setOffset(2);
	sport.getLinkEndDecorator().setDecorator(ArrowheadStyle.NONE, 10,10);
	sport.getTerminusDecorator().setDecoratorType(ShapeType.RECTANGLE);
	sport.getTerminusDecorator().setSize(0,0);
	int[] csport=new int[]{255,255,255};
	sport.getTerminusDecorator().setColourProperties(csport[0],csport[1],csport[2]);
	sport.getTerminusDecorator().setLineProperties(0, LineStyle.SOLID);
	sport.getTerminusDecorator().setShapeTypeEditable(true);
	sport.getTerminusDecorator().setColourEditable(true);
	 	IPropertyDefinition Stoich=reassignVal(getPropStoich(),"1",true,false);
	 	sport.addProperty(Stoich);
	 tport.setOffset(0);//to set default offset value
	tport.getLinkEndDecorator().setDecorator(ArrowheadStyle.DIAMOND, 10,10);
	tport.getTerminusDecorator().setDecoratorType(ShapeType.RECTANGLE);
	tport.getTerminusDecorator().setSize(0,0);
	int[] ctport=new int[]{255,255,255};
	tport.getTerminusDecorator().setColourProperties(ctport[0],ctport[1],ctport[2]);
	tport.getTerminusDecorator().setLineProperties(0, LineStyle.SOLID);
	tport.getTerminusDecorator().setShapeTypeEditable(true);
	tport.getTerminusDecorator().setColourEditable(true);
	 	IPropertyDefinition Role=reassignVal(getPropRole(),"activator",true,false);
	 	tport.addProperty(Role);
	 
	//this.Catalysis.setDetailedDescription(detailedDescription);
	this.Catalysis.setUrl("http://");
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
	HashSet<IShapeObjectType> set=null;
	this.Inhibition= new LinkObjectType(this.context, ObjectTypes.Inhibition);
	int[] lc=new int[]{0,0,0};
	this.Inhibition.setLineProperty(1, LineStyle.SOLID,lc[0],lc[1],lc[2]);
	this.Inhibition.setName("Inhibition Link");
	this.Inhibition.setLineColourEditable(true);
	this.Inhibition.setLineStyleEditable(true);
	this.Inhibition.setLineWidthEditable(true);
	 	IPropertyDefinition VarName=reassignVal(getPropVarName(),"v1",true,false);
	 	Inhibition.addProperty(VarName);
	 
	LinkEndDefinition sport=this.Inhibition.getLinkSource();
	LinkEndDefinition tport=this.Inhibition.getLinkTarget();
	sport.setOffset(2);
	sport.getLinkEndDecorator().setDecorator(ArrowheadStyle.NONE, 10,10);
	sport.getTerminusDecorator().setDecoratorType(ShapeType.RECTANGLE);
	sport.getTerminusDecorator().setSize(0,0);
	int[] csport=new int[]{255,255,255};
	sport.getTerminusDecorator().setColourProperties(csport[0],csport[1],csport[2]);
	sport.getTerminusDecorator().setLineProperties(0, LineStyle.SOLID);
	sport.getTerminusDecorator().setShapeTypeEditable(true);
	sport.getTerminusDecorator().setColourEditable(true);
	 	IPropertyDefinition Stoich=reassignVal(getPropStoich(),"1",true,false);
	 	sport.addProperty(Stoich);
	 tport.setOffset(0);//to set default offset value
	tport.getLinkEndDecorator().setDecorator(ArrowheadStyle.BAR, 10,10);
	tport.getTerminusDecorator().setDecoratorType(ShapeType.RECTANGLE);
	tport.getTerminusDecorator().setSize(0,0);
	int[] ctport=new int[]{255,255,255};
	tport.getTerminusDecorator().setColourProperties(ctport[0],ctport[1],ctport[2]);
	tport.getTerminusDecorator().setLineProperties(0, LineStyle.SOLID);
	tport.getTerminusDecorator().setShapeTypeEditable(true);
	tport.getTerminusDecorator().setColourEditable(true);
	 	IPropertyDefinition Role=reassignVal(getPropRole(),"inhibitor",true,false);
	 	tport.addProperty(Role);
	 
	//this.Inhibition.setDetailedDescription(detailedDescription);
	this.Inhibition.setUrl("http://");
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
		IPropertyDefinition ECnum=new TextPropertyDefinition("EC","-",true,true);
		return ECnum;
	}
	private IPropertyDefinition getPropSmiles(){
		IPropertyDefinition Smiles=new TextPropertyDefinition("SMILES"," ",false,true);
		return Smiles;
	}
	private IPropertyDefinition getPropStoich(){
		IPropertyDefinition Stoich=new TextPropertyDefinition("STOICH"," ",true,true);
		return Stoich;
	}
	private IPropertyDefinition getPropRole(){
		IPropertyDefinition Role=new TextPropertyDefinition("ROLE"," ",false,true);
		return Role;
	}
	private IPropertyDefinition getPropIC(){
		IPropertyDefinition IC= new NumberPropertyDefinition(
	 "IC","0.0",false,true);
		return IC;
	}
	private IPropertyDefinition getPropKineticLaw(){
		IPropertyDefinition KineticLaw=new TextPropertyDefinition("KineticLaw"," ",false,true);
		return KineticLaw;
	}
	private IPropertyDefinition getPropCID(){
		IPropertyDefinition CID=new TextPropertyDefinition("CID"," ",false,true);
		return CID;
	}
	private IPropertyDefinition getPropChEBI(){
		IPropertyDefinition ChEBI=new TextPropertyDefinition("ChEBI"," ",false,true);
		return ChEBI;
	}
	private IPropertyDefinition getPropPubChem(){
		IPropertyDefinition PubChem=new TextPropertyDefinition("PubChem"," ",false,true);
		return PubChem;
	}
	private IPropertyDefinition getPropInChI(){
		IPropertyDefinition InChI=new TextPropertyDefinition("InChI"," ",false,true);
		return InChI;
	}
	private IPropertyDefinition getPropGOTerm(){
		IPropertyDefinition GOTerm=new TextPropertyDefinition("GO term"," ",false,true);
		return GOTerm;
	}
	private IPropertyDefinition getPropVarName(){
		IPropertyDefinition VarName=new TextPropertyDefinition("VarName"," ",false,true);
		return VarName;
	}


}
