package com.alan.clients.module.api;

import com.alan.clients.ui.click.standard.screen.Screen;
import com.alan.clients.ui.click.standard.screen.impl.CategoryScreen;
import com.alan.clients.ui.click.standard.screen.impl.ScriptScreen;
import com.alan.clients.ui.click.standard.screen.impl.LanguageScreen;
import com.alan.clients.ui.click.standard.screen.impl.SearchScreen;
import com.alan.clients.ui.click.standard.screen.impl.ThemeScreen;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.font.FontManager;
import lombok.Generated;

public enum Category {
    SEARCH("category.search", FontManager.ICONS_2.o(17), "U", 1, new SearchScreen()),
    COMBAT("category.combat", FontManager.ICONS_1.o(17), "a", 2, new CategoryScreen()),
    MOVEMENT("category.movement", FontManager.ICONS_1.o(17), "b", 3, new CategoryScreen()),
    PLAYER("category.player", FontManager.ICONS_1.o(17), "c", 4, new CategoryScreen()),
    RENDER("category.render", FontManager.ICONS_1.o(17), "g", 5, new CategoryScreen()),
    EXPLOIT("category.exploit", FontManager.ICONS_1.o(17), "a", 6, new CategoryScreen()),
    GHOST("category.ghost", FontManager.ICONS_1.o(17), "f", 7, new CategoryScreen()),
    SCRIPT("category.script", FontManager.ICONS_2.o(17), "m", 7, new ScriptScreen()),
    THEME("category.themes", FontManager.ICONS_2.o(17), "U", 10, new ThemeScreen()),
    LANGUAGE("category.language", FontManager.ICONS_2.o(17), "U", 10, new LanguageScreen());

    private final String name;
    private final String icon;
    private final int color;
    private final Font fontRenderer;
    public final Screen clickGUIScreen;
    private static final Category[] $VALUES = createValues();

    Category(String var3, Font var4, String var5, int color, Screen screen) {
        this.name = var3;
        this.icon = var5;
        this.color = color;
        this.clickGUIScreen = screen;
        this.fontRenderer = var4;
    }

    @Generated
    public String getIcon() {
        return this.icon;
    }

    @Generated
    public Font getFontRenderer() {
        return this.fontRenderer;
    }

    @Generated
    public Screen getClickGUIScreen() {
        return this.clickGUIScreen;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public int getColor() {
        return this.color;
    }

    private static Category[] createValues() {
        return new Category[]{SEARCH, COMBAT, MOVEMENT, PLAYER, RENDER, EXPLOIT, GHOST, SCRIPT, THEME, LANGUAGE};
    }
}
