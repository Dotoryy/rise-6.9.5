package com.alan.clients.util.account.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Claim {
    @Expose
    @SerializedName("uhs")
    String uhs;

    private Claim() {
    }
}
