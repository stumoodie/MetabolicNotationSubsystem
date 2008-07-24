package uk.ac.ed.inf.Metabolic.sbmlexport;

public class SBMLLoaderTestStub extends LibSBMLLoader {
	
	   
	public static void overrideSingleton (LibSBMLLoader loader) {
		instance = loader;
	}

}
