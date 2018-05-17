package me.boops.cursedownloader.file;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import me.boops.cursedownloader.threads.DownloadModThread;
import me.boops.cursedownloader.threads.URLGenThread;

public class GrabMods {
	
	public static List<String> urls = new ArrayList<String>();
	
	public GrabMods(String path, JSONArray mods) throws Exception {
		
		// Gen the list of links
		genLinks(mods);
		
		// Loop over all the URLs and download them
		ThreadGroup dlGroup = new ThreadGroup("dlGroup");
		for(int i = 0; i < GrabMods.urls.size(); i++) {
			
			// Limit to ten threads
			while(dlGroup.activeCount() > 9) {
				Thread.sleep(10);
			}
			
			// Start a new thread
			Thread thread = new Thread(dlGroup, new DownloadModThread(path, GrabMods.urls.get(i)));
			thread.start();
			
		}
		
		// Wait for all threads
		while(dlGroup.activeCount() > 0) {
			Thread.sleep(10);
		}
	}
	
	private void genLinks(JSONArray mods) throws Exception{
		ThreadGroup rsGroup = new ThreadGroup("rsGroup");
		
		// Loop over all the current URLs to get DL links
		for(int i = 0; i < mods.length(); i++) {
			
			// Only allow 50 threads
			while(rsGroup.activeCount() > 49) {
				Thread.sleep(10);
			}
			System.out.println("Getting mod name for: " + mods.getJSONObject(i).getInt("projectID"));
			
			// Start the thread
			Thread thread = new Thread(rsGroup, new URLGenThread(mods.getJSONObject(i).getInt("projectID"), mods.getJSONObject(i).getInt("fileID")));
			thread.start();
		}
		
		// Wait for all threads to finish
		while(rsGroup.activeCount() > 0) {
			Thread.sleep(10);
		}
	}
}
