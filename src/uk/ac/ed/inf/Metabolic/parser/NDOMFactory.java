package uk.ac.ed.inf.Metabolic.parser;

import java.util.List;

import org.pathwayeditor.businessobjectsAPI.IContextProperty;
import org.pathwayeditor.businessobjectsAPI.ILink;
import org.pathwayeditor.businessobjectsAPI.IMapObject;
import org.pathwayeditor.businessobjectsAPI.IRootMapObject;
import org.pathwayeditor.contextadapter.toolkit.ndom.AbstractNDOMParser;
import org.pathwayeditor.contextadapter.toolkit.ndom.ModelObject;

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

	// private
	public NDOMFactory(IRootMapObject rmo) {
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
			IMapObject mapObject);

	protected abstract void compound(MetabolicCompartment compartment,
			IMapObject mapObject);

	protected abstract void compound(MetabolicMacromolecule m,
			IMapObject mapObject);

	protected abstract void macromolecule(MetabolicCompartment comaprtment,
			IMapObject mapObject);

	protected abstract void macromolecule(MetabolicMacromolecule parent,
			IMapObject mapObject);

	protected abstract void process(ModelObject parent, IMapObject mapObject);

	/**
	 * Creates Activation relation in the model. That method creates activation relation
	 * by invocation of {@link #activation(ILink)} method and registered this relation with 
	 * reaction <code>r</code> and source shape,obtained from <code>el</code>.
	 * Preconditions:<br>
	 * <ul><li>Reaction is not <code>NULL</code></li>
	 * <li> link <code>el</code> has non-null and registered with the model source shape
	 * </li></ul>
	 * 	Postconditions:<br>
	 * <ul><li>Fully defined valid Relation object of type {@link ERelType#Activation}</li>
	 * </ul> 
	 * @param el non-null Link object to convert to Relation
	 * @param r non-null reaction to register new Relation with
	 */
	protected abstract void activate(ILink el, MetabolicReaction r);

	/**
	 * Creates Catalysis relation in the model. That method creates catalysis relation
	 * by invocation of {@link #catalysis(ILink)} method and registered this relation with 
	 * reaction <code>r</code> and source shape,obtained from <code>el</code>.
	 * Preconditions:<br>
	 * <ul><li>Reaction is not <code>NULL</code></li>
	 * <li> link <code>el</code> has non-null and registered with the model source shape
	 * </li></ul>
	 * 	Postconditions:<br>
	 * <ul><li>Fully defined valid Relation object of type {@link ERelType#Catalysis}</li>
	 * </ul> 
	 * @param el non-null Link object to convert to Relation
	 * @param r non-null reaction to register new Relation with
	 */
protected abstract void catalysis(ILink el, MetabolicReaction r);

	/**
	 * Creates inhibitory relation in the model. That method creates inhibitory relation
	 * by invocation of {@link #inhibition(ILink)} method and registered this relation with 
	 * reaction <code>r</code> and source shape,obtained from <code>el</code>.
	 * Preconditions:<br>
	 * <ul><li>Reaction is not <code>NULL</code></li>
	 * <li> link <code>el</code> has non-null and registered with the model source shape
	 * </li></ul>
	 * 	Postconditions:<br>
	 * <ul><li>Fully defined valid Relation object of type {@link ERelType#Inhibition}</li>
	 * </ul> 
	 * @param el non-null Link object to convert to Relation
	 * @param r non-null reaction to register new Relation with
	 */
	protected abstract void inhibit(ILink el, MetabolicReaction r);

	/**
	 * Creates Production relation in the model. That method creates production relation
	 * by invocation of {@link #production(ILink)} method and registered this relation with 
	 * reaction <code>r</code> and source shape,obtained from <code>el</code>.
	 * Preconditions:<br>
	 * <ul><li>Reaction is not <code>NULL</code></li>
	 * <li> link <code>el</code> has non-null and registered with the model source shape
	 * </li></ul>
	 * 	Postconditions:<br>
	 * <ul><li>Fully defined valid Relation object of type {@link ERelType#Production}</li>
	 * </ul> 
	 * @param el non-null Link object to convert to Relation
	 * @param r non-null reaction to register new Relation with
	 */
	protected abstract void products(ILink el, MetabolicReaction r);

	/**
	 * Creates Consumption relation in the model. That method creates consumption relation
	 * by invocation of {@link #consumption(ILink)} method and registered this relation with 
	 * reaction <code>r</code> and source shape,obtained from <code>el</code>.
	 * Preconditions:<br>
	 * <ul><li>Reaction is not <code>NULL</code></li>
	 * <li> link <code>el</code> has non-null and registered with the model source shape
	 * </li></ul>
	 * 	Postconditions:<br>
	 * <ul><li>Fully defined valid Relation object of type {@link ERelType#Consumption}</li>
	 * </ul> 
	 * @param el non-null Link object to convert to Relation
	 * @param r non-null reaction to register new Relation with
	 */
	protected abstract void substrate(ILink el, MetabolicReaction r);

	protected MetabolicCompartment compartment(IMapObject mapObject) {
		MetabolicCompartment compartment = new MetabolicCompartment(
				getId(mapObject), mapObject.getName().getHTML(),
				AbstractNDOMParser.getASCIIName(mapObject), ndom);
		compartment.setDescription(mapObject.getDescription().getHTML());
		compartment.setDetailedDescription(mapObject.getDetailedDescription()
				.getHTML());
		compartment
				.setGOTerm(mapObject.getPropertyByName("GO term").getValue());
		compartment.setVolume(1.0d);
		return compartment;
	}

	protected MetabolicCompound compound(IMapObject mapObject) {
		MetabolicCompound comp = new MetabolicCompound(getId(mapObject),
				mapObject.getName().getHTML(), AbstractNDOMParser
						.getASCIIName(mapObject));
		comp.setDescription(mapObject.getDescription().getHTML());
		comp.setDetailedDescription(mapObject.getDetailedDescription()
				.getHTML());
		comp.setCID(mapObject.getPropertyByName("CID").getValue());
		comp.setChEBIId(mapObject.getPropertyByName("ChEBI").getValue());
		comp.setIC(getDouble(mapObject.getPropertyByName("IC").getValue(),
				"Wrong IC"));
		comp.setInChI(mapObject.getPropertyByName("InChI").getValue());
		comp.setPubChemId(mapObject.getPropertyByName("PubChem").getValue());
		comp.setSmiles(mapObject.getPropertyByName("SMILES").getValue());
		return comp;
	}

	protected MetabolicMacromolecule macromolecule(IMapObject mapObject) {
		MetabolicMacromolecule m = new MetabolicMacromolecule(getId(mapObject),
				mapObject.getName().getHTML(), AbstractNDOMParser
						.getASCIIName(mapObject));
		m.setDescription(mapObject.getDescription().getHTML());
		m.setDetailedDescription(mapObject.getDetailedDescription().getHTML());
		m.setGOTerm(mapObject.getPropertyByName("GO term").getValue());
		m.setUniProt(mapObject.getPropertyByName("UniProt").getValue());
		List<IMapObject> ch = mapObject.getChildren();
		for (IMapObject el : ch) {
			String ot = el.getObjectType().getTypeName();
			if ("Compound".equals(ot)) {
				compound(m, el);
			} else if ("Macromolecule".equals(ot)) {
				macromolecule(m, el);
			}
		}
		return m;
	}

	@Override
	protected void ndom() {
		ndom = new MetabolicModel(getId(getRmo()), getRmo().getParentMap()
				.getName(), getRmo().getParentMap().getName());// AbstractNDOMParser.getASCIIName(getRmo()));
		ndom.setDescription(getRmo().getParentMap().getDescription());
		ndom.setDetailedDescription("");
	}

	protected MetabolicReaction process(IMapObject mapObject) {
		MetabolicReaction re = new MetabolicReaction(getId(mapObject),
				mapObject.getName().getHTML(), AbstractNDOMParser
						.getASCIIName(mapObject));
		re.setDescription(mapObject.getDescription().getHTML());
		re.setDetailedDescription(mapObject.getDetailedDescription().getHTML());
		re.setECNumber(mapObject.getPropertyByName("EC").getValue());
		re.setKineticLaw(mapObject.getPropertyByName("KineticLaw").getValue());
		re.setParameters(mapObject.getPropertyByName("Parameters").getValue());
		IContextProperty revProp = mapObject.getPropertyByName("Reversibility");
		String value = revProp.getValue();
		re.setReversible(value);
		return re;
	}

	@Override
	protected void rmo() {
//		MetabolicCompartment def = new MetabolicCompartment(getId(getRmo()),
//				"default", "default", ndom);
		MetabolicCompartment def = compartment(getRmo());
		try {
			ndom.addCompartment(def);
			List<IMapObject> ch = getRmo().getChildren();
			for (IMapObject el : ch) {
				String ot = el.getObjectType().getTypeName();
				if ("Compartment".equals(ot)) {
					compartment(def, el);
				} else if ("Process".equals(ot)) {
					process(def, el);
				} else if ("Compound".equals(ot)) {
					compound(def, el);
				} else if ("Macromolecule".equals(ot)) {
					macromolecule(def, el);
				}
			}
		} catch (NdomException e) {
			report(e);
			// e.printStackTrace();
		}
	}

	protected MetabolicRelation production(ILink el) {
		MetabolicRelation rel = relation(el, ERelType.Production);
		rel.setRole(el.getTargetPort().getPropertyByName("ROLE").getValue());
		rel.setStoichiometry(getInt(el.getSrcPort().getPropertyByName("STOICH")
				.getValue(), "Wrong stoichiometry\t"));
		return rel;
	}

	protected MetabolicRelation activation(ILink el) {
		MetabolicRelation rel = relation(el, ERelType.Activation);
		rel.setRole(el.getTargetPort().getPropertyByName("ROLE").getValue());
		rel.setStoichiometry(getInt(el.getSrcPort().getPropertyByName("STOICH")
				.getValue(), "Wrong stoichiometry\t"));
		return rel;
	}

	protected MetabolicRelation inhibition(ILink el) {
		MetabolicRelation rel = relation(el, ERelType.Inhibition);
		rel.setRole(el.getTargetPort().getPropertyByName("ROLE").getValue());
		rel.setStoichiometry(getInt(el.getSrcPort().getPropertyByName("STOICH")
				.getValue(), "Wrong stoichiometry\t"));
		return rel;
	}

	protected MetabolicRelation catalysis(ILink el) {
		MetabolicRelation rel = relation(el, ERelType.Catalysis);
		rel.setRole(el.getTargetPort().getPropertyByName("ROLE").getValue());
		rel.setStoichiometry(getInt(el.getSrcPort().getPropertyByName("STOICH")
				.getValue(), "Wrong stoichiometry\t"));
		return rel;
	}

	protected MetabolicRelation consumption(ILink el) {
		MetabolicRelation rel = relation(el, ERelType.Consumption);
		rel.setRole(el.getSrcPort().getPropertyByName("ROLE").getValue());
		rel.setStoichiometry(getInt(el.getTargetPort().getPropertyByName(
				"STOICH").getValue(), "Wrong stoichiometry\t"));
		return rel;
	}

	protected MetabolicRelation relation(ILink el, ERelType type) {
		MetabolicRelation rel = new MetabolicRelation(getId(el), "", "", type);
		rel.setDescription(el.getDescription().getHTML());
		rel.setDetailedDescription(el.getDetailedDescription().getHTML());
		rel.setVarName(el.getPropertyByName("VarName").getValue());
		return rel;
	}

}

/*
 * $Log$
 * Revision 1.5  2008/06/27 13:22:15  radams
 * adapt to validation servic einterface changes
 *
 * Revision 1.4  2008/06/20 22:48:19  radams
 * imports
 *
 * Revision 1.3  2008/06/09 13:26:29  asorokin
 * Bug fixes for SBML export
 *
 * Revision 1.2  2008/06/02 15:15:13  asorokin
 * KineticLaw parameters parsing and validation
 *
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */