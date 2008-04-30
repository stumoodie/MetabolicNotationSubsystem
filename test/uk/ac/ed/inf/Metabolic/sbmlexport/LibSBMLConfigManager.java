package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class LibSBMLConfigManager {
	
	private static boolean isWindows() {
		return System.getProperty("os.name").indexOf("Win") != -1;
	}

	private static String getPath() throws Exception {
		InputStream is = LibSBMLConfigManager.class.getResourceAsStream("PathToNativeSBMLLibraries.setUp");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String rc = br.readLine();
		return rc;
       
	}
	/**
	 * 
	 * @return true if sbml libraries loaded ok
	 * @throws Exception 
	 */
	static boolean configure () throws Exception {
		try {
		String path = getPath();
    	if(isWindows()) {
    	      System.load(path + "os/win32/sbmlj.dll");
    	      return true;
    	} else {
    		return false;
    	}
		}catch (Exception e) {
			return false;
		}
  
	}
}
