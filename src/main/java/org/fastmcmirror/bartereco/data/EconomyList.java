package org.fastmcmirror.bartereco.data;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class EconomyList {
    @SerializedName("data")
    public Map<String, Integer> data;
}
