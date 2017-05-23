package me.boops.cursedownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Download {
	
	public void downloadMod(String filename, String name, int fileID, String path, URL URL) throws Exception{
		
		URLConnection conn = URL.openConnection();
		
		InputStream is = conn.getInputStream();
		FileOutputStream fos = new FileOutputStream(new File(path + filename));
		
		double fileBytes = conn.getContentLength();
		
		int inByte;
		double totalBytes = 0;
		int lastProgress = 0;
		while((inByte = is.read()) != -1){

			fos.write(inByte);
			totalBytes++;

			int percent = ((int) ((totalBytes / fileBytes) * 100));

			if(percent > lastProgress || lastProgress == 0){

				lastProgress = percent;
				System.out.print("Downloading: " + filename + " - File size: " + ((int) fileBytes/1024) + " KB - Progress: " + percent + "%\r");

			}

		}
		
		System.out.println("Downloaded: " + filename + " - File size: " + ((int) fileBytes/1024) + " KB - Progress: 100% ");

		is.close();
		fos.close();

	}
}