package me.boops.cursedownloader.threads;

import me.boops.cursedownloader.file.GrabMods;
import me.boops.cursedownloader.remote.FetchRealURL;

public class URLGenThread implements Runnable {

    private int project_id;
    private int file_id;

    public URLGenThread(int project_id, int file_id) {
        this.project_id = project_id;
        this.file_id = file_id;
    }

    @Override
    public void run() {
        GrabMods.urls.add(new FetchRealURL().fetch("https://minecraft.curseforge.com/mc-mods/" + this.project_id) + "/files/" + this.file_id + "/download");
    }
}
