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

import org.pathwayeditor.metabolic.ndomAPI.ICompound;

 class CompoundAnnotationBuilder extends AnnotationBuilder {
    ICompound compound;
    
	CompoundAnnotationBuilder(ICompound compound) {
		super(compound);
		this.compound = compound;
	}


	@Override
	String getRDFResources() {
		String resource="<rdf:li rdf:resource=\"";
		StringBuffer rc = new StringBuffer();
		rc.append(RDF_IS_ST).append(getBagStart());
		if(compound.getChEBIId().length() != 0)
			rc.append(resource).append(AnnotationURLs.ChEBIID.getURL()).append(compound.getChEBIId()).append(RDFListEnd);
		if(compound.getPubChemId().length() != 0)
		  rc.append(resource).append(AnnotationURLs.PUBCHEM).append(compound.getPubChemId()).append(RDFListEnd);
		if(compound.getInChI().length() != 0)
		  rc.append(resource).append(AnnotationURLs.IUPAC).append(compound.getInChI()).append(RDFListEnd);
		if(compound.getCID().length() != 0)
	      rc.append(resource).append(AnnotationURLs.KEGG.getURL()).append(compound.getCID()).append(RDFListEnd);
		rc.append(getBagEnd()).append(RDF_IS_END);
		return rc.toString();
	}

	@Override
	protected String buildSpecificNotes() {
	    String rc;
        if (compound.getSmiles().length() != 0) {
        	StringBuffer  notes = new StringBuffer().append("<h2 xmlns='http://www.w3.org/1999/xhtml'>")
        
		  .append("Smiles")
		  .append("</h2>")
		  .append("<p xmlns='http://www.w3.org/1999/xhtml'>")
		  .append(compound.getSmiles())
		  .append("</p>");
         rc = notes.toString();
        } else {
        	rc="";
        	
        }
        return rc;
	}
}
