package fr.redstonneur1256.redutilities.sql;

import fr.redstonneur1256.redutilities.sql.serialization.Serializer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CResultSet {

    private final ResultSet result;
    private SQL sql;

    public CResultSet(SQL sql, ResultSet result) {
        this.sql = sql;
        this.result = result;
    }

    public <T> T get(Class<T> type, String columnName) {
        Serializer<T> serializer = sql.getSerializer(type);
        try {
            return serializer.get(result, columnName);
        }catch(Exception exception) {
            sql.handle(exception);
            return null;
        }
    }

    public String getString(String columnName) {
        return get(String.class, columnName);
    }

    public int getInt(String columnName) {
        return get(Integer.class, columnName);
    }

    public short getShort(String columnName) {
        return get(Short.class, columnName);
    }

    public long getLong(String columnName) {
        return get(Long.class, columnName);
    }

    public boolean getBoolean(String columnName) {
        return get(Boolean.class, columnName);
    }

    public double getDouble(String columnName) {
        return get(Double.class, columnName);
    }

    public boolean next() {
        try {
            return result.next();
        }catch(SQLException exception) {
            sql.handle(exception);
            return false;
        }
    }

    public int findColumn(String columnName) {
        try {
            return result.findColumn(columnName);
        }catch(SQLException exception) {
            sql.handle(exception);
            return -1;
        }
    }

    public void close() {
        try {
            result.close();
        }catch(SQLException exception) {
            sql.handle(exception);
        }
    }

    public SQL getSQL() {
        return sql;
    }

    public ResultSet getResult() {
        return result;
    }

}
