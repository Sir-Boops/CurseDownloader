package me.boops.cursedownloader.file;

import org.json.JSONArray;

import me.boops.cursedownloader.remote.FetchFile;
import me.boops.cursedownloader.remote.FetchRealURL;

public class GrabMods {
	public GrabMods(String path, JSONArray mods) {
		
		ThreadGroup dlGroup = new ThreadGroup("dlGroup");
		
		for(int i = 0; i < mods.length(); i++) {
			String URL = (new FetchRealURL().fetch("https://minecraft.curseforge.com/mc-mods/" + mods.getJSONObject(i).getInt("projectID"))
					+ "/files/" + mods.getJSONObject(i).getInt("fileID") + "/download");
			
			while(dlGroup.activeCount() > 10) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("Downloading: " + URL);
			
			new Thread(dlGroup, new Runnable() {
				public void run() {
					
					new CreateFolder(path);
					new FetchFile().fetch(path, URL);
					return;
					
				}
			}).start();

		}
		
		while(dlGroup.activeCount() > 0) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
