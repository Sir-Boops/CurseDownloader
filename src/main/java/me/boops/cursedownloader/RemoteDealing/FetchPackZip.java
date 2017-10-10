package me.boops.cursedownloader.RemoteDealing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class FetchPackZip {
	
	public FetchPackZip(URL URL, String SaveLoc) throws Exception {
		
		// First get the direct link to the file
		// Connect
		URLConnection connGrabDir = new URL(URL + "/download").openConnection();
		HttpURLConnection httpConnGrabDir = (HttpURLConnection)connGrabDir;
		httpConnGrabDir.setInstanceFollowRedirects(false);
		connGrabDir.connect();
		
		// Grab the direct URL but also grab the filename from it
		String fileName = connGrabDir.getHeaderField("Location").substring((connGrabDir.getHeaderField("Location").lastIndexOf("/") + 1));
		URL directLink = new URL(connGrabDir.getHeaderField("Location"));
		
		// Close this connection
		connGrabDir.getInputStream().close();
		
		// ---------------------------
		
		// Now Acually Download the pack
		URLConnection connGrabPack = directLink.openConnection();
		
		connGrabPack.connect();
		
		InputStream is = connGrabPack.getInputStream();
		FileOutputStream fos = new FileOutputStream(new File(SaveLoc + fileName));
		
		double fileSize = connGrabPack.getContentLength();
		System.out.println("Downloading " + fileName + ", " + ((int) (fileSize/1024)) + "KB");
		double downloaded = 0;
		double lastPrint = 0;
		int inByte;
		while((inByte = is.read()) != -1){
			downloaded++;
			fos.write(inByte);
			if(downloaded > (lastPrint + (5 * 1024))) {
				lastPrint = downloaded;
				System.out.print((int) ((downloaded / fileSize) * 100) + "% \r");
			}
		}
		
		System.out.print("100" + "% \r");
		
		is.close();
		fos.close();
		
		return;
	}
}
