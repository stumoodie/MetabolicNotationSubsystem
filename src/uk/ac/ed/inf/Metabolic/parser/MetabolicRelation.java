package uk.ac.ed.inf.Metabolic.parser;

import org.pathwayeditor.contextadapter.toolkit.ndom.ModelObject;

import uk.ac.ed.inf.Metabolic.ndomAPI.ERelType;
import uk.ac.ed.inf.Metabolic.ndomAPI.IMolecule;
import uk.ac.ed.inf.Metabolic.ndomAPI.IReaction;
import uk.ac.ed.inf.Metabolic.ndomAPI.IRelation;

public class MetabolicRelation extends ModelObject implements IRelation {

	private final ERelType type;

	private MetabolicReaction reaction;
	private String role;
	private String varName;
	private int stoichiometry;
	private MetabolicMolecule molecule;

	public MetabolicRelation(String id, String name, String asciiName,
			ERelType type) {
		super(id,name,asciiName);
		this.type = type;
	}

	public MetabolicRelation(String description, String detailedDescription,
			String id, String name, String asciiName, ERelType type,
			MetabolicReaction reaction, String role, int stoichiometry,
			MetabolicMolecule molecule) {
		super(id,name,asciiName);
		this.type = type;
		this.reaction = reaction;
		this.role = role;
		this.stoichiometry = stoichiometry;
		this.molecule = molecule;
	}

	public IMolecule getMolecule() {
		return molecule;
	}

	public IReaction getReaction() {
		return reaction;
	}

	public String getRole() {
		return role;
	}

	public int getStoichiometry() {
		return stoichiometry;
	}

	public ERelType getType() {
		return type;
	}

	void setReaction(MetabolicReaction reaction) {
		if (this.reaction != null) {
			switch (type) {
			case Catalysis:
				this.reaction.removeCatalyst(this);
				break;
			case Activation:
				this.reaction.removeActivator(this);
				break;
			case Inhibition:
				this.reaction.removeInhibitor(this);
				break;
			case Consumption:
				this.reaction.removeSubstrate(this);
				break;
			case Production:
				this.reaction.removeProduct(this);
			default: // throw new NDOMFactory.NdomException("Unknown realtion
				// type");
			}

		}
		this.reaction = reaction;
	}

	void setRole(String role) {
		this.role = role;
	}

	void setStoichiometry(int stoichiometry) {
		this.stoichiometry = stoichiometry;
	}

	void setMolecule(MetabolicMolecule molecule) {
		if (this.molecule != null) {
			switch (type) {
			case Catalysis:
				this.molecule.removeCatalyticRelation(this);
				break;
			case Activation:
				this.molecule.removeActivatoryRelation(this);
				break;
			case Inhibition:
				this.molecule.removeInhibitoryRelation(this);
				break;
			case Consumption:
				this.molecule.removeSink(this);
				break;
			case Production:
				this.molecule.removeSource(this);
			default: // throw new NDOMFactory.NdomException("Unknown realtion
				// type");
			}

		}
		this.molecule = molecule;
	}
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer("MetabolicRelation:[");
		sb.append("\nID:").append(getId());
		sb.append("\nname:").append(getASCIIName());
		sb.append("\nDescription:").append(getDescription());
		sb.append("\nDetailedDescription:").append(getDetailedDescription());
		sb.append("\nStoichiometry:").append(getStoichiometry());
		sb.append("\nRole:").append(getRole());
		sb.append("\nMolecule:").append(getMolecule().getId());
		sb.append("\nReaction:").append(getReaction().getId());
		sb.append("\n]\n");
		return sb.toString();
	}

	String getVarName() {
		return varName;
	}

	void setVarName(String varName) {
		this.varName = varName;
	}

}

/*
 * $Log: MetabolicRelation.java,v $
 * Revision 1.1  2008/06/02 10:31:42  asorokin
 * Reference to Service provider from all Service interfaces
 *
 */