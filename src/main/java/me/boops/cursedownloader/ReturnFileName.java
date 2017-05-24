package me.boops.cursedownloader;

import java.net.URL;
import java.net.URLConnection;

public class ReturnFileName {
	
	public String getMod(String name, int fileID, String path) throws Exception {
		
		URLConnection conn = new URL("https://minecraft.curseforge.com/projects/" + name + "/files/" + fileID + "/download").openConnection();
		conn.connect();
		
		String modName = conn.getHeaderField("Location").toString().substring((conn.getHeaderField("Location").lastIndexOf("/") + 1), conn.getHeaderField("Location").toString().length());
		
		return modName;
		
	}
	
}
