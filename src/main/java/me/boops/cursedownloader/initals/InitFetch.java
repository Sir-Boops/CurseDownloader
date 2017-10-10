package me.boops.cursedownloader.initals;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;

import me.boops.cursedownloader.LocalDealing.PackInstall;
import me.boops.cursedownloader.RemoteDealing.FetchPackZip;

public class InitFetch {
	
	public InitFetch(URL URL) throws Exception {
		
		// We want to grab the pack before working
		// Get a random string
		SecureRandom random = new SecureRandom();
		String tempFolderName = ("." + new BigInteger(130, random).toString(32));
		
		// Get current dir
		String myPath = new File(".").getAbsolutePath();
		myPath = myPath.substring(0, (myPath.length() - 1));
		
		// Download the pack zip and return the zip name
		String packFileName = new FetchPackZip().Fetch(URL, myPath);
		
		// Now do the normal install
		new PackInstall().get(tempFolderName, packFileName);
			
		System.out.println("Removing Curse export");
		
		// Finally delete the downloaded pack
		new File(myPath + packFileName).delete();
		
	}
}
