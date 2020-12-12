package fr.redstonneur1256.redutilities.sql.serialization;

import java.sql.PreparedStatement;

public interface SQLSetter<T> {

    void set(PreparedStatement statement, int position, T argument) throws Exception;

}
