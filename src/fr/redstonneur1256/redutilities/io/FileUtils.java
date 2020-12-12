package fr.redstonneur1256.redutilities.io;

import fr.redstonneur1256.redutilities.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class FileUtils {

    public static String getFileHash(File file, String method) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(method);
        FileInputStream input = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int count;
        while((count = input.read(buffer)) != -1) {
            messageDigest.update(buffer, 0, count);
        }
        return Utils.binaryToHex(messageDigest.digest());
    }

    public static void removeDirectory(File directory) {
        if(!directory.isDirectory()) {
            throw new IllegalStateException("The specified file is not a directory");
        }
        Queue<File> files = new LinkedList<>();
        files.add(directory);

        String exc = null;
        while(!files.isEmpty()) {
            File file = files.poll();
            if(file.isDirectory()) {
                File[] listFiles = file.listFiles();
                assert listFiles != null : "The directory exist";
                files.addAll(Arrays.asList(listFiles));
                continue;
            }

            if(!file.delete() && exc == null) {
                exc = "Failed to delete " + file;
            }
        }

        if(exc != null) {
            throw new RuntimeException(exc);
        }
    }

    public static File getRunningFile(Class<?> clazz) throws URISyntaxException {
        String path = clazz
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();

        return new File(path);
    }

}
