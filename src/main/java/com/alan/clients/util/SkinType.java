package com.alan.clients.util;

public enum SkinType {
    AVATAR,
    HELM,
    BUST,
    ARMOR_BUST,
    BODY,
    ARMOR_BODY,
    CUBE,
    SKIN;

    private static final SkinType[] $VALUES = sa();

    SkinType() {
    }

    private static SkinType[] sa() {
        return new SkinType[]{AVATAR, HELM, BUST, ARMOR_BUST, BODY, ARMOR_BODY, CUBE, SKIN};
    }
}
