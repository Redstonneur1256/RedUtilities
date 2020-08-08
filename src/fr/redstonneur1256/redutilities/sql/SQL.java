package fr.redstonneur1256.redutilities.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

public class SQL {
    private LoginData login;
    private Connection connection;
    public SQL(LoginData login) {
        this.login = login;
    }

    public boolean connect() {
        if(isConnected())
            return false;
        try {
            connection = DriverManager.getConnection(String.format("%s/%s?autoReconnect=true&useUnicode=true&characterEncoding=utf8", login.address, login.base),
                    login.name, login.pass);
            return true;
        } catch (SQLException exception) {
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

    public void execute(String q, Object...arguments) {
        try {
            PreparedStatement statement = connection.prepareStatement(q);
            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                set(statement, i + 1, argument);
            }
            statement.execute();
            statement.close();
        } catch (Exception e) {
            log("Error while executing query:", e);
        }
    }

    public CResultSet executeQuery(String query, Object...arguments) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                set(statement, i + 1, argument);
            }
            return new CResultSet(statement.executeQuery());
        } catch (Exception e) {
            log("Error while executing query:", e);
            return null;
        }
    }

    private void set(PreparedStatement statement, int i, Object argument) throws SQLException {
        if(argument == null) {
            statement.setNull(i, 0);
            return;
        }
        switch (argument.getClass().getSimpleName()) {
            case "Integer":
                statement.setInt(i, (Integer) argument);
                break;
            case "Long":
                statement.setLong(i, (Long) argument);
                break;
            case "Boolean":
                statement.setBoolean(i, (Boolean) argument);
                break;
            case "Short":
                statement.setShort(i, (Short) argument);
                break;
            default:
                statement.setString(i, String.valueOf(argument));
                break;
        }
    }

    protected static void log(String message, Exception exception) {
        System.out.println(message);
        exception.printStackTrace();
    }

    public static class LoginData {
        private String address;
        private String base;
        private String name;
        private String pass;
    }


    public static <T> String serialize(List<T> values, Function<T, String> mapper) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            builder.append(mapper.apply(values.get(i)));
            if(i <= values.size())
                builder.append("\u0000");
        }
        return builder.toString();
    }

}
