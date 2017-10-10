package me.boops.cursedownloader.LocalDealing;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;

public class Manifest {
	
	// Private Vars
	private String mcVersion;
	private String forgeVersion;
	private String packName;
	private String packVersion;
	private String authorName;
	private JSONArray mods;
	
	public String getMCVersion(){
		return mcVersion;
	}
	
	public String getForgeVersion(){
		return forgeVersion;
	}
	
	public String getPackAuthor(){
		return authorName;
	}
	
	public String getPackName(){
		return packName;
	}
	
	public String getPackVersion(){
		return packVersion;
	}
	
	public JSONArray getModList(){
		return mods;
	}
	
	public void readManifest(String file) throws Exception{

		StringBuilder sb = new StringBuilder();
		
		BufferedReader br = new BufferedReader(new FileReader(file + "manifest.json"));
		String line = br.readLine();
		
		while (line != null) {
			sb.append(line);
			line = br.readLine();
		}
		
		br.close();
		
		JSONObject manifest = new JSONObject(sb.toString());
		
		// Extract data out of the JSON
		
		this.mcVersion = manifest.getJSONObject("minecraft").getString("version");
		this.forgeVersion = manifest.getJSONObject("minecraft").getJSONArray("modLoaders").getJSONObject(0).getString("id");
		this.packName = manifest.getString("name");
		this.packVersion = manifest.getString("version");
		this.authorName = manifest.getString("author");
		this.mods = manifest.getJSONArray("files");
		
	}
}
