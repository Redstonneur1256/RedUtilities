package fr.redstonneur1256.redutilities.io.zip.strategy;

import java.io.*;
import java.util.List;

public abstract class ZipStrategy {

    public abstract void load(File zipFile) throws IOException;

    public abstract void save(File zipFile) throws IOException;

    public abstract void close();

    public abstract ZipFile open(String path, String name);

    public abstract List<? extends ZipFile> listFiles();

    public static abstract class ZipFile {

        private String name;
        private String path;

        public ZipFile(String name, String path) {
            this.name = name;
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public abstract long getSize();

        public abstract void delete();

        public abstract InputStream getInput() throws IOException;

        public abstract OutputStream getOutput() throws IOException;

    }

}
