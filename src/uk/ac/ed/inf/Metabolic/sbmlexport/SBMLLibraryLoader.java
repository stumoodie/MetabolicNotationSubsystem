package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.Platform;

 class SBMLLibraryLoader {
	
   protected static SBMLLibraryLoader instance;
   
   protected SBMLLibraryLoader () {
	   
   }
   
   public static  SBMLLibraryLoader getInstance () {
	   if(instance == null){
		   instance = new SBMLLibraryLoader();
	   }
	   return instance;
   }
   
   public boolean loadLibrary () {
	     String os = Platform.getOS();
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
		 }catch(IOException ie) {
			 System.out.println(ie.getMessage());
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
