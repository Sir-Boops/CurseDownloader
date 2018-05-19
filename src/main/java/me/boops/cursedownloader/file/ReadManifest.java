package me.boops.cursedownloader.file;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipFile;

import org.json.JSONObject;

public class ReadManifest {

    public JSONObject read(String fullPath) throws Exception {

        JSONObject ans = new JSONObject();
        ZipFile packZip = new ZipFile(fullPath);
        InputStream is = packZip.getInputStream(packZip.getEntry("manifest.json"));

        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String inByte;

        while ((inByte = in.readLine()) != null) {
            sb.append(inByte);
        }

        is.close();
        packZip.close();

        ans = new JSONObject(sb.toString());

        return ans;
    }

}
