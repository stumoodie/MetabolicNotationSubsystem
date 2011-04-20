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

import java.util.List;

import org.pathwayeditor.metabolic.ndomAPI.ICompound;
import org.pathwayeditor.metabolic.ndomAPI.IMacromolecule;


public class MacromoleculeAnnotationBuilder extends AnnotationBuilder {

	private IMacromolecule macromolecule;
    private boolean isTopLevel;
	MacromoleculeAnnotationBuilder(IMacromolecule object, boolean isTopLevel) {
		super(object);
		this.macromolecule=object;
		this.isTopLevel = isTopLevel;
		// TODO Auto-generated constructor stub
	}

	

	@Override
	String getRDFResources() {
		StringBuffer sb = new StringBuffer();
		if(isTopLevel){
			sb.append(getRDFIsData());
			sb.append(getHasPartData());
		} else {
			sb.append(getRDFIsData());
		}
		return sb.toString();
	}
	  private String getHasPartData() {
		  if(macromolecule.getCompoundList().isEmpty() && macromolecule.getSubunitList().isEmpty()){
			  return "";
		  }
		  StringBuffer sb = new StringBuffer();
		  sb.append(RDF_HAS_ST).append(getBagStart());
		  addChildElementData(sb, macromolecule);
		  sb.append(getBagEnd()).append(RDF_HAS_END);
		  return sb.toString();
		  
	}

	private void addChildElementData(StringBuffer sb, IMacromolecule macromolecule2) {
		List<ICompound> compounds = macromolecule2.getCompoundList();
		  for (ICompound c: compounds) {
			  sb.append(RDFList).append(AnnotationURLs.ChEBIID.getURL())
            .append( c.getChEBIId())
            .append(RDFListEnd);
		  }
		  
		  List<IMacromolecule> macromolChildren = macromolecule2.getSubunitList();
		  for (IMacromolecule child: macromolChildren) {
			  sb.append(RDFList).append(AnnotationURLs.UNIPROT.getURL())
            .append( child.getUniProt())
            .append(RDFListEnd);
			  addChildElementData(sb, child);
		  }
		 
		
	}

	private String getRDFIsData() {
		 
		StringBuffer sb = new StringBuffer();
		
		sb.append(RDF_IS_ST).append(getBagStart());
		if(macromolecule.getUniProt().length() != 0)	{
			sb.append(RDFList).append(AnnotationURLs.UNIPROT.getURL())
		                  .append( macromolecule.getUniProt())
		                  .append(RDFListEnd);
		}
		sb.append(getBagEnd()).append(RDF_IS_END);
		
		sb.append(RDF_ISVERSIONOF_ST).append(getBagStart());
		if(macromolecule.getGOTerm().length() != 0)	{
			sb.append(RDFList).append(AnnotationURLs.GO.getURL())
		                  .append( macromolecule.getGOTerm())
		                  .append(RDFListEnd);
		}
		sb.append(getBagEnd()).append(RDF_ISVERSIONOF_END);
		return sb.toString();
	}

}
