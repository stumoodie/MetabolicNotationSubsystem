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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.pathwayeditor.businessobjects.drawingprimitives.ICanvas;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.Version;
import org.pathwayeditor.businessobjects.hibernate.pojos.HibNotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotation;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationAutolayoutService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationConversionService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationExportService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationImportService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationPluginService;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationSubsystem;
import org.pathwayeditor.businessobjects.notationsubsystem.INotationValidationService;
import org.pathwayeditor.metabolic.excelexport.ExcelExportService;
import org.pathwayeditor.metabolic.paxexport.BioPAXExportService;
import org.pathwayeditor.metabolic.sbmlexport.SBMLExportService;
import org.pathwayeditor.notationsubsystem.toolkit.validation.NotationValidationService;


public class MetabolicNotationSubsystem implements INotationSubsystem {
    
	// made public for access within plugin
	public static final String GLOBAL_ID = "org.pathwayeditor.metabolic.Metabolic";
	//private static final String GLOBAL_ID = "12635452516346262546";
	public static final String DISPLAY_NAME = "Metabolic Notation";
	private static final String NAME = "Metabolic validationService";
	public static final Version version= new Version(1,0,0);
	
	
	private MetabolicNotationSyntaxService syntaxService;
	private INotation notation;
	private Set<INotationExportService> exportServices = new HashSet<INotationExportService>();
	private INotationValidationService validationService;
	private static INotationSubsystem instance;
	
	/**
	 * Service providers should be instantiated but accessed through getInstance
	 */
	private MetabolicNotationSubsystem() {
	   
		this.notation = new HibNotation(GLOBAL_ID,DISPLAY_NAME,NAME,version);
		this.syntaxService = new MetabolicNotationSyntaxService(this);
		exportServices.add(new SBMLExportService(this));
		exportServices.add(new ExcelExportService(this));
		exportServices.add(new BioPAXExportService(this));
		MetabolicNDOMValidationService ndomVal = MetabolicNDOMValidationService.getInstance();
		this.validationService=new NotationValidationService(this,ndomVal);
	}
	
	public static INotationSubsystem getInstance() {
		if(instance == null){
			instance= new MetabolicNotationSubsystem();
		}
		return instance;
	}

	 public INotation getNotation() {
		return this.notation;
	}
	
    /**
     * Returns an unmodifiable collection of export services
     */
	public Set <INotationExportService>getExportServices() {
		return Collections.unmodifiableSet (exportServices);
	}

	public Set<INotationImportService> getImportServices() {
		return Collections.emptySet();
	}

	public Set<INotationPluginService> getPluginServices() {
		return Collections.emptySet();
	}

	public MetabolicNotationSyntaxService getSyntaxService() {
		return this.syntaxService;
	}


	public INotationValidationService getValidationService() {
		return validationService;
	}

	public Set<INotationConversionService> getConversionServices() {
		return Collections.emptySet();
	}

	public INotationAutolayoutService getAutolayoutService() {
		return new DefaultAutolayoutService();
	}

	private class DefaultAutolayoutService implements INotationAutolayoutService {

		public INotation getNotation() {
			return notation;
		}

		public boolean isImplemented() {
			return false;
		}
		public INotationSubsystem getNotationSubsystem() {
			return MetabolicNotationSubsystem.this;
		}

		public void layout(ICanvas canvas) {
			throw new UnsupportedOperationException();
		}

	}

	public boolean isFallback() {
		return false;
	}
}
