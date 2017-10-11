package me.boops.cursedownloader.initals;

public class InitHelp {
	
	public InitHelp() {
		
		// Print the help
		System.out.println("CurseDownloader Help Page");
		System.out.println("");

		// Extract/Fetch help
		System.out.println("Flag Guide");
		System.out.println("--extract <Filename>.zip - This flag is used to extract a Twitch launcher modpack export");
		System.out.println("--fetch <pack url> - This flag is used to fetch a modpack directly from curse. Be sure to copy the URL for said version of the modpack");
		System.out.println("--folder-name <folder name> - Set the name of the output folder");
		System.out.println("--thread-count X - Where X is a number. Set the amount of downloading threads to use defaults to 6");
		System.out.println("");
		
		// Examples
		System.out.println("Examples");
		System.out.println("--extract MyEpicModpack.zip");
		System.out.println("--fetch https://minecraft.curseforge.com/projects/foolcraft/files/2415352");
		System.out.println("");
		
		// Finally exit
		return;
		
	}
}
