package me.boops.cursedownloader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class NewLinkDecoder {

    public String get_url(String url) throws Exception {
        String ccip_url = get_ccip_url(url);
        String ccip_xml = get_ccip_text("https://www.curseforge.com" + ccip_url);
        int[] ids = get_ids(ccip_xml);
        String final_url = getRedir("https://minecraft.curseforge.com/projects/" + ids[0]) + "/files/" + ids[1] + "/download";
        return final_url;
    }

    private String get_ccip_url(String url) throws Exception {

        Document doc = Jsoup.connect(url).get();
        Elements ele = doc.getAllElements();
        String ccip_url = "";

        for (int i = 0; i < ele.size(); i++) {
            if (ele.get(i).hasClass("download__link")) {
                ccip_url = ele.get(i).attr("href");
            }
        }
        return ccip_url;
    }

    private String get_ccip_text(String ccip_url) throws Exception {
        URL url = new URL(ccip_url);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setReadTimeout(10 * 1000);
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", Main.HttpUser);

        conn.connect();

        InputStream is = conn.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int inByte;
        while ((inByte = is.read()) != -1) {
            bos.write(inByte);
        }

        String ans = new String(bos.toByteArray());

        bos.close();
        is.close();
        return ans;

    }

    private int[] get_ids(String xml) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        
        org.w3c.dom.Document doc = db.parse(is);
        NodeList ele = doc.getElementsByTagName("project");
        // 0 => id 1 => file_id
        return new int[] {Integer.parseInt(ele.item(0).getAttributes().getNamedItem("id").getTextContent()), Integer.parseInt(ele.item(0).getAttributes().getNamedItem("file").getTextContent())};
    }
    
    private String getRedir(String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
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
