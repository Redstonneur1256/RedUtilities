package fr.redstonneur1256.redutilities.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int count;
        while((count = input.read(buf, 0, buf.length)) != -1) {
            output.write(buf, 0, count);
        }
    }

}
