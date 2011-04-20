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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.pathwayeditor.metabolic.excelexport.ExcelGenerator;
import org.pathwayeditor.metabolic.ndomAPI.IModel;


public class ExcelGeneratorTestStub extends ExcelGenerator {

	public ExcelGeneratorTestStub(IModel model, String templatePath)
			throws IllegalArgumentException {
		super(model, templatePath);

	}

	@Override
	protected HSSFWorkbook loadTemplateFromPath() throws IOException {
		
		InputStream inputStream = new FileInputStream ( templatePath ) ;
		
		try {
        POIFSFileSystem fs;
		fs = new POIFSFileSystem( inputStream );
		return new HSSFWorkbook(fs);
		}
		finally {
			inputStream.close() ;
		}

	}
	
	
	

}
