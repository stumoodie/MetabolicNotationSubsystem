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
package org.pathwayeditor.metabolic.excelexport;

import java.io.IOException;
import java.io.OutputStream;

import org.pathwayeditor.metabolic.ExportAdapterCreationException;
import org.pathwayeditor.metabolic.IExportAdapter;
import org.pathwayeditor.metabolic.ndomAPI.IModel;


public class MetabolicExcelExportAdapter<N extends IModel> implements IExportAdapter<IModel> {

	private IExcelFileGenerator generator ;
	
	public void createTarget(IModel model)
			throws ExportAdapterCreationException {
		
		try 
		{
			generator = new ExcelGenerator ( model , "../uk.ac.ed.inf.csb.Metabolic/Template.xls" ) ;
		}
		catch ( IllegalArgumentException e)
		{
			throw new ExportAdapterCreationException () ;
		}

	}

	public boolean isTargetCreated() {
		if ( generator == null )
		return false;
		
		return true ;
	}

	public void writeTarget(OutputStream stream) throws IOException {
		

	}

}
