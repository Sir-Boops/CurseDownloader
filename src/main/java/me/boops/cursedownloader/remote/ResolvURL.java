package me.boops.cursedownloader.remote;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.boops.cursedownloader.Main;

public class ResolvURL {
    public String resolv_url(String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setReadTimeout(10 * 1000);
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", Main.HttpUser);
        conn.setInstanceFollowRedirects(false);

        InputStream is = conn.getInputStream();
        is.close();
        String ans = "";
        
        // If it dosn't contain a full URL then we're also good
        // Just return
        if(conn.getHeaderFields().containsKey("Location")) {
            if(!conn.getHeaderField("Location").contains("https") && !conn.getHeaderField("Location").contains("http")) {
                return cleanURL(conn.getHeaderField("Location"));
            }
        }

        // If code is not 200 go deeper
        if (conn.getResponseCode() > 200 && conn.getResponseCode() < 400) {
            ans = resolv_url(cleanURL(conn.getHeaderField("Location")));
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
