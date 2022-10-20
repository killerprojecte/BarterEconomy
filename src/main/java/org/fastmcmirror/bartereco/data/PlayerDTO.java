package org.fastmcmirror.bartereco.data;

import com.google.gson.Gson;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

@DatabaseTable(tableName = "economydata")
public class PlayerDTO implements Serializable {
    @DatabaseField(id = true, dataType = DataType.UUID)
    private UUID uuid;

    @DatabaseField
    private String data;

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public EconomyList getData() {
        return new Gson().fromJson(data, EconomyList.class);
    }

    public void setData(EconomyList list) {
        data = new Gson().toJson(list);
    }
}
