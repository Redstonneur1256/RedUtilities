package fr.redstonneur1256.redutilities.sql;

import fr.redstonneur1256.redutilities.sql.serialization.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SQL {

    private LoginData login;
    private Map<Class<?>, Serializer<?>> serializers;
    private boolean silentErrors;
    private Connection connection;

    public SQL(String address, String base, String name, String pass) {
        this(new LoginData(address, base, name, pass));
    }

    public SQL(LoginData login) {
        this.login = login;
        this.serializers = new HashMap<>();

        SQLSerializers.registerDefaults(this);
    }

    public boolean connect() {
        if(isConnected())
            return false;
        try {
            String url = login.address + "/" + login.base + "?autoReconnect=true&useUnicode=true&characterEncoding=utf8";
            connection = DriverManager.getConnection(url, login.name, login.pass);
            return true;
        }catch(SQLException exception) {
            exception.printStackTrace();
            return false;
        }
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
