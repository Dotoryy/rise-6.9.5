package com.alan.clients.util.file;

import com.alan.clients.util.interfaces.InstanceAccess;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import lombok.Generated;

public abstract class File implements InstanceAccess {
    private final java.io.File file;
    private final FileType fileType;

    public abstract boolean te();

    public abstract boolean write();

    public void a(String var1, ArrayList<String> var2) {
        PrintWriter printwriter;
        try {
            printwriter = new PrintWriter(var1, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException filenotfoundexception) {
            throw new RuntimeException(filenotfoundexception);
        }

        for (String s : var2) {
            printwriter.println(s);
        }

        printwriter.close();
    }

    public ArrayList<String> tg() {
        ArrayList arraylist = new ArrayList();

        try {
            FileReader filereader = new FileReader(this.getFile());
            new BufferedReader(filereader);
            return arraylist;
        } catch (FileNotFoundException filenotfoundexception) {
            throw new RuntimeException(filenotfoundexception);
        }
    }

    @Generated
    public java.io.File getFile() {
        return this.file;
    }

    @Generated
    public FileType getFileType() {
        return this.fileType;
    }

    @Generated
    public File(java.io.File file, FileType fileType) {
        this.file = file;
        this.fileType = fileType;
    }
}
