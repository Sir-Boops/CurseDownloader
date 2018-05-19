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
            HttpsURLConnection conn = (HttpsURLConnection) new URL(getRedir(URL)).openConnection();
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

            FileOutputStream fos = new FileOutputStream(new File(path + fileName));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int inByte;
            while ((inByte = is.read()) != -1) {
                bos.write(inByte);
            }

            fos.write(bos.toByteArray());

            fos.close();
            bos.close();
            is.close();

        } catch (Exception e) {
            ans = "false";
        }

        return ans;
    }

    private String getRedir(String url) throws Exception {
        HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
        conn.setReadTimeout(10 * 1000);
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", Main.HttpUser);
        conn.setInstanceFollowRedirects(false);

        InputStream is = conn.getInputStream();
        is.close();
        String ans = "";

        // If code is not 200 go deeper
        if (conn.getResponseCode() > 200 && conn.getResponseCode() < 400) {
            ans = getRedir(cleanURL(conn.getHeaderField("Location")));
        }

        // If code is 200 return this URL
        if (conn.getResponseCode() == 200) {
            ans = cleanURL(conn.getURL().toString());
        }
        return ans;
    }

    private String cleanURL(String url) throws Exception {
        char[] url_arr = url.toCharArray();
        String ans = "";
        for (int i = 0; i < url_arr.length; i++) {
            String some_char = String.valueOf(url_arr[i]);
            if (some_char.equals(" ")) {
                ans += "%20";
            } else {
                ans += some_char;
            }
        }
        return ans;
    }
}
