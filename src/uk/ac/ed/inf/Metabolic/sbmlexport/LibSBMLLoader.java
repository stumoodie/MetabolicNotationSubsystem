package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

class LibSBMLLoader {
	final static String[]SBML_WINDOWS_LIBS = new String[] { "libexpat.dll", "libsbml.dll", "sbmlj.dll" };
	final static String[]SBML_MAC_LIBS = new String[] { "libxml2.2.dylib", "libsbml.dylib", "libsbmlj.jnilib" };
	protected static LibSBMLLoader instance;

	protected LibSBMLLoader() {

	}

	public static LibSBMLLoader getInstance() {
		if (instance == null) {
			instance = new LibSBMLLoader();
		}
		return instance;
	}

	// this works when the app is running but not for junits. To get
	// lib sbml for junits use LisSBMLConfigManager in the test folders
	public boolean loadLibrary() {
		String [] librariesToLoad = new String [0];
		String os = System.getProperty("os.name");
		if (os.contains("win") || os.contains("Win")) {
			os = "win32";
			librariesToLoad = SBML_WINDOWS_LIBS;
		} else if (os.contains("Mac OS X")){
			os = "macosx";
			librariesToLoad = SBML_MAC_LIBS;
		}
		
		
		for (String lib : librariesToLoad) {
			InputStream is = MetabolicSBMLExportAdapter.class.getResourceAsStream("/os/" + os + "/" + lib);
			File temp = new File(System.getProperty("java.io.tmpdir"), lib);

			try {
				temp.createNewFile();
				FileOutputStream fos = new FileOutputStream(temp);
				byte[] bytes = new byte[4096];
				int read = 0;

				while ((read = is.read(bytes)) != -1) {
					fos.write(bytes, 0, read);
				}
				is.close();
				fos.close();
			} catch (Throwable ie) {
				return false;
			}

			try {
				System.load(temp.getAbsolutePath());
			} catch (UnsatisfiedLinkError e) {
				return false;
			} finally {
				if (temp.exists()) {
					temp.deleteOnExit();
				}
			}
		}
		return true;

	}

}
