package com.alan.clients.util.file.insult;

import com.alan.clients.Client;
import com.alan.clients.module.impl.other.Insults;
import com.alan.clients.util.file.File;
import com.alan.clients.util.file.FileType;
import com.alan.clients.value.impl.SubMode;
import java.nio.file.Files;

public final class InsultFile extends File {
    public InsultFile(java.io.File var1, FileType var2) {
        super(var1, var2);
    }

    @Override
    public boolean te() {
        if (this.getFile().exists() && this.getFile().isFile() && this.getFile().canRead()) {
            try {
                Insults insults = Client.a.g().c(Insults.class);
                String s = this.getFile().getName().replace(".txt", "");
                insults.mode.add(new SubMode(s));
                insults.map.put(s, Files.readAllLines(this.getFile().toPath()));
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean write() {
        try {
            if (!this.getFile().exists()) {
                this.getFile().createNewFile();
            }

            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
