package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LibSBMLConfigManager {
	
	private static boolean isWindows() {
		return System.getProperty("os.name").indexOf("Win") != -1;
	}

	private static String getPath() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("./testSetUp/PathToNativeSBMLLibraries.setUp"));
		String rc = br.readLine();
		System.out.println(rc);
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
			throw e;
		}
  
	}
}
