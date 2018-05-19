package me.boops.cursedownloader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.json.JSONObject;

import me.boops.cursedownloader.file.CreateFolder;
import me.boops.cursedownloader.file.ExtractOverrides;
import me.boops.cursedownloader.file.GrabMods;
import me.boops.cursedownloader.file.ReadManifest;
import me.boops.cursedownloader.remote.FetchFile;

public class Main {
	
	// Get A Random String for natives
	private static SecureRandom random = new SecureRandom();

	static public String HttpUser = "Mozilla/5.0 (X11; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0";
	static public String fullPath = "";
	static public String zipPath = "";
	
	public static void main(String[] args) throws Exception {
		
		// Set the temp folder dir and create it
		Main.fullPath = (new File(".").getCanonicalPath() + File.separator + ".temp-" + new BigInteger(32, Main.random).toString() + File.separator);
		new CreateFolder(Main.fullPath);
		
		// Download the requested modpack
		String zipFileName = "";
		if(args[0].contains("https://")) {
			zipFileName = new File(Main.fullPath + new FetchFile().fetch(Main.fullPath, args[0])).getAbsolutePath();
		} else {
			zipFileName = new File(args[0]).getAbsolutePath();
			System.out.println(zipFileName);
		}
		
		// Read the manifest from the pack zip
		JSONObject manifest = new ReadManifest().read(zipFileName);
		
		// Fetch all the mods
		new GrabMods(Main.fullPath + "mods" + File.separator, manifest.getJSONArray("files"), manifest.getJSONObject("minecraft").getString("version"));
		
		new ExtractOverrides(zipFileName);
		
		System.out.println(manifest.getString("name") + " Has finsihed downloading!");
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!");
		System.out.println("Use MC Version: " + manifest.getJSONObject("minecraft").getString("version"));
		System.out.println("Use Forge Version: " + manifest.getJSONObject("minecraft").getJSONArray("modLoaders")
				.getJSONObject(0).getString("id"));
		System.out.println("!!!!!!!!!!!!!!!!!!!!");
		
		File oldPath = new File(Main.fullPath);
		File newPath = new File("");
		try {
			
			if(args.length == 2) {
				if(args[1].equalsIgnoreCase("--mcboop")) {
					newPath = new File(System.getProperty("user.home") + File.separator + ".mcboop" + File.separator + "profiles"
							+ File.separator + manifest.getString("name") + "-" + manifest.getString("version") + File.separator);
					System.out.println("Saved to McBoops profiles folder you can now launch it using: McBoop --profile " + manifest.getString("name") + "-" + manifest.getString("version"));
					
				} else {
					newPath = new File(new File(".").getCanonicalPath() + File.separator + manifest.getString("name") + "-" + manifest.getString("version") + File.separator);
				}
			} else {
				newPath = new File(new File(".").getCanonicalPath() + File.separator + manifest.getString("name") + "-" + manifest.getString("version") + File.separator);
			}
			
			oldPath.renameTo(newPath);
			System.out.println("Folder saved to: " + newPath);
			
			if(args.length == 2) {
				if(args[1].equalsIgnoreCase("--mcboop")) {
					JSONObject profile = new JSONObject().put("mcVersion", manifest.getJSONObject("minecraft").getString("version"))
							.put("forgeVersion", manifest.getJSONObject("minecraft").getJSONArray("modLoaders").getJSONObject(0).getString("id"));
					
					File profileJSON = new File(newPath + File.separator + "profile.json");
					BufferedWriter out = new BufferedWriter(new FileWriter(profileJSON));
					out.write(profile.toString());
					out.close();
					
					System.out.println("Saved to McBoops profiles folder you can now launch it using: McBoop --profile \"" + manifest.getString("name") + "-" + manifest.getString("version") + "\"");
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
