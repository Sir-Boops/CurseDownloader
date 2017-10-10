package me.boops.cursedownloader.RemoteDealing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FetchPackZip {
	
	public String Fetch(URL URL, String SaveLoc) throws Exception {
		
		// Download the file
		// Setup the URL
		URLConnection conn = new URL(URL + "/download").openConnection();
		
		// Connect
		conn.connect();
		
		// Setup the input stream (This follows all the redirects to the direct link)
		InputStream is = conn.getInputStream();
		
		// Get the filename from the URL
		String fileName = conn.getURL().toString().substring(conn.getURL().toString().lastIndexOf('/') + 1);
		
		// Setup the steam to the disk
		FileOutputStream fos = new FileOutputStream(new File(SaveLoc + fileName));
		
		// Acually download the file
		double fileSize = conn.getContentLength();
		double downloaded = 0;
		double lastPrint = 0;
		int inByte;
		while((inByte = is.read()) != -1){
			downloaded++;
			fos.write(inByte);
			if(downloaded > (lastPrint + (5 * 1024))) {
				lastPrint = downloaded;
				System.out.print("Downloading " + fileName + ", " + ((int) (fileSize/1024)) + "KB " + (int) ((downloaded / fileSize) * 100) + "% \r");
			}
		}
		
		// Print done and close the connections
		System.out.println("Downloading " + fileName + ", " + ((int) (fileSize/1024)) + "KB " + (int) ((downloaded / fileSize) * 100) + "% \r");
		System.out.println("Downloaded " + fileName);
		
		is.close();
		fos.close();
		
		return fileName;
	}
}
