package com.alan.clients.util.account.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class McResponse {
    @Expose
    @SerializedName("access_token")
    public String access_token;

    public McResponse() {
    }
}
