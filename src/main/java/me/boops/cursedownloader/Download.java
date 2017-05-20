package me.boops.cursedownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.HttpClients;

public class Download {
	
	public void downloadMod(String filename, String name, int fileID, String path, String URL) throws ClientProtocolException, IOException{
		
		
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
		HttpGet get = new HttpGet(URL);
		HttpResponse res = null;
		res = client.execute(get);
		
		HttpEntity entity = res.getEntity();
		
		InputStream is = entity.getContent();
		FileOutputStream fos = new FileOutputStream(new File(path + filename));
		
		double fileBytes;
		
		if(res.containsHeader("Content-Length")){
			
			fileBytes = Integer.parseInt(res.getFirstHeader("Content-Length").toString().split(" ")[1]);
			
		} else {
			
			fileBytes = 1.0d;
			
		}
		
		int inByte;
		double totalBytes = 0;
		int lastProgress = 0;
		while ((inByte = is.read()) != -1){
			fos.write(inByte);
			totalBytes++;

			int percent = ((int) ((totalBytes / fileBytes) * 100));

			if(percent > lastProgress || lastProgress == 0){

				lastProgress = percent;
				System.out.print("Downloading: " + filename + " - File size: " + ((int) fileBytes/1024) + " KB - Progress: " + percent + "%\r");

			}
		}
		
		System.out.println("Downloading: " + filename + " - File size: " + ((int) fileBytes/1024) + " KB - Progress: 100%");
		
		is.close();
		fos.close();
	}
}