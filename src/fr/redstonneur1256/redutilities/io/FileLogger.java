package fr.redstonneur1256.redutilities.io;

import java.io.*;

public class FileLogger {

    private File file;
    private FileWriter writer;
    private PrintStream realOut;
    private PrintStream realErr;

    public FileLogger(String fileName) {
        this(new File(fileName));
    }

    public FileLogger(File file) {
        this.file = file;
    }

    public void setup(boolean append) throws IOException {
        if(writer != null) {
            throw new IllegalStateException("The FileLogger is already opened");
        }

        writer = new FileWriter(file, append);

        realOut = System.out;
        realErr = System.err;

        System.setOut(new PrintStream(new BufferedOutputStream("[OUT] ", realOut, writer)));
        System.setErr(new PrintStream(new BufferedOutputStream("[ERR] ", realErr, writer)));
    }

    public void close() throws IOException {
        if(writer == null) {
            throw new IllegalStateException("The FileLogger is not opened");
        }

        FileWriter writer = this.writer;
        this.writer = null;

        System.out.flush();
        System.err.flush();

        System.setOut(realOut);
        System.setErr(realErr);

        writer.close();

    }

    private static class BufferedOutputStream extends OutputStream {

        private String prefix;
        private PrintStream copy;
        private FileWriter writer;
        private StringBuilder builder;

        public BufferedOutputStream(String prefix, PrintStream copy, FileWriter writer) {
            this.prefix = prefix;
            this.copy = copy;
            this.writer = writer;
            this.builder = new StringBuilder();
        }

        @Override
        public void write(int b) throws IOException {
            builder.append((char) b);
            if(b == '\n') {
                flush();
            }
        }

        @Override
        public void flush() throws IOException {
            String s = prefix + builder.toString();
            builder.setLength(0);

            copy.print(s);
            copy.flush();

            writer.write(s);
            writer.flush();

        }

        @Override
        public void write(byte[] b) throws IOException {
            builder.ensureCapacity(b.length);
            super.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            builder.ensureCapacity(len);
            super.write(b, off, len);
        }

    }

}
