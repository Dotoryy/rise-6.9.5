package com.alan.clients.util.account.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @Expose
    @SerializedName("id")
    public String aEZ;
    @Expose
    @SerializedName("name")
    public String name;

    public ProfileResponse() {
    }
}
