package fr.redstonneur1256.redutilities.io;

import fr.redstonneur1256.redutilities.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

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

}
