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
package org.pathwayeditor.metabolic.sbmlexport;

public enum AnnotationURLs {
 ChEBIID("http://www.ebi.ac.uk/chebi/#"), GO("http://www.geneontology.org/#"),
 UNIPROT("http://www.uniprot.org/#"), KEGG("http://www.genome.jp/kegg/compound/#"),
 EC("http://www.ec-code.org/#"), PUBCHEM("http://www.pubchem.gov/substance/#"),
 IUPAC("http://www.iupac.org/inchi/#");
 
 private String url;
 
 private AnnotationURLs(String url) {
	 this.url = url;
 }
 
 String getURL () {
	 return url;
 }
}
