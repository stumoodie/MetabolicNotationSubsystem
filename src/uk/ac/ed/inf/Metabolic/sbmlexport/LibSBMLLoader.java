package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

class LibSBMLLoader {
	final String[]SBML_LIBS = new String[] { "libexpat", "libsbml", "sbmlj" };
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
		String os = System.getProperty("os.name");
		if (os.contains("win") || os.contains("Win")) {
			os = "win32";
		}
		
		for (int i = 0; i < SBML_LIBS.length; i++) {
			String lib = System.mapLibraryName(SBML_LIBS[i]);
			InputStream is = MetabolicSBMLExportAdapter.class.getResourceAsStream("/os/" + os + "/"
					+ lib);
			File temp = new File(System.getProperty("java.io.tmpdir") + File.separator + lib);

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
