package fr.redstonneur1256.redutilities.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class JDownload {
    
    private URL url;
    private Map<String, String> properties;
    private File output;
    private int bufferSize;
    private Status status;
    private long downloadSize;
    private long downloadedBytes;
    private long speed;
    private List<DownloadListener> listenerList;

    private JDownload(JDownload original) {
        // Internal constructor to copy it.
        this.url = original.url;
        this.properties = new HashMap<>(original.properties);
        this.output = original.output;
        this.bufferSize = original.bufferSize;
        this.status = Status.STOPPED;
        this.downloadSize = 0;
        this.downloadedBytes = 0;
        this.speed = 0;
        this.listenerList = new ArrayList<>(original.listenerList);
    }

    public JDownload(String url, File output) throws MalformedURLException {
        this(url, output, 1024);
    }

    public JDownload(String url, File output, int bufferSize) throws MalformedURLException {
        this.url = new URL(url);
        this.properties = new HashMap<>();
        this.output = output;
        this.bufferSize = bufferSize;
        this.status = Status.STOPPED;
        this.downloadSize = 0;
        this.downloadedBytes = 0;
        this.speed = 0;
        this.listenerList = new ArrayList<>();

        this.properties.put("User-Agent", "JDownloader");

    }

    /**
     * @see JDownload#start(boolean)
     */
    public void start() throws IOException {
        start(false);
    }

    /**
     * Start the download
     *
     * @param shouldRetry if the download should shouldRetry in case of fail
     * @throws IOException if the download failed
     * @throws IllegalStateException if the download is already complete
     */
    public void start(boolean shouldRetry) throws IOException {
        if(status == Status.COMPLETE) {
            throw new IllegalStateException("Download is already complete, to start it back use JDownload#copy()");
        }

        debug("Starting download...");
        debug("Download URL = " + url);
        debug("Should retry = " + shouldRetry);
        debug("Connection properties:");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            debug("  " + property.getKey() + " = " + property.getValue());
        }
        debug("Buffer size = " + sizeFormat(bufferSize) + " (" + bufferSize + ")");
        debug("Output directory =  " + output.getParent());
        debug("Output file = " + output.getName());
        debug("Downloaded bytes = " + downloadedBytes);

        setStatus(Status.STARTING);

        File downloadFile = new File(output.getAbsolutePath() + ".jdl");

        if(status != Status.PAUSED) { // Does not need to check back all files if download is paused.

            if(!output.exists()) {
                File parent = output.getParentFile();
                if(!parent.exists()) {
                    info("Parent directory ('" + parent.getAbsolutePath() + "') does not exist.");
                    if(!parent.mkdirs()) {
                        info("Could not create the parent file '" + parent.getAbsolutePath() + "'");
                    }
                }
            }

            if(!downloadFile.exists() && output.exists()) {
                if(!output.renameTo(downloadFile)) {
                    warn("Cannot rename existing file to temporary JDL file, creating a new one");
                }
            }
        }

        RandomAccessFile file = new RandomAccessFile(downloadFile, "rw");

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        for (Map.Entry<String, String> property : properties.entrySet()) {
            urlConnection.setRequestProperty(property.getKey(), property.getValue());
        }

        urlConnection.setRequestProperty("Range", "bytes=" + downloadedBytes + "-");

        long start = System.currentTimeMillis();
        urlConnection.connect();
        long end = System.currentTimeMillis();
        debug("Connected, time = " + (end - start) + "ms");
        debug("Connection response code = " + urlConnection.getResponseCode());

        int code = urlConnection.getResponseCode();

        if(code / 100 != 2) {
            warn("Got wrong response code for URL '" + url + "' (" + code + ")");
            switch (code) {
                case 301:
                case 302:
                    boolean permanent = code == 301;
                    url = new URL(urlConnection.getHeaderField("Location"));

                    info("Swapping to: '" + url + "' (" + (permanent ? "Permanent" : "Temporary") + " redirect)");
                    if(shouldRetry) {
                        start(false);
                        return;
                    }
                    break;
                case 304: // Not edited
                    info("File not modified from last request (304)");
                    // Normally end download:
                    if(!downloadFile.renameTo(output)) {
                        warn("Can't rename file to original name.");
                    }
                    setStatus(Status.COMPLETE);
                    return;
                case 401:
                case 403:
                    fatal("Server denied access to file.");
                    break;
                case 404:
                    fatal("Server returned 404 (Not Found) for URL ('" + url + "') ");
                    break;
                default:
                    break;
            }
            setStatus(Status.STOPPED);
            return;
        }

        downloadSize = urlConnection.getContentLengthLong();
        InputStream inputStream = urlConnection.getInputStream();

        debug("Content size = " + sizeFormat(downloadSize) + " (" + downloadSize+ ")");

        info("Starting download from URL " + url);

        setStatus(Status.DOWNLOADING);

        Timer timer = new Timer();
        long updatesPerSecond = 20;
        timer.scheduleAtFixedRate(new TimerTask() {
            private long lastDownloadedBytes = 0;
            @Override
            public void run() {
                if(status != Status.DOWNLOADING) {
                    timer.cancel();
                    return;
                }
                speed = (downloadedBytes - lastDownloadedBytes) * updatesPerSecond;
                lastDownloadedBytes = downloadedBytes;
                for (DownloadListener downloadListener : listenerList) {
                    downloadListener.speedChanged(JDownload.this);
                }
            }
        }, 1000, 1000 / updatesPerSecond);

        byte[] buffer = new byte[bufferSize];
        int length;

        start = System.currentTimeMillis();
        while(status == Status.DOWNLOADING) {
            length = inputStream.read(buffer);
            if(length == -1) {
                break;
            }
            downloadedBytes += length;
            file.write(buffer, 0, length);
        }
        end = System.currentTimeMillis();
        debug("Download complete, time = " + (end - start) + "ms");

        file.close();

        if(!downloadFile.renameTo(output)) {
            boolean done = output.delete() && downloadFile.renameTo(output);
            if(!done)
                warn("Can't rename file to original name.");
        }

        for (DownloadListener downloadListener : listenerList) {
            downloadListener.downloadComplete(this, output);
        }
        
        info("Download complete.");

        setStatus(Status.COMPLETE);
    }

    /**
     * Pause the download, can be resumed calling back <code>JDownload#start()</code>
     */
    public void pause() {
        setStatus(Status.PAUSED);
    }
    
    public void addListener(DownloadListener listener) {
        listenerList.add(listener);
    }

    public JDownload copy() {
        return new JDownload(this);
    }

    /**
     * @return the current downloaded bytes count
     */
    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    /**
     * @return the size of the file after the download (in bytes)
     */
    public long getDownloadSize() {
        return downloadSize;
    }

    /**
     * Get the current download progress
     * @return the download progress from 0 to 1
     */
    public double getProgress() {
        return downloadedBytes / (double) downloadSize;
    }

    /**
     * Get the download speed
     * @return the download speed from last second (in bytes)
     */
    public long getSpeed() {
        return speed;
    }

    /**
     * @return the download status
     */
    public Status getStatus() {
        return status;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Generate a progress bar of the download
     * @param length the length of the bar inside the []
     * @return The progress bar
     */
    public String progressBar(int length) {
        StringBuilder builder = new StringBuilder();

        int progress = (int) (getProgress() * length);

        builder.append("[");
        for (int i = 0; i < progress; i++) {
            builder.append("=");
        }
        builder.append(">");
        for (int i = progress + 1; i < length; i++) {
            builder.append(" ");
        }
        builder.append("] ").append(sizeFormat(speed)).append("/s").append(" ").append(sizeFormat(downloadedBytes))
                .append("/").append(sizeFormat(downloadSize));

        return builder.toString();
    }


    public enum Status {
        STOPPED(),
        STARTING(),
        DOWNLOADING(),
        PAUSED(),
        COMPLETE()
    }

    public static abstract class DownloadListener {
        public void downloadComplete(JDownload download, File file) { }
        public void statusChanged(JDownload download, Status oldStatus, Status newStatus) { }
        public void speedChanged(JDownload download) { }
    }
    
    // Internal methods:

    private static String sizeFormat(long size) {
        String[] units = new String[] {"B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB, YB"};
        int index = 0;
        double temp = size;
        while(temp >= 1024 && index < units.length - 1) {
            temp /= 1024;
            index++;
        }
        return String.format("%.2f %s", temp, units[index]);
    }

    private void setStatus(Status status) {
        for (DownloadListener downloadListener : listenerList) {
            downloadListener.statusChanged(this, this.status, status);
        }
        this.status = status;
    }

    private static void info(String message) {
        System.out.println("INFO: " + message);
    }
    private static void warn(String message) {
        System.err.println("WARN: " + message);
    }
    private static void fatal(String message) {
        System.err.println("FATAL: " + message);
    }
    private static void debug(String message) {
        //System.out.println("DEBUG: " + message);
    }

}