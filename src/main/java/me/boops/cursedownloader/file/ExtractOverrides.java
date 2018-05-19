package me.boops.cursedownloader.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import me.boops.cursedownloader.Main;

public class ExtractOverrides {

    public ExtractOverrides(String fullFilePath) throws Exception {

        ZipFile zipFile = new ZipFile(fullFilePath);
        @SuppressWarnings("unchecked")
        List<ZipEntry> fileList = (List<ZipEntry>) Collections.list(zipFile.entries());

        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getName().toLowerCase().contains("overrides")) {
                String newPath = (fileList.get(i).getName().substring(fileList.get(i).getName().indexOf("/") + 1, fileList.get(i).getName().length()));

                String filePath = (newPath.substring(0, newPath.lastIndexOf("/") + 1));
                new CreateFolder(Main.fullPath + filePath);

                if (!newPath.isEmpty() && !new File(Main.fullPath + newPath).isDirectory()) {

                    System.out.println("Extracting: " + fileList.get(i).getName());

                    InputStream is = zipFile.getInputStream(fileList.get(i));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    FileOutputStream fos = new FileOutputStream(Main.fullPath + newPath);

                    int inByte;
                    while ((inByte = is.read()) != -1) {
                        bos.write(inByte);
                    }

                    fos.write(bos.toByteArray());

                    fos.close();
                    bos.close();
                    is.close();
                }
            }
        }
        zipFile.close();
    }
}
