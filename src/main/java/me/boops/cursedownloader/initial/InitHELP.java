package me.boops.cursedownloader.initial;

public class InitHELP {
	
	public InitHELP() {
		
		// Print the help
		System.out.println("CurseDownloader Help Page");
		System.out.println("");
		
		//CLI Help
		System.out.println("Notice");
		System.out.println("This help is for CLI only for help with the GUI please check the GIT page for a video guide!");
		System.out.println("");
		
		// Extract/Fetch help
		System.out.println("Flag Guide");
		System.out.println("--extract <Filename>.zip - This flag is used to extract a Twitch launcher modpack export");
		System.out.println("--fetch <pack url> - This flag is used to fetch a modpack directly from curse. Be sure to copy the URL for said version of the modpack");
		System.out.println("");
		
		// Examples
		System.out.println("Examples");
		System.out.println("--extract MyEpicModpack.zip");
		System.out.println("--fetch https://minecraft.curseforge.com/projects/foolcraft/files/2415352");
		System.out.println("");
		
		// Finally exit
		System.exit(0);
	}
	
}
