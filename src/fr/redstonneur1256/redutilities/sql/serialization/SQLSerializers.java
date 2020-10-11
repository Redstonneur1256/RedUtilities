package fr.redstonneur1256.redutilities.sql.serialization;

import fr.redstonneur1256.redutilities.sql.SQL;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;

public class SQLSerializers {

    public static void registerDefaults(SQL sql) {
        sql.registerSerializer(null, (statement, position, argument) -> statement.setNull(position, 0),
                (resultSet, column) -> null);

        sql.registerSerializer(Boolean.class, PreparedStatement::setBoolean, ResultSet::getBoolean);
        sql.registerSerializer(Byte.class, PreparedStatement::setByte, ResultSet::getByte);
        sql.registerSerializer(Short.class, PreparedStatement::setShort, ResultSet::getShort);
        sql.registerSerializer(Integer.class, PreparedStatement::setInt, ResultSet::getInt);
        sql.registerSerializer(Long.class, PreparedStatement::setLong, ResultSet::getLong);
        sql.registerSerializer(Float.class, PreparedStatement::setFloat, ResultSet::getFloat);
        sql.registerSerializer(Double.class, PreparedStatement::setDouble, ResultSet::getDouble);
        sql.registerSerializer(BigDecimal.class, PreparedStatement::setBigDecimal, ResultSet::getBigDecimal);
        sql.registerSerializer(String.class, PreparedStatement::setString, ResultSet::getString);
        sql.registerSerializer(byte[].class, PreparedStatement::setBytes, ResultSet::getBytes);
        sql.registerSerializer(Date.class, PreparedStatement::setDate, ResultSet::getDate);
        sql.registerSerializer(Time.class, PreparedStatement::setTime, ResultSet::getTime);
        sql.registerSerializer(Timestamp.class, PreparedStatement::setTimestamp, ResultSet::getTimestamp);
        sql.registerSerializer(InputStream.class, PreparedStatement::setBinaryStream, ResultSet::getBinaryStream);
        sql.registerSerializer(Ref.class, PreparedStatement::setRef, ResultSet::getRef);
        sql.registerSerializer(Blob.class, PreparedStatement::setBlob, ResultSet::getBlob);
        sql.registerSerializer(Clob.class, PreparedStatement::setClob, ResultSet::getClob);
        sql.registerSerializer(Array.class, PreparedStatement::setArray, ResultSet::getArray);
        sql.registerSerializer(URL.class, PreparedStatement::setURL, ResultSet::getURL);
    }

}
