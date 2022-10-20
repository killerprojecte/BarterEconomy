package org.fastmcmirror.bartereco.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.fastmcmirror.bartereco.data.PlayerDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class SQLData {
    public static Dao<PlayerDTO, UUID> playerDTOs;
    private static SQLData instance;
    private final ConnectionSource connectionSource;

    public SQLData() throws SQLException {
        instance = this;
        connectionSource = getConnectionSource();
        playerDTOs = DaoManager.createDao(connectionSource, PlayerDTO.class);
        if (!playerDTOs.isTableExists()) {
            TableUtils.createTable(this.connectionSource, PlayerDTO.class);
        }
    }

    public static SQLData getInstance() {
        return instance;
    }

    protected abstract ConnectionSource getConnectionSource() throws SQLException;

    public List<PlayerDTO> queryAll() throws SQLException {
        return playerDTOs.queryForAll();
    }

    public boolean createOrUpdate(PlayerDTO dto) throws SQLException {
        return playerDTOs.createOrUpdate(dto).isUpdated();
    }

    public PlayerDTO getData(UUID uuid) throws SQLException {
        return playerDTOs.queryForId(uuid);
    }

    public boolean hasPlayer(UUID uuid) throws SQLException {
        return playerDTOs.idExists(uuid);
    }

    public void close() {
        connectionSource.closeQuietly();
    }
}
