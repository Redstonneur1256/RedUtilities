package fr.redstonneur1256.redutilities.io.zip;

import fr.redstonneur1256.redutilities.io.zip.strategy.ZipStrategy;

import java.io.File;
import java.io.IOException;

public class Zip {

    private File zipFile;
    private ZipStrategy strategy;

    public Zip(File zipFile, Strategy strategy) {
        if(zipFile.exists() && zipFile.isDirectory()) {
            throw new IllegalArgumentException("File " + zipFile + " exist and is a directory");
        }

        this.zipFile = zipFile;
        this.strategy = strategy.getStrategy().get();
    }

    public void open() throws IOException {
        strategy.open(zipFile);
    }

    public void close() throws IOException {
        strategy.close();
    }

    public ZipStrategy.ZipFile open(String name) {
        return open("", name);
    }

    public ZipStrategy.ZipFile open(String path, String name) {
        return strategy.open(path, name);
    }

    /**
     * Save back all files into the file
     */
    public void compress() throws IOException {
        strategy.save(zipFile);
    }

    public File getZipFile() {
        return zipFile;
    }

}
