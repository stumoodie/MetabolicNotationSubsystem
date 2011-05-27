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

import org.pathwayeditor.metabolic.parser.MetabolicCompartment;
import org.pathwayeditor.metabolic.parser.MetabolicCompound;
import org.pathwayeditor.metabolic.parser.MetabolicModel;
import org.pathwayeditor.metabolic.parser.MetabolicReaction;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.NdomException;

public class ModelTestDouble extends MetabolicModel 
{

	public ModelTestDouble(String id, String name, String asciiName) {
		super(id, name, asciiName);
	}

	public boolean addCompartment(MetabolicCompartment e) throws NdomException {
		return super.addCompartment(e);
	}

	public boolean addReaction(MetabolicReaction e) {
		return super.addReaction(e);
	}

	public void registerCompartment(MetabolicCompartment e) throws NdomException {
		super.registerCompartment(e);
	}

	public void registerCompound(MetabolicCompound c) {
		super.registerCompound(c);
	}
	
}