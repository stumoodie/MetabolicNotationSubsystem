package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

 class LibSBMLLoader {
	
   protected static LibSBMLLoader instance;
   
   protected LibSBMLLoader () {
	   
   }
   
   public static  LibSBMLLoader getInstance () {
	   if(instance == null){
		   instance = new LibSBMLLoader();
	   }
	   return instance;
   }
   
   public boolean loadLibrary () {
	  System.out.println(System.getProperty("java.home"));
	     String os = System.getProperty("os.name");
	     if(os.contains("win") || os.contains("Win")){
	    	 os="win32";
	     }
		 String lib = System.mapLibraryName("sbmlj");
		 InputStream is = MetabolicSBMLExportAdapter.class.getResourceAsStream("/os/" +os +"/" + lib);
		 File temp = new File(System.getProperty("java.io.tmpdir") + File.separator + lib);
		
		 try {
			 temp.createNewFile();
		 FileOutputStream fos = new FileOutputStream(temp);
		 byte [] bytes = new byte[4096];
		 int read = 0;
		
		 while( (read = is.read(bytes)) != -1){
			fos.write(bytes, 0, read);
		 }
		 is.close();
		 fos.close();
		 }catch(Exception ie) {
			 return false;
		 }
	
		try {
		  System.load(temp.getAbsolutePath());
		}catch(UnsatisfiedLinkError e ) {
			System.out.println(e.getMessage());
			return false;
		}finally{
			if(temp.exists()){
				temp.deleteOnExit();
			}
		}
		return true;
		
	
   }

}
