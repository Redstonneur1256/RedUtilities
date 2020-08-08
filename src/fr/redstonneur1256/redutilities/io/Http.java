package fr.redstonneur1256.redutilities.io;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Http {
    private String url;
    private Map<String, String> properties;
    private int timeout;
    private Proxy proxy;
    public Http(String url) {
        this.url = url;
        this.properties = new HashMap<>();
        this.timeout = 1000;
        this.proxy = Proxy.NO_PROXY;
    }

    public Http property(String name, String value) {
        properties.put(name, value);
        return this;
    }

    public Http timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public Http proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public InputStream openSilent() {
        try {
            return open();
        }catch (IOException exception) {
            return null;
        }
    }

    public InputStream open() throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        connection.setConnectTimeout(timeout);
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
        return connection.getInputStream();
    }

    public String readSilent() {
        try {
            return read();
        } catch (IOException e) {
            return null;
        }
    }

    public String read() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(open()));
        StringBuilder output = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    }

    public static Http url(String url) {
        return new Http(url);
    }

}
