package me.boops.cursedownloader.initals;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;

import me.boops.cursedownloader.PackInstall;
import me.boops.cursedownloader.RemoteDealing.FetchPackZip;

public class InitFetch {
	
	public InitFetch(URL URL) {
		
		// We want to grab the pack before working
		// Get a random string
		SecureRandom random = new SecureRandom();
		String tempFolderName = ("." + new BigInteger(130, random).toString(32));
		
		// Get current dir
		String myPath = new File(".").getAbsolutePath();
		myPath = myPath.substring(0, (myPath.length() - 1));
					
		// Get pack name/fileID
		//int packID = Integer.parseInt(URL.substring((URL.lastIndexOf("/") + 1), URL.length()));
		//String packName = (URL.replace(URL.substring(URL.lastIndexOf("/"), URL.length()), ""));
		//packName = (packName.replace(packName.substring(packName.lastIndexOf("/"), packName.length()), ""));
		//packName = (packName.substring((packName.lastIndexOf("/") + 1), packName.length()));
		
		//System.out.println("Downloading pack: " + packName);
		
		// Download the pack
		try {
			new FetchPackZip(URL, myPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
		// Get pack name
		String packZip = "";
		try {
			//packZip = new ReturnFileName().getMod(packName, packID, myPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(0);
		
		// Now do the normal install
		try {
			new PackInstall().get(tempFolderName, packZip);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Removing Curse export");
		// Finally delete the downloaded pack
		new File(myPath + packZip).delete();
		
	}
}
