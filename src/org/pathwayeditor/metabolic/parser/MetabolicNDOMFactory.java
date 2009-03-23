package org.pathwayeditor.metabolic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingNode;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkEdge;
import org.pathwayeditor.businessobjects.drawingprimitives.IRootNode;
import org.pathwayeditor.businessobjects.drawingprimitives.IShapeNode;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.Location;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;
import org.pathwayeditor.metabolic.ndomAPI.ERelType;
import org.pathwayeditor.metabolic.ndomAPI.IReaction;
import org.pathwayeditor.metabolic.validation.MetabolicRuleStore;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.GeometryUtils;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.NdomException;


public class MetabolicNDOMFactory extends NDOMFactory {
	private Map<IShapeNode, MetabolicMolecule> shape2Molecule = new HashMap<IShapeNode, MetabolicMolecule>();
	Map<MetabolicReaction, IShapeNode> reaction2Shape = new HashMap<MetabolicReaction, IShapeNode>();
	LinkedList<Map<String, MetabolicCompound>> name2Compound = new LinkedList<Map<String, MetabolicCompound>>();
	List<ILinkEdge> prodLinks;

	public MetabolicNDOMFactory(IRootNode rmo) {
		super(rmo);
	}

	public MetabolicNDOMFactory() {
		super();
	}

	@Override
	protected void semanticValidation() {
		checkOrphanCompounds();
		checkOrphanReactions();
	}

	void checkOrphanReactions() {
		for (IReaction r : ndom.getReactionList()) {
			if ((r.getSubstrateList().size() == 0)) {

				IValidationRuleDefinition rd = getReportBuilder()
						.getRuleStore().getRuleById(
								MetabolicRuleStore.ORPHAN_PROCESS_ERROR_ID);
				IShapeNode el = reaction2Shape.get(r);
				getReportBuilder().setRuleFailed(el, rd.getRuleNumber(), "Reaction has no substrates");
			}
			if ((r.getProductList().size() == 0)) {

				IValidationRuleDefinition rd = getReportBuilder()
						.getRuleStore().getRuleById(
								MetabolicRuleStore.ORPHAN_PROCESS_ERROR_ID);
				IShapeNode el = reaction2Shape.get(r);
				getReportBuilder().setRuleFailed(el, rd.getRuleNumber(), "Reaction has no products");
			}
		}
	}

	void checkOrphanCompounds() {
//		ModelProcessor proc=new ModelProcessor(ndom);
		for(Entry<IShapeNode,MetabolicMolecule> e:shape2Molecule.entrySet()){
			IShapeNode el=e.getKey();
			MetabolicMolecule c=e.getValue();
			if((c.getInhibitoryRelationList().size()+c.getActivatoryRelationList().size()+c.getCatalyticRelationList().size()+c.getSinkList().size()+c.getSourceList().size())==0){
				IValidationRuleDefinition rd = getReportBuilder()
						.getRuleStore().getRuleById(
								MetabolicRuleStore.ORPHAN_COMPOUND_ERROR_ID);
				getReportBuilder().setRuleFailed(el, rd.getRuleNumber(), "Reaction has no products");
				
			}
		}

	}

	protected void rmo() {
		name2Compound.addFirst(new HashMap<String, MetabolicCompound>());
		super.rmo();
		name2Compound.removeFirst();
	}

	void processProdLinks(MetabolicReaction r) {
		if (r.isReversible()) {
			IShapeNode s = reaction2Shape.get(r);
			// Set<ILink> substr = new HashSet<ILink>();
			// Set<ILink> prod = new HashSet<ILink>();
			Location srcLoc = null;
			// double srcAngle;
			for (ILinkEdge l : prodLinks) {
				if (srcLoc == null) {
					// first link
					srcLoc = GeometryUtils.getSrcLocation(l, s);
					substrate(l, r);
				} else {
					// all consequitive links
					Location newLoc = GeometryUtils.getSrcLocation(l, s);
					if (GeometryUtils.getAngle(srcLoc, newLoc) > 0) {
						substrate(l, r);
					} else {
						productsIrr(l, r);
					}
				}
			}
		}
		prodLinks = null;
	}

	@Override
	protected void products(ILinkEdge el, MetabolicReaction r) {
		if (r.isReversible()) {
			prodLinks.add(el);
		} else {
			productsIrr(el, r);
		}
	}

	private void productsIrr(ILinkEdge el, MetabolicReaction r) {
		// TODO separate substrates from products
		MetabolicRelation rel = production(el);
		r.addProduct(rel);
		IShapeNode targ = el.getTargetShape();
		MetabolicMolecule mol = shape2Molecule.get(targ);
		if (mol == null) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.NOT_REGISTERED_ERROR_ID);
			getReportBuilder()
					.setRuleFailed(el, rd.getRuleNumber(),
							"Molecule in Production relation is not registered in the model");
			// error("Molecule in Production relation is not registered in the
			// model");
		}
		try {
			mol.addSource(rel);
			String vn = rel.getVarName();
			String id = mol.getId();
			updateKL(r, vn, id);

		} catch (NdomException e) {
			report(e);
			e.printStackTrace();
		}
	}

	void updateKL(MetabolicReaction r, String vn, String id) {
		String kineticLaw = r.getKineticLaw();
		if (kineticLaw != null) {
			kineticLaw = kineticLaw.replaceAll(vn, id);
			r.setKineticLaw(kineticLaw);
		}
	}

	@Override
	protected void substrate(ILinkEdge el, MetabolicReaction r) {
		IShapeNode targ =null;
		MetabolicRelation rel = null;
		if (r.isReversible()){
				if( "Consume".equals(el.getAttribute().getObjectType().getName())) {
			// error("Consumption link to reversible reaction");
			IValidationRuleDefinition rd = getReportBuilder()
					.getRuleStore()
					.getRuleById(
							MetabolicRuleStore.CONSUMPTION_TO_REVERSIBLE_ERROR_ID);
			getReportBuilder().setRuleFailed(el, rd.getRuleNumber(),
					"Consumption link to reversible reaction");
			return;
			}else{
				 rel = consumptionRev(el);
				 targ = el.getTargetShape();
			}
		}else{
		 rel = consumption(el);
		 targ = el.getSourceShape();
		}
		 r.addSubstrate(rel);
		MetabolicMolecule mol = shape2Molecule.get(targ);
		if (mol == null) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.NOT_REGISTERED_ERROR_ID);
			getReportBuilder()
					.setRuleFailed(el, rd.getRuleNumber(),
							"Molecule in Consumption relation is not registered in the model");
			// error("Molecule in Consumption relation is not registered in the
			// model");
		}
		try {
			mol.addSink(rel);
			String vn = rel.getVarName();
			String id = mol.getId();
			updateKL(r, vn, id);
		} catch (NdomException e) {
			report(e);
			e.printStackTrace();
		}
	}

	private MetabolicRelation consumptionRev(ILinkEdge el) {
		MetabolicRelation rel = relation(el, ERelType.Consumption);
		ILinkAttribute att = (ILinkAttribute) el.getAttribute();
		rel.setRole(att.getSourceTerminus().getProperty(ROLE_PROP).getValue().toString());
		setStoichiometry(el, att.getTargetTerminus(), rel);
		return rel;
	}

	@Override
	protected void activate(ILinkEdge el, MetabolicReaction r) {
		MetabolicRelation rel = activation(el);
		if (r == null) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.NOT_REGISTERED_ERROR_ID);
			getReportBuilder()
					.setRuleFailed(el, rd.getRuleNumber(),
							"Reaction for Activation relation is not registered in the model");
			// error("Reaction for Activation relation is not registered in the
			// model");
		}
		r.addActivator(rel);
		IShapeNode src = el.getSourceShape();
		MetabolicMolecule mol = shape2Molecule.get(src);
		if (mol == null) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.NOT_REGISTERED_ERROR_ID);
			getReportBuilder()
					.setRuleFailed(el, rd.getRuleNumber(),
							"Molecule for Activation relation is not registered in the model");
		}
		try {
			mol.addActivatoryRelation(rel);
		} catch (NdomException e) {
			report(e);
			e.printStackTrace();
		}
	}

	@Override
	protected void inhibit(ILinkEdge el, MetabolicReaction r) {
		MetabolicRelation rel = inhibition(el);
		if (r == null) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.NOT_REGISTERED_ERROR_ID);
			getReportBuilder()
					.setRuleFailed(el, rd.getRuleNumber(),
							"Reaction for Inhibiton relation is not registered in the model");
		}
		r.addInhibitor(rel);
		IShapeNode src = el.getSourceShape();
		MetabolicMolecule mol = shape2Molecule.get(src);
		if (mol == null) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.NOT_REGISTERED_ERROR_ID);
			getReportBuilder()
					.setRuleFailed(el, rd.getRuleNumber(),
							"Molecule for Inhibiton relation is not registered in the model");
		}
		try {
			mol.addInhibitoryRelation(rel);
		} catch (NdomException e) {
			report(e);
			e.printStackTrace();
		}
	}

	@Override
	protected void catalysis(ILinkEdge el, MetabolicReaction r) {
		MetabolicRelation rel = catalysis(el);
		r.addCatalyst(rel);
		IShapeNode src = el.getSourceShape();
		MetabolicMolecule mol = shape2Molecule.get(src);
		if (mol == null) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.NOT_REGISTERED_ERROR_ID);
			getReportBuilder()
					.setRuleFailed(el, rd.getRuleNumber(),
							"Molecule for Catalysis relation is not registered in the model");
		}
		try {
			mol.addCatalyticRelation(rel);
		} catch (NdomException e) {
			report(e);
			e.printStackTrace();
		}
	}



	@Override
	protected void compound(MetabolicCompartment compartment, IShapeNode mapObject) {
		MetabolicCompound comp = compound(mapObject);
		if (name2Compound.getFirst().containsKey(comp.getName())) {
			MetabolicCompound comp1 = name2Compound.getFirst().get(
					comp.getName());
			compareCompounds(comp1, comp, compartment, mapObject);
			comp = comp1;
		} else {
			name2Compound.getFirst().put(comp.getName(), comp);
			compartment.addCompound(comp);
		}
		comp.setCloneNumber(comp.getCloneNumber() + 1);
		shape2Molecule.put((IShapeNode) mapObject, comp);
	}

	/**
	 * Check external references for two compound definitions. Validate that two
	 * compounds, having the same name have the same set of references to
	 * external databases.
	 * 
	 * @param comp1
	 *            reference compound
	 * @param comp
	 *            compound under creation
	 */
	private void compareCompounds(MetabolicCompound comp1,
			MetabolicCompound comp, ModelObject parent, IDrawingNode mapObject) {
		String name = comp1.getASCIIName();
		// TODO replace with rules
		if (!comp1.getDescription().equals(comp.getDescription())) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.COMP_DEF_ERROR_ID);
			getReportBuilder().setRuleFailed(
					mapObject,
					rd.getRuleNumber(),
					"Compound " + name + "has two sets of Descriptions: '"
							+ comp1.getDescription() + "' and '"
							+ comp.getDescription() + "'");
		}
		if (!comp1.getDetailedDescription().equals(
				comp.getDetailedDescription())) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.COMP_DEF_ERROR_ID);
			getReportBuilder().setRuleFailed(
					mapObject,
					rd.getRuleNumber(),
					"Compound " + name
							+ "has two sets of DetailedDescription: '"
							+ comp1.getDetailedDescription() + "' and '"
							+ comp.getDetailedDescription() + "'");
		}
		if (!comp1.getCID().equals(comp.getCID())) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.COMP_DEF_ERROR_ID);
			getReportBuilder().setRuleFailed(
					mapObject,
					rd.getRuleNumber(),
					"Compound " + name + "has two sets of CID: '"
							+ comp1.getCID() + "' and '" + comp.getCID() + "'");
		}
		if (!comp1.getChEBIId().equals(comp.getChEBIId())) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.COMP_DEF_ERROR_ID);
			getReportBuilder().setRuleFailed(
					mapObject,
					rd.getRuleNumber(),
					"Compound " + name + "has two sets of ChEBIId: '"
							+ comp1.getChEBIId() + "' and '"
							+ comp.getChEBIId() + "'");
		}
		if (!comp1.getInChI().equals(comp.getInChI())) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.COMP_DEF_ERROR_ID);
			getReportBuilder().setRuleFailed(
					mapObject,
					rd.getRuleNumber(),
					"Compound " + name + "has two sets of InChI: '"
							+ comp1.getInChI() + "' and '" + comp.getInChI()
							+ "'");
		}
		if (comp1.getIC() != (comp.getIC())) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.COMP_DEF_ERROR_ID);
			getReportBuilder().setRuleFailed(
					mapObject,
					rd.getRuleNumber(),
					"Compound " + name + "has two sets of getIC: '"
							+ comp1.getIC() + "' and '" + comp.getIC() + "'");
		}
		if (!comp1.getPubChemId().equals(comp.getPubChemId())) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.COMP_DEF_ERROR_ID);
			getReportBuilder().setRuleFailed(
					mapObject,
					rd.getRuleNumber(),
					"Compound " + name + "has two sets of PubChemId: '"
							+ comp1.getPubChemId() + "' and '"
							+ comp.getPubChemId() + "'");
		}
		if (!comp1.getSmiles().equals(comp.getSmiles())) {
			IValidationRuleDefinition rd = getReportBuilder().getRuleStore()
					.getRuleById(MetabolicRuleStore.COMP_DEF_ERROR_ID);
			getReportBuilder().setRuleFailed(
					mapObject,
					rd.getRuleNumber(),
					"Compound " + name + "has two sets of Smiles: '"
							+ comp1.getSmiles() + "' and '" + comp.getSmiles()
							+ "'");
		}
	}

	@Override
	protected void compartment(MetabolicCompartment parent, IShapeNode mapObject) {
		MetabolicCompartment compartment = compartment(mapObject);
		try {
			name2Compound.addFirst(new HashMap<String, MetabolicCompound>());
			parent.addChildCompartment(compartment);
//			List<IDrawingNode> ch = mapObject.getChildren();
//			for (IMapObject el : ch) {
			Iterator<IShapeNode> it = mapObject.getSubModel().shapeNodeIterator();
			
			while (it.hasNext()){
				IShapeNode el =it.next(); 
				String ot = el.getObjectType().getName();
				if ("Compartment".equals(ot)) {
					compartment(compartment, el);
				} else if ("Process".equals(ot)) {
					process(compartment, el);
				} else if ("Compound".equals(ot)) {
					compound(compartment, el);
				}
			}
			name2Compound.removeFirst();
		} catch (NdomException e) {
			report(e);
			e.printStackTrace();
		}

	}

	@Override
	protected void connectivity() {
		for (MetabolicReaction r : reaction2Shape.keySet()) {
			IShapeNode s = reaction2Shape.get(r);
			prodLinks = new ArrayList<ILinkEdge>();
			Iterator<ILinkEdge> its = s.sourceLinkIterator();//getSubModel().SourceLinks();
			if (s.getNumSourceLinks() == 0) {
			}
			while (its.hasNext()) {
				ILinkEdge el = its.next();
				String ot = el.getAttribute().getObjectType().getName();
				if ("Produce".equals(ot)) {
					products(el, r);
				}
			}
			its = s.targetLinkIterator();
			int nsubstr = 0;
			while (its.hasNext()) {
				ILinkEdge el = its.next();
				String ot = el.getAttribute().getObjectType().getName();
				if ("Consume".equals(ot)) {
					substrate(el, r);
					nsubstr++;
				} else if ("Activation".equals(ot)) {
					activate(el, r);
				} else if ("Catalysis".equals(ot)) {
					catalysis(el, r);
				} else if ("Inhibition".equals(ot)) {
					inhibit(el, r);
				}
			}
			if (nsubstr == 0 && !r.isReversible()) {
				// TODO replace with rules
				IValidationRuleDefinition rd = getReportBuilder()
						.getRuleStore().getRuleById(
								MetabolicRuleStore.RE_DEF_ERROR_ID);
				getReportBuilder().setRuleFailed(
						s,
						rd.getRuleNumber(),
						"Reaction is not reversible and do not have substrates \t"
								+ s.getIndex());
			}
			processProdLinks(r);
		}
	}

	@Override
	protected void process(ModelObject parent, IShapeNode mapObject) {
		MetabolicReaction re = process(mapObject);
		// checkParameters(re);
		ndom.addReaction(re);
		reaction2Shape.put(re, (IShapeNode) mapObject);
	}


}

/*
 * $Log: MetabolicNDOMFactory.java,v $ Revision 1.8 2008/07/15 11:14:32 smoodie
 * Refactored so code compiles with new Toolkit framework.
 * 
 * Revision 1.7 2008/06/24 10:07:40 radams moved rule builder upt oAbstractNDOM
 * parsers
 * 
 * Revision 1.6 2008/06/23 14:42:35 radams added report builder to parser
 * 
 * Revision 1.5 2008/06/23 14:22:34 radams added report builder to parser
 * 
 * Revision 1.4 2008/06/20 22:48:19 radams imports
 * 
 * Revision 1.3 2008/06/09 13:26:29 asorokin Bug fixes for SBML export
 * 
 * Revision 1.2 2008/06/02 15:14:31 asorokin KineticLaw parameters parsing and
 * validation
 * 
 * Revision 1.1 2008/06/02 10:31:42 asorokin Reference to Service provider from
 * all Service interfaces
 * 
 */