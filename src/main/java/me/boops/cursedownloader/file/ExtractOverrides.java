package me.boops.cursedownloader.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractOverrides {
	
	@SuppressWarnings("unchecked")
	public ExtractOverrides(String path, String file) {
		
		try {
			
			ZipFile zipFile = new ZipFile(path + file);
			List<ZipEntry> fileList = (List<ZipEntry>) Collections.list(zipFile.entries());
			
			for(int i = 0; i < fileList.size(); i++) {
				if(fileList.get(i).getName().toLowerCase().contains("overrides")) {
					String newPath = (fileList.get(i).getName().substring(fileList.get(i).getName().indexOf("/") + 1, fileList.get(i).getName().length()));
					
					String filePath = (newPath.substring(0, newPath.lastIndexOf("/") + 1));
					new CreateFolder(path + filePath);
					
					if(!newPath.isEmpty() && !new File(path + newPath).isDirectory()) {
						
						System.out.println("Extracting: " + fileList.get(i).getName());
						
						InputStream is = zipFile.getInputStream(fileList.get(i));
						FileOutputStream fos = new FileOutputStream(path + newPath);
						
						int inByte;
						while((inByte = is.read()) != -1) {
							fos.write(inByte);
						}
						
						fos.close();
						is.close();
					}
				}
			}
			
			zipFile.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
