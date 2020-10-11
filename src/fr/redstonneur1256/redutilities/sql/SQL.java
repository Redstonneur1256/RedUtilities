package fr.redstonneur1256.redutilities.sql;

import fr.redstonneur1256.redutilities.sql.serialization.SQLReader;
import fr.redstonneur1256.redutilities.sql.serialization.SQLSerializers;
import fr.redstonneur1256.redutilities.sql.serialization.SQLSetter;
import fr.redstonneur1256.redutilities.sql.serialization.Serializer;
import org.apache.commons.lang3.SerializationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    public void disconnect() throws SQLException {
        if(!isConnected())
            return;
        connection.close();
        connection = null;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void execute(String query, Object... arguments) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for(int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                set(statement, i + 1, argument);
            }
            statement.execute();
            statement.close();
        }catch(Exception exception) {
            handle(exception);
        }
    }

    public CResultSet executeQuery(String query, Object... arguments) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for(int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                set(statement, i + 1, argument);
            }
            return new CResultSet(this, statement.executeQuery());
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
            throw new SerializationException("Failed to serialize argument " + argument, exception);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Serializer<T> getSerializer(Class<?> type) {
        Serializer<T> serializer = (Serializer<T>) serializers.get(type);
        if(serializer == null) {
            throw new SerializationException("No serializer found for " + type);
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
