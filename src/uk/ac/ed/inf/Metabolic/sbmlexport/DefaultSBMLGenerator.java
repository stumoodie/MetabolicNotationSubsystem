package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.util.Set;

import org.pathwayeditor.businessobjectsAPI.ILink;
import org.pathwayeditor.businessobjectsAPI.IMapObject;
import org.pathwayeditor.businessobjectsAPI.IRootMapObject;
import org.pathwayeditor.businessobjectsAPI.IShape;
import org.sbml.libsbml.Compartment;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.Reaction;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.Species;
import org.sbml.libsbml.SpeciesReference;
import org.sbml.libsbml.XMLAttributes;
import org.sbml.libsbml.XMLNode;

import uk.ac.ed.inf.Metabolic.MetabolicContextAdapterSyntaxService;


public class DefaultSBMLGenerator implements ISBMLGenerator {

	public SBMLDocument generateSBMLModel(IRootMapObject rmo) {
		SBMLDocument document = new SBMLDocument(2,3);
		Model m = document.createModel();
		setUpDiagramCompartment(rmo, m);
		Set<IShape> species = findSpecies(rmo, m);
		for(IShape imo: species) {
			Species s = new Species(imo.getId());
			s.setName(imo.getName().getString());
			m.addSpecies(s);
		}
		Set<IShape> processes = findProcesses(rmo, m);
		for(IShape process: processes) {
			Reaction r = new Reaction(process.getId());
			addReactants(process, r);
			addProducts(process,r);
			m.addReaction(r);
		}
		
		// now do links
		return document;
	}
	private void addProducts(IShape process, Reaction r) {
		Set <ILink> produceLinks = new DiagramFilterer().getLinks(process, new OTFilter () {
			public boolean accept (IMapObject imo) {
				return imo.getObjectType().getTypeName().equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Produce.name());
			}
		});
		for(ILink l:produceLinks) {
			r.addProduct(new SpeciesReference(l.getTarget().getId()));
		}
		
	}
	private void addReactants(IShape process, Reaction r) {
		Set <ILink> consumeLinks = new DiagramFilterer().getLinks(process, new OTFilter () {
			public boolean accept (IMapObject imo) {
				return imo.getObjectType().getTypeName().equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Consume.name());
			}
		});
		for(ILink l:consumeLinks) {
			r.addReactant(new SpeciesReference(l.getSource().getId()));
		}
		
	}
	private Set<IShape> findProcesses(IRootMapObject rmo, Model m) {
		Set <IShape> processes =new DiagramFilterer().getRequiredShapes(rmo, new OTFilter (){
			public boolean accept (IMapObject imo) {
				return imo.getObjectType().getTypeName().equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Process.name());
			}
		});
		return processes;
	}
	private Set <IShape> findSpecies(IRootMapObject rmo, Model m) {
		Set <IShape> species =new DiagramFilterer().getRequiredShapes(rmo, new OTFilter (){
			public boolean accept (IMapObject imo) {
				return imo.getObjectType().getTypeName().equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Compound.name()) ||
				       imo.getObjectType().getTypeName().equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Macromolecule.name());
			}
		});
		return species;
	}
	
	String RDFdec = "rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:dcterms=\"http://purl.org/dc/terms/\" xmlns:vCard=\"http://www.w3.org/2001/vcard-rdf/3.0#\" xmlns:bqbiol=\"http://biomodels.net/biology-qualifiers/\" xmlns:bqmodel=\"http://biomodels.net/model-qualifiers/";
	String Desc ="rdf:Description rdf:about=\"#\"";
	/* <annotation>
      <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:vCard="http://www.w3.org/2001/vcard-rdf/3.0#" xmlns:bqbiol="http://biomodels.net/biology-qualifiers/" xmlns:bqmodel="http://biomodels.net/model-qualifiers/">
        <rdf:Description rdf:about="#">
          <bqbiol:is>
            <rdf:Bag>

              <rdf:li rdf:resource="http://www.ebi.ac.uk/chebi/#CHEBI:28972"/>
              <rdf:li rdf:resource="http://www.pubchem.gov/substance/#5836"/>
              <rdf:li rdf:resource="http://www.iupac.org/inchi/#InChI=1/C3H8O2/c1-3(5)2-4/h3-5H,2H2,1H3/t3-/m1/s1"/>
              <rdf:li rdf:resource="http://www.genome.jp/kegg/compound/#C02912"/>
            </rdf:Bag>
          </bqbiol:is>
        </rdf:Description>*/
	private void addAnnotations(Species s, IMapObject imo) {
		if(isCompound(imo)){
			XMLNode top = XMLNode.convertStringToXMLNode(RDFdec);
			XMLAttributes atts = top.getAttributes();
			top.addChild(XMLNode.convertStringToXMLNode(Desc));
			s.appendAnnotation(top);
		}
		
	}
	private boolean isCompound(IMapObject imo) {
		return imo.getObjectType().getTypeName().equals(MetabolicContextAdapterSyntaxService.ObjectTypes.Compound.name());
	}

	private void setUpDiagramCompartment(IRootMapObject rmo, Model m) {
		Compartment c1 = m.createCompartment();
		c1.setId(rmo.getId());
	}

}
