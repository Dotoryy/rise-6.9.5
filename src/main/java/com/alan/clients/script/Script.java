package com.alan.clients.script;

import com.alan.clients.Client;
import com.alan.clients.script.util.ScriptHandler;
import java.io.File;
import java.nio.charset.Charset;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import lombok.Generated;
import org.apache.commons.io.FileUtils;

public final class Script {
    private final String name;
    private final String author;
    private final String version;
    private final String description;
    private String code;
    private final File sourceFile;
    private ScriptEngine engine;
    private ScriptHandler apiHandler;
    private boolean loaded;

    public Script(String var1, String var2, String var3, String var4, String var5) {
        this(var1, var2, var3, var4, var5, null);
    }

    public Script(String var1, String var2, String var3, String var4, File file) {
        this(var1, var2, var3, var4, null, file);
    }

    public Script(String name, String author, String version, String description, String code, File sourceFile) {
        this.name = name;
        this.author = author;
        this.version = version;
        this.description = description;
        this.code = code;
        this.sourceFile = sourceFile;
    }

    public void load() throws ScriptException {
        try {
            if (this.loaded) {
                this.unload();
            }

            if (this.sourceFile != null) {
                this.code = FileUtils.readFileToString(this.sourceFile, (Charset)null);
            }

            if (this.code == null) {
                throw new ScriptException("Empty script");
            }

            this.engine = Client.a.getScriptManager().createEngine();
            this.apiHandler = new ScriptHandler();
            this.engine.put("script", this.apiHandler);
            this.engine.eval(this.code);
            this.call("onLoad");
            this.loaded = true;
        } catch (ScriptException scriptexception) {
            throw scriptexception;
        } catch (Exception exception) {
            throw new ScriptException(exception);
        }
    }

    public void unload() throws ScriptException {
        try {
            this.call("onUnload");
        } catch (Exception exception) {
            throw new ScriptException(exception);
        } finally {
            Client.a.getStandardClickGUI().oS();
            this.engine = null;
            this.apiHandler = null;
            this.loaded = false;
        }
    }

    public void reload() throws ScriptException {
        if (this.loaded) {
            this.unload();
        }

        this.load();
    }

    private void call(String var1, Object... var2) {
        this.apiHandler.call(var1, var2);
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getAuthor() {
        return this.author;
    }

    @Generated
    public String getVersion() {
        return this.version;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public File getSourceFile() {
        return this.sourceFile;
    }

    @Generated
    public ScriptEngine getEngine() {
        return this.engine;
    }

    @Generated
    public ScriptHandler getApiHandler() {
        return this.apiHandler;
    }

    @Generated
    public boolean isLoaded() {
        return this.loaded;
    }

    @Generated
    public void setCode(String code) {
        this.code = code;
    }

    @Generated
    public void setEngine(ScriptEngine engine) {
        this.engine = engine;
    }

    @Generated
    public void setApiHandler(ScriptHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    @Generated
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Generated
    @Override
    public boolean equals(Object var1) {
        if (var1 == this) {
            return true;
        } else if (!(var1 instanceof Script script)) {
            return false;
        } else {
            if (this.isLoaded() != script.isLoaded()) {
                return false;
            }

            String s = this.getName();
            String s1 = script.getName();
            if (s == null ? s1 == null : s.equals(s1)) {
                String s2 = this.getAuthor();
                String s3 = script.getAuthor();
                if (s2 == null ? s3 == null : s2.equals(s3)) {
                    String s4 = this.getVersion();
                    String s5 = script.getVersion();
                    if (s4 == null ? s5 == null : s4.equals(s5)) {
                        String s6 = this.getDescription();
                        String s7 = script.getDescription();
                        if (s6 == null ? s7 == null : s6.equals(s7)) {
                            String s8 = this.getCode();
                            String s9 = script.getCode();
                            if (s8 == null ? s9 == null : s8.equals(s9)) {
                                File file1 = this.getSourceFile();
                                File file2 = script.getSourceFile();
                                if (file1 == null ? file2 == null : file1.equals(file2)) {
                                    ScriptEngine scriptengine = this.getEngine();
                                    ScriptEngine scriptengine1 = script.getEngine();
                                    if (scriptengine == null ? scriptengine1 == null : scriptengine.equals(scriptengine1)) {
                                        ScriptHandler scripthandler = this.getApiHandler();
                                        ScriptHandler scripthandler1 = script.getApiHandler();
                                        return scripthandler == null ? scripthandler1 == null : scripthandler.equals(scripthandler1);
                                    }
                                    return false;
                                }
                                return false;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
    }

    @Generated
    @Override
    public int hashCode() {
        int i = 59 + (this.isLoaded() ? 79 : 97);
        String s = this.getName();
        int j = i * 59 + (s == null ? 43 : s.hashCode());
        String s1 = this.getAuthor();
        int k = j * 59 + (s1 == null ? 43 : s1.hashCode());
        String s2 = this.getVersion();
        int l = k * 59 + (s2 == null ? 43 : s2.hashCode());
        String s3 = this.getDescription();
        int i1 = l * 59 + (s3 == null ? 43 : s3.hashCode());
        String s4 = this.getCode();
        int j1 = i1 * 59 + (s4 == null ? 43 : s4.hashCode());
        File file1 = this.getSourceFile();
        int k1 = j1 * 59 + (file1 == null ? 43 : file1.hashCode());
        ScriptEngine scriptengine = this.getEngine();
        int l1 = k1 * 59 + (scriptengine == null ? 43 : scriptengine.hashCode());
        ScriptHandler scripthandler = this.getApiHandler();
        return l1 * 59 + (scripthandler == null ? 43 : scripthandler.hashCode());
    }

    @Generated
    @Override
    public String toString() {
        return "Script(name="
            + this.getName()
            + ", author="
            + this.getAuthor()
            + ", version="
            + this.getVersion()
            + ", description="
            + this.getDescription()
            + ", code="
            + this.getCode()
            + ", sourceFile="
            + this.getSourceFile()
            + ", engine="
            + this.getEngine()
            + ", apiHandler="
            + this.getApiHandler()
            + ", loaded="
            + this.isLoaded()
            + ")";
    }
}
