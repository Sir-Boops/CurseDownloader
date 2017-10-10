package me.boops.cursedownloader.LocalDealing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ApplyOverrides {
	
	public void AddOverrides(String path) throws IOException{
		
		String[] fileList = new File(path + "overrides/").list();
		
		for (int i=0; i<fileList.length; i++){
			
			
			//Check if the dest dir exists
			if(new File(path + fileList[i]).exists()){
				
				// Folder is already there so copy stuff from inside
				String[] fileList2 = new File(path + "/overrides/" + fileList[i]).list();
				for (int i2=0; i2<fileList2.length; i2++){
					
					Path from = new File(path + "overrides/" + fileList[i] + "/" + fileList2[i2]).toPath();
					Path to = new File(path + fileList[i] + "/" + fileList2[i2]).toPath();
					
					Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
					
				}
				
				new File(path + "/overrides/" + fileList[i]).delete();
				
			} else {
				
				Path from = new File(path + "overrides/" + fileList[i]).toPath();
				Path to = new File(path + fileList[i]).toPath();
				
				Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
				
			}
		}
		
		new File(path + "overrides/").delete();
		
	}
}
