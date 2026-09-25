package com.alan.clients.util.account.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class GameOwnershipItem {
    @Expose
    @SerializedName("name")
    String gK;

    private GameOwnershipItem() {
    }
}
