package fr.redstonneur1256.redutilities.io.compression;

import fr.redstonneur1256.redutilities.io.compression.methods.GZip;
import fr.redstonneur1256.redutilities.io.compression.methods.Zip;
import fr.redstonneur1256.redutilities.io.compression.methods.ZLib;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.function.Supplier;

public class Compression {

    private static CompressionProcessor processor;
    private static boolean threadSafe;
    private static int bufferSize;
    private static ByteArrayOutputStream output;
    private static byte[] tempBuffer;
    static {
        setMethod(Method.ZLIB);
        setThreadSafe(false);
        setBufferSize(1024);
    }

    public static void setMethod(Method method) { setProcessor(method.processor); }
    public static void setProcessor(CompressionProcessor processor) { Compression.processor = processor; }
    /**
     * Set if the compression can be used by multiple thread at same time, if true a common buffer will be used, else a new buffed will be created each time
     */
    public static void setThreadSafe(boolean threadSafe) { Compression.threadSafe = threadSafe; }
    public static void setBufferSize(int bufferSize) {
        Compression.bufferSize = bufferSize;
        Compression.output = new ByteArrayOutputStream(bufferSize);
        Compression.tempBuffer = new byte[bufferSize];
    }

    public static byte[] decompress(String compressed) throws Exception {
        return decompress(compressed.getBytes());
    }

    public static byte[] decompress(byte[] compressed) throws Exception {
        ByteArrayOutputStream outputStream = threadSafe ? new ByteArrayOutputStream(bufferSize) : output;
        byte[] buffer = threadSafe ? new byte[bufferSize] : tempBuffer;

        outputStream.reset();
        processor.decompress(compressed, outputStream, buffer, threadSafe);
        return outputStream.toByteArray();
    }

    public static byte[] compress(String data) throws Exception {
        return compress(data.getBytes());
    }

    public static byte[] compress(byte[] decompressed) throws Exception {
        ByteArrayOutputStream outputStream = threadSafe ? new ByteArrayOutputStream(bufferSize) : output;
        byte[] buffer = threadSafe ? new byte[bufferSize] : tempBuffer;

        outputStream.reset();
        processor.compress(decompressed, outputStream, buffer, threadSafe);
        return outputStream.toByteArray();
    }

    public enum Method {
        GZIP(new GZip()),
        ZIP(new Zip()),
        ZLIB(new ZLib());

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