package de.florianmichael.vialoadingbase;

import com.viaversion.viaversion.ViaManagerImpl.ViaManagerBuilder;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.protocol.ProtocolManagerImpl;
import de.florianmichael.vialoadingbase.model.Platform;
import de.florianmichael.vialoadingbase.platform.ViaBackwardsPlatformImpl;
import de.florianmichael.vialoadingbase.platform.ViaRewindPlatformImpl;
import de.florianmichael.vialoadingbase.platform.ViaVersionPlatformImpl;
import de.florianmichael.vialoadingbase.platform.viaversion.VLBViaCommandHandler;
import de.florianmichael.vialoadingbase.platform.viaversion.VLBViaInjector;
import de.florianmichael.vialoadingbase.platform.viaversion.VLBViaProviders;
import de.florianmichael.vialoadingbase.util.JLoggerToLog4j;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

public class ViaLoadingBase {
    public static final String VERSION = "${vialoadingbase_version}";
    public static final Logger LOGGER = new JLoggerToLog4j(LogManager.getLogger());
    public static final Platform PSEUDO_VIA_VERSION = new Platform(
        "ViaVersion", () -> true, () -> {}, var0 -> var0.addAll(ViaVersionPlatformImpl.createVersionList())
    );
    public static final Platform PLATFORM_VIA_BACKWARDS = new Platform(
        "ViaBackwards",
        () -> inClassPath("com.viaversion.viabackwards.api.ViaBackwardsPlatform"),
        () -> new ViaBackwardsPlatformImpl(Via.getManager().getPlatform().getDataFolder())
    );
    public static final Platform PLATFORM_VIA_REWIND = new Platform(
        "ViaRewind",
        () -> inClassPath("com.viaversion.viarewind.api.ViaRewindPlatform"),
        () -> new ViaRewindPlatformImpl(Via.getManager().getPlatform().getDataFolder())
    );
    public static final List<ProtocolVersion> PROTOCOLS = new ArrayList<>();
    private static ViaLoadingBase instance;
    private final LinkedList<Platform> platforms;
    private final File runDirectory;
    private final int nativeVersion;
    private final BooleanSupplier forceNativeVersionCondition;
    private final Supplier<JsonObject> dumpSupplier;
    private final Consumer<ViaProviders> providers;
    private final Consumer<ViaManagerBuilder> managerBuilderConsumer;
    private final Consumer<ProtocolVersion> onProtocolReload;
    private ProtocolVersion nativeProtocolVersion;
    private ProtocolVersion targetProtocolVersion;

    public ViaLoadingBase(
        LinkedList<Platform> platforms,
        File file,
        int nativeVersion,
        BooleanSupplier booleanSupplier,
        Supplier<JsonObject> dumpSupplier,
        Consumer<ViaProviders> providers,
        Consumer<ViaManagerBuilder> managerBuilderConsumer,
        Consumer<ProtocolVersion> onProtocolReload
    ) {
        this.platforms = platforms;
        this.runDirectory = new File(file, "ViaLoadingBase");
        this.nativeVersion = nativeVersion;
        this.forceNativeVersionCondition = booleanSupplier;
        this.dumpSupplier = dumpSupplier;
        this.providers = providers;
        this.managerBuilderConsumer = managerBuilderConsumer;
        this.onProtocolReload = onProtocolReload;
        instance = this;
        this.initPlatform();
    }

    public ProtocolVersion getTargetVersion() {
        return this.forceNativeVersionCondition != null && this.forceNativeVersionCondition.getAsBoolean()
            ? this.nativeProtocolVersion
            : this.targetProtocolVersion;
    }

    public void reload(ProtocolVersion targetProtocolVersion) {
        this.targetProtocolVersion = targetProtocolVersion;
        if (this.onProtocolReload != null) {
            this.onProtocolReload.accept(this.targetProtocolVersion);
        }
    }

    public void initPlatform() {
        for (Platform platform : this.platforms) {
            platform.createProtocolPath();
        }

        this.nativeProtocolVersion = ProtocolVersion.getProtocol(this.nativeVersion);
        this.targetProtocolVersion = this.nativeProtocolVersion;
        ViaVersionPlatformImpl viaversionplatformimpl = new ViaVersionPlatformImpl(LOGGER);
        ViaManagerBuilder viamanagerbuilder = ViaManagerImpl.builder()
            .platform(viaversionplatformimpl)
            .loader(new VLBViaProviders())
            .injector(new VLBViaInjector())
            .commandHandler(new VLBViaCommandHandler());
        if (this.managerBuilderConsumer != null) {
            this.managerBuilderConsumer.accept(viamanagerbuilder);
        }

        Via.init(viamanagerbuilder.build());
        ViaManagerImpl viamanagerimpl = (ViaManagerImpl)Via.getManager();
        viamanagerimpl.addEnableListener(() -> {
            Iterator iterator = this.platforms.iterator();

            while (iterator.hasNext()) {
                ((Platform)iterator.next()).build(LOGGER);
            }
        });
        viamanagerimpl.init();
        viamanagerimpl.onServerLoaded();
        viamanagerimpl.getProtocolManager().setMaxProtocolPathSize(Integer.MAX_VALUE);
        viamanagerimpl.getProtocolManager().setMaxPathDeltaIncrease(-1);
        ((ProtocolManagerImpl)viamanagerimpl.getProtocolManager()).refreshVersions();
        LOGGER.info("ViaLoadingBase has loaded " + Platform.COUNT + "/" + this.platforms.size() + " platforms");
    }

    public static ViaLoadingBase getInstance() {
        return instance;
    }

    public List<Platform> getSubPlatforms() {
        return this.platforms;
    }

    public File getRunDirectory() {
        return this.runDirectory;
    }

    public int getNativeVersion() {
        return this.nativeVersion;
    }

    public Supplier<JsonObject> getDumpSupplier() {
        return this.dumpSupplier;
    }

    public Consumer<ViaProviders> getProviders() {
        return this.providers;
    }

    public static boolean inClassPath(String var0) {
        try {
            Class.forName(var0);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Deprecated
    public static List<ProtocolVersion> getProtocols() {
        return PROTOCOLS;
    }

    public static class ViaLoadingBaseBuilder {
        private final LinkedList<Platform> platforms = new LinkedList<>();
        private File runDirectory;
        private Integer nativeVersion;
        private BooleanSupplier forceNativeVersionCondition;
        private Supplier<JsonObject> dumpSupplier;
        private Consumer<ViaProviders> providers;
        private Consumer<ViaManagerBuilder> managerBuilderConsumer;
        private Consumer<ProtocolVersion> onProtocolReload;

        public ViaLoadingBaseBuilder() {
            this.platforms.add(ViaLoadingBase.PSEUDO_VIA_VERSION);
            this.platforms.add(ViaLoadingBase.PLATFORM_VIA_BACKWARDS);
            this.platforms.add(ViaLoadingBase.PLATFORM_VIA_REWIND);
        }

        public static ViaLoadingBase.ViaLoadingBaseBuilder create() {
            return new ViaLoadingBase.ViaLoadingBaseBuilder();
        }

        public ViaLoadingBase.ViaLoadingBaseBuilder platform(Platform var1) {
            this.platforms.add(var1);
            return this;
        }

        public ViaLoadingBase.ViaLoadingBaseBuilder platform(Platform var1, int var2) {
            this.platforms.add(var2, var1);
            return this;
        }

        public ViaLoadingBase.ViaLoadingBaseBuilder runDirectory(File file) {
            this.runDirectory = file;
            return this;
        }

        public ViaLoadingBase.ViaLoadingBaseBuilder nativeVersion(int var1) {
            this.nativeVersion = var1;
            return this;
        }

        public ViaLoadingBase.ViaLoadingBaseBuilder forceNativeVersionCondition(BooleanSupplier booleanSupplier) {
            this.forceNativeVersionCondition = booleanSupplier;
            return this;
        }

        public ViaLoadingBase.ViaLoadingBaseBuilder dumpSupplier(Supplier<JsonObject> supplier) {
            this.dumpSupplier = supplier;
            return this;
        }

        public ViaLoadingBase.ViaLoadingBaseBuilder providers(Consumer<ViaProviders> consumer) {
            this.providers = consumer;
            return this;
        }

        public ViaLoadingBase.ViaLoadingBaseBuilder managerBuilderConsumer(Consumer<ViaManagerBuilder> consumer) {
            this.managerBuilderConsumer = consumer;
            return this;
        }

        public ViaLoadingBase.ViaLoadingBaseBuilder onProtocolReload(Consumer<ProtocolVersion> consumer) {
            this.onProtocolReload = consumer;
            return this;
        }

        public void build() {
            if (ViaLoadingBase.getInstance() != null) {
                ViaLoadingBase.LOGGER.severe("ViaLoadingBase has already started the platform!");
            } else if (this.runDirectory != null && this.nativeVersion != null) {
                new ViaLoadingBase(
                    this.platforms,
                    this.runDirectory,
                    this.nativeVersion,
                    this.forceNativeVersionCondition,
                    this.dumpSupplier,
                    this.providers,
                    this.managerBuilderConsumer,
                    this.onProtocolReload
                );
            } else {
                ViaLoadingBase.LOGGER.severe("Please check your ViaLoadingBaseBuilder arguments!");
            }
        }
    }
}
