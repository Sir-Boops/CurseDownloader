package me.boops.cursedownloader.RemoteDealing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONArray;

public class Download {
	
	public Download(String DLLoc, JSONArray modList) throws Exception {
		
		// Create the mod folder
		new File(DLLoc).mkdir();
		
		// Setup the thread group / Download counter
		ThreadGroup dlGroup = new ThreadGroup("dlGroup");
		ArrayList<String> Downloaded = new ArrayList<String>();
		
		
		// Start the counting thread
		new Thread(new Runnable(){
			
			public void run() {
			
				boolean done = false;
				
				while(!done) {
					
					if(Downloaded.size() >= modList.length()) {
						
						// Done Downloading!
						System.out.println("Downloading Mod(s): " + Downloaded.size() + " - " + (Downloaded.size() + dlGroup.activeCount()) + " Of " + modList.length() + "\r");
						done = true;
						
					} else {
						
						System.out.print("Downloading Mod(s): " + Downloaded.size() + " - " + (Downloaded.size() + dlGroup.activeCount()) + " Of " + modList.length() + "\r");
						
					}
					
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
				return;
				
			}
		}).start();
		
		
		// Loop over all the mods to download them
		for(int i = 0; i < modList.length(); i++) {
			
			// Create a URL
			URL modURL = new URL("https://minecraft.curseforge.com/projects/" + modList.getJSONObject(i).getInt("projectID") + "/files/" + modList.getJSONObject(i).getInt("fileID") + "/download");
			
			// Make sure that there are always X threads
			while(dlGroup.activeCount() >= 6) {
				Thread.sleep(10);
			}
			
			// Start a thread to download the mod with
			new Thread(dlGroup, new Runnable() {
				public void run() {
					
					try {
						
						// Open the conection
						URLConnection conn = modURL.openConnection();
						conn.connect();
						
						// Setup the input stream (This follows all the redirects to the direct link)
						InputStream is = conn.getInputStream();
						
						// Get the fileName
						String fileName = URLDecoder.decode(conn.getURL().toString().substring(conn.getURL().toString().lastIndexOf('/') + 1), "UTF-8");
						
						// Setup the steam to the disk
						FileOutputStream fos = new FileOutputStream(new File(DLLoc + fileName));
						
						int inByte;
						while((inByte = is.read()) != -1){
							fos.write(inByte);
						}
						
						is.close();
						fos.close();
						
						System.out.println("Downloaded mod: " + fileName);
						Downloaded.add(fileName);
						return;
						
					} catch (FileNotFoundException e) {
						
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println("Missing mod: " + modURL);
						System.out.println("Please tell the pack creator!");
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}
		
		// Allow all the threads to finish downloading
		while(dlGroup.activeCount() > 0) {
			Thread.sleep(10);
		}
		
	}
}
