package com.alan.clients.util.file.data;

import com.alan.clients.Client;
import com.alan.clients.ui.click.standard.RiseClickGUI;
import com.alan.clients.util.file.FileManager;
import com.alan.clients.util.file.FileType;
import com.alan.clients.util.file.config.DataConfigFile;
import java.io.File;
import java.util.ArrayList;

public class DataManager extends ArrayList<DataConfigFile> {
    public static final File DATA_DIRECTORY = new File(FileManager.DIRECTORY, "data");

    public DataManager() {
    }

    public void init() {
        Client.a.a(new RiseClickGUI());
        if (!DATA_DIRECTORY.exists()) {
            DATA_DIRECTORY.mkdir();
        }

        this.update();
    }

    public DataConfigFile e(String var1, boolean var2) {
        File file1 = new File(DATA_DIRECTORY, var1 + ".json");
        DataConfigFile afy = new DataConfigFile(file1, FileType.CONFIG);
        if (var2) {
            afy.tp();
        }

        return afy;
    }

    public DataConfigFile bM(String var1) {
        File file1 = new File(DATA_DIRECTORY, var1 + ".json");
        DataConfigFile afy = new DataConfigFile(file1, FileType.CONFIG);
        afy.tp();
        return afy;
    }

    public void set(String var1) {
        File file1 = new File(DATA_DIRECTORY, var1 + ".json");
        DataConfigFile afy = this.bM(var1);
        if (afy == null) {
            afy = new DataConfigFile(file1, FileType.CONFIG);
            this.add(afy);
            System.out.println("Creating new config...");
        } else {
            System.out.println("Overwriting existing config...");
        }

        afy.write();
        System.out.println("Config saved to files.");
    }

    public boolean update() {
        this.clear();
        File[] afile = DATA_DIRECTORY.listFiles();
        if (afile == null) {
            return false;
        }

        for (File file1 : afile) {
            if (file1.getName().endsWith(".json")) {
                this.add(new DataConfigFile(file1, FileType.CONFIG));
            }
        }

        return true;
    }

    public boolean delete(String var1) {
        DataConfigFile afy = this.bM(var1);
        if (afy == null) {
            return false;
        }

        this.remove(afy);
        return afy.getFile().delete();
    }
}
