package fr.redstonneur1256.redutilities.io.zip.strategy;

import fr.redstonneur1256.redutilities.function.Functions;
import fr.redstonneur1256.redutilities.io.FileUtils;
import fr.redstonneur1256.redutilities.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DiscStrategy extends ZipStrategy {

    private File tempFolder;
    private List<DiscFileData> files;

    public DiscStrategy() {
        tempFolder = Functions.runtime(() -> Files.createTempDirectory("zip" + UUID.randomUUID().toString()).toFile());
    }

    @Override
    public void load(File zipFile) throws IOException {
        try(FileInputStream fileInput = new FileInputStream(zipFile)) {
            ZipInputStream input = new ZipInputStream(fileInput);

            List<DiscFileData> files = new ArrayList<>();

            ZipEntry entry;
            while((entry = input.getNextEntry()) != null) {
                if(entry.isDirectory()) {
                    continue;
                }
                File file = new File(tempFolder, entry.getName());
                File parent = file.getParentFile();
                if(!parent.exists() && !parent.mkdirs()) {
                    throw new IllegalStateException("Failed to create parent directory at" + parent);
                }

                FileOutputStream output = new FileOutputStream(file);
                IOUtils.copy(input, output, 8192);
                output.close();

                files.add(new DiscFileData(file));
            }

            input.close();

            this.files = files;
        }
    }

    @Override
    public void save(File zipFile) throws IOException {
        try(FileOutputStream fileOutput = new FileOutputStream(zipFile);
            ZipOutputStream output = new ZipOutputStream(fileOutput)) {

            File[] files = tempFolder.listFiles();
            save(files, output, "");

            tempFolder.delete();

            this.files = null;
        }
    }

    private void save(File[] files, ZipOutputStream output, String path) throws IOException {
        if(files == null) {
            return;
        }

        for(File file : files) {
            if(file.isDirectory()) {
                save(file.listFiles(), output, path + "/" + file.getName());
                continue;
            }

            String fileName = path.isEmpty() ? file.getName() : path + "/" + file.getName();
            if(fileName.startsWith("/")) {
                fileName = fileName.substring(1);
            }

            ZipEntry entry = new ZipEntry(fileName);
            FileInputStream input = new FileInputStream(file);

            output.putNextEntry(entry);
            IOUtils.copy(input, output, 8192);
            output.closeEntry();

            input.close();
        }

    }

    @Override
    public void close() {
        FileUtils.removeDirectory(tempFolder);
    }

    @Override
    public ZipFile open(String path, String name) {
        return new DiscFileData(name, path);
    }

    @Override
    public List<? extends ZipFile> listFiles() {
        return files;
    }

    private class DiscFileData extends ZipFile {

        private File file;

        private DiscFileData(File file) {
            super(file.getName(), file.getParent());
            this.file = file;
        }

        public DiscFileData(String name, String path) {
            super(name, path);
            File parentDirectory = new File(tempFolder, path);
            if(!parentDirectory.exists() && !parentDirectory.mkdirs()) {
                throw new RuntimeException("Failed to create parent folder " + parentDirectory);
            }
            file = new File(parentDirectory, name);
        }

        @Override
        public long getSize() {
            return file.length();
        }

        @Override
        public void delete() {
            if(file.exists() && !file.delete()) {
                throw new IllegalStateException("Failed to delete the file");
            }
        }

        @Override
        public InputStream getInput() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public OutputStream getOutput() throws IOException {
            return new FileOutputStream(file);
        }
    }


}
