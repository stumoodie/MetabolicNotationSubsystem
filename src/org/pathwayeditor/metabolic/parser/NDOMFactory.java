/*
  Licensed to the Court of the University of Edinburgh (UofE) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The UofE licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
*/
package org.pathwayeditor.metabolic.parser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pathwayeditor.businessobjects.drawingprimitives.ICanvasAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.ILabelNode;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkEdge;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkTerminus;
import org.pathwayeditor.businessobjects.drawingprimitives.IRootNode;
import org.pathwayeditor.businessobjects.drawingprimitives.IShapeNode;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotatedObject;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.INumberAnnotationProperty;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IPlainTextAnnotationProperty;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.metabolic.ndomAPI.ERelType;
import org.pathwayeditor.metabolic.ndomAPI.IModel;
import org.pathwayeditor.metabolic.validation.MetabolicRuleStore;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.NdomException;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IValidationRuleStore;
import org.pathwayeditor.notationsubsystem.toolkit.validation.RuleValidationReportBuilder;


/**
 * $Id$
 * 
 * @author Anatoly Sorokin
 * @date 12 May 2008
 * 
 */
public abstract class NDOMFactory extends AbstractNDOMParser {
	protected static final String ROLE_PROP = "ROLE";
	protected static final String VAR_NAME_PROP = "VarName";
	private static final String GO_TERM_PROP = "GO term";
	private static final String CID_PROP = "CID";
	private static final String CH_EBI_PROP = "ChEBI";
	private static final String IN_CHI_PROP = "InChI";
	private static final String PUB_CHEM_PROP = "PubChem";
	private static final String SMILES_PROP = "SMILES";
	private static final String EC_PROP = "EC";
	private static final String KINETIC_LAW_PROP = "KineticLaw";
	private static final String REVERSIBILITY_PROP = "Reversibility";
	private static final String IC_PROP = "IC";
	private static final String STOICH_PROP = "STOICH";
	private static final String PARAMETERS_PROP = "Parameters";
	
	protected MetabolicModel ndom;
	private RuleValidationReportBuilder reportBuilder;
	private IValidationRuleStore loader = MetabolicRuleStore.getInstance();
	private final Map<IValidationRuleDefinition, IParserRule> rules = new HashMap<IValidationRuleDefinition, IParserRule>();

	// private
	protected NDOMFactory(IRootNode rmo) {
		super(rmo);
		IParserRule r = new IntPropertyRule(this.loader.getRuleById(MetabolicRuleStore.STOICH_ERROR_ID), STOICH_PROP);
		rules.put(r.getRuleDef(), r);
		r = new DoublePropertyRule(this.loader.getRuleById(MetabolicRuleStore.IC_ERROR_ID), IC_PROP);
		rules.put(r.getRuleDef(), r);
		r = new RegexpPropertyRule(this.loader.getRuleById(MetabolicRuleStore.RE_PARAM_ERROR_ID), PARAMETERS_PROP,	"^(\\s*\\w+\\s*=\\s*[0-9eE\\-+.]+\\s*;)+$");
		((RegexpPropertyRule)r).setEmptyValid(true);
		rules.put(r.getRuleDef(), r);
	}

	protected NDOMFactory() {
		this(null);
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
	protected abstract void compartment(MetabolicCompartment parent, IShapeNode mapObject);

	protected abstract void compound(MetabolicCompartment compartment, IShapeNode mapObject);

	protected abstract void process(ModelObject parent, IShapeNode mapObject);

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

	protected MetabolicCompartment compartment(IShapeNode mapObject) {
		MetabolicCompartment compartment = new MetabolicCompartment(
				getId(mapObject), mapObject, ndom);
		IAnnotatedObject att = (IAnnotatedObject) mapObject.getAttribute();
		compartment.setGOTerm(att.getProperty(GO_TERM_PROP).getValue()
				.toString());
		compartment.setVolume(1.0d);
		return compartment;
	}

	protected MetabolicCompound compound(IShapeNode mapObject) {
		MetabolicCompound comp = new MetabolicCompound(getId(mapObject),
				mapObject, ndom);
		IAnnotatedObject att = (IAnnotatedObject) mapObject.getAttribute();
		comp.setCID(att.getProperty(CID_PROP)
				.getValue().toString());
		comp.setChEBIId(att.getProperty(
				CH_EBI_PROP).getValue()
				.toString());
		comp.setInChI(att.getProperty(
				IN_CHI_PROP).getValue()
				.toString());
		comp.setPubChemId(att.getProperty(
				PUB_CHEM_PROP).getValue()
				.toString());
		comp.setSmiles(att.getProperty(
				SMILES_PROP).getValue()
				.toString());
		setIC(mapObject, comp);
		return comp;
	}

	private void setIC(IShapeNode mapObject, MetabolicCompound comp) {
		IValidationRuleDefinition defn = this.loader.getRuleById(MetabolicRuleStore.IC_ERROR_ID);
		IParserRule r = this.rules.get(defn);
		r.setObject(mapObject.getAttribute());
		r.setRefObject(mapObject);
		if (r.validate(reportBuilder)) {
			INumberAnnotationProperty numberProp = (INumberAnnotationProperty)mapObject.getAttribute().getProperty(IC_PROP);
			comp.setIC(numberProp.getValue().doubleValue());
		}
	}

	@Override
	protected void ndom() {
		String name = getRmo().getModel().getCanvas().getName();
		ndom = new MetabolicModel(getId(getRmo()), name, name);// AbstractNDOMParser.getASCIIName(name));
	}

	protected MetabolicReaction process(IShapeNode mapObject) {
		MetabolicReaction re = new MetabolicReaction(getId(mapObject),
				mapObject, ndom);
		IAnnotatedObject att = mapObject.getAttribute();
		re.setECNumber(att.getProperty(EC_PROP).getValue().toString());
		IPlainTextAnnotationProperty prop = (IPlainTextAnnotationProperty)att.getProperty(KINETIC_LAW_PROP); 
		re.setKineticLaw(prop.getValue().toString());
		setReParam(mapObject, prop, re);
		String value = att.getProperty(REVERSIBILITY_PROP).getValue()
				.toString();
		re.setReversible(value);
		return re;
	}

	@Override
	protected void rmo() {
		 MetabolicCompartment def = new MetabolicCompartment(getId(getRmo()), "default", "default", ndom);
//		MetabolicCompartment def = compartment(getRmo());
		try {
			ndom.addCompartment(def);
			Iterator<IShapeNode> it = getRmo().getModel().shapeNodeIterator();
			
			while (it.hasNext()){
				IShapeNode el =it.next(); 
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
		rel.setRole(att.getTargetTerminus().getProperty(ROLE_PROP).getValue().toString());
		setStoichiometry(el, att.getSourceTerminus(), rel);
		return rel;
	}

	protected MetabolicRelation activation(ILinkEdge el) {
		MetabolicRelation rel = relation(el, ERelType.Activation);
		ILinkAttribute att = (ILinkAttribute) el.getAttribute();
		rel.setRole(att.getTargetTerminus().getProperty(ROLE_PROP).getValue().toString());
		// rel.setStoichiometry(getInt(el.getSrcPort().getPropertyByName("STOICH")
		// .getValue(), "Wrong stoichiometry\t"));
		setStoichiometry(el, att.getSourceTerminus(), rel);
		return rel;
	}

	protected void setStoichiometry(ILinkEdge el, ILinkTerminus p, MetabolicRelation rel) {
		IValidationRuleDefinition defn = this.loader.getRuleById(MetabolicRuleStore.STOICH_ERROR_ID);
		IParserRule r = this.rules.get(defn);
		r.setObject(p);
		r.setRefObject(el);
		if (r.validate(reportBuilder)) {
			INumberAnnotationProperty prop = (INumberAnnotationProperty)rules.put(r.getRuleDef(), r);
			BigDecimal val = prop.getValue();
			rel.setStoichiometry(val.intValue());
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
	protected void setReParam(IShapeNode mapObject, IPlainTextAnnotationProperty prop, MetabolicReaction re) {
		IValidationRuleDefinition rd = loader.getRuleById(MetabolicRuleStore.RE_PARAM_ERROR_ID);
		IParserRule r = rules.get(rd);
		r.setRefObject(mapObject);
		if (r.validate(reportBuilder)) {
			re.setParameters(prop.getValue());
		}
	}

	protected MetabolicRelation inhibition(ILinkEdge el) {
		MetabolicRelation rel = relation(el, ERelType.Inhibition);
		ILinkAttribute att = (ILinkAttribute) el.getAttribute();
		rel.setRole(att.getTargetTerminus().getProperty(ROLE_PROP).getValue().toString());
		setStoichiometry(el, att.getSourceTerminus(), rel);
		return rel;
	}

	protected MetabolicRelation catalysis(ILinkEdge el) {
		MetabolicRelation rel = relation(el, ERelType.Catalysis);
		ILinkAttribute att = (ILinkAttribute) el.getAttribute();
		rel.setRole(att.getTargetTerminus().getProperty(ROLE_PROP).getValue().toString());
		setStoichiometry(el, att.getSourceTerminus(), rel);
		return rel;
	}

	protected MetabolicRelation consumption(ILinkEdge el) {
		MetabolicRelation rel = relation(el, ERelType.Consumption);
		ILinkAttribute att = (ILinkAttribute) el.getAttribute();
		rel.setRole(att.getSourceTerminus().getProperty(ROLE_PROP).getValue().toString());
		setStoichiometry(el, att.getTargetTerminus(), rel);
		return rel;
	}

	protected MetabolicRelation relation(ILinkEdge el, ERelType type) {
		MetabolicRelation rel = new MetabolicRelation(getId(el), el, type);
		IAnnotatedObject att = (IAnnotatedObject) el.getAttribute();
		rel.setVarName(att.getProperty(VAR_NAME_PROP).getValue().toString());
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