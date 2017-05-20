package me.boops.cursedownloader;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Main {
	
	public static void main(String[] args) throws Exception{
		
		// Get a random string
		SecureRandom random = new SecureRandom();
		String tempFolderName = ("." + new BigInteger(130, random).toString(32));
		
		// Check if downloading whole pack or using a pack we already have
		
		if(args[0].toLowerCase().equals("fetch")){
			
			// We want to grab the pack before working
			
			// Get current dir
			String myPath = new File(".").getAbsolutePath();
			myPath = myPath.substring(0, (myPath.length() - 1));
			
			// Get pack name/fileID
			int packID = Integer.parseInt(args[1].substring((args[1].lastIndexOf("/") + 1), args[1].length()));
			String packName = (args[1].replace(args[1].substring(args[1].lastIndexOf("/"), args[1].length()), ""));
			packName = (packName.replace(packName.substring(packName.lastIndexOf("/"), packName.length()), ""));
			packName = (packName.substring((packName.lastIndexOf("/") + 1), packName.length()));
			
			System.out.println("Downloading pack: " + packName);
			
			// Download the pack
			new GetFileName().getMod(packName, packID, myPath);
			
			// Get pack name
			String packZip = new ReturnFileName().getMod(packName, packID, myPath);
			
			// Now do the normal install
			new PackInstall().get(tempFolderName, packZip);
			
			System.out.println("Removing Curse export");
			// Finally delete the downloaded pack
			new File(myPath + packZip).delete();
			
		}
		
		if(args[0].toLowerCase().equals("extract")){
			
			
			// We just want to extract the pack
			new PackInstall().get(tempFolderName, args[1]);
			
			
		}
	}
}
