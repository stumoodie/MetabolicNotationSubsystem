package uk.ac.ed.inf.Metabolic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedLibLoader {
	private static final String LIB_PREFIX = "lib";
	private final List<String> mandatoryLibList;
	private final List<String> optionalLibList;
	private final String rootLib;
	
	/**
	 * This initialiser creates a new instance of this class. It is a convenient way of
	 * initialising the class in a single line of code - useful when used in a static code block.
	 * @param rootLib the stub name of the library without the lib prefix or .dll or .so suffix.
	 * @param libList The list of mandatory libraries that must be loaded. Should include the <code>rootLib</code> too.
	 * @return A newly created instance of this class, cannot be null.
	 */
	public static SharedLibLoader createInstance(String rootLib, List<String> libList){
		return new SharedLibLoader(rootLib, libList);
	}


	/**
	 * This initialiser creates a new instance of this class. It is a convenient way of
	 * initialising the class in a single line of code - useful when used in a static code block.
	 * @param rootLib the stub name of the library without the lib prefix or .dll or .so suffix.
	 * @param mandLibList A list of mandatory libraries that must be loaded. Should include the <code>rootLib</code> too.
	 * @param optLibList A list of optional libraries that may present and if so loaded.
	 * @return A newly created instance of this class, cannot be null.
	 */
	public static SharedLibLoader createInstance(String rootLib, List<String> mandLibList, List<String> optLibList){
		return new SharedLibLoader(rootLib, mandLibList, optLibList);
	}
	
	/**
	 * Constructs the class without any optional libraries.
	 * @param rootLib the stub name of the library without the lib prefix or .dll or .so suffix.
	 * @param libList The list of mandatory libraries that must be loaded. Should include the <code>rootLib</code> too.
	 */
	public SharedLibLoader(String rootLib, List<String> libList){
		this.rootLib = rootLib;
		this.mandatoryLibList = new ArrayList<String>(libList);
		this.optionalLibList = Collections.emptyList();
	}
	
	/**
	 * Constructs the class with both mandatory and optional libraries.
	 * @param rootLib
	 * @param rootLib the stub name of the library without the lib prefix or .dll or .so suffix.
	 * @param mandatoryLibList A list of mandatory libraries that must be loaded. Should include the <code>rootLib</code> too.
	 * @param optionalLibList A list of optional libraries that may present and if so loaded.
	 */
	public SharedLibLoader(String rootLib, List<String> mandatoryLibList, List<String> optionalLibList){
		this.rootLib = rootLib;
		this.mandatoryLibList = new ArrayList<String>(mandatoryLibList);
		this.optionalLibList = new ArrayList<String>(optionalLibList);
	}
	
	/**
	 * Attempts to load the root library and allow the O/S to find its dependent libraries.
	 * If this fails then it will 
	 * @throws unsatisfiedLinkError if a mandatory library cannot be loaded during the fallback step. 
	 */
	public void loadRootLibAndFallback(){
		try{
			loadRootLib();
		}
		catch(UnsatisfiedLinkError e){
			loadAll();
		}
	}
	
	/**
	 * Loads the root library, taking a library stub name. If this fails will add a lib prefix to see if this
	 * loads the library on a Windows System,.
	 * @throws unsatisfiedLinkError if the library cannot be loaded. 
	 */
	public void loadRootLib(){
		// this should work, but due to a bug with eclipse plugin packaging mechanisms it will currently only work
		// on some O/Ss if the shared library path environment variable points to the sbml libs. This
		// must be set BEFORE the app is executed.
		this.loadLib(this.rootLib, true);
	}
	
	/**
	 * Loads all the libraries added to this class. Tries hard to load the libraries by adding a lib prefix to the
	 * library name stub, which may help on Windows systems. It loads optional libs first then mandatory ones.
	 * @throws unsatisfiedLinkError if a mandatory library cannot be loaded. 
	 */
	public void loadAll(){
		// our fallback is to load all the poss sbml libs in dependency order on Windows it expects some
		// libs to be prefixed with a lib, Unix like O/Ss automatically prefix with lib so we need to try both forms
		loadOptionalLibs();
		loadMandatoryLibs();
	}

	public void loadMandatoryLibs(){
		for(String libStub : this.mandatoryLibList){
			loadLib(libStub, true);
		}
	}
	
	public void loadOptionalLibs(){
		for(String libStub : this.optionalLibList){
			loadLib(libStub, false);
		}
	}
	
	private void loadLib(String libStub, boolean mandatory){
		try{
			// try loading using just the stub name 
			System.loadLibrary(libStub);
		}
		catch(UnsatisfiedLinkError e1){
			try{
				// load with a lib prefix
				StringBuilder buf = new StringBuilder(LIB_PREFIX);
				buf.append(libStub);
				System.loadLibrary(buf.toString());
			}
			catch(UnsatisfiedLinkError e2){
				if(mandatory){
					// if this fails then we cannot load the mandatory library and we should let things take there course.
					throw e2;
				}
			}
		}
	}

}
