package com.alan.clients.util.file.config;

import com.alan.clients.util.file.File;
import com.alan.clients.util.file.FileType;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataConfigFile extends File {
    private static final SimpleDateFormat aHO = new SimpleDateFormat("dd.MM.yyyy");
    private boolean aHP;

    public DataConfigFile(java.io.File var1, FileType var2) {
        super(var1, var2);
    }

    @Override
    public boolean te() {
        if (!this.getFile().exists()) {
            return false;
        }

        try {
            FileReader filereader = new FileReader(this.getFile());
            BufferedReader bufferedreader = new BufferedReader(filereader);
            JsonObject jsonobject = this.A().fromJson(bufferedreader, JsonObject.class);
            bufferedreader.close();
            filereader.close();
            return jsonobject != null;
        } catch (IOException ioexception) {
            return false;
        }
    }

    @Override
    public boolean write() {
        try {
            this.getFile().createNewFile();
            JsonObject jsonobject = new JsonObject();
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("version", "6");
            jsonobject1.addProperty("creationDate", aHO.format(new Date()));
            jsonobject.add("Metadata", jsonobject1);
            FileWriter filewriter = new FileWriter(this.getFile());
            BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
            this.A().toJson(jsonobject, bufferedwriter);
            bufferedwriter.flush();
            bufferedwriter.close();
            return true;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return false;
        }
    }

    public void tp() {
        this.aHP = true;
    }
}
