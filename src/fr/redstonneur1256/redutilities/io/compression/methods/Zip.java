package fr.redstonneur1256.redutilities.io.compression.methods;

import fr.redstonneur1256.redutilities.io.compression.Compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zip implements Compression.CompressionProcessor {

    @Override
    public void compress(byte[] input, ByteArrayOutputStream output, byte[] buffer, boolean threadSafe) throws Exception {
        ZipOutputStream zipOutput = new ZipOutputStream(output);
        zipOutput.putNextEntry(new ZipEntry("content"));
        zipOutput.write(input, 0, input.length);
        zipOutput.closeEntry();
        zipOutput.close();
    }

    @Override
    public void decompress(byte[] input, ByteArrayOutputStream output, byte[] buffer, boolean threadSafe) throws Exception {
        ZipInputStream zipInput = new ZipInputStream(new ByteArrayInputStream(input));
        zipInput.getNextEntry();

        int count;
        do {
            count = zipInput.read(buffer);
            if(count != -1)
                output.write(buffer, 0, count);
        } while(count != -1);
        zipInput.close();
    }

}