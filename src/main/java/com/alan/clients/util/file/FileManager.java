package com.alan.clients.util.file;

import com.alan.clients.Client;
import com.alan.clients.util.interfaces.InstanceAccess;
import java.io.File;

public class FileManager {
    public static final File DIRECTORY = new File(InstanceAccess.aEg.mcDataDir, Client.b);

    public FileManager() {
    }

    public void init() {
        if (!DIRECTORY.exists()) {
            DIRECTORY.mkdir();
        }
    }
}
