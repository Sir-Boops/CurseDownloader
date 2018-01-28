package me.boops.cursedownloader.remote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;

import javax.net.ssl.HttpsURLConnection;

import me.boops.cursedownloader.Main;

public class FetchFile {
	
	public String fetch(String path, String URL) {
		String ans = "";
		
		try {
			
			URL url = new URL(getRealURL(URL));

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setReadTimeout(10 * 1000);
			conn.setConnectTimeout(10 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", Main.HttpUser);
			conn.setInstanceFollowRedirects(true);
			
			InputStream is = conn.getInputStream();
			
			
			new URLDecoder();
			String fileName = URLDecoder.decode(conn.getURL().toString().substring(conn.getURL().toString().lastIndexOf("/") + 1,
					conn.getURL().toString().length()), "UTF-8");
			
			ans = fileName;
			
			FileOutputStream fos = new FileOutputStream(new File(path + fileName));
			
			int inByte;
			while((inByte = is.read()) != -1) {
				fos.write(inByte);
			}
			
			fos.close();
			is.close();
			
		} catch(Exception e) {
			System.out.println("Failed to download: " + URL);
			System.out.println("Tell your pack author!");
		}
		
		return ans;
	}
	
	private String getRealURL(String URL) {
		String ans = "";
		
		try {
			
			URL url = new URL(URL);

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setReadTimeout(10 * 1000);
			conn.setConnectTimeout(10 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", Main.HttpUser);
			conn.setInstanceFollowRedirects(false);
			
			String locHeader = conn.getHeaderField("Location");
			String fileURL = (locHeader.substring(0, locHeader.lastIndexOf("/") + 1));
			String fileName = (locHeader.substring(locHeader.lastIndexOf("/") + 1, locHeader.length()));
			
			ans = fileURL + fileName.replaceAll(" ", "%20");
			
		} catch(Exception e) {
		}
		
		return ans;
	}
}
