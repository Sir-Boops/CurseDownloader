package me.boops.cursedownloader.remote;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import me.boops.cursedownloader.Main;

public class FetchRealURL {
	public String fetch(String URL) {
		String ans = "";
		
		try {
			URL url = new URL(URL);

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setReadTimeout(10 * 1000);
			conn.setConnectTimeout(10 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", Main.HttpUser);

			InputStream is = conn.getInputStream();
			
			ans = conn.getURL().toString();
			
			is.close();
			
		} catch(Exception e) {}
		return ans;
	}
}
