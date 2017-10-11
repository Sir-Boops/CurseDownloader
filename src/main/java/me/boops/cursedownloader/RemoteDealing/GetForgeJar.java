package me.boops.cursedownloader.RemoteDealing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class GetForgeJar {
	
	public GetForgeJar(String mcVersion, String forgeVersion) throws Exception {
		
		String myPath = new File(".").getAbsolutePath();
		myPath = myPath.substring(0, (myPath.length() - 1));
		
		String fileName = ("forge-" + mcVersion + "-" + forgeVersion + "-universal.jar");
		URL forgeURL = new URL("https://files.minecraftforge.net/maven/net/minecraftforge/forge/" + mcVersion + "-" + forgeVersion + "/" + fileName);
		URLConnection conn = forgeURL.openConnection();
		conn.connect();
		
		InputStream is = conn.getInputStream();
		FileOutputStream fos = new FileOutputStream(new File(myPath + fileName));
		
		double fileSize = conn.getContentLength();
		double downloaded = 0;
		double lastPrint = 0;
		int inByte;
		while((inByte = is.read()) != -1){
			fos.write(inByte);
			downloaded++;
			if(downloaded > (lastPrint + (5 * 1024))) {
				lastPrint = downloaded;
				System.out.print("Downloading " + fileName + ", " + ((int) (fileSize/1024)) + "KB " + (int) ((downloaded / fileSize) * 100) + "% \r");
			}
		}
		
		System.out.println("Downloading " + fileName + ", " + ((int) (fileSize/1024)) + "KB " + (int) ((downloaded / fileSize) * 100) + "% \r");
		System.out.println("Downloaded " + fileName);
		
		is.close();
		fos.close();
		
		return;
		
	}
}
