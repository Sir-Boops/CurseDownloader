package me.boops.cursedownloader.file;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import me.boops.cursedownloader.remote.FetchFile;
import me.boops.cursedownloader.remote.FetchRealURL;

public class GrabMods {
	
	public static List<String> urls = new ArrayList<String>();
	
	public GrabMods(String path, JSONArray mods) throws Exception {
		
		ThreadGroup dlGroup = new ThreadGroup("dlGroup");
		ThreadGroup rsGroup = new ThreadGroup("rsGroup");
		
		for(int i = 0; i < mods.length(); i++) {
			
			while(rsGroup.activeCount() > 49) {
				Thread.sleep(10);
			}
			
			int i2 = i;
			System.out.println("Getting mod name for: " + mods.getJSONObject(i2).getInt("projectID"));
			
			new Thread(rsGroup, new Runnable() {
				public void run() {
					
					GrabMods.urls.add(new FetchRealURL().fetch("https://minecraft.curseforge.com/mc-mods/" + mods.getJSONObject(i2).getInt("projectID"))
							+ "/files/" + mods.getJSONObject(i2).getInt("fileID") + "/download");
					
				}
			}).start();
		}
		
		while(rsGroup.activeCount() > 0) {
			Thread.sleep(10);
		}
		
		for(int i = 0; i < GrabMods.urls.size(); i++) {
			
			while(dlGroup.activeCount() > 19) {
				Thread.sleep(10);
			}
			
			String URL = GrabMods.urls.get(i);
			
			new Thread(dlGroup, new Runnable() {
				public void run() {
					
					new CreateFolder(path);
					new FetchFile().fetch(path, URL);
					
				}
			}).start();	
			
		}
		
		while(dlGroup.activeCount() > 0) {
			Thread.sleep(10);
		}
	}
}
