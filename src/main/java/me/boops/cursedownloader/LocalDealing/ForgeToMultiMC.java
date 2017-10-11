package me.boops.cursedownloader.LocalDealing;

import org.json.JSONArray;
import org.json.JSONObject;

public class ForgeToMultiMC {
	
	public JSONObject convertJSON(JSONObject forgeJSON) {
		
		JSONObject mainMultiMC = new JSONObject();
		JSONArray mainLibs = new JSONArray();
		JSONArray mainTweaks = new JSONArray();
		
		// Add the libs to the MultiMC json
		JSONArray forgeJSONLibs = forgeJSON.getJSONArray("libraries");
		
		for(int i = 0; i < forgeJSONLibs.length(); i++) {
			
			JSONObject lib = new JSONObject();
			
			if(forgeJSONLibs.getJSONObject(i).getString("name").contains("net.minecraftforge")) {
				lib.put("name", (forgeJSONLibs.getJSONObject(i).getString("name") + ":universal"));
			} else {
				lib.put("name", forgeJSONLibs.getJSONObject(i).getString("name"));
			}
			
			if(forgeJSONLibs.getJSONObject(i).has("checksums")) {
				lib.put("MMC-hint", "forge-pack-xz");
			}
			if(forgeJSONLibs.getJSONObject(i).has("url")) {
				lib.put("url", forgeJSONLibs.getJSONObject(i).getString("url"));
			}
			
			mainLibs.put(lib);
		}
		
		// Add tweaks to the MultiMC JSON
		String[] args = forgeJSON.getString("minecraftArguments").split(" ");
		for(int i = 0; i < args.length; i++) {
			
			if(args[i].equalsIgnoreCase("--tweakClass")) {
				
				mainTweaks.put(args[i + 1]);
			}
		}
		
		// Add everything to the main JSONObject now
		
		// Get forge fileID and version
		String[] nameArgs = null;
		for(int i = 0; i < mainLibs.length(); i++) {
			
			if(mainLibs.getJSONObject(i).getString("name").contains("net.minecraftforge")) {
				
				nameArgs = mainLibs.getJSONObject(i).getString("name").split(":");
			}
		}
		
		// Finally put everything into the main JSON
		mainMultiMC.put("+libraries", mainLibs);
		mainMultiMC.put("+tweakers", mainTweaks);
		mainMultiMC.put("fileID", nameArgs[0]);
		mainMultiMC.put("mainClass", forgeJSON.getString("mainClass"));
		mainMultiMC.put("mcVersion", forgeJSON.getString("jar"));
		mainMultiMC.put("name", "Forge");
		mainMultiMC.put("order", 5);
		mainMultiMC.put("version", nameArgs[2]);
		
		return mainMultiMC;
		
	}
}
