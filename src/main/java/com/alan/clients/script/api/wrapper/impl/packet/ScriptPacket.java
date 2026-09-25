package com.alan.clients.script.api.wrapper.impl.packet;

import com.alan.clients.compat.RevivedPaths;
import com.alan.clients.script.api.wrapper.ScriptWrapper;
import java.lang.reflect.Field;
import net.minecraft.network.Packet;

public abstract class ScriptPacket<T extends Packet<?>> extends ScriptWrapper<T> {
    public ScriptPacket(T var1) {
        super(var1);
    }

    //add code

    /**
     * Looks a vanilla packet field up by the name the subclass spells, or throws what the
     * shipped client threw.
     *
     * <p>Rise's obfuscator did not adapt string literals, so the names the 14 call sites pass
     * -- {@code "centerX"}, {@code "mapScale"}, {@code "field_149152_f"} and the rest -- named
     * fields that the shipped client's vanilla layer declared as {@code deC}, {@code ddA},
     * {@code dcW}. Every lookup threw {@code NoSuchFieldException}, every caller's
     * {@code catch} ran, and that was the genuine client's behaviour. Deobfuscating the
     * vanilla layer restored those MCP names, so the lookups would now succeed.
     *
     * <p>This is the one place that decision lives. Off -- the default -- it throws the same
     * exception type with the same message the JDK threw, before touching the packet, so every
     * caller's {@code catch} block reproduces its shipped answer unchanged, including the three
     * {@code ScriptPacketExplosion} setters whose fallback is
     * {@code exception.printStackTrace()}. On, via
     * {@code -Drise.revived.scriptvanillafields=true}, the accessors read the real field.
     *
     * <p>The literals are deliberately left correct rather than reverted to {@code deC} and
     * friends: they are the only surviving record of which vanilla field each accessor was
     * written to expose, and a name that resolves is checkable where a misspelling is not.
     */
    protected final Field vanillaField(String name) throws NoSuchFieldException {
        if (!RevivedPaths.scriptVanillaFields()) {
            throw new NoSuchFieldException(name);
        }

        return this.wrapped.getClass().getDeclaredField(name);
    }

    public String getType() {
        return this.wrapped.getClass().getSimpleName();
    }

    public String getFullType() {
        return this.wrapped.getClass().getName();
    }

    public boolean isType(String var1) {
        String s = this.wrapped.getClass().getSimpleName();
        String s1 = this.wrapped.getClass().getName();
        return s.equals(var1) || s1.equals(var1) || s1.endsWith("." + var1);
    }

    public T getWrapped() {
        return this.wrapped;
    }
}
