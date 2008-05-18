package uk.ac.ed.inf.Metabolic.sbmlexport;

public class TestSBMLLoader extends LibSBMLLoader {
	
	   
	public static void overrideSingleton (LibSBMLLoader loader) {
		instance = loader;
	}

}
