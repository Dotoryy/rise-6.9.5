package com.alan.clients.util.account.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class DisplayClaims {
    @Expose
    @SerializedName("xui")
    Claim[] xui;

    private DisplayClaims() {
    }
}
