package fr.redstonneur1256.redutilities.io.zip;

import fr.redstonneur1256.redutilities.io.zip.strategy.ZipStrategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Zip {

    private ZipStrategy strategy;
    private boolean opened;

    public Zip(Strategy strategy) {
        this.strategy = strategy.getStrategy().get();
        this.opened = false;
    }

    public void load(File zipFile) throws IOException {
        if(opened) {
            throw new IllegalStateException("The Zip is already opened");
        }

        strategy.load(zipFile);
        opened = true;
    }

    public void compress(File zipFile) throws IOException {
        if(!opened) {
            throw new IllegalStateException("The Zip file is not opened");
        }

        strategy.save(zipFile);
    }

    public void close() throws IOException {
        if(!opened) {
            throw new IllegalStateException("The Zip file is not opened");
        }

        strategy.close();
        opened = false;
    }

    public ZipStrategy.ZipFile open(String name) {
        return open("", name);
    }

    public ZipStrategy.ZipFile open(String path, String name) {
        if(!opened) {
            throw new IllegalStateException("The Zip file is not opened");
        }

        return strategy.open(path, name);
    }

    public List<? extends ZipStrategy.ZipFile> listFiles() {
        return strategy.listFiles();
    }

}
