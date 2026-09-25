package com.alan.clients.command.impl;

import com.alan.clients.command.Command;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.file.FileManager;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Spotify extends Command {
    public final File bv = new File(FileManager.DIRECTORY, "data");
    private static final String bw = "spotify.json";

    public Spotify() {
        super("set Spotify API secret & ID", "spotify", "music");
    }

    @Override
    public void execute(String[] var1) {
        if (var1.length >= 3) {
            Map map;
            label26: {
                String s1;
                {
                    String s = var1[1];
                    s1 = var1[2];
                    map = this.aS();
                    String s2 = s;
                    switch (s2) {
                        case "clientsecret":
                            map.remove("client_secret");
                            map.put("client_secret", s1);
                            ChatUtil.b("Successfully Set clientsecret");
                            break label26;
                        case "clientid":
                            break;
                        default:
                            break label26;
                    }
                }

                map.remove("client_id");
                map.put("client_id", s1);
                ChatUtil.b("Successfully Set clientid");
            }

            this.a(map);
        } else {
            ChatUtil.b("command.spotify.usage");
        }
    }

    private Map<String, String> aS() {
        File file1 = new File(this.bv, "spotify.json");
        Map map = new HashMap();
        if (file1.exists()) {
            try (FileReader filereader = new FileReader(file1)) {
                Gson gson = new Gson();
                map = gson.fromJson(filereader, Map.class);
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }

        return map;
    }

    private void a(Map<String, String> var1) {
        File file1 = new File(this.bv, "spotify.json");

        try (FileWriter filewriter = new FileWriter(file1)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(var1, filewriter);
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}
