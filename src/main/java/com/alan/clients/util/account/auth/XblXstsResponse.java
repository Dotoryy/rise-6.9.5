package com.alan.clients.util.account.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class XblXstsResponse {
    @Expose
    @SerializedName("Token")
    public String Token;
    @Expose
    @SerializedName("DisplayClaims")
    public DisplayClaims DisplayClaims;

    private XblXstsResponse() {
    }
}
