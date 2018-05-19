package me.boops.cursedownloader.threads;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import me.boops.cursedownloader.CurseMCMappings;
import me.boops.cursedownloader.file.CreateFolder;
import me.boops.cursedownloader.remote.FetchFile;

public class DownloadModThread implements Runnable {
	
	private String path;
	private String url;
	private String mc_version;
	
	public DownloadModThread(String path, String url, String mc_version) {
		this.path = path;
		this.url = url;
		this.mc_version = mc_version;
	}
	
	@Override
	public void run() {
		
		new CreateFolder(this.path);
		String res = new FetchFile().fetch(this.path, this.url);
		
		if(res.equalsIgnoreCase("false")) {
			
			System.out.println("Trying alternative version for -> " + this.url);
			
			// Mod failed to download
			// So get the latest version of the mod
			// For this version of MC
			
			String version_id = "";
			int runs = 0;
			while(CurseMCMappings.mc_mappings.length > runs && version_id.isEmpty()) {
				if(CurseMCMappings.mc_mappings[runs].equals(mc_version)) {
					version_id = CurseMCMappings.mc_mappings[runs + 1];
				}
				runs++;
			}
		
			try {
				// Get all the mod versions for this MC version
				Document doc = Jsoup.connect(url.split("/files/")[0] + "?filter-game-version=" + version_id).get();
				
				// Find all the download links for the mod jars
				Elements ele = doc.getAllElements();
				Elements links = new Elements();
				for(int i = 0; i < ele.size(); i++) {
					if(ele.get(i).hasClass("fa-icon-download")) {
						links.add(ele.get(i));
					}
				}
				
				// The second link is always the newest
				// For this version
				String new_url = "https://minecraft.curseforge.com" + links.get(1).attr("href");
				String new_res = new FetchFile().fetch(this.path, new_url);
				
				if(new_res.equalsIgnoreCase("false")) {
					System.out.println("Failed to download -> " + this.url);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}
