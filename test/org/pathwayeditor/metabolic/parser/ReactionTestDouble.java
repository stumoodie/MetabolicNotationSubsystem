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

import org.pathwayeditor.metabolic.parser.MetabolicReaction;
import org.pathwayeditor.metabolic.parser.MetabolicRelation;

public class ReactionTestDouble extends MetabolicReaction 
{

	public ReactionTestDouble(String id, String name, String asciiName) {
		super(id, name, asciiName);
	}

	public boolean addActivator(MetabolicRelation rel) {
		return super.addActivator(rel);
	}

	public boolean addCatalyst(MetabolicRelation rel) {
		return super.addCatalyst(rel);
	}

	public boolean addInhibitor(MetabolicRelation rel) {
		return super.addInhibitor(rel);
	}

	public boolean addProduct(MetabolicRelation rel) {
		return super.addProduct(rel);
	}

	public boolean addSubstrate(MetabolicRelation rel) {
		return super.addSubstrate(rel);
	}
}
