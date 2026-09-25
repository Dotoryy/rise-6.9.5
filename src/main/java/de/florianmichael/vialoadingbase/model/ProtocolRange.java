package de.florianmichael.vialoadingbase.model;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

@Deprecated
public class ProtocolRange {
    private final ProtocolVersion lowerBound;
    private final ProtocolVersion upperBound;

    public ProtocolRange(ProtocolVersion lowerBound, ProtocolVersion upperBound) {
        if (lowerBound == null && upperBound == null) {
            throw new RuntimeException("Invalid protocol range");
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static ProtocolRange andNewer(ProtocolVersion protocolVersion) {
        return new ProtocolRange(null, protocolVersion);
    }

    public static ProtocolRange singleton(ProtocolVersion protocolVersion) {
        return new ProtocolRange(protocolVersion, protocolVersion);
    }

    public static ProtocolRange andOlder(ProtocolVersion protocolVersion) {
        return new ProtocolRange(protocolVersion, null);
    }

    public boolean contains(ProtocolVersion protocolVersion) {
        return this.lowerBound != null && protocolVersion.olderThan(this.lowerBound) ? false : this.upperBound == null || protocolVersion.olderThanOrEqualTo(this.upperBound);
    }

    @Override
    public String toString() {
        if (this.lowerBound == null) {
            return this.upperBound.getName() + "+";
        }
        if (this.upperBound == null) {
            return this.lowerBound.getName() + "-";
        }
        return this.lowerBound == this.upperBound ? this.lowerBound.getName() : this.lowerBound.getName() + " - " + this.upperBound.getName();
    }
}
