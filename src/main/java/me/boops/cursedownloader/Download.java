package me.boops.cursedownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;

public class Download {
	
	public void dlMods(String folder, JSONArray mods) throws Exception {
		
		// Create the mods folder
		new File(folder).mkdir();
		
		// Scan though the list and convert modids to names
		
		for (int i=0; i<mods.length(); i++){
			
			// Convert IDs
			URLConnection conn = new URL("https://minecraft.curseforge.com/projects/" + mods.getJSONObject(i).getInt("projectID")).openConnection();
			conn.connect();
			conn.getInputStream();
			
			getDirectLink(new URL(conn.getURL().toString().substring(0, conn.getURL().toString().indexOf("?"))), mods.getJSONObject(i).getInt("fileID"), folder);
			
		}
	}
	
	public void dlPack(String packName, int fileID, String folder) throws Exception{
		
		URL url = new URL("https://minecraft.curseforge.com/projects/" + packName);
		getDirectLink(url, fileID, folder);
		
	}
	
	private void getDirectLink(URL url, int fileID, String folder) throws Exception {
		
		// Get file name
		URLConnection conn = new URL(url +  "/files/" + fileID + "/download").openConnection();
		conn.connect();
		
		String fileName = conn.getHeaderField("Location").substring((conn.getHeaderField("Location").lastIndexOf("/") + 1), conn.getHeaderField("Location").length());
		String modName = url.toString().substring((url.toString().lastIndexOf("/") + 1), url.toString().length());
		URL directLink = new URL(conn.getHeaderField("Location").replaceFirst("http", "https"));
		
		downloadMod(directLink, fileName, modName, folder);
		
	}
	
	private void downloadMod(URL url, String fileName, String modName, String folderName) throws Exception{
		
		// Finally download the mod
		URLConnection conn = url.openConnection();
		
		InputStream is = conn.getInputStream();
		FileOutputStream fos = new FileOutputStream(new File(folderName + fileName));
		
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
				System.out.print("Downloading: " + fileName + " - File size: " + ((int) fileBytes/1024) + " KB - Progress: " + percent + "%\r");

			}

		}
		
		System.out.println("Downloaded: " + fileName + " - File size: " + ((int) fileBytes/1024) + " KB - Progress: 100% ");

		is.close();
		fos.close();

	}
}
