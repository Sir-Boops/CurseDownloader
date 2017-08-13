package me.boops.cursedownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONArray;

public class Download {
	
	public void dlMods(String folder, JSONArray mods) throws Exception {
		
		// Create the mods folder
		new File(folder).mkdir();
		
		// Scan though the list and convert modids to names
		
		ArrayList<URL> ModNames = new ArrayList<URL>();
		ArrayList<Integer> ModFileIDs = new ArrayList<Integer>();
		System.out.println("Converting Mod IDs to Mod Names");
		
		for (int i=0; i<mods.length(); i++) {
			
			final int internal = i;
			
			new Thread(new Runnable(){
				public void run(){
					try {
						
						// Convert IDs
						URLConnection conn = new URL("https://minecraft.curseforge.com/projects/" + mods.getJSONObject(internal).getInt("projectID")).openConnection();
						conn.setConnectTimeout(20 * 1000);
						conn.connect();
						conn.getInputStream();
						
						//getDirectLink(new URL(conn.getURL().toString()), mods.getJSONObject(i).getInt("fileID"), folder);
						ModNames.add(conn.getURL());
						ModFileIDs.add(mods.getJSONObject(internal).getInt("fileID"));
						
					} catch (Exception e) {
						System.out.println("Error downloading mod: " + mods.getJSONObject(internal).getInt("projectID"));
						e.printStackTrace();
						System.exit(1);
					}
				}
			}).start();
			
		}
		
		
		int lastCheck=0;
		boolean done = false;
		while(mods.length() >= ModNames.size() && !done){
			if(ModNames.size() > lastCheck){
				System.out.print("Converted Mod ID # " + ModNames.size() + "/" + mods.length() + "\r");
				lastCheck = ModNames.size();
			}
			if(ModNames.size() == mods.length()){
				System.out.println("Converted Mod ID # " + ModNames.size() + "/" + mods.length());
				done = true;
			}
			if(lastCheck == 0){
				System.out.print("Converted Mod ID # 0/" + mods.length() + "\r");
			}
		}
		
		getDirectLinks(ModNames, ModFileIDs, folder);
		
	}
	
	private void getDirectLinks(ArrayList<URL> URLs, ArrayList<Integer> FileIDs, String folder) throws Exception {
		
		ArrayList<String> FileNames = new ArrayList<String>();
		ArrayList<String> ModNames = new ArrayList<String>();
		ArrayList<URL> ModLinks = new ArrayList<URL>();
		
		for(int i = 0; i<URLs.size(); i++){
			
			final int internal = i;
			
			new Thread(new Runnable(){
				public void run(){
					try {
						
						// Get file name
						URLConnection conn = new URL(URLs.get(internal) +  "/files/" + FileIDs.get(internal) + "/download").openConnection();
						
						HttpURLConnection httpConn = (HttpURLConnection)conn;
						httpConn.setInstanceFollowRedirects(false);
						
						conn.connect();
						
						FileNames.add(URLDecoder.decode(conn.getHeaderField("Location").substring((conn.getHeaderField("Location").lastIndexOf("/") + 1), conn.getHeaderField("Location").length()), "UTF-8"));
						ModNames.add(URLDecoder.decode(conn.getHeaderField("Location").toString().substring((conn.getHeaderField("Location").lastIndexOf("/") + 1), conn.getHeaderField("Location").toString().length()), "UTF-8"));
						ModLinks.add(new URL(conn.getHeaderField("Location")));
						
					} catch (Exception e) {
						System.out.println("Error downloading mod: " + URLs.get(internal));
						e.printStackTrace();
						System.exit(1);
					}
				}
			}).start();
			
		}
		
		int lastCheck=0;
		boolean done = false;
		while(URLs.size() >= ModNames.size() && !done){
			if(ModNames.size() > lastCheck){
				System.out.print("Got Mod Download URL # " + ModNames.size() + "/" + URLs.size() + "\r");
				lastCheck = ModNames.size();
			}
			if(ModNames.size() == URLs.size()){
				System.out.println("Got Mod Download URL # " + ModNames.size() + "/" + URLs.size());
				done = true;
			}
			if(lastCheck == 0){
				System.out.print("Got Mod Download URL # 0/" + URLs.size() + "\r");
			}
		}
		
		//System.out.print(FileNames);
		
		downloadMods(ModLinks, FileNames, ModNames, folder);
		
	}
	
	private void downloadMods(ArrayList<URL> URLs, ArrayList<String> FileNames, ArrayList<String> ModNames, String folderName) throws Exception {
		
		ArrayList<String> Downloaded = new ArrayList<String>();
		
		for(int i = 0; i<URLs.size(); i++){
			
			final int internal = i;
			
			new Thread(new Runnable(){
				public void run(){
					try {
						
						//Finally Download the mod
						URLConnection conn = URLs.get(internal).openConnection();
						
						InputStream is = conn.getInputStream();
						FileOutputStream fos = new FileOutputStream(new File(folderName + FileNames.get(internal)));
						
						int inByte;
						while((inByte = is.read()) != -1){
							fos.write(inByte);
						}
						
						Downloaded.add(ModNames.get(internal));
						
						is.close();
						fos.close();
						
					} catch (Exception e) {
						
					}
				}
			}).start();
			
		}
		
		ArrayList<String> DownloadedShown = new ArrayList<String>();
		boolean done = false;
		while(!done){
			
			System.out.print("Downloaded Mods: " + Downloaded.size() + "/" + URLs.size() + "\r");
			
			if(Downloaded.size() == URLs.size()){
				System.out.print("Downloaded Mods: " + Downloaded.size() + "/" + URLs.size());
				done = true;
			}
			
		}
		
	}
	
	// Stuff only used for packs
	
	public void dlPack(String packName, int fileID, String folder) throws Exception{
		
		URL url = new URL("https://minecraft.curseforge.com/projects/" + packName);
		getDirectLink(url, fileID, folder);
		
	}
	
	private void getDirectLink(URL url, int fileID, String folder) throws Exception {
		
		// Get file name
		URLConnection conn = new URL(url +  "/files/" + fileID + "/download").openConnection();
		
		HttpURLConnection httpConn = (HttpURLConnection)conn;
		httpConn.setInstanceFollowRedirects(false);
		
		conn.connect();
		
		String fileName = conn.getHeaderField("Location").substring((conn.getHeaderField("Location").lastIndexOf("/") + 1), conn.getHeaderField("Location").length());
		String modName = URLDecoder.decode(conn.getHeaderField("Location").toString().substring((conn.getHeaderField("Location").lastIndexOf("/") + 1), conn.getHeaderField("Location").toString().length()), "UTF-8");
		URL directLink = new URL(conn.getHeaderField("Location"));
		
		downloadMod(directLink, URLDecoder.decode(fileName, "UTF-8"), modName, folder);
		
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
	
	// End Stuff only used for packs
}
