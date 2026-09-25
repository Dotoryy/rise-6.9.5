package com.alan.clients.component.impl.community;

import lombok.Generated;

public enum UserRank {
    Regular("b"),
    Admin("a"),
    Developer("c"),
    Gato("b");

    private final String colorCode;
    private static final UserRank[] $VALUES = cc();

    UserRank(String var3) {
        this.colorCode = var3;
    }

    @Generated
    public String getColorCode() {
        return this.colorCode;
    }

    private static UserRank[] cc() {
        return new UserRank[]{Regular, Admin, Developer, Gato};
    }
}
