package fr.redstonneur1256.redutilities.io.compression;

import fr.redstonneur1256.redutilities.io.compression.methods.GZip;
import fr.redstonneur1256.redutilities.io.compression.methods.ZLib;
import fr.redstonneur1256.redutilities.io.compression.methods.Zip;

import java.io.ByteArrayOutputStream;

public class Compression {

    private CompressionProcessor processor;
    private boolean threadSafe;
    private int bufferSize;
    private ByteArrayOutputStream output;
    private byte[] tempBuffer;

    public Compression() {
        setMethod(Method.zLib);
        setThreadSafe(false);
        setBufferSize(1024);
    }

    public void setMethod(Method method) {
        setProcessor(method.processor);
    }

    public void setProcessor(CompressionProcessor processor) {
        this.processor = processor;
    }

    /**
     * Set if the compression can be used by multiple thread at same time, if true a common buffer will be used, else a new buffed will be created each time
     */
    public void setThreadSafe(boolean threadSafe) {
        this.threadSafe = threadSafe;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        this.output = new ByteArrayOutputStream(bufferSize);
        this.tempBuffer = new byte[bufferSize];
    }

    public byte[] decompress(String compressed) throws Exception {
        return decompress(compressed.getBytes());
    }

    public byte[] decompress(byte[] compressed) throws Exception {
        ByteArrayOutputStream outputStream = threadSafe ? new ByteArrayOutputStream(bufferSize) : output;
        byte[] buffer = threadSafe ? new byte[bufferSize] : tempBuffer;

        outputStream.reset();
        processor.decompress(compressed, outputStream, buffer, threadSafe);
        return outputStream.toByteArray();
    }

    public byte[] compress(String data) throws Exception {
        return compress(data.getBytes());
    }

    public byte[] compress(byte[] decompressed) throws Exception {
        ByteArrayOutputStream outputStream = threadSafe ? new ByteArrayOutputStream(bufferSize) : output;
        byte[] buffer = threadSafe ? new byte[bufferSize] : tempBuffer;

        outputStream.reset();
        processor.compress(decompressed, outputStream, buffer, threadSafe);
        return outputStream.toByteArray();
    }

    public enum Method {
        gZip(new GZip()),
        zip(new Zip()),
        zLib(new ZLib());

        private CompressionProcessor processor;

        Method(CompressionProcessor processor) {
            this.processor = processor;
        }
    }

    public interface CompressionProcessor {

        void compress(byte[] input, ByteArrayOutputStream output, byte[] buffer, boolean threadSafe) throws Exception;

        void decompress(byte[] input, ByteArrayOutputStream output, byte[] buffer, boolean threadSafe) throws Exception;

    }


}