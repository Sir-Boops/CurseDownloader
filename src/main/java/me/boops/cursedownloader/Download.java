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
		
		ThreadGroup IDGroup = new ThreadGroup("IDGroup");
		
		new Thread(new Runnable(){
			public void run(){
				try {
					
					for (int i=0; i<mods.length(); i++) {
						
						final int internal = i;
						
						while(IDGroup.activeCount() > 10){
							Thread.currentThread();
							Thread.sleep(10);
						}
						
						new Thread(IDGroup, new Runnable(){
							public void run(){
								try {
									
									// Convert IDs
									URLConnection conn = new URL("https://minecraft.curseforge.com/projects/" + mods.getJSONObject(internal).getInt("projectID")).openConnection();
									conn.setConnectTimeout(20 * 1000);
									conn.connect();
									conn.getInputStream();
									
									ModNames.add(conn.getURL());
									ModFileIDs.add(mods.getJSONObject(internal).getInt("fileID"));
									//System.out.println("Mod ID: " + mods.getJSONObject(internal).getInt("projectID") + " is mod " +
									//		conn.getURL().toString().substring(conn.getURL().toString().lastIndexOf('/') + 1, conn.getURL().toString().length()) + " " +
									//		internal + "/" + mods.length());
									return;
									
								} catch (Exception e) {
									System.out.println("Error downloading mod: " + mods.getJSONObject(internal).getInt("projectID"));
									e.printStackTrace();
									System.exit(1);
								}
							}
						}).start();
						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		
		boolean done = false;
		System.out.print("Converted Mod ID # 0/" + mods.length() + "\r");
		
		while(!done){
			
			System.out.print("Converted Mod ID # " + ModNames.size() + "/" + mods.length() + "\r");
			
			if(ModNames.size() == mods.length()){
				System.out.println("Converted Mod ID # " + ModNames.size() + "/" + mods.length());
				done = true;
			}

		}
		
		getDirectLinks(ModNames, ModFileIDs, folder);
		
	}
	
	private void getDirectLinks(ArrayList<URL> URLs, ArrayList<Integer> FileIDs, String folder) throws Exception {
		
		ArrayList<String> FileNames = new ArrayList<String>();
		ArrayList<URL> ModLinks = new ArrayList<URL>();
		
		System.out.println("Getting mod direct download links");
		ThreadGroup DLGroup = new ThreadGroup("DLGroup");
		
		new Thread(new Runnable(){
			public void run(){
				try {
					
					for(int i = 0; i<URLs.size(); i++){
						
						final int internal = i;
						
						while(DLGroup.activeCount() > 10){
							Thread.currentThread();
							Thread.sleep(10);
						}
						
						new Thread(DLGroup, new Runnable(){
							public void run(){
								try {
									
									// Get file name
									URLConnection conn = new URL(URLs.get(internal) +  "/files/" + FileIDs.get(internal) + "/download").openConnection();
									
									HttpURLConnection httpConn = (HttpURLConnection)conn;
									httpConn.setInstanceFollowRedirects(false);
									
									conn.connect();
									
									FileNames.add(URLDecoder.decode(conn.getHeaderField("Location").substring((conn.getHeaderField("Location").lastIndexOf("/") + 1), conn.getHeaderField("Location").length()), "UTF-8"));
									ModLinks.add(new URL(conn.getHeaderField("Location")));
									return;
									
								} catch (Exception e) {
									System.out.println("Error downloading mod: " + URLs.get(internal));
									e.printStackTrace();
									System.exit(1);
								}
							}
						}).start();
						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		int lastCheck=0;
		boolean done = false;
		while(URLs.size() >= FileNames.size() && !done){
			if(FileNames.size() > lastCheck){
				System.out.print("Got Mod Download URL # " + FileNames.size() + "/" + URLs.size() + "\r");
				lastCheck = FileNames.size();
			}
			if(FileNames.size() == URLs.size()){
				System.out.println("Got Mod Download URL # " + FileNames.size() + "/" + URLs.size());
				done = true;
			}
			if(lastCheck == 0){
				System.out.print("Got Mod Download URL # 0/" + URLs.size() + "\r");
			}
		}
		
		//System.out.print(FileNames);
		
		downloadMods(ModLinks, FileNames, folder);
		
	}
	
	private void downloadMods(ArrayList<URL> URLs, ArrayList<String> FileNames, String folderName) throws Exception {
		
		ArrayList<String> Downloaded = new ArrayList<String>();
		System.out.println("Downloading Mods");
		
		ThreadGroup DLGroup = new ThreadGroup("DLGroup");
		
		new Thread(new Runnable(){
			public void run(){
				try {
					
					for(int i = 0; i < URLs.size(); i++){
						
						final int internal = i;
						
						while(DLGroup.activeCount() > 6){
							Thread.currentThread();
							Thread.sleep(10);
						}
						
						new Thread(DLGroup, new Runnable(){
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
									
									Downloaded.add(FileNames.get(internal));
									System.out.println("Downloaded Mod " + Downloaded.size() + "/" + URLs.size() + " -> " + FileNames.get(internal));
									
									is.close();
									fos.close();
									
									return;
									
								} catch (Exception e) {
									e.printStackTrace();
									System.exit(1);
								}
							}
						}).start();
						
					}
					
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}).start();
		
		boolean done = false;
		while(!done){
			
			System.out.print("Downloading Mod: " + (Downloaded.size() + 1) + "/" + URLs.size() + "\r");
			
			if(Downloaded.size() == URLs.size()){
				System.out.println("Downloaded Mods: " + Downloaded.size() + "/" + URLs.size());
				done = true;
			}
			
		}
		
	}
}
