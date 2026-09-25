package rip.vantage.commons.util.vantage;

import java.awt.Color;

public enum VantageClient {
    RISE("Rise", "63d0f9bc46ca6bf7ad9572b7", "§b", new Color(71, 148, 253)),
    WEED("Weed", "63d32b8e3012b50e9686dd39", "§2", new Color(70, 200, 70)),
    PRESTIGE("Prestige", "", "§1", new Color(79, 70, 229)),
    MONSOON("Monsoon", "63d106727842de723ada3bf0", "§3", new Color(32, 117, 171));

    private final String displayName;
    private final String productId;
    private final String chatColor;
    private final Color color;
    private static final VantageClient[] $VALUES = aKA();

    VantageClient(String var3, String var4, String var5, Color color) {
        this.displayName = var3;
        this.productId = var4;
        this.chatColor = var5;
        this.color = color;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getProductId() {
        return this.productId;
    }

    public String getChatColor() {
        return this.chatColor;
    }

    public Color getColor() {
        return this.color;
    }

    private static VantageClient[] aKA() {
        return new VantageClient[]{RISE, WEED, PRESTIGE, MONSOON};
    }
}
