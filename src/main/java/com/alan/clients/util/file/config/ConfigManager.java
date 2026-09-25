package com.alan.clients.util.file.config;

import com.alan.clients.util.file.FileManager;
import com.alan.clients.util.file.FileType;
import com.alan.clients.util.file.config.ConfigFile;
import java.io.File;
import java.util.ArrayList;
import lombok.Generated;
import rip.vantage.commons.util.time.StopWatch;

public class ConfigManager extends ArrayList<ConfigFile> {
    public static final File CONFIG_DIRECTORY = new File(FileManager.DIRECTORY, "configs");
    private ConfigFile aHM;
    private final StopWatch aHN = new StopWatch();

    public ConfigManager() {
    }

    public void init() {
        if (!CONFIG_DIRECTORY.exists()) {
            CONFIG_DIRECTORY.mkdir();
        }

        this.aHN.setMillis(5000L);
        this.update();
    }

    public void tn() {
        File file1 = new File(CONFIG_DIRECTORY, "latest.json");
        ConfigFile configfile = new ConfigFile(file1, FileType.CONFIG);
        configfile.tm();
        configfile.te();
        this.aHM = configfile;
    }

    public ConfigFile get(String var1) {
        File file1 = new File(CONFIG_DIRECTORY, var1 + ".json");
        return new ConfigFile(file1, FileType.CONFIG);
    }

    public void d(String var1, boolean var2) {
        File file1 = new File(CONFIG_DIRECTORY, var1 + ".json");
        ConfigFile configfile = this.get(var1);
        if (var2) {
            configfile.tm();
        }

        if (configfile == null) {
            configfile = new ConfigFile(file1, FileType.CONFIG);
            this.add(configfile);
            System.out.println("Creating new config...");
        } else {
            System.out.println("Overwriting existing config...");
        }

        configfile.write();
        System.out.println("Config saved to files.");
    }

    public boolean update() {
        this.clear();
        File[] afile = CONFIG_DIRECTORY.listFiles();
        if (afile == null) {
            return false;
        }

        for (File file1 : afile) {
            if (file1.getName().endsWith(".json")) {
                this.add(new ConfigFile(file1, FileType.CONFIG, file1.getName().replace(".json", "")));
            }
        }

        return true;
    }

    public boolean delete(String var1) {
        ConfigFile configfile = this.get(var1);
        if (configfile == null) {
            return false;
        }

        this.remove(configfile);
        return configfile.getFile().delete();
    }

    public StopWatch mQ() {
        return this.aHN;
    }

    @Generated
    public ConfigFile getConfigfile() {
        return this.aHM;
    }
}
