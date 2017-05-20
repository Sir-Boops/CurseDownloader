package me.boops.cursedownloader;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

@SuppressWarnings("deprecation")
public class GetFileName {
	
	public void getMod(String name, int fileID, String path) throws ClientProtocolException, IOException {
		
		HttpParams params = new BasicHttpParams();
		params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
		
		String url = "https://minecraft.curseforge.com/projects/" + name + "/files/" + fileID + "/download?cookieTest=1";
		HttpClient client = HttpClients.custom().setSSLHostnameVerifier(new DefaultHostnameVerifier()).build();
		HttpGet get = new HttpGet(url);
		get.setParams(params);
		HttpResponse res = null;
		res = client.execute(get);
		
		String locationHeader = res.getFirstHeader("Location").toString();
		String modName = URLDecoder.decode(locationHeader.substring((locationHeader.lastIndexOf("/") + 1), locationHeader.length()), "UTF-8");
		
		String URL = res.getFirstHeader("Location").toString().split(" ")[1];
		String fileName = URLEncoder.encode(URL.substring((URL.lastIndexOf("/") + 1), URL.length()), "UTF-8");
		URL = ((URL.substring(0, (URL.lastIndexOf("/") + 1)).replaceFirst("http", "https")) + fileName);

		new Download().downloadMod(modName, name, fileID, path, URL);
		
	}
}
