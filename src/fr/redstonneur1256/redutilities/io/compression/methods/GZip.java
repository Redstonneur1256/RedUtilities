package fr.redstonneur1256.redutilities.io.compression.methods;

import fr.redstonneur1256.redutilities.io.compression.Compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZip implements Compression.CompressionProcessor {

    @Override
    public void compress(byte[] input, ByteArrayOutputStream output, byte[] buffer, boolean threadSafe) throws Exception {
        GZIPOutputStream zipOutput = new GZIPOutputStream(output, buffer.length);
        zipOutput.write(input, 0, input.length);
        zipOutput.close();
    }

    @Override
    public void decompress(byte[] input, ByteArrayOutputStream output, byte[] buffer, boolean threadSafe) throws Exception {
        GZIPInputStream zipInput = new GZIPInputStream(new ByteArrayInputStream(input));
        int count;
        do {
            count = zipInput.read(buffer);
            if(count != -1)
                output.write(buffer, 0, count);
        } while(count != -1);
        zipInput.close();
    }

}
