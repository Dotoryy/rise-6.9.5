package com.alan.clients.util.file.insult;

import com.alan.clients.util.file.FileManager;
import com.alan.clients.util.file.FileType;
import java.io.File;
import java.util.ArrayList;

public final class InsultManager extends ArrayList<InsultFile> {
    public static final File INSULT_DIRECTORY = new File(FileManager.DIRECTORY, "insults");

    public InsultManager() {
    }

    public void init() {
        if (!INSULT_DIRECTORY.exists()) {
            INSULT_DIRECTORY.mkdir();
        }
    }

    public InsultFile get(String var1) {
        for (InsultFile insultFile : this) {
            if (insultFile.getFile().getName().equalsIgnoreCase(var1 + ".txt")) {
                return insultFile;
            }
        }

        return null;
    }

    public void set(String var1) {
        File file1 = new File(INSULT_DIRECTORY, var1 + ".txt");
        InsultFile aga = this.get(var1);
        if (aga == null) {
            aga = new InsultFile(file1, FileType.INSULT);
            this.add(aga);
            System.out.println("Creating new ..");
        } else {
            System.out.println("Overwriting existing ..");
        }

        aga.write();
        System.out.println("Insults saved to files.");
    }

    public boolean update() {
        this.clear();
        File[] afile = INSULT_DIRECTORY.listFiles();
        if (afile == null) {
            return false;
        }

        for (File file1 : afile) {
            if (file1.getName().endsWith(".txt")) {
                this.add(new InsultFile(file1, FileType.INSULT));
            }
        }

        return true;
    }

    public boolean delete(String var1) {
        InsultFile insultFile = this.get(var1);
        if (insultFile == null) {
            return false;
        }

        this.remove(insultFile);
        return insultFile.getFile().delete();
    }
}
