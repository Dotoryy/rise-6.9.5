package com.alan.clients.module.impl.other.clientspoofer;

import com.alan.clients.module.impl.other.clientspoofer.LabyModClientSpoofer;
import com.alan.clients.util.tuples.Triple;
import java.util.ArrayList;

public class LabyModPayloadList extends ArrayList<Triple<String, String, Boolean>> {
    final LabyModClientSpoofer spoofer;

    public LabyModPayloadList(LabyModClientSpoofer labyModClientSpoofer) {
        this.spoofer = labyModClientSpoofer;
        this.add(new Triple<>("MC|Brand", "labymod", true));
        this.add(new Triple<>("REGISTER", "labymod:neominecraft:intavelabymod3:main", false));
        this.add(new Triple<>("labymod:neo", "{\"version\":\"" + this.spoofer.spoofedVersionLatestWouldBePreferred.wo() + "\"}", false));
        this.add(new Triple<>("labymod3:main", "INFO{\"version\":\"" + this.spoofer.spoofedVersionLatestWouldBePreferred.wo() + "\"}", false));
        this.add(new Triple<>("minecraft:intave", "L{\"legacySneakHeight\":false,\"legacyOldRange\":false,\"legacyOldSlowdown\":false}", false));
        this.add(new Triple<>("minecraft:intave", "\nclientconfigL{\"legacySneakHeight\":false,\"legacyOldRange\":false,\"legacyOldSlowdown\":false}", false));
    }
}
