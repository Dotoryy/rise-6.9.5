package com.alan.clients.script;

import com.alan.clients.script.api.GameSettingsAPI;
import com.alan.clients.script.api.MinecraftAPI;
import com.alan.clients.script.api.MoveAPI;
import com.alan.clients.script.api.NetworkAPI;
import com.alan.clients.script.api.PacketAPI;
import com.alan.clients.script.api.PlayerAPI;
import com.alan.clients.script.api.RenderAPI;
import com.alan.clients.script.api.RiseAPI;
import com.alan.clients.script.api.WebAPI;
import com.alan.clients.script.api.WorldAPI;
import javax.script.SimpleBindings;

public class ScriptManager$1 extends SimpleBindings {
    ScriptManager$1(ScriptManager scriptManager) {
        this.put("mc", new MinecraftAPI());
        this.put("rise", new RiseAPI());
        this.put("player", new PlayerAPI());
        this.put("world", new WorldAPI());
        this.put("network", new NetworkAPI());
        this.put("render", new RenderAPI());
        this.put("packet", new PacketAPI());
        this.put("input", new GameSettingsAPI());
        this.put("web", new WebAPI());
        this.put("move", new MoveAPI());
    }
}
