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
package org.pathwayeditor.metabolic.ndomAPI;

import java.util.List;
import java.util.Set;

import org.pathwayeditor.notationsubsystem.toolkit.ndom.INdomModel;

public interface IModel extends INdomModel {
	public List<ICompartment> getCompartmentList();

	public List<IReaction> getReactionList();
	
	//Defined properties dictionaries
	public Set<String> getECnumberList();
	
	public Set<String> getSmilesList();
	
	public Set<String> getRoleList();
	
	public Set<String> getCIDList();

	public Set<String> getChEBIList();

	public Set<String> getPubChemList();

	public Set<String> getInChIList();

	public Set<String> getGOTermList();

/*
//getters for object from property 
 	public List<String> get();

	public List<String> get();

	public List<String> get();

	public List<String> get();

*/
}
