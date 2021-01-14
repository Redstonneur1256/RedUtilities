package fr.redstonneur1256.redutilities.sql.serialization;

import java.sql.ResultSet;

public interface SQLGetter<T> {

    T get(ResultSet resultSet, String column) throws Exception;

}
