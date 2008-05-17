package uk.ac.ed.inf.Metabolic.sbmlexport;

public class TestSBMLLoader extends SBMLLibraryLoader {
	
	   
	public static void overrideSingleton (SBMLLibraryLoader loader) {
		instance = loader;
	}

}
