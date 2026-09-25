package com.alan.clients.anticheat.listener;

import com.alan.clients.Client;
import com.alan.clients.anticheat.data.PlayerData;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public final class RegistrationListener {
    public RegistrationListener() {
    }

    public void b(EntityOtherPlayerMP other) {
        Client.a.n().getPlayerMap().put(other.getUniqueID(), new PlayerData(other));
    }

    public void handleDestroy(UUID uuid) {
        Client.a.n().getPlayerMap().remove(uuid);
    }
}
