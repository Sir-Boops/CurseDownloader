package me.boops.cursedownloader.LocalDealing;

import java.io.File;
import java.util.ArrayList;

public class CleanDir {
	
	private ArrayList<String> list = new ArrayList<String>();
	
	public CleanDir(String path) {
		
		// Generate the list of things to delete
		genList(path);
		
		// Add the top folder to the list
		list.add(path);
		
		// Go though and delete everything on the list
		for(int i = 0; i < list.size(); i++) {
			
			new File(list.get(i)).delete();
			
		}
		
	}
	
	private void genList(String path) {
		
		String[] fileList = new File(path).list();
		
		for (int i = 0; i < fileList.length; i++) {
			
			if(new File(path + fileList[i]).isDirectory()) {
				
				// Run deeper to list everything
				genList(path + fileList[i] + File.separator);
				list.add(path + fileList[i]);
				
			} else {
				
				// Just delete the file
				list.add(path + fileList[i]);
			}
		}
	}
}
