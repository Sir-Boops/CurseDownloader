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
	
	public void downloadMod(String filename, String name, int fileID, String path) throws ClientProtocolException, IOException{
		
		
		String url = "https://minecraft.curseforge.com/projects/" + name + "/files/" + fileID + "/download";
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
		HttpGet get = new HttpGet(url);
		HttpResponse res = null;
		res = client.execute(get);
		
		System.out.println("Downloading mod -> '" + name + "' to -> '" + filename + "'");
		
		HttpEntity entity = res.getEntity();
		
		InputStream is = entity.getContent();
		FileOutputStream fos = new FileOutputStream(new File(path + filename));
		
		int inByte;
		while ((inByte = is.read()) != -1){
			fos.write(inByte);
		}
		
		is.close();
		fos.close();
	}
}
