package me.boops.cursedownloader.file;

import java.io.File;

public class CreateFolder {
	
	public CreateFolder(String path) {
		
		File folder = new File(path);
		
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
	}
}
