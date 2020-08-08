package fr.redstonneur1256.redutilities.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Function;

import static fr.redstonneur1256.redutilities.sql.SQL.log;

public class CResultSet {


    private final ResultSet result;
    CResultSet(ResultSet result) {
        this.result = result;
    }

    public String getString(String columnLabel) {
        try {
            return result.getString(columnLabel);
        } catch (SQLException e) {
            log("Error while executing query:", e);
            return null;
        }
    }

    public int getInt(String columnLabel) {
        try {
            return result.getInt(columnLabel);
        } catch (SQLException e) {
            log("Error while executing query:", e);
            return 0;
        }
    }

    public short getShort(String columnLabel) {
        try {
            return result.getShort(columnLabel);
        }catch (SQLException e) {
            log("Error while executing query:", e);
            return 0;
        }
    }

    public long getLong(String columnLabel) {
        try {
            return result.getLong(columnLabel);
        }catch (SQLException e) {
            log("Error while executing query:", e);
            return 0;
        }
    }

    public boolean getBoolean(String columnLabel) {
        try {
            return result.getBoolean(columnLabel);
        } catch (SQLException e) {
            log("Error while executing query:", e);
            return false;
        }
    }

    public double getDouble(String columnLabel) {
        try {
            return result.getDouble(columnLabel);
        } catch (SQLException e) {
            log("Error while executing query:", e);
            return 0;
        }
    }

    public int findColumn(String columnLabel) {
        try {
            return result.findColumn(columnLabel);
        } catch (SQLException e) {
            log("Error while executing query:", e);
            return -1;
        }
    }

    public ArrayList<Integer> deserializeIntList(String column) {
        return deserializeList(column, Integer::parseInt);
    }

    public ArrayList<Long> deserializeLongList(String column) {
        return deserializeList(column, Long::parseLong);
    }

    public <T> ArrayList<T> deserializeList(String column, Function<String, T> mapper) {
        ArrayList<T> output = new ArrayList<>();
        for (String element : getString(column).split("\u0000")) {
            if(element == null || element.isEmpty()) {
                return output;
            }
            output.add(mapper.apply(element));
        }
        return output;
    }

    public void close() {
        try {
            result.close();
        } catch (SQLException e) {
            log("Error while closing query:", e);
        }
    }

    public ResultSet getResult() {
        return result;
    }

    public boolean next() {
        try {
            return result.next();
        } catch (SQLException e) {
            log("Error while executing query:", e);
            return false;
        }
    }

}
