package org.fastmcmirror.bartereco.database;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.fastmcmirror.bartereco.BarterEconomy;

import java.sql.SQLException;

public class MySQLData extends SQLData {

    public MySQLData() throws SQLException {
        super();
    }

    @Override
    protected ConnectionSource getConnectionSource() throws SQLException {
        int minimumIdle;
        HikariConfig config = new HikariConfig();
        config.setPoolName("bartereconomy-mysql");
        boolean ssl = BarterEconomy.instance.getConfig().getBoolean("mysql.ssl");
        String url = "jdbc:mysql://" + BarterEconomy.instance.getConfig().getString("mysql.host") + "/" +
                BarterEconomy.instance.getConfig().getString("mysql.database") + "?useSSL=" + ssl + "&serverTimezone=UTC&autoReconnect=true&allowPublicKeyRetrieval=true&characterEncoding=utf8";
        config.setJdbcUrl(url);
        String user = BarterEconomy.instance.getConfig().getString("mysql.user");
        String password = BarterEconomy.instance.getConfig().getString("mysql.password");
        if (user != null) {
            config.setUsername(user);
        }
        if (password != null) {
            config.setPassword(password);
        }
        config.setMinimumIdle((minimumIdle = BarterEconomy.instance.getConfig().getInt("mysql.minimum-idle")) > 0 ? minimumIdle : 4);
        int maximumPoolSize = BarterEconomy.instance.getConfig().getInt("mysql.maximum-pool-size");
        config.setMaximumPoolSize(maximumPoolSize > 0 ? maximumPoolSize : 16);
        long maxLifetime = BarterEconomy.instance.getConfig().getLong("mysql.max-lifetime");
        config.setMaxLifetime(maxLifetime > 0L ? maxLifetime : 600000L);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource dataSource = new HikariDataSource(config);
        return new HikariConnectionSource(dataSource, url);
    }

    private class HikariConnectionSource extends DataSourceConnectionSource {
        private final HikariDataSource hikariDataSource;

        public HikariConnectionSource(HikariDataSource source, String url) throws SQLException {
            super(source, url);
            hikariDataSource = source;
        }

        @Override
        public void close() throws Exception {
            super.close();
            hikariDataSource.close();
        }
    }
}
