package uk.ac.ed.inf.Metabolic;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pathwayeditor.businessobjects.drawingprimitives.attributes.LineStyle;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.LinkEndDecoratorShape;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.PrimitiveShapeType;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.RGB;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.Size;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IHtmlPropertyDefinition;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.INumberPropertyDefinition;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IPlainTextPropertyDefinition;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IPropertyDefinition;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSyntaxService;
import org.pathwayeditor.businessobjects.typedefn.ILinkObjectType;
import org.pathwayeditor.businessobjects.typedefn.IObjectType;
import org.pathwayeditor.businessobjects.typedefn.IRootObjectType;
import org.pathwayeditor.businessobjects.typedefn.IShapeAttributeDefaults;
import org.pathwayeditor.businessobjects.typedefn.IShapeObjectType;
import org.pathwayeditor.businessobjects.typedefn.ILinkTerminusDefinition.LinkTermEditableAttributes;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.FormattedTextPropertyDefinition;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.LinkConnectionRules;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.LinkObjectType;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.LinkTerminusDefaults;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.LinkTerminusDefinition;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.NumberPropertyDefinition;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.PlainTextPropertyDefinition;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.RootObjectType;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.ShapeObjectType;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.ShapeParentingRules;

public class MetabolicNotationSyntaxService implements INotationSyntaxService {

	public static final String PARAMETERS_PROP = "Parameters";
	public static final String REVERSIBILITY_PROP = "Reversibility";
	public static final String EC_PROP = "EC";
	public static final String SMILES_PROP = "SMILES";
	public static final String STOICH_PROP = "STOICH";
	public static final String ROLE_PROP = "ROLE";
	public static final String IC_PROP = "IC";
	public static final String KINETIC_LAW_PROP = "KineticLaw";
	public static final String CID_PROP = "CID";
	public static final String CH_EBI_PROP = "ChEBI";
	public static final String PUB_CHEM_PROP = "PubChem";
	public static final String IN_CHI_PROP = "InChI";
	public static final String VAR_NAME_PROP = "VarName";
	public static final String GO_TERM_PROP = "GO term";
	private static final String VOLUME_PROP = "volume";

	private static IPropertyDefinition reassignVal(IPropertyDefinition prop, String val, boolean isEdit, boolean isVis) {
		if (prop instanceof IPlainTextPropertyDefinition)
			return reassignVal((PlainTextPropertyDefinition) prop, val, isEdit, isVis);
		if (prop instanceof IHtmlPropertyDefinition)
			return reassignVal((FormattedTextPropertyDefinition) prop, val, isEdit, isVis);
		if (prop instanceof INumberPropertyDefinition)
			return reassignVal((NumberPropertyDefinition) prop, val, isEdit, isVis);
		return prop;
	}

	private static IPlainTextPropertyDefinition reassignVal(PlainTextPropertyDefinition prop, String val, boolean isEdit, boolean isVis) {
		PlainTextPropertyDefinition newP = new PlainTextPropertyDefinition(prop.getName(), val, (prop.isVisualisable() | isVis), (prop.isEditable() & isEdit));
		if (newP.isVisualisable())
			newP.setLabelAttributeDefaults(prop.getLabelDefaults());
		return newP;
	}

	private static IHtmlPropertyDefinition reassignVal(FormattedTextPropertyDefinition prop, String val, boolean isEdit, boolean isVis) {
		FormattedTextPropertyDefinition newP = new FormattedTextPropertyDefinition(prop.getName(), val, (prop.isVisualisable() | isVis), (prop.isEditable() & isEdit));
		if (newP.isVisualisable())
			newP.setLabelAttributeDefaults(prop.getLabelDefaults());
		return newP;
	}

	private static NumberPropertyDefinition reassignVal(NumberPropertyDefinition prop, String val, boolean isEdit, boolean isVis) {
		NumberPropertyDefinition newP = new NumberPropertyDefinition(prop.getName(), val, (prop.isVisualisable() | isVis), (prop.isEditable() & isEdit));
		if (newP.isVisualisable())
			newP.setLabelAttributeDefaults(prop.getLabelDefaults());
		return newP;
	}

	private final INotation context;
	private final Set<IShapeObjectType> shapeSet = new HashSet<IShapeObjectType>();
	private final Set<ILinkObjectType> linkSet = new HashSet<ILinkObjectType>();

	private final Map<Integer, IShapeObjectType> shapeObjectTypeMap = new HashMap<Integer, IShapeObjectType>();
	private final Map<Integer, ILinkObjectType> linkObjectTypeMap = new HashMap<Integer, ILinkObjectType>();

	private RootObjectType rmo;
	// shapes
	private ShapeObjectType Compartment;
	private ShapeObjectType process;
	private ShapeObjectType Compound;

	// links
	private LinkObjectType Consume;
	private LinkObjectType Produce;
	private LinkObjectType Activation;
	private LinkObjectType Catalysis;
	private LinkObjectType Inhibition;

	private INotationSubsystem serviceProvider;

	public INotationSubsystem getServiceProvider() {
		return serviceProvider;
	}

	public MetabolicNotationSyntaxService(INotationSubsystem serviceProvider) {
		this.serviceProvider = serviceProvider;
		this.context = serviceProvider.getNotation();
		createRMO();
		// shapes
		createCompartment();
		createProcess();
		createCompound();
		defineParentingRMO();
		// shapes parenting
		defineParentingCompartment();
		defineParentingProcess();
		defineParentingCompound();

		// links
		createConsume();
		createProduce();
		createActivation();
		createCatalysis();
		createInhibition();

		// shape set
		this.shapeObjectTypeMap.put(this.Compartment.getUniqueId(), this.Compartment);
		this.shapeObjectTypeMap.put(this.process.getUniqueId(), this.process);
		this.shapeObjectTypeMap.put(this.Compound.getUniqueId(), this.Compound);

		// link set
		this.linkObjectTypeMap.put(this.Consume.getUniqueId(), this.Consume);
		this.linkObjectTypeMap.put(this.Produce.getUniqueId(), this.Produce);
		this.linkObjectTypeMap.put(this.Activation.getUniqueId(), this.Activation);
		this.linkObjectTypeMap.put(this.Catalysis.getUniqueId(), this.Catalysis);
		this.linkObjectTypeMap.put(this.Inhibition.getUniqueId(), this.Inhibition);
	}

	private void createCompartment() {
		this.Compartment = new ShapeObjectType(new CompartmentDefaults(), 10, "Functional compartment", "Compartment", serviceProvider.getSyntaxService());
		Compartment.setEditableAttributes(EnumSet.allOf(IShapeObjectType.EditableShapeAttributes.class));
	}

	private void createRMO() {
		this.rmo = new RootObjectType(-10, "ROOT_MAP_OBJECT", "ROOT_MAP_OBJECT", this);
	}

	private void defineParentingRMO() {
		HashSet<IShapeObjectType> set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.process, this.Compound, this.Compartment }));
		for (IShapeObjectType child : set) {
			((ShapeParentingRules) this.rmo.getParentingRules()).addChild(child);
		}

	}

	private class CompartmentDefaults implements IShapeAttributeDefaults {
		private PrimitiveShapeType shapeType = PrimitiveShapeType.ROUNDED_RECTANGLE;
		private LineStyle lineStyle = LineStyle.SOLID;
		private Size size = new Size(20, 20);
		private RGB fillColour = new RGB(255, 255, 255);
		private RGB lineColour = new RGB(0, 0, 0);
		private String detailedDescription = "";
		private int lineWidth = 1;
		private String url = "http://www.proteinatlas.org";
		private Set<IPropertyDefinition> propertyDefinitions = new HashSet();
		private String description = "Functional compartment";
		{
			IPropertyDefinition goTerm = reassignVal(getPropGOTerm(), " ", true, false);
			IPropertyDefinition volume = new NumberPropertyDefinition(MetabolicNotationSyntaxService.VOLUME_PROP, "1.0", false, true);
			propertyDefinitions.add(goTerm);
			propertyDefinitions.add(volume);
		}

		public String getDescription() {
			return description;
		}

		public String getDetailedDescription() {
			return detailedDescription;
		}

		public RGB getFillColour() {
			return fillColour;
		}

		public RGB getLineColour() {
			return lineColour;
		}

		public LineStyle getLineStyle() {
			return lineStyle;
		}

		public int getLineWidth() {
			return lineWidth;
		}

		public String getName() {
			return "compartment";
		}

		public PrimitiveShapeType getShapeType() {
			return shapeType;
		}

		public Size getSize() {
			return size;
		}

		public String getURL() {
			return url;
		}

		public Iterator<IPropertyDefinition> propertyDefinitionIterator() {
			return propertyDefinitions.iterator();
		}
	}

	private void defineParentingCompartment() {
		Set<IShapeObjectType> set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Compartment, this.process, this.Compound }));
		ShapeParentingRules rules = (ShapeParentingRules) Compartment.getParentingRules();
		for (IShapeObjectType child : set) {
			rules.addChild(child);
		}

	}

	public ShapeObjectType getCompartment() {
		return this.Compartment;
	}

	private void createProcess() {
		MetabolicFallbackShapeAttributeDefaults processDefaults = new MetabolicFallbackShapeAttributeDefaults();
		processDefaults.setDescription("chemical conversion of compounds");
		processDefaults.setShapeType(PrimitiveShapeType.RECTANGLE);
		processDefaults.setFillColour(new RGB(255, 255, 255));
		processDefaults.setSize(new Size(10, 10));
		processDefaults.setLineStyle(LineStyle.SOLID);
		processDefaults.setLineWidth(2);
		processDefaults.setShapeType(PrimitiveShapeType.RECTANGLE);
		processDefaults.setLineColour(new RGB(0, 0, 0));
		processDefaults.setUrl("http://");
		Set<IPropertyDefinition> properties = new HashSet<IPropertyDefinition>();
		IPropertyDefinition ECnum = reassignVal(getPropECnum(), "-.-.-.-", true, false);
		properties.add(ECnum);
		IPropertyDefinition KineticLaw = reassignVal(getPropKineticLaw(), " ", true, false);
		properties.add(KineticLaw);
		IPropertyDefinition Reversibility = new PlainTextPropertyDefinition(REVERSIBILITY_PROP, "irreversible", false, true);
		properties.add(Reversibility);
		IPropertyDefinition Parameters = new PlainTextPropertyDefinition(PARAMETERS_PROP, " ", false, true);
		properties.add(Parameters);
		processDefaults.setPropertyDefinitions(properties);
		this.process = new ShapeObjectType(processDefaults, 11, "chemical conversion of compounds", "process", serviceProvider.getSyntaxService());
		process.setEditableAttributes(EnumSet.complementOf(EnumSet.of(IShapeObjectType.EditableShapeAttributes.LINE_WIDTH, IShapeObjectType.EditableShapeAttributes.SHAPE_SIZE)));
	}

	private void defineParentingProcess() {
		((ShapeParentingRules)this.process.getParentingRules()).clear();
	}

	public ShapeObjectType getProcess() {
		return this.process;
	}

	private void createCompound() {
		MetabolicFallbackShapeAttributeDefaults compoundDefaults = new MetabolicFallbackShapeAttributeDefaults();
		compoundDefaults.setDescription("chemical entity");
		compoundDefaults.setShapeType(PrimitiveShapeType.ELLIPSE);
		compoundDefaults.setFillColour(new RGB(255, 255, 255));
		compoundDefaults.setSize(new Size(60, 40));
		compoundDefaults.setLineColour(new RGB(255, 0, 0));
		compoundDefaults.setLineStyle(LineStyle.SOLID);
		compoundDefaults.setLineWidth(1);
		compoundDefaults.setFillColour(new RGB(255, 25, 255));
		compoundDefaults.setUrl("http://");
		Set<IPropertyDefinition> properties = new HashSet<IPropertyDefinition>();
		IPropertyDefinition CID = reassignVal(getPropCID(), " ", true, false);
		properties.add(CID);
		IPropertyDefinition ChEBI = reassignVal(getPropChEBI(), " ", true, false);
		properties.add(ChEBI);
		IPropertyDefinition PubChem = reassignVal(getPropPubChem(), " ", true, false);
		properties.add(PubChem);
		IPropertyDefinition InChI = reassignVal(getPropInChI(), " ", true, false);
		properties.add(InChI);
		IPropertyDefinition Smiles = reassignVal(getPropSmiles(), " ", true, false);
		properties.add(Smiles);
		IPropertyDefinition IC = reassignVal(getPropIC(), "1.0", true, false);
		properties.add(IC);
		compoundDefaults.setPropertyDefinitions(properties);
		this.Compound = new ShapeObjectType(compoundDefaults, 12, "chemical entity", "Compound", this);
		Compound.setEditableAttributes(EnumSet.complementOf(EnumSet.of(IShapeObjectType.EditableShapeAttributes.SHAPE_TYPE, IShapeObjectType.EditableShapeAttributes.SHAPE_SIZE)));

	}

	private void defineParentingCompound() {
		((ShapeParentingRules)this.Compound.getParentingRules()).clear();
	}

	public ShapeObjectType getCompound() {
		return this.Compound;
	}

	private void createConsume() {
		Consume = createLinkObjectType("Consumption Link", "Consume", 20, LinkEndDecoratorShape.NONE, LinkEndDecoratorShape.NONE, reassignVal(getPropRole(), "substrate", true, true), reassignVal(
				getPropStoich(), "1", true, false), new Size(10, 10), new Size(10, 10));
		Set<IShapeObjectType> set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.process }));
		for (IShapeObjectType tgt : set) {
			LinkConnectionRules connRules = (LinkConnectionRules) Consume.getLinkConnectionRules();
			connRules.addConnection(this.Compound, tgt);
		}
	}

	public LinkObjectType getConsume() {
		return this.Consume;
	}

	private void createProduce() {
		Produce = createLinkObjectType("Production Link", "Production Link", 21, LinkEndDecoratorShape.NONE, LinkEndDecoratorShape.TRIANGLE, reassignVal(getPropStoich(), "1", true, false),
				reassignVal(getPropRole(), "product", true, false), new Size(10, 10), new Size(8, 8));
		Set<IShapeObjectType> set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.Compound }));
		for (IShapeObjectType tgt : set) {
			((LinkConnectionRules) this.Produce.getLinkConnectionRules()).addConnection(this.process, tgt);
		}

	}

	public LinkObjectType getProduce() {
		return this.Produce;
	}

	private void createActivation() {
		Activation = createLinkObjectType("Activation Link", "Activation Link", 22, LinkEndDecoratorShape.NONE, LinkEndDecoratorShape.ARROW, reassignVal(getPropStoich(), "1", true, false),
				reassignVal(getPropRole(), "activator", true, false), new Size(10, 10), new Size(10, 10));
		HashSet<IShapeObjectType> set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.process }));
		for (IShapeObjectType tgt : set) {
			((LinkConnectionRules) this.Activation.getLinkConnectionRules()).addConnection(this.Compound, tgt);
		}
	}

	public LinkObjectType getActivation() {
		return this.Activation;
	}

	private void createCatalysis() {
		Catalysis = createLinkObjectType("Catalysis Link", "Catalysis Link", 23, LinkEndDecoratorShape.NONE, LinkEndDecoratorShape.DIAMOND, reassignVal(getPropStoich(), "1", true, false),
				reassignVal(getPropRole(), "activator", true, false), new Size(10, 10), new Size(10, 10));
		HashSet<IShapeObjectType> set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.process }));
		for (IShapeObjectType tgt : set) {
			((LinkConnectionRules) this.Catalysis.getLinkConnectionRules()).addConnection(this.Compound, tgt);
		}
	}

	public LinkObjectType getCatalysis() {
		return this.Catalysis;
	}

	private void createInhibition() {
		Inhibition = createLinkObjectType("Inhibition Link", "Inhibition Link", 24, LinkEndDecoratorShape.NONE, LinkEndDecoratorShape.BAR, reassignVal(getPropStoich(), "1", true, false), reassignVal(
				getPropRole(), "inhibitor", true, false), new Size(10, 10), new Size(10, 10));
		HashSet<IShapeObjectType> set = new HashSet<IShapeObjectType>();
		set.addAll(Arrays.asList(new IShapeObjectType[] { this.process }));
		for (IShapeObjectType tgt : set) {
			((LinkConnectionRules) this.Inhibition.getLinkConnectionRules()).addConnection(this.Compound, tgt);
		}
	}

	public LinkObjectType getInhibition() {
		return this.Inhibition;
	}

	private IPropertyDefinition getPropECnum() {
		PlainTextPropertyDefinition ECnum = new PlainTextPropertyDefinition(EC_PROP, "-", true, true);
		return ECnum;
	}

	private IPropertyDefinition getPropSmiles() {
		IPropertyDefinition Smiles = new PlainTextPropertyDefinition(SMILES_PROP, " ", false, true);
		return Smiles;
	}

	private IPropertyDefinition getPropStoich() {
		PlainTextPropertyDefinition Stoich = new PlainTextPropertyDefinition(STOICH_PROP, " ", true, true);
		return Stoich;
	}

	private IPropertyDefinition getPropRole() {
		PlainTextPropertyDefinition Role = new PlainTextPropertyDefinition(ROLE_PROP, " ", false, true);
		return Role;
	}

	private IPropertyDefinition getPropIC() {
		IPropertyDefinition IC = new NumberPropertyDefinition(IC_PROP, "0.0", false, true);
		return IC;
	}

	private IPropertyDefinition getPropKineticLaw() {
		IPropertyDefinition KineticLaw = new PlainTextPropertyDefinition(KINETIC_LAW_PROP, " ", false, true);
		return KineticLaw;
	}

	private IPropertyDefinition getPropCID() {
		IPropertyDefinition CID = new PlainTextPropertyDefinition(CID_PROP, " ", false, true);
		return CID;
	}

	private IPropertyDefinition getPropChEBI() {
		IPropertyDefinition ChEBI = new PlainTextPropertyDefinition(CH_EBI_PROP, " ", false, true);
		return ChEBI;
	}

	private IPropertyDefinition getPropPubChem() {
		IPropertyDefinition PubChem = new PlainTextPropertyDefinition(PUB_CHEM_PROP, " ", false, true);
		return PubChem;
	}

	private IPropertyDefinition getPropInChI() {
		IPropertyDefinition InChI = new PlainTextPropertyDefinition(IN_CHI_PROP, " ", false, true);
		return InChI;
	}

	private IPropertyDefinition getPropGOTerm() {
		IPropertyDefinition GOTerm = new PlainTextPropertyDefinition(GO_TERM_PROP, " ", false, true);
		return GOTerm;
	}

	private IPropertyDefinition getPropVarName() {
		IPropertyDefinition VarName = new PlainTextPropertyDefinition(VAR_NAME_PROP, " ", false, true);
		return VarName;
	}

	public boolean containsLinkObjectType(int uniqueID) {
		return linkObjectTypeMap.containsKey(uniqueID);
	}

	public boolean containsObjectType(int uniqueID) {
		if (linkObjectTypeMap.containsKey(uniqueID))
			return true;
		if (shapeObjectTypeMap.containsKey(uniqueID))
			return true;
		if(rmo.getUniqueId()==uniqueID)
			return true;
		return false;
	}

	public boolean containsShapeObjectType(int uniqueID) {
		return shapeObjectTypeMap.containsKey(uniqueID);
	}

	public ILinkObjectType getLinkObjectType(int uniqueId) {
		if(!containsLinkObjectType(uniqueId))
			throw new IllegalArgumentException("No object type exists for this id");
		return linkObjectTypeMap.get(uniqueId);
	}

	public IObjectType getObjectType(int uniqueId) {
		if(!containsObjectType(uniqueId))
			throw new IllegalArgumentException("No object type exists for this id");
		if (linkObjectTypeMap.containsKey(uniqueId))
			return linkObjectTypeMap.get(uniqueId);
		if (shapeObjectTypeMap.containsKey(uniqueId))
			return shapeObjectTypeMap.get(uniqueId);
		return rmo;
	}

	public IShapeObjectType getShapeObjectType(int uniqueId) {
		if(!containsShapeObjectType(uniqueId))
			throw new IllegalArgumentException("No object type exists for this id");
		return shapeObjectTypeMap.get(uniqueId);
	}

	public Iterator<IObjectType> objectTypeIterator() {
		Set<IObjectType> objectTypes = new HashSet<IObjectType>();
		objectTypes.addAll(linkObjectTypeMap.values());
		objectTypes.addAll(shapeObjectTypeMap.values());
		objectTypes.add(rmo);
		return objectTypes.iterator();
	}

	public INotationSubsystem getNotationSubsystem() {
		return serviceProvider;
	}

	public INotation getNotation() {
		return this.context;
	}

	public Iterator<ILinkObjectType> linkTypeIterator() {
		return linkObjectTypeMap.values().iterator();
	}

	public IRootObjectType getRootObjectType() {
		return this.rmo;
	}

	public Iterator<IShapeObjectType> shapeTypeIterator() {
		return shapeObjectTypeMap.values().iterator();
	}

	public LinkObjectType createLinkObjectType(String name, String description, int id, LinkEndDecoratorShape src, LinkEndDecoratorShape target, IPropertyDefinition srcP, IPropertyDefinition targP,
			Size srcEndSize, Size targEndSize) {

		MetabolicFallbackLinkAttributeDefaults attrDefaults = new MetabolicFallbackLinkAttributeDefaults();
		attrDefaults.setLineColour(new RGB(0, 0, 0));
		attrDefaults.setLineWidth(1);
		attrDefaults.setLineStyle(LineStyle.SOLID);
		attrDefaults.setUrl("http://");
		IPropertyDefinition VarName = reassignVal(getPropVarName(), "v1", true, false);
		Set<IPropertyDefinition> properties = new HashSet<IPropertyDefinition>();
		properties.add(VarName);
		attrDefaults.setPropertyDefinitions(properties);

		LinkTerminusDefaults srcLinkTerminusDefaults = new LinkTerminusDefaults();
		srcLinkTerminusDefaults.setTermDecoratorType(PrimitiveShapeType.RECTANGLE);
		srcLinkTerminusDefaults.setGap((short) 2);
		srcLinkTerminusDefaults.setLinkEndDecoratorShape(src);
		srcLinkTerminusDefaults.setEndSize(srcEndSize);
		srcLinkTerminusDefaults.setTermSize(new Size(0, 0));
		srcLinkTerminusDefaults.setTermColour(new RGB(255, 255, 255));
		LinkTerminusDefaults targetTerminusDefaults = new LinkTerminusDefaults(srcLinkTerminusDefaults);
		targetTerminusDefaults.setGap((short) 0);
		srcLinkTerminusDefaults.addPropertyDefinition(srcP);
		targetTerminusDefaults.addPropertyDefinition(targP);

		LinkObjectType consume = new LinkObjectType(attrDefaults, id, name, description, this, srcLinkTerminusDefaults, targetTerminusDefaults);
		consume.setEditableAttributes(EnumSet.of(ILinkObjectType.LinkEditableAttributes.COLOUR, ILinkObjectType.LinkEditableAttributes.LINE_WIDTH, ILinkObjectType.LinkEditableAttributes.LINE_STYLE));

		LinkTerminusDefinition sport = (LinkTerminusDefinition) consume.getSourceTerminusDefinition();
		LinkTerminusDefinition tport = (LinkTerminusDefinition) consume.getTargetTerminusDefinition();
		EnumSet<LinkTermEditableAttributes> editable = EnumSet.of(LinkTermEditableAttributes.TERM_DECORATOR_TYPE, LinkTermEditableAttributes.TERM_COLOUR);
		sport.setEditableAttributes(editable);
		tport.setEditableAttributes(editable);
		return consume;
	}

}
