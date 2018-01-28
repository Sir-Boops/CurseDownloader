package me.boops.cursedownloader;

import java.io.File;
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
	
	public static void main(String[] args) {
		
		// Set the temp folder dir
		try {
			Main.fullPath = (new File(".").getCanonicalPath() + File.separator + ".temp-" + new BigInteger(32, Main.random).toString() + File.separator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Create the temp folder
		new CreateFolder(Main.fullPath);
		
		// Download the requested modpack
		String zipFileName = new FetchFile().fetch(Main.fullPath, args[0]);
		
		// Read the manifest from the pack zip
		JSONObject manifest = new ReadManifest().read(Main.fullPath, zipFileName);
		
		// Fetch all the mods
		new GrabMods(Main.fullPath + "mods" + File.separator, manifest.getJSONArray("files"));
		
		new ExtractOverrides(Main.fullPath, zipFileName);
		
		System.out.println(manifest.getString("name") + " Has finsihed downloading!");
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!");
		System.out.println("Use MC Version: " + manifest.getJSONObject("minecraft").getString("version"));
		System.out.println("Use Forge Version: " + manifest.getJSONObject("minecraft").getJSONArray("modloaders")
				.getJSONObject(0).getString("id"));
		System.out.println("!!!!!!!!!!!!!!!!!!!!");
		
		File oldPath = new File(Main.fullPath);
		File newPath;
		try {
			newPath = new File(new File(".").getCanonicalPath() + File.separator + manifest.getString("name") + "-" + manifest.getString("version") + File.separator);
			
			oldPath.renameTo(newPath);
			System.out.println("Folder saved to: " + newPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
