package fr.redstonneur1256.redutilities.io;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            for(Map.Entry<String, String> property : properties.entrySet()) {
                connection.addRequestProperty(property.getKey(), property.getValue());
            }
            connection.connect();

            int code = connection.getResponseCode();

            boolean ok = code / 100 == 2;

            if(!ok) {
                switch(code) {
                    case 301:
                    case 302:
                        url = connection.getHeaderField("Location");
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

            input = ok ? connection.getInputStream() : connection.getErrorStream();
            downloadSize = connection.getContentLengthLong();

            setStatus(Status.connected);
        }catch(IOException exception) {
            setStatus(Status.stopped);
            throw new IOException(exception);
        }
    }

    public void download() throws IOException {
        if(status != Status.connected) {
            throw new IllegalStateException("Download is not connected.");
        }
        setStatus(Status.downloading);

        Timer updateTimer = new Timer("JDownload Speed");
        updateTimer.schedule(new TimerTask() {
            private long lastDownloadedBytes = downloadedBytes;
            @Override
            public void run() {
                speed = (downloadedBytes - lastDownloadedBytes);
                lastDownloadedBytes = downloadedBytes;
                for(Listener listener : listeners) {
                    listener.speedChanged(speed);
                }
            }
        }, 1000, 1000);

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

    public String createBar(int length, String fill, String empty) {
        int progress = (int) (getProgress() * length);

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < length; i++) {
            builder.append(i < progress ? fill : empty);
        }
        return builder.toString();
    }

    public String getURL() { return url; }
    public Status getStatus() { return status; }
    public long getDownloadSize() { return downloadSize; }
    public long getDownloadedBytes() { return downloadedBytes; }
    public long getSpeed() { return speed; }

    /**
     * @return the download progress from 0 to 1 or -1 if the download size is unknown
     */
    public double getProgress() {
        return downloadSize == -1 ? -1 : downloadedBytes / (double) downloadSize;
    }

    /* Classes */

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
        public void speedChanged(long speed) { }

        @Override
        public void statusChanged(Status status) { }

        @Override
        public void downloadComplete() { }

    }

    /* Internal methods */

    private void setStatus(Status status) {
        this.status = status;

        for(Listener listener : listeners) {
            listener.statusChanged(status);
        }
    }

}
