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
package org.pathwayeditor.metabolic;

import org.pathwayeditor.businessobjects.drawingprimitives.ICanvas;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationReport;
import org.pathwayeditor.metabolic.parser.MetabolicNDOMFactory;
import org.pathwayeditor.metabolic.parser.NDOMFactory;
import org.pathwayeditor.metabolic.validation.MetabolicRuleStore;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.INDOMValidationService;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.INdomModel;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.NdomException;
import org.pathwayeditor.notationsubsystem.toolkit.validation.IValidationRuleStore;
import org.pathwayeditor.notationsubsystem.toolkit.validation.RuleValidationReportBuilder;


public class MetabolicNDOMValidationService implements INDOMValidationService {

	private static MetabolicNDOMValidationService instance;

	public static MetabolicNDOMValidationService getInstance() {
		if (instance == null) {
			instance = new MetabolicNDOMValidationService();
		}
		return instance;
	}

	private NDOMFactory factory;
	private RuleValidationReportBuilder reportBuilder;
	private ICanvas map;
	private boolean readyToValidate = false;
	private boolean validated;
	private IValidationReport validationReport;
//	private IValidationRuleStore ruleStore;
	private boolean ndomCreated;
	
	MetabolicNDOMValidationService() {
	}

	public ICanvas getMapBeingValidated() {
		return map;
	}

	public INdomModel getNDOM() {
		return factory.getNdom();
	}

	public IValidationRuleStore getRuleStore() {
		return MetabolicRuleStore.getInstance();
	}

//	protected IDefaultValidationRuleConfigLoader getRuleLoader() {
//		return MetabolicRuleLoader.getInstance();
//	}

	public IValidationReport getValidationReport() {
		return validationReport;
	}

	public boolean hasBeenValidated() {
		return validated;
	}

	public boolean isReadyToValidate() {
		
		return readyToValidate;
	}

	public boolean ndomWasCreated() {
		return ndomCreated;
	}

	public void setMapToValidate(ICanvas map) {
		if(map==null) throw new NullPointerException("Map to validate set to null");
		this.map=map;
		readyToValidate=true;
		validated=false;
		validationReport=null;
		ndomCreated=false;
	}

	public void validateMap() {
		factory = createNdomFactory();
		reportBuilder = new RuleValidationReportBuilder(getRuleStore(), map);
		factory.setReportBuilder(reportBuilder);
		factory.setRmo(map.getModel().getRootNode());
		try {
			generateNdom();
		} catch (NdomException e) {
			handleNdomException(e);
		} finally{
			validated=true;
			reportBuilder.createValidationReport();
		    validationReport = reportBuilder.getValidationReport();
		}
	}

	protected void generateNdom() throws NdomException{
		ndomCreated=false;
		validated=false;
		getFactory().parse();
	}

	protected NDOMFactory createNdomFactory() {
		NDOMFactory ret=new MetabolicNDOMFactory(getMapBeingValidated().getModel().getRootNode());
		return ret;
	}

	protected void handleNdomException(NdomException e) {
		throw new RuntimeException(e);//no ndomExceptions should ever be thrown in this basic validationService
	}

	protected NDOMFactory getFactory() {
		return factory;
	}

}

/*
 * $Log:$
 */