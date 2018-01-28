package me.boops.cursedownloader.file;

import org.json.JSONArray;

import me.boops.cursedownloader.remote.FetchFile;
import me.boops.cursedownloader.remote.FetchRealURL;

public class GrabMods {
	public GrabMods(String path, JSONArray mods) {
		
		for(int i = 0; i < mods.length(); i++) {
			String URL = (new FetchRealURL().fetch("https://minecraft.curseforge.com/mc-mods/" + mods.getJSONObject(i).getInt("projectID"))
					+ "/files/" + mods.getJSONObject(i).getInt("fileID") + "/download");
			
			System.out.println("Downloading: " + URL);
			
			new CreateFolder(path);
			new FetchFile().fetch(path, URL);
		}
		
	}
}
