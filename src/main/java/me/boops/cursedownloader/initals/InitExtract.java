package me.boops.cursedownloader.initals;

import java.math.BigInteger;
import java.security.SecureRandom;

import me.boops.cursedownloader.LocalDealing.PackInstall;

public class InitExtract {
	
	public InitExtract(String path) {
		
		// Check to make sure that the file is a zip
		
		if(path.substring(path.lastIndexOf('.') + 1).equalsIgnoreCase("zip")) {
			
			// It's a zip so let's use it!
			
			// Get a random string
			SecureRandom random = new SecureRandom();
			String tempFolderName = ("." + new BigInteger(130, random).toString(32));
			
			try {
				new PackInstall().get(tempFolderName, path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
