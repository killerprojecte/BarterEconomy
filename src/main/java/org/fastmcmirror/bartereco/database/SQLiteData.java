package org.fastmcmirror.bartereco.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.fastmcmirror.bartereco.BarterEconomy;

import java.io.File;
import java.sql.SQLException;

public class SQLiteData extends SQLData {
    public SQLiteData() throws SQLException {
        super();
    }

    @Override
    protected ConnectionSource getConnectionSource() throws SQLException {
        String url = "jdbc:sqlite:" + BarterEconomy.instance.getDataFolder().getPath() + File.separator + "data.db";
        return new JdbcConnectionSource(url);
    }
}
