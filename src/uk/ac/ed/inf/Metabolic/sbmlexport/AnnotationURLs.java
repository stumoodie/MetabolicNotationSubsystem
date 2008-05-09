package uk.ac.ed.inf.Metabolic.sbmlexport;

public enum AnnotationURLs {
 ChEBIID("http://www.ebi.ac.uk/chebi/#"), GO("http://www.geneontology.org/#"),
 UNIPROT("http://www.uniprot.org/#"), KEGG("http://www.genome.jp/kegg/compound/#");
 
 private String url;
 
 private AnnotationURLs(String url) {
	 this.url = url;
 }
 
 String getURL () {
	 return url;
 }
}
