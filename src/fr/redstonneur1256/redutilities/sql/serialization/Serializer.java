package fr.redstonneur1256.redutilities.sql.serialization;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Serializer<T> implements SQLSetter<T>, SQLReader<T> {

    private SQLSetter<T> writer;
    private SQLReader<T> reader;

    public Serializer(SQLSetter<T> writer, SQLReader<T> reader) {
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

    public SQLReader<T> getReader() {
        return reader;
    }
}
