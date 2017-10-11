package me.boops.cursedownloader.LocalDealing;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.json.JSONObject;

public class ReadForgeVersionJSON {
	
	public JSONObject getJSON(String mcVersion, String forgeVersion) throws Exception {
		
		String myPath = new File(".").getAbsolutePath();
		myPath = myPath.substring(0, (myPath.length() - 1));
		
		String fileName = ("forge-" + mcVersion + "-" + forgeVersion + "-universal.jar");
		
		JarFile forgeJar = new JarFile(myPath + fileName);
		JarEntry versionJSON = forgeJar.getJarEntry("version.json");
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(forgeJar.getInputStream(versionJSON)));
		
		String line = "";
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		
		br.close();
		forgeJar.close();
		
		return new JSONObject(sb.toString());
		
	}
}
