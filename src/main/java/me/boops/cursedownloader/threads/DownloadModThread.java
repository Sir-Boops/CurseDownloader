package me.boops.cursedownloader.threads;

import me.boops.cursedownloader.file.CreateFolder;
import me.boops.cursedownloader.remote.FetchFile;

public class DownloadModThread implements Runnable {
	
	private String path;
	private String url;
	
	public DownloadModThread(String path, String url) {
		this.path = path;
		this.url = url;
	}
	
	@Override
	public void run() {
		
		new CreateFolder(this.path);
		new FetchFile().fetch(this.path, this.url);
	}
}
