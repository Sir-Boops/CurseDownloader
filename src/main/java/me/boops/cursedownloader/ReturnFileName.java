package me.boops.cursedownloader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class ReturnFileName {
	
	public String getMod(String name, int fileID, String path) throws Exception {
		
		URLConnection conn = new URL("https://minecraft.curseforge.com/projects/" + name + "/files/" + fileID + "/download").openConnection();
		
		HttpURLConnection httpConn = (HttpURLConnection)conn;
		httpConn.setInstanceFollowRedirects(false);
		
		conn.connect();
		
		String modName = URLDecoder.decode(conn.getHeaderField("Location").toString().substring((conn.getHeaderField("Location").lastIndexOf("/") + 1), conn.getHeaderField("Location").toString().length()), "UTF-8");
		
		return modName;
		
	}
	
}
