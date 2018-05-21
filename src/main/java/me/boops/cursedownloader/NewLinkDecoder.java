package me.boops.cursedownloader;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import me.boops.cursedownloader.remote.FetchContent;
import me.boops.cursedownloader.remote.ResolvURL;

public class NewLinkDecoder {

    public String get_url(String url) throws Exception {
        String ccip_path = get_ccip_url(url);
        String ccip_xml = new FetchContent().fetchRemoteText("https://www.curseforge.com" + ccip_path);
        int[] ids = get_ids(ccip_xml);
        String final_url = new ResolvURL().resolv_url("https://minecraft.curseforge.com/projects/" + ids[0]) + "/files/" + ids[1] + "/download";
        if(!final_url.contains("https") && !final_url.contains("http")) {
            final_url = ("https://minecraft.curseforge.com" + final_url);
        }
        return final_url;
    }

    private String get_ccip_url(String url) throws Exception {

        Document doc = Jsoup.connect(url).header("User-Agent", Main.HttpUser).get();
        Elements ele = doc.getAllElements();
        String ccip_url = "";

        for (int i = 0; i < ele.size(); i++) {
            if (ele.get(i).hasClass("download__link")) {
                ccip_url = ele.get(i).attr("href");
            }
        }
        return ccip_url;
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
}
