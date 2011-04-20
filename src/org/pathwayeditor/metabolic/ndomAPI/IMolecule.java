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

import org.pathwayeditor.notationsubsystem.toolkit.ndom.INdomModel;


public interface IMolecule extends INdomModel {
	public List<IRelation> getSinkList();

	public List<IRelation> getSourceList();

	public List<IRelation> getActivatoryRelationList();

	public List<IRelation> getInhibitoryRelationList();

	public List<IRelation> getCatalyticRelationList();
	
	public INdomModel    getParent () ;
}
