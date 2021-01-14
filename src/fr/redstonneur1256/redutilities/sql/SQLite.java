package fr.redstonneur1256.redutilities.sql;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends SQL {

    protected File file;

    public SQLite(File file) {
        super(null);
        this.file = file;
    }

    @Override
    public void connect() throws SQLException {
        if(isConnected()) {
            return;
        }

        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    public File getFile() {
        return file;
    }

}
