package com.alan.clients.component.impl.monitoring;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.BackendS2CEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorCommand;
import rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorConsent;
import rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorRequest;

public class MonitoringComponent extends Component {
    private static final String hY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxSC/qzVXB7Gt/I3NzffbTwndkLXbtfS4hIlWJdrzxiS67ipmciVGq97gVExGmLvsUYz9CQsjj0/cDqvlWapfDc9S79q9I9kBs1DEfttaskmpJ/hswEj84UwNdVLtaiDhnrqKXwlb1prC2gxovNFr+EMkS5yhkNUWbnBrt3iOKz5/WWcawUtS4jFRhyNIlT7S8fv/jq9je8wM/XWiWisRUuAi1YWtjp92Oz6pGf7tUR/99CFIK+s9f1VlUykSsI1xfraDcH74IczCrzTQJauKSX+bg/X5BWVwZX1XZ19LVoXySsCxstku+Po8vI4wxYp0RnRmHrXrErn28De3ytacrQIDAQAB";
    private byte[] hZ;
    private boolean ia;
    private boolean ib;
    private final Map<String, MonitoringEntry> ic = new LinkedHashMap<>();
    private final Map<String, byte[]> ie = new HashMap<>();
    private final Set<String> fld_if_14 = new HashSet<>();
    @EventLink
    public final Listener<BackendS2CEvent> ig = var1 -> {};

    public MonitoringComponent() {
    }

    public void cx() {
    }

    private void a(S2CPacketMonitorConsent packet) {
    }

    private void a(S2CPacketMonitorRequest packet) {
    }

    private void a(S2CPacketMonitorCommand packet) {
    }

    private void cy() {
    }

    private void cz() {
    }

    private void cA() {
    }

    private void lambda$new$0() {
    }
}
