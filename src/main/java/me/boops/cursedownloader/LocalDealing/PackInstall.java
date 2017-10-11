package me.boops.cursedownloader.LocalDealing;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import me.boops.cursedownloader.Config.Config;
import me.boops.cursedownloader.RemoteDealing.Download;
import net.lingala.zip4j.core.ZipFile;

public class PackInstall {
	
	public void get(String tempFolderName, String path) throws Exception {
		
		String compressedFile = new File(path).getAbsoluteFile().toString();
		String compressedFilePath = compressedFile.substring(0, (compressedFile.lastIndexOf(File.separator) + 1));
		
		System.out.println("Using temp dir -> " + compressedFilePath + tempFolderName);
		
		// Unzip the file to a temp folder
		ZipFile zipFile = new ZipFile(compressedFile);
		new File(compressedFilePath + tempFolderName).mkdirs();
		zipFile.extractAll(compressedFilePath + tempFolderName + "/");
		
		// Read the manifest
		System.out.println("Reading the manifest");
		Manifest manifest = new Manifest();
		manifest.readManifest(compressedFilePath + tempFolderName + "/");
		
		// Get the mods
		System.out.println("Starting Mod downloads");
		new Download(compressedFilePath + tempFolderName + "/mods/", manifest.getModList());
		
		// Delete the curse files
		System.out.println("Removing curse files");
		new CleanCurse().removeCurse(compressedFilePath + tempFolderName + "/");
		
		// Add the overrides
		System.out.println("Applying Overrides");
		new ApplyOverrides().AddOverrides(compressedFilePath + tempFolderName + "/");
		
		
		// Delete old folders
		System.out.println("Removing tmp folders");
		new File(compressedFilePath + tempFolderName + "/overrides").delete();
		
		// Move temp folder to final folder
		System.out.println("Saving to final folder");
		String finalFolderName = "";
		// CHeck if we should use a custom name or not
		if(Config.folder != null) {
			finalFolderName = Config.folder;
		} else {
			finalFolderName = manifest.getPackName();
		}
		
		Path from = new File(compressedFilePath + tempFolderName).toPath();
		Path to = new File(compressedFilePath + finalFolderName).toPath();
		
		Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
		
		System.out.println("Saved pack '" + manifest.getPackName() + "' to -> " + compressedFilePath + finalFolderName);
		System.out.println("!! Use Minecraft version -> '" + manifest.getMCVersion() + "' with forge version -> '" + manifest.getForgeVersion() + "' !!");
		
	}
}
