package me.boops.cursedownloader;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

@SuppressWarnings("deprecation")
public class GetMod {
	
	public void Download(String folder, JSONArray mods) throws ClientProtocolException, IOException {
		
		for (int i=0; i< mods.length(); i++) {
			
			new File(folder).mkdirs();
			
			HttpParams params = new BasicHttpParams();
			params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
			
			String url = "https://minecraft.curseforge.com/projects/" + mods.getJSONObject(i).getInt("projectID") + "?cookieTest=1";
			HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
			HttpGet get = new HttpGet(url);
			get.setParams(params);
			HttpResponse res = null;
			
			res = client.execute(get);
			String locationHeader = res.getFirstHeader("Location").toString();
			String modName = locationHeader.substring((locationHeader.lastIndexOf("/") + 1), locationHeader.length());
			
			new GetModFileName().getMod(modName, mods.getJSONObject(i).getInt("fileID"), folder);
			
		}
	}
}
