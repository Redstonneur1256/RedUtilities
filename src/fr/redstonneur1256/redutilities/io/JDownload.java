package fr.redstonneur1256.redutilities.io;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class JDownload {

    public static final int defaultBuffer;

    static {
        defaultBuffer = 1024 * 8; // 8 KB Buffer
    }

    private String url;
    private OutputStream output;
    private byte[] buffer;
    private Map<String, String> properties;
    private List<Listener> listeners;
    private float refreshRate;
    private Status status;
    private InputStream input;
    private long downloadSize;
    private long downloadedBytes;
    private long speed;

    public JDownload(String url, OutputStream output, int bufferSize) {
        this.url = url;
        this.output = output;
        this.buffer = new byte[bufferSize];
        this.properties = new HashMap<>();
        this.status = Status.stopped;
        this.listeners = new ArrayList<>();
        this.refreshRate = 1;

        this.input = null;
        this.downloadSize = -1;
        this.downloadedBytes = 0;
        this.speed = 0;
    }

    public JDownload(String url, OutputStream output) {
        this(url, output, defaultBuffer);
    }

    public JDownload(String url, File file) throws FileNotFoundException {
        this(url, new FileOutputStream(file));
    }

    public JDownload(String url, File file, int bufferSize) throws FileNotFoundException {
        this(url, new FileOutputStream(file), bufferSize);
    }

    private JDownload(JDownload original) {
        this.url = original.url;
        this.output = original.output;
        this.buffer = new byte[original.buffer.length];
        this.properties = new HashMap<>(original.properties);
        this.status = Status.stopped;
        this.listeners = new ArrayList<>(original.listeners);
        this.refreshRate = original.refreshRate;

        this.input = null;
        this.downloadSize = -1;
        this.downloadedBytes = 0;
        this.speed = 0;

    }


    public void connect() throws IOException {
        connect(false);
    }

    public void connect(boolean shouldRetry) throws IOException {
        if(status == Status.connected || status == Status.connecting) {
            throw new IllegalStateException("Download is already connected, use JDownload#copy() for multiple downloads");
        }
        if(status == Status.complete || status == Status.cancelled) {
            throw new IllegalStateException("Download is complete or have been cancelled, use JDownload#copy() for multiple downloads");
        }

        setStatus(Status.connecting);
        try {
            URLConnection connection = new URL(url).openConnection();
            HttpURLConnection httpConnection = connection instanceof HttpURLConnection ? (HttpURLConnection) connection : null;
            for(Map.Entry<String, String> property : properties.entrySet()) {
                connection.addRequestProperty(property.getKey(), property.getValue());
            }
            connection.connect();

            int code = httpConnection == null ? 200 : httpConnection.getResponseCode();
            boolean ok = code / 100 == 2;

            if(!ok) {
                switch(code) {
                    case 301:
                    case 302:
                        url = connection.getHeaderField("Location");
                        setStatus(Status.stopped);
                        if(shouldRetry) {
                            connect(false);
                        }
                        return;
                    case 401:
                        throw new IllegalStateException("Server returned code 401 (Unauthorized)");
                    case 403:
                        throw new IllegalStateException("Server returned code 403 (Forbidden)");
                    case 404:
                        throw new IllegalStateException("Server returned code 404 (Not Found)");
                }
            }

            input = ok ? connection.getInputStream() : httpConnection.getErrorStream();
            downloadSize = connection.getContentLengthLong();

            setStatus(Status.connected);
        }catch(IOException exception) {
            setStatus(Status.stopped);
            throw exception;
        }
    }

    public void download() throws IOException {
        if(status != Status.connected) {
            throw new IllegalStateException("Download is not connected.");
        }
        setStatus(Status.downloading);

        float refreshRate = this.refreshRate;
        long time = (long) (1000 / refreshRate);

        Timer updateTimer = new Timer("JDownload Speed");
        updateTimer.schedule(new TimerTask() {
            private long lastDownloadedBytes = downloadedBytes;

            @Override
            public void run() {
                speed = (long) ((downloadedBytes - lastDownloadedBytes) * refreshRate);
                lastDownloadedBytes = downloadedBytes;
                for(Listener listener : listeners) {
                    listener.speedChanged(speed);
                }
            }
        }, time, time);

        int count;
        while(status == Status.downloading) {
            count = input.read(buffer);
            if(count < 0) {
                setStatus(Status.complete);
                break;
            }
            output.write(buffer, 0, count);
            downloadedBytes += count;
        }

        updateTimer.cancel();

        if(status == Status.complete) {
            output.close();
            listeners.forEach(Listener::downloadComplete);
        }
    }

    public void pauseDownload() {
        setStatus(Status.connected);
    }

    public void cancel() {
        if(status != Status.connected) {
            throw new IllegalStateException("Cannot cancel the download while in status " + status.name());
        }
        setStatus(Status.cancelled);
        try {
            if(input != null) {
                input.close();
            }
        }catch(IOException exception) {
            throw new IllegalStateException("Cannot close input");
        }
    }

    public JDownload copy() {
        return new JDownload(this);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void setProperty(String name, String value) {
        properties.put(name, value);
    }

    public void removeProperty(String name) {
        properties.remove(name);
    }

    public String getProperty(String name) {
        return properties.get(name);
    }

    /**
     * Create a progressbar of the download
     *
     * @param length the length of the bar
     * @param fill   the character of the filled part of the bar
     * @param empty  the character of empty part of the bar
     * @return the progress bar
     */
    public String createBar(int length, String fill, String empty) {
        int progress = (int) (getProgress() * length);

        StringBuilder builder = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            builder.append(i < progress ? fill : empty);
        }
        return builder.toString();
    }

    /**
     * Define how much time JDownload.Listener#speedChanged(long) must be called per seconds
     * Higher value = more updates but can some times return 0 if the buffer is too big
     * Lower value = Better result
     *
     * @param refreshRate the amount of updates per second
     */
    public void setRefreshRate(float refreshRate) {
        this.refreshRate = refreshRate;
    }

    public String getURL() {
        return url;
    }

    public Status getStatus() {
        return status;
    }

    private void setStatus(Status status) {
        this.status = status;

        for(Listener listener : listeners) {
            listener.statusChanged(status);
        }
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    public long getSpeed() {
        return speed;
    }

    /* Classes */

    /**
     * @return the download progress from 0 to 1 or -1 if the download size is unknown
     */
    public double getProgress() {
        return downloadSize <= 0 ? -1 : downloadedBytes / (double) downloadSize;
    }

    public enum Status {
        stopped,
        connecting,
        connected,
        downloading,
        complete,
        cancelled
    }

    public interface Listener {
        void speedChanged(long speed);

        void statusChanged(Status status);

        void downloadComplete();
    }

    public static class ListenerAdapter implements Listener {

        @Override
        public void speedChanged(long speed) {
        }

        @Override
        public void statusChanged(Status status) {
        }

        @Override
        public void downloadComplete() {
        }

    }

}
