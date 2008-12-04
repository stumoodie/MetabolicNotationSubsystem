package uk.ac.ed.inf.Metabolic.parser;

import java.util.Iterator;
import java.util.List;

import org.pathwayeditor.businessobjects.drawingprimitives.ICanvasAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingNode;
import org.pathwayeditor.businessobjects.drawingprimitives.ILabelNode;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkEdge;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkTerminus;
import org.pathwayeditor.businessobjects.drawingprimitives.IRootNode;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotatedObject;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.businessobjects.repository.IMap;
import org.pathwayeditor.contextadapter.toolkit.ndom.AbstractNDOMParser;
import org.pathwayeditor.contextadapter.toolkit.ndom.ModelObject;
import org.pathwayeditor.contextadapter.toolkit.ndom.NdomException;
import org.pathwayeditor.contextadapter.toolkit.validation.RuleValidationReportBuilder;

import uk.ac.ed.inf.Metabolic.MetabolicNotationSyntaxService;
import uk.ac.ed.inf.Metabolic.ndomAPI.ERelType;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

/**
 * $Id$
 * 
 * @author Anatoly Sorokin
 * @date 12 May 2008
 * 
 */
public abstract class NDOMFactory extends AbstractNDOMParser {
	protected MetabolicModel ndom;
	private RuleValidationReportBuilder reportBuilder;
	private MetabolicRuleLoader loader = (MetabolicRuleLoader) MetabolicRuleLoader
			.getInstance();

	// private
	public NDOMFactory(IRootNode rmo) {
		super(rmo);
	}

	public NDOMFactory() {
		super();
	}

	// coould be in abstract class??
	public IModel getNdom() {
		return ndom;
	}

	/**
	 * 
	 * @param parent
	 * @param mapObject
	 */
	protected abstract void compartment(MetabolicCompartment parent,
			IDrawingNode mapObject);

	protected abstract void compound(MetabolicCompartment compartment,
			IDrawingNode mapObject);

	protected abstract void process(ModelObject parent, IDrawingNode mapObject);

	/**
	 * Creates Activation relation in the model. That method creates activation
	 * relation by invocation of {@link #activation(ILinkEdge)} method and
	 * registered this relation with reaction <code>r</code> and source
	 * shape,obtained from <code>el</code>. Preconditions:<br>
	 * <ul>
	 * <li>Reaction is not <code>NULL</code></li>
	 * <li>link <code>el</code> has non-null and registered with the model
	 * source shape</li>
	 * </ul>
	 * Postconditions:<br>
	 * <ul>
	 * <li>Fully defined valid Relation object of type
	 * {@link ERelType#Activation}</li>
	 * </ul>
	 * 
	 * @param el
	 *            non-null Link object to convert to Relation
	 * @param r
	 *            non-null reaction to register new Relation with
	 */
	protected abstract void activate(ILinkEdge el, MetabolicReaction r);

	/**
	 * Creates Catalysis relation in the model. That method creates catalysis
	 * relation by invocation of {@link #catalysis(ILinkEdge)} method and
	 * registered this relation with reaction <code>r</code> and source
	 * shape,obtained from <code>el</code>. Preconditions:<br>
	 * <ul>
	 * <li>Reaction is not <code>NULL</code></li>
	 * <li>link <code>el</code> has non-null and registered with the model
	 * source shape</li>
	 * </ul>
	 * Postconditions:<br>
	 * <ul>
	 * <li>Fully defined valid Relation object of type
	 * {@link ERelType#Catalysis}</li>
	 * </ul>
	 * 
	 * @param el
	 *            non-null Link object to convert to Relation
	 * @param r
	 *            non-null reaction to register new Relation with
	 */
	protected abstract void catalysis(ILinkEdge el, MetabolicReaction r);

	/**
	 * Creates inhibitory relation in the model. That method creates inhibitory
	 * relation by invocation of {@link #inhibition(ILinkEdge)} method and
	 * registered this relation with reaction <code>r</code> and source
	 * shape,obtained from <code>el</code>. Preconditions:<br>
	 * <ul>
	 * <li>Reaction is not <code>NULL</code></li>
	 * <li>link <code>el</code> has non-null and registered with the model
	 * source shape</li>
	 * </ul>
	 * Postconditions:<br>
	 * <ul>
	 * <li>Fully defined valid Relation object of type
	 * {@link ERelType#Inhibition}</li>
	 * </ul>
	 * 
	 * @param el
	 *            non-null Link object to convert to Relation
	 * @param r
	 *            non-null reaction to register new Relation with
	 */
	protected abstract void inhibit(ILinkEdge el, MetabolicReaction r);

	/**
	 * Creates Production relation in the model. That method creates production
	 * relation by invocation of {@link #production(ILinkEdge)} method and
	 * registered this relation with reaction <code>r</code> and source
	 * shape,obtained from <code>el</code>. Preconditions:<br>
	 * <ul>
	 * <li>Reaction is not <code>NULL</code></li>
	 * <li>link <code>el</code> has non-null and registered with the model
	 * source shape</li>
	 * </ul>
	 * Postconditions:<br>
	 * <ul>
	 * <li>Fully defined valid Relation object of type
	 * {@link ERelType#Production}</li>
	 * </ul>
	 * 
	 * @param el
	 *            non-null Link object to convert to Relation
	 * @param r
	 *            non-null reaction to register new Relation with
	 */
	protected abstract void products(ILinkEdge el, MetabolicReaction r);

	/**
	 * Creates Consumption relation in the model. That method creates
	 * consumption relation by invocation of {@link #consumption(ILinkEdge)}
	 * method and registered this relation with reaction <code>r</code> and
	 * source shape,obtained from <code>el</code>. Preconditions:<br>
	 * <ul>
	 * <li>Reaction is not <code>NULL</code></li>
	 * <li>link <code>el</code> has non-null and registered with the model
	 * source shape</li>
	 * </ul>
	 * Postconditions:<br>
	 * <ul>
	 * <li>Fully defined valid Relation object of type
	 * {@link ERelType#Consumption}</li>
	 * </ul>
	 * 
	 * @param el
	 *            non-null Link object to convert to Relation
	 * @param r
	 *            non-null reaction to register new Relation with
	 */
	protected abstract void substrate(ILinkEdge el, MetabolicReaction r);

	protected MetabolicCompartment compartment(IDrawingNode mapObject) {
		MetabolicCompartment compartment = new MetabolicCompartment(
				getId(mapObject), mapObject, ndom);
		IAnnotatedObject att = (IAnnotatedObject) mapObject.getAttribute();
		compartment.setGOTerm(att.getProperty(
				MetabolicNotationSyntaxService.GO_TERM_PROP).getValue()
				.toString());
		compartment.setVolume(1.0d);
		return compartment;
	}

	protected MetabolicCompound compound(IDrawingNode mapObject) {
		MetabolicCompound comp = new MetabolicCompound(getId(mapObject),
				mapObject, ndom);
		IAnnotatedObject att = (IAnnotatedObject) mapObject.getAttribute();
		comp.setCID(att.getProperty(MetabolicNotationSyntaxService.CID_PROP)
				.getValue().toString());
		comp.setChEBIId(att.getProperty(
				MetabolicNotationSyntaxService.CH_EBI_PROP).getValue()
				.toString());
		comp.setInChI(att.getProperty(
				MetabolicNotationSyntaxService.IN_CHI_PROP).getValue()
				.toString());
		comp.setPubChemId(att.getProperty(
				MetabolicNotationSyntaxService.PUB_CHEM_PROP).getValue()
				.toString());
		comp.setSmiles(att.getProperty(
				MetabolicNotationSyntaxService.SMILES_PROP).getValue()
				.toString());
		setIC(mapObject, comp);
		return comp;
	}

	private void setIC(IDrawingNode mapObject, MetabolicCompound comp) {
		IValidationRuleDefinition rd = reportBuilder.getRuleStore()
				.getRuleById(MetabolicRuleLoader.IC_ERROR_ID);
		DoublePropertyRule r = (DoublePropertyRule) loader.getRuleByDef(rd);
//		r.setObject((IAnnotatedObject) mapObject.getAttribute());
		r.setRefObject(mapObject);
		if (r.validate(reportBuilder)) {
			comp.setIC(r.getValue());
		}
	}

	@Override
	protected void ndom() {
		IMap owningMap = getRmo().getModel().getCanvas().getOwningMap();
		String name = owningMap.getName();
		ndom = new MetabolicModel(getId(getRmo()), name, name);// AbstractNDOMParser.getASCIIName(name));
		ndom.setDescription(owningMap.getDescription());
		ndom.setDetailedDescription("");
	}

	protected MetabolicReaction process(IDrawingNode mapObject) {
		MetabolicReaction re = new MetabolicReaction(getId(mapObject),
				mapObject, ndom);
		IAnnotatedObject att = (IAnnotatedObject) mapObject.getAttribute();
		re.setECNumber(att.getProperty(MetabolicNotationSyntaxService.EC_PROP)
				.getValue().toString());
		re.setKineticLaw(att.getProperty(
				MetabolicNotationSyntaxService.KINETIC_LAW_PROP).getValue()
				.toString());
		setReParam(mapObject, re);
		String value = att.getProperty(
				MetabolicNotationSyntaxService.REVERSIBILITY_PROP).getValue()
				.toString();
		re.setReversible(value);
		return re;
	}

	@Override
	protected void rmo() {
		// MetabolicCompartment def = new MetabolicCompartment(getId(getRmo()),
		// "default", "default", ndom);
		MetabolicCompartment def = compartment(getRmo());
		try {
			ndom.addCompartment(def);
			Iterator<IDrawingNode> it = getRmo().getModel().drawingNodeIterator();
			
			while (it.hasNext()){
				IDrawingNode el =it.next(); 
				ICanvasAttribute att=(ICanvasAttribute) el.getAttribute();
				if (!(el instanceof ILabelNode)) {
					String ot = att.getObjectType().getName();
					if ("Compartment".equals(ot)) {
						compartment(def, el);
					} else if ("Process".equals(ot)) {
						process(def, el);
					} else if ("Compound".equals(ot)) {
						compound(def, el);
					}
				}
			}
		} catch (NdomException e) {
			report(e);
			// e.printStackTrace();
		}
	}

	protected MetabolicRelation production(ILinkEdge el) {
		MetabolicRelation rel = relation(el, ERelType.Production);
		ILinkAttribute att = (ILinkAttribute) el.getAttribute();
		rel.setRole(att.getTargetTerminus().getProperty(MetabolicNotationSyntaxService.ROLE_PROP).getValue().toString());
		setStoichiometry(el, att.getSourceTerminus(), rel);
		return rel;
	}

	protected MetabolicRelation activation(ILinkEdge el) {
		MetabolicRelation rel = relation(el, ERelType.Activation);
		ILinkAttribute att = (ILinkAttribute) el.getAttribute();
		rel.setRole(att.getTargetTerminus().getProperty(MetabolicNotationSyntaxService.ROLE_PROP).getValue().toString());
		// rel.setStoichiometry(getInt(el.getSrcPort().getPropertyByName("STOICH")
		// .getValue(), "Wrong stoichiometry\t"));
		setStoichiometry(el, att.getSourceTerminus(), rel);
		return rel;
	}

	protected void setStoichiometry(ILinkEdge el, ILinkTerminus p, MetabolicRelation rel) {
		IValidationRuleDefinition rd = reportBuilder.getRuleStore()
				.getRuleById(MetabolicRuleLoader.STOICH_ERROR_ID);
		IntPropertyRule r = (IntPropertyRule) loader.getRuleByDef(rd);
		r.setObject(p);
		r.setRefObject(el);
		if (r.validate(reportBuilder)) {
			rel.setStoichiometry(r.getValue());
		}
	}

	/**
	 * check validity of parameter string in the process node. Parameter string
	 * should contains set of name=value pairs separated by <code>;</code>.
	 * RegExp:<br>
	 * <code>^(\\s*\\w+\\s*=\\s*[0-9eE-+.]\\s*;)+$</code>
	 * 
	 * @param re
	 *            process node;
	 */
	protected void setReParam(IDrawingNode mapObject, MetabolicReaction re) {
		IValidationRuleDefinition rd = reportBuilder.getRuleStore()
				.getRuleById(MetabolicRuleLoader.RE_PARAM_ERROR_ID);
		RegexpPropertyRule r = (RegexpPropertyRule) loader.getRuleByDef(rd);
		r.setRefObject(mapObject);
		if (r.validate(reportBuilder)) {
			re.setParameters(r.getValue());
		}
	}

	protected MetabolicRelation inhibition(ILinkEdge el) {
		MetabolicRelation rel = relation(el, ERelType.Inhibition);
		ILinkAttribute att = (ILinkAttribute) el.getAttribute();
		rel.setRole(att.getTargetTerminus().getProperty(MetabolicNotationSyntaxService.ROLE_PROP).getValue().toString());
		setStoichiometry(el, att.getSourceTerminus(), rel);
		return rel;
	}

	protected MetabolicRelation catalysis(ILinkEdge el) {
		MetabolicRelation rel = relation(el, ERelType.Catalysis);
		ILinkAttribute att = (ILinkAttribute) el.getAttribute();
		rel.setRole(att.getTargetTerminus().getProperty(MetabolicNotationSyntaxService.ROLE_PROP).getValue().toString());
		setStoichiometry(el, att.getSourceTerminus(), rel);
		return rel;
	}

	protected MetabolicRelation consumption(ILinkEdge el) {
		MetabolicRelation rel = relation(el, ERelType.Consumption);
		ILinkAttribute att = (ILinkAttribute) el.getAttribute();
		rel.setRole(att.getSourceTerminus().getProperty(MetabolicNotationSyntaxService.ROLE_PROP).getValue().toString());
		setStoichiometry(el, att.getTargetTerminus(), rel);
		return rel;
	}

	protected MetabolicRelation relation(ILinkEdge el, ERelType type) {
		MetabolicRelation rel = new MetabolicRelation(getId(el), el, type);
		IAnnotatedObject att = (IAnnotatedObject) el.getAttribute();
		rel.setVarName(att.getProperty(MetabolicNotationSyntaxService.VAR_NAME_PROP).getValue().toString());
		return rel;
	}

	public void setReportBuilder(RuleValidationReportBuilder reportBuilder) {
		this.reportBuilder = reportBuilder;
		prepareParsingRules();
	}

	public RuleValidationReportBuilder getReportBuilder() {
		return reportBuilder;
	}

	/**
	 * Extracts rules from store. All rules, which could be used on parser step
	 * should be extracted and ready to use.
	 */
	private void prepareParsingRules() {

	}

	@Override
	protected void connectivity() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void error(String message) {
		// TODO Auto-generated method stub
		// super.error(message);
		throw new UnsupportedOperationException();
	}

//	/**
//	 * @deprecated
//	 * @param srcLoc
//	 * @param newLoc
//	 * @return
//	 */
//	protected double getAngle(Location srcLoc, Location newLoc) {
//		throw new UnsupportedOperationException();
//	}

	@Override
	protected double getDouble(String st, String message) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected int getInt(String st, String message) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getReport() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void report(NdomException e) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void warning(String message) {
		throw new UnsupportedOperationException();
	}

}

/*
 * $Log: NDOMFactory.java,v $ Revision 1.6 2008/07/15 11:14:32 smoodie
 * Refactored so code compiles with new Toolkit framework.
 * 
 * Revision 1.5 2008/06/27 13:22:15 radams adapt to validation servic einterface
 * changes
 * 
 * Revision 1.4 2008/06/20 22:48:19 radams imports
 * 
 * Revision 1.3 2008/06/09 13:26:29 asorokin Bug fixes for SBML export
 * 
 * Revision 1.2 2008/06/02 15:15:13 asorokin KineticLaw parameters parsing and
 * validation
 * 
 * Revision 1.1 2008/06/02 10:31:42 asorokin Reference to Service provider from
 * all Service interfaces
 */