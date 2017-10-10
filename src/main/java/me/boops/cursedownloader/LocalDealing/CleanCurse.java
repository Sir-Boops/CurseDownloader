package me.boops.cursedownloader.LocalDealing;

import java.io.File;

public class CleanCurse {
	
	public void removeCurse(String path){
		
		String[] fileList = new File(path).list();
		
		for (int i=0; i<fileList.length; i++){
			
			// Check if file
			if(!new File(path + fileList[i]).isDirectory()){
				
				// Delete the curse junk
				new File(path + fileList[i]).delete();
				
			}
			
		}
		
	}
}
