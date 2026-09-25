package com.alan.clients.compat;

//add code

/**
 * Code paths that no shipped Rise build ever executed, and that only became reachable
 * because this tree was deobfuscated.
 *
 * <p>Rise's obfuscator did not adapt string literals. The shipped client therefore carried
 * clean MCP names inside {@code getDeclaredField(...)} arguments while the vanilla layer it
 * ran against declared obfuscated ones -- {@code S44PacketWorldBorder.centerX} was
 * {@code deC}, {@code ChunkProviderClient.chunkListing} was {@code bCw}. Every one of those
 * lookups threw, the {@code catch} ran, and that is what the genuine 6.9.5 client did. The
 * recovery restored exactly the names the literals were written against, so the same lookups
 * now succeed for the first time in any build.
 *
 * <p>Every switch here is therefore {@code false} by default, and {@code false} means
 * "behave like the genuine client". Nothing here is a bug being kept; it is a difference
 * being kept out of the default build, with the better path left in the tree for anyone who
 * wants it. Verified against {@code pruned/} site by site -- see
 * {@code recovery-kit/docs/05-findings.md}, "Hooks that our renames brought to life".
 */
public final class RevivedPaths {

    /**
     * {@code ScriptPacket} subclasses reading vanilla packet fields by MCP name.
     *
     * <p>14 accessors across {@code ScriptPacketWorldBorder}, {@code ScriptPacketExplosion},
     * {@code ScriptPacketMaps}, {@code ScriptPacketResourcePackStatus} and
     * {@code ScriptPacketSpectate}. Off, they answer what they answered in every shipped
     * build: {@code ""}, {@code 0}, {@code 0.0}, {@code 0L}, and -- for the three
     * {@code Explosion} setters -- a printed {@code NoSuchFieldException} with the packet
     * left untouched.
     */
    private static final boolean SCRIPT_VANILLA_FIELDS =
            Boolean.getBoolean("rise.revived.scriptvanillafields");

    /**
     * {@code ReplayRecordingComponent} recording every loaded chunk instead of the
     * render-distance square.
     *
     * <p>The reflective block and the square loop are not an if/else: the block fills the
     * chunk set and the loop then adds whatever the block missed. Upstream the block threw on
     * its first instruction, so the square was the whole recording. On, recordings carry every
     * loaded chunk as the author plainly intended -- and are not the files the shipped client
     * produced.
     */
    private static final boolean REPLAY_ALL_LOADED_CHUNKS =
            Boolean.getBoolean("rise.revived.replaychunks");

    private RevivedPaths() {
    }

    public static boolean scriptVanillaFields() {
        return SCRIPT_VANILLA_FIELDS;
    }

    public static boolean replayAllLoadedChunks() {
        return REPLAY_ALL_LOADED_CHUNKS;
    }
}
