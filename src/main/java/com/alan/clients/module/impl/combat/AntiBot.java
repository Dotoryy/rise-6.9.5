package com.alan.clients.module.impl.combat;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.combat.antibot.Advanced2AntiBot;
import com.alan.clients.module.impl.combat.antibot.AdvancedAntiBot;
import com.alan.clients.module.impl.combat.antibot.CubecraftBedrockCheckAntiBot;
import com.alan.clients.module.impl.combat.antibot.DuplicateNameCheckAntiBot;
import com.alan.clients.module.impl.combat.antibot.FuncraftCheckAntiBot;
import com.alan.clients.module.impl.combat.antibot.MiddleClickBotAntiBot;
import com.alan.clients.module.impl.combat.antibot.NPCDetectionCheckAntiBot;
import com.alan.clients.module.impl.combat.antibot.NoPingCheckAntiBot;
import com.alan.clients.module.impl.combat.antibot.TabCheckAntiBot;
import com.alan.clients.module.impl.combat.antibot.TimeVisibleCheckAntiBot;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.module.impl.combat.antibot.DuplicateUniqueIDCheckAntiBot;

@ModuleInfo(aliases = "module.combat.antibot.name", description = "module.combat.antibot.description", category = Category.COMBAT)
public final class AntiBot extends Module {
    private final BooleanValue funcraftAntiBot = new BooleanValue("Funcraft Check", this, false, new FuncraftCheckAntiBot("", this));
    private final BooleanValue tabCheck = new BooleanValue("Tab Check", this, false, new TabCheckAntiBot("", this));
    private final BooleanValue ncps = new BooleanValue("NPC Detection Check", this, false, new NPCDetectionCheckAntiBot("", this));
    private final BooleanValue duplicateNameCheck = new BooleanValue("Duplicate Name Check", this, false, new DuplicateNameCheckAntiBot("", this));
    private final BooleanValue noPingCheck = new BooleanValue("No Ping Check", this, false, new NoPingCheckAntiBot("", this));
    private final BooleanValue cubecraftBedrockCheck = new BooleanValue("Cubecraft Bedrock Check", this, false, new CubecraftBedrockCheckAntiBot("", this));
    private final BooleanValue duplicateUniqueIDCheck = new BooleanValue("Duplicate Unique ID Check", this, false, new DuplicateUniqueIDCheckAntiBot("", this));
    private final BooleanValue colourCheck = new BooleanValue("Colour Check", this, false, new DuplicateUniqueIDCheckAntiBot("", this));
    private final BooleanValue timeVisibleCheck = new BooleanValue("Time Visible Check", this, false, new TimeVisibleCheckAntiBot("", this));
    private final BooleanValue middleClick = new BooleanValue("Middle Click Bot", this, false, new MiddleClickBotAntiBot("", this));
    private final BooleanValue advanced = new BooleanValue("Advanced", this, false, new AdvancedAntiBot("", this));
    private final BooleanValue advanced2 = new BooleanValue("Advanced 2", this, false, new Advanced2AntiBot("", this));

    public AntiBot() {
    }

    @Override
    public void onDisable() {
        Client.a.getBotManager().clear();
    }
}
