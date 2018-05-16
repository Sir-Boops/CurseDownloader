package me.boops.cursedownloader.remote;

import java.io.ByteArrayOutputStream;
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
			URL url = new URL(URL);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setReadTimeout(10 * 1000);
			conn.setConnectTimeout(10 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", Main.HttpUser);
			conn.setInstanceFollowRedirects(true);
			
			InputStream is = conn.getInputStream();
			is.close();
			
			conn = (HttpsURLConnection) conn.getURL().openConnection();
			is = conn.getInputStream();
			
			new URLDecoder();
			String fileName = URLDecoder.decode(conn.getURL().toString().substring(conn.getURL().toString().lastIndexOf("/") + 1,
					conn.getURL().toString().length()), "UTF-8");
			
			ans = fileName;
			
			System.out.println("Downloading: " + fileName);
			
			FileOutputStream fos = new FileOutputStream(new File(path + fileName));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			int inByte;
			while((inByte = is.read()) != -1) {
				bos.write(inByte);
			}
			
			fos.write(bos.toByteArray());
			
			fos.close();
			bos.close();
			is.close();
			
		} catch(Exception e) {
			System.out.println("Failed to download: " + URL);
			System.out.println("Tell your pack author!");
		}
		
		return ans;
	}
}
