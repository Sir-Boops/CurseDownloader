package me.boops.cursedownloader.LocalDealing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.json.JSONObject;

import me.boops.cursedownloader.Config.Config;
import me.boops.cursedownloader.RemoteDealing.Download;
import me.boops.cursedownloader.RemoteDealing.GetForgeJar;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class PackInstall {
	
	public void get(String tmpFolder, String packZipFile) throws Exception {
		
		// Get packZip file 
		String compressedFile = new File(packZipFile).getAbsoluteFile().toString();
		String myPath = compressedFile.substring(0, (compressedFile.lastIndexOf(File.separator) + 1));
		
		System.out.println("Using temp dir -> " + myPath + tmpFolder);
		
		// Unzip the file to a temp folder
		ZipFile zipFile = new ZipFile(compressedFile);
		new File(myPath + tmpFolder).mkdirs();
		zipFile.extractAll(myPath + tmpFolder + File.separator);
		
		// Read and delete the manifest and modlist
		System.out.println("Reading the manifest");
		Manifest manifest = new Manifest();
		manifest.readManifest(myPath + tmpFolder + File.separator);
		new File(myPath + tmpFolder + File.separator + "manifest.json").delete();
		new File(myPath + tmpFolder + File.separator + "modlist.html").delete();
		
		// Download all the mods for the pack
		System.out.println("Starting Mod downloads");
		new Download(myPath + tmpFolder + File.separator + "mods" + File.separator, manifest.getModList());
		
		// Apply the overrides then remove the folder
		System.out.println("Applying Overrides");
		new ApplyOverrides(myPath + tmpFolder + File.separator);
		new CleanDir(myPath + tmpFolder + File.separator + "overrides" + File.separator);
		
		// Move temp folder to final folder
		System.out.println("Saving to output folder");
		String finalFolderName = "";
		
		// Check if we should use a custom name or not
		if(Config.folder != null) {
			finalFolderName = Config.folder;
		} else {
			finalFolderName = manifest.getPackName();
		}
		
		Path from = new File(myPath + tmpFolder).toPath();
		
		//Check if i'm creating a multimc export
		Path to = null;
		if(Config.convertToMultiMC) {
			
			// Hold on to your hats we are creating an export!
			System.out.println("Starting MultiMC export creation");
			
			// Make the dirs for it
			new File(myPath + finalFolderName).mkdir();
			new File(myPath + finalFolderName + File.separator + "patches").mkdir();
			
			// Generate the patch json
			new GetForgeJar(manifest.getMCVersion(), manifest.getForgeVersion());
			JSONObject forgeJSON = new ReadForgeVersionJSON().getJSON(manifest.getMCVersion(), manifest.getForgeVersion());
			JSONObject multiMCJSON = new ForgeToMultiMC().convertJSON(forgeJSON);
			
			BufferedWriter bws = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(myPath + finalFolderName + File.separator + "patches" + File.separator + "net.minecraftforge.json"), "UTF-8"));
			bws.write(multiMCJSON.toString());
			bws.flush();
			bws.close();
			
			File jar = new File(myPath + File.separator + "forge-" + manifest.getMCVersion() + "-" + manifest.getForgeVersion() + "-universal.jar");
			jar.deleteOnExit();
			
			BufferedWriter inBWS = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(myPath + finalFolderName + File.separator + "instance.cfg"), "UTF-8"));
			inBWS.write("InstanceType=OneSix");
			inBWS.newLine();
			inBWS.write("IntendedVersion=" + manifest.getMCVersion());
			inBWS.flush();
			inBWS.close();
			
			to = new File(myPath + finalFolderName + File.separator + "minecraft" + File.separator).toPath();
			
			
		} else {
			
			to = new File(myPath + finalFolderName).toPath();
			
		}
		
		if(Config.convertToMultiMC) {
			
			Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
			
			String zipFileName = (finalFolderName + ".zip");
			ZipFile exportZip = new ZipFile(new File(myPath + zipFileName));
			ZipParameters zipPara = new ZipParameters();
			zipPara.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			zipPara.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			exportZip.addFolder(myPath + finalFolderName, zipPara);
			
			// Delete old folders
			System.out.println("Deleting old folders");
			new CleanDir(myPath + finalFolderName + File.separator);
			
			System.out.println("Saved MultiMC Export to -> " + myPath + zipFileName);
			
		} else {
			
			Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("Saved pack '" + manifest.getPackName() + "' to -> " + myPath + finalFolderName);
			System.out.println("!! Use Minecraft version -> '" + manifest.getMCVersion() + "' with forge version -> '" + manifest.getForgeVersion() + "' !!");
			
		}
	}
}
