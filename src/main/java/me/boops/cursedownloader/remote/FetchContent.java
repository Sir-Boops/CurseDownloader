package me.boops.cursedownloader.remote;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;

import javax.net.ssl.HttpsURLConnection;

import me.boops.cursedownloader.Main;

public class FetchContent {
    
    public String fetchRemoteText(String url) {
        return fetch("{string}", url);
    }

    public String fetch(String path, String URL) {
        String ans = "";
        try {
            HttpsURLConnection conn = (HttpsURLConnection) new URL(new ResolvURL().resolv_url(URL)).openConnection();
            conn.setReadTimeout(10 * 1000);
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", Main.HttpUser);
            conn.setInstanceFollowRedirects(false);

            InputStream is = conn.getInputStream();

            new URLDecoder();
            String fileName = URLDecoder.decode(conn.getURL().toString().substring(conn.getURL().toString().lastIndexOf("/") + 1, conn.getURL().toString().length()), "UTF-8");

            ans = fileName;

            System.out.println("Downloading: " + fileName);
            
            FileOutputStream fos = null;
            if(!path.equals("{string}")) {
                fos = new FileOutputStream(new File(path + fileName));
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int inByte;
            while ((inByte = is.read()) != -1) {
                bos.write(inByte);
            }
            
            if(path.equals("{string}")) {
                ans = new String(bos.toByteArray());
            } else {
                fos.write(bos.toByteArray());
                fos.close();
            }

            bos.close();
            is.close();

        } catch (Exception e) {
            ans = "false";
            e.printStackTrace();
        }

        return ans;
    }
}
