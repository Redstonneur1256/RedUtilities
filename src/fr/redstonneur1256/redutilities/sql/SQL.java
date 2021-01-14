package fr.redstonneur1256.redutilities.sql;

import fr.redstonneur1256.redutilities.sql.serialization.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SQL {

    protected LoginData login;
    protected Map<String, Object> parameters;
    protected Map<Class<?>, Serializer<?>> serializers;
    protected boolean silentErrors;
    protected Connection connection;

    public SQL(String address, String base, String name, String pass) {
        this(new LoginData(address, base, name, pass));
    }

    public SQL(LoginData login) {
        this.login = login;
        this.parameters = new HashMap<>();
        this.serializers = new HashMap<>();

        SQLSerializers.registerDefaults(this);
    }

    public void defaultParameters() {
        setParameter("autoReconnect", true);
        setParameter("useUnicode", true);
        setParameter("characterEncoding", "utf8");
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public void removeParameter(String key) {
        parameters.remove(key);
    }

    public boolean connectSilent() {
        try {
            connect();
            return true;
        }catch(Exception ignored) {
            return false;
        }
    }

    public void connect() throws SQLException {
        if(isConnected()) {
            return;
        }

        StringBuilder parametersString = new StringBuilder();
        if(!parameters.isEmpty()) {
            parametersString.append('?');
            for(Map.Entry<String, Object> entry : parameters.entrySet()) {
                parametersString.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
            }
            parametersString.setLength(parametersString.length() - 1);
        }
        System.out.println(parametersString);

        String url = login.address + "/" + login.base + parametersString.toString();
        connection = DriverManager.getConnection(url, login.name, login.pass);
    }

    public boolean disconnect() {
        if(!isConnected())
            return false;
        try {
            connection.close();
            connection = null;
            return true;
        }catch(Exception exception) {
            return false;
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public PreparedStatement prepareStatement(String statement, Object... arguments) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        for(int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            set(preparedStatement, i + 1, argument);
        }
        return preparedStatement;
    }

    public void execute(String query, Object... arguments) {
        try {
            PreparedStatement statement = prepareStatement(query, arguments);
            statement.executeUpdate();
            statement.close();
        }catch(Exception exception) {
            handle(exception);
        }
    }

    public CResultSet executeQuery(String query, Object... arguments) {
        try {
            return new CResultSet(this, prepareStatement(query, arguments).executeQuery());
        }catch(Exception exception) {
            handle(exception);
            return null;
        }
    }

    private <T> void set(PreparedStatement statement, int position, T argument) throws SQLException {
        Serializer<T> serializer = getSerializer(argument);

        try {
            serializer.set(statement, position, argument);
        }catch(SQLException exception) {
            throw exception;
        }catch(Exception exception) {
            throw new RuntimeException("Failed to serialize argument " + argument, exception);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Serializer<T> getSerializer(Class<?> type) {
        Serializer<T> serializer = (Serializer<T>) serializers.get(type);
        if(serializer == null) {
            throw new RuntimeException("No serializer found for " + type);
        }
        return serializer;
    }

    public <T> Serializer<T> getSerializer(T t) {
        return getSerializer(t == null ? null : t.getClass());
    }

    public <T> void registerSerializer(Class<T> type, Serializer<T> serializer) {
        serializers.put(type, serializer);
    }

    public <T> void registerSerializer(Class<T> type, SQLSetter<T> writer, SQLReader<T> reader) {
        registerSerializer(type, new Serializer<>(writer, reader));
    }

    public boolean isSilentErrors() {
        return silentErrors;
    }

    public void setSilentErrors(boolean silentErrors) {
        this.silentErrors = silentErrors;
    }

    protected void handle(Throwable throwable) {
        if(silentErrors)
            return;
        throwable.printStackTrace();
    }

    public static class LoginData {
        private String address;
        private String base;
        private String name;
        private String pass;

        public LoginData() {
        }

        public LoginData(String address, String base, String name, String pass) {
            this.address = address;
            this.base = base;
            this.name = name;
            this.pass = pass;
        }
    }

}
