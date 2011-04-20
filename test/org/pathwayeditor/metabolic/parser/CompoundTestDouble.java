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

import org.pathwayeditor.metabolic.ndomAPI.IRelation;
import org.pathwayeditor.metabolic.parser.MetabolicCompound;

public class CompoundTestDouble extends MetabolicCompound 
{

	public CompoundTestDouble(String id, String name, String asciiName) {
		super(id, name, asciiName);
		// TODO Auto-generated constructor stub
	}

	public boolean addActivatoryRelation(IRelation rel) {
		return super.addActivatoryRelation(rel);
	}

	public boolean addCatalyticRelation(IRelation rel) {
		return super.addCatalyticRelation(rel);
	}

	public boolean addInhibitoryRelation(IRelation rel) {
		return super.addInhibitoryRelation(rel);
	}

	public boolean addSink(IRelation rel) {
		return super.addSink(rel);
	}

	public boolean addSsource(IRelation rel) {
		return super.addSsource(rel);
	}
	
}
