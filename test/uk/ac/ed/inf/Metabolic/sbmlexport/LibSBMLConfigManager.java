package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;



public class LibSBMLConfigManager {
	
	private static boolean isWindows() {
		return System.getProperty("os.name").indexOf("Win") != -1;
	}

	private static boolean isIntelMac() {
		String osName = System.getProperty("os.name");
		return osName.indexOf("Mac") != -1;
//		return false;
	}
	static int i;
	static boolean isLoaded;

	private static String getPath() throws Exception {
		InputStream is = LibSBMLConfigManager.class.getResourceAsStream("PathToNativeSBMLLibraries.setUp");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String rc = br.readLine();
		return rc;
       
	}
	/**
	 * 
	 * @return true if sbml libraries loaded ok
	 */
	static boolean configure () {
		try {
		String libName = System.mapLibraryName("sbmlj");
		System.loadLibrary(libName);
		return true;
		}catch(UnsatisfiedLinkError e) {
		 try{ 
			 String path = getPath();
			 if(isWindows()) {
				 System.load(path + "libexpat.dll");
				 System.load(path + "libsbml.dll");
				 System.load(path + "sbmlj.dll");
    	    
				 return true;
    		
			 } else if(isIntelMac()) {
					 System.load(path + "libxml2.2.dylib");
					 System.load(path + "libsbml.dylib");
					 System.load(path + "libsbmlj.jnilib");
	    	    
					 return true;
	    		
			 } else {
				 return false;
			 }
		 }catch (Throwable  errorOrException) {
			 return false;
		 }
		}
  
	}


}
