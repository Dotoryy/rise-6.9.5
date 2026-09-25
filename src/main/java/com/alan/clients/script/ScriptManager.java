package com.alan.clients.script;

import com.alan.clients.Client;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.script.util.ScriptClassFilter;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.file.FileManager;
import com.alan.clients.component.impl.render.NotificationComponent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import lombok.Generated;
import org.apache.commons.io.FileUtils;
import org.openjdk.nashorn.api.scripting.ClassFilter;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

public final class ScriptManager implements InstanceAccess {
    public static final File SCRIPT_DIRECTORY = new File(FileManager.DIRECTORY, "scripts");
    private static final FilenameFilter SCRIPT_FILE_FILTER = (var0, var1) -> var1.toLowerCase(Locale.ENGLISH).endsWith(".js");
    private static final ClassFilter SCRIPT_CLASS_FILTER = new ScriptClassFilter();
    private NashornScriptEngineFactory engineFactory;
    private Bindings globalBindings;
    private boolean securityMeasures = true;
    private final List<Script> scripts = new ArrayList<>();
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> this.loadBindings();

    public ScriptManager() {
    }

    public void init() {
        this.unloadScripts();
        this.engineFactory = new NashornScriptEngineFactory();
        this.loadBindings();

        try {
            this.loadScripts();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

        Client.a.e().b(this);
    }

    public Script getScript(String var1) {
        return this.scripts.stream().filter(var1x -> var1x.getName().equalsIgnoreCase(var1)).findAny().orElse(null);
    }

    public void loadScripts() throws IOException {
        this.loadScriptFiles();
        this.scripts.removeIf(var0 -> {
            try {
                var0.load();
                return false;
            } catch (ScriptException scriptexception) {
                scriptexception.printStackTrace();
                ChatUtil.d("");
                ChatUtil.b("Syntax error!");
                ChatUtil.d("");
                ChatUtil.b(scriptexception.getMessage());
                ChatUtil.d("");
                return true;
            }
        });
    }

    public void loadBindings() {
        this.globalBindings = new ScriptManager$1(this);
    }

    public void reloadScripts() {
        System.out.println("Reloaded Scripts");
        this.unloadScripts();

        try {
            this.loadScriptFiles();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

        this.scripts.removeIf(var0 -> {
            try {
                var0.reload();
                return false;
            } catch (ScriptException scriptexception) {
                scriptexception.printStackTrace();
                ChatUtil.d("");
                ChatUtil.b("Syntax error!");
                ChatUtil.d("");
                ChatUtil.b(scriptexception.getMessage());
                ChatUtil.d("");
                return true;
            }
        });
    }

    public void unloadScripts() {
        this.scripts.forEach(var0 -> {
            try {
                var0.unload();
            } catch (ScriptException scriptexception) {
                scriptexception.printStackTrace();
                NotificationComponent.e("Script \"" + var0.getName() + "\" unloaded incorrectly", "More details have been printed in a stacktrace.");
            }
        });
        this.scripts.clear();
    }

    public void setSecurityMeasures(boolean securityMeasures) {
        this.securityMeasures = securityMeasures;
        this.init();
    }

    private void loadScriptFiles() throws IOException {
        if (!SCRIPT_DIRECTORY.exists()) {
            SCRIPT_DIRECTORY.mkdirs();
        }

        for (File file1 : Objects.requireNonNull(SCRIPT_DIRECTORY.listFiles(SCRIPT_FILE_FILTER))) {
            if (file1.canRead()) {
                String s = this.getName(file1);
                if (!this.scripts.stream().anyMatch(var1 -> var1.getName().equals(s))) {
                    Script script = this.parseScript(FileUtils.readFileToString(file1, (Charset)null), file1);
                    this.scripts.add(script);
                }
            }
        }
    }

    public ScriptEngine createEngine() {
        ScriptEngine scriptengine = this.isSecurityMeasures() ? this.engineFactory.getScriptEngine(SCRIPT_CLASS_FILTER) : this.engineFactory.getScriptEngine();
        scriptengine.setBindings(this.globalBindings, 200);
        return scriptengine;
    }

    public Script parseScript(String var1, File file) {
        String s = "Unknown author";
        String s1 = "Unknown version";
        String s2 = "No description provided";
        boolean flag = false;

        for (String s3 : var1.split("\n")) {
            String s4 = s3.trim();
            if (s4.startsWith("//@")) {
                String s7;
                label50: {
                    label49: {
                        {
                            String s5 = s4.substring(3).trim();
                            String s6 = s5.toLowerCase(Locale.ENGLISH).split(" ")[0];
                            s7 = s5.substring(s6.length()).trim();
                            String s8 = s6;
                            switch (s8) {
                                case "author":
                                    s = s7;
                                    continue;
                                case "version":
                                    break label50;
                                case "description":
                                    break label49;
                                case "nosecurity":
                                    break;
                                default:
                                    continue;
                            }
                        }

                        flag = true;
                        continue;
                    }

                    s2 = s7;
                    continue;
                }

                s1 = s7;
            }
        }

        if (flag && this.securityMeasures) {
            throw new IllegalStateException("Script requires no security measures!");
        }
        return new Script(this.getName(file), s, s1, s2, var1, file);
    }

    private String getName(File file) {
        return file.getName().replace(".js", "");
    }

    @Generated
    public NashornScriptEngineFactory getEngineFactory() {
        return this.engineFactory;
    }

    @Generated
    public Bindings getGlobalBindings() {
        return this.globalBindings;
    }

    @Generated
    public boolean isSecurityMeasures() {
        return this.securityMeasures;
    }

    @Generated
    public List<Script> getScripts() {
        return this.scripts;
    }

    @Generated
    public Listener<WorldChangeEvent> getOnWorldChange() {
        return this.onWorldChange;
    }
}
