package fr.redstonneur1256.redutilities.io.compression.methods;

import fr.redstonneur1256.redutilities.io.compression.Compression;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZLib implements Compression.CompressionProcessor {

    private static Inflater commonInflater;
    private static Deflater commonDeflater;
    static {
        commonInflater = new Inflater();
        commonDeflater = new Deflater();
    }

    @Override
    public void compress(byte[] input, ByteArrayOutputStream output, byte[] buffer, boolean threadSafe) {
        Deflater deflater = threadSafe ? new Deflater() : ZLib.commonDeflater;
        deflater.reset();
        deflater.setInput(input);
        deflater.finish();
        while(!deflater.finished()) {
            int count = deflater.deflate(buffer);
            output.write(buffer, 0, count);
        }
        if(threadSafe) // Not the public one, close it
            deflater.end();
    }

    @Override
    public void decompress(byte[] input, ByteArrayOutputStream output, byte[] buffer, boolean threadSafe) throws Exception {
        Inflater inflater = threadSafe ? new Inflater() : ZLib.commonInflater;
        inflater.reset();
        inflater.setInput(input);
        while(!inflater.finished()) {
            int count = inflater.inflate(buffer);
            output.write(buffer, 0, count);
        }
        if(threadSafe) // Not the public one, close it
           inflater.end();

    }

}
