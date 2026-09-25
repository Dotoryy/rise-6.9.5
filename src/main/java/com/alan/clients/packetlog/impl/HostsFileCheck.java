package com.alan.clients.packetlog.impl;

import com.alan.clients.component.impl.player.LastConnectionComponent;
import com.alan.clients.security.SecurityFeature;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HostsFileCheck extends SecurityFeature
{


    public boolean run() {
        while (true) {
            Label_0335: {
                if (LastConnectionComponent.ip == null || !System.getProperty("os.name").toLowerCase().contains("win")) {
                    break Label_0335;
                }
                final String separator = File.separator;
                final File file = new File(new StringBuilder().append((Object)System.getenv("windir")).append((Object)separator).append("System32").append((Object)separator).append("drivers").append((Object)separator).append("etc").append((Object)separator).append("hosts").toString());
                if (!file.exists() || file.isDirectory()) {
                    break Label_0335;
                }
                try {
                    String line = null;
                    final BufferedReader reader = new BufferedReader(new FileReader(file));
                    Label_0292: {
                        while ((line = reader.readLine()) != null) {
                            if ((line.toLowerCase().contains(LastConnectionComponent.ip.toLowerCase()) && !LastConnectionComponent.ip.toLowerCase().contains("localhost") && !LastConnectionComponent.ip.toLowerCase().contains("127.0.0.1")) || line.toLowerCase().contains("rise")) {
                                break Label_0292;
                            }
                        }
                        break Label_0335;
                    }
                    System.out.println("HFC " + (Object)line);
                    return true;
                }
                catch (final IOException ex) {}
            }
            return false;
        }
    }

    static {
    }

    public String getReason() {
        return "hostsfile";
    }
}
