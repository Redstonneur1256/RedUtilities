package fr.redstonneur1256.redutilities.sql.serialization;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Serializer<T> implements SQLSetter<T>, SQLGetter<T> {

    private SQLSetter<T> writer;
    private SQLGetter<T> reader;

    public Serializer(SQLSetter<T> writer, SQLGetter<T> reader) {
        this.writer = writer;
        this.reader = reader;
    }

    @Override
    public T get(ResultSet resultSet, String column) throws Exception {
        return reader.get(resultSet, column);
    }

    @Override
    public void set(PreparedStatement statement, int position, T argument) throws Exception {
        writer.set(statement, position, argument);
    }

    public SQLSetter<T> getWriter() {
        return writer;
    }

    public SQLGetter<T> getReader() {
        return reader;
    }
}
