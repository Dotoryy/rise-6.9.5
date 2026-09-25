package com.alan.clients.util.file.account;

import com.alan.clients.Client;
import com.alan.clients.util.file.FileType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.alan.clients.util.account.AltAccount;
import com.alan.clients.util.account.AltType;
import com.alan.clients.util.account.impl.CrackedAccount;
import com.alan.clients.util.account.impl.MicrosoftAccount;
import com.alan.clients.util.account.impl.RaveAccount;
import com.alan.clients.util.file.account.AccountFileSwitchMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AccountFile
extends com.alan.clients.util.file.File {
    private static final SimpleDateFormat aHE = new SimpleDateFormat("dd.MM.yyyy");
    private static final long aHF = 172800000L;

    public AccountFile(File file, FileType fileType) {
        super(file, fileType);
    }

    private static boolean a(AltAccount altAccount) {
        if (!(altAccount instanceof MicrosoftAccount)) {
            return false;
        }
        String string = ((MicrosoftAccount)altAccount).so();
        if (string != null && !string.isEmpty()) {
            return false;
        }
        long l2 = altAccount.sj();
        if (l2 <= 0L) return false;
        if (System.currentTimeMillis() - l2 <= 172800000L) return false;
        return true;
    }

    @Override
    public boolean te() {
        if (!this.getFile().exists()) {
            return false;
        }
        try {
            FileReader fileReader = new FileReader(this.getFile());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            JsonObject jsonObject = this.A().fromJson((Reader)bufferedReader, JsonObject.class);
            bufferedReader.close();
            fileReader.close();
            if (jsonObject == null) {
                return false;
            }
            List<AltAccount> list = Client.a.getAltManager().getAccounts();
            list.clear();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            if (jsonArray != null) {
                for (int i2 = 0; i2 < jsonArray.size(); ++i2) {
                    JsonObject jsonObject2 = jsonArray.get(i2).getAsJsonObject();
                    AltAccount ael2 = new AltAccount(AltType.CRACKED, "", "", "");
                    ael2.b(jsonObject2);
                    switch (ael2.sg()) {
                        case CRACKED: {
                            ael2 = new CrackedAccount("");
                            ael2.b(jsonObject2);
                            break;
                        }
                        case MICROSOFT: {
                            ael2 = new MicrosoftAccount("", "", "", "");
                            ael2.b(jsonObject2);
                            break;
                        }
                        case RAVE: {
                            ael2 = new RaveAccount("", "", "", "");
                            ael2.b(jsonObject2);
                        }
                    }
                    if (AccountFile.a(ael2)) {
                        System.out.println("purging expired access-only account: " + ael2.getName());
                        continue;
                    }
                    list.add(ael2);
                    System.out.println("loading account: " + ael2.getName());
                }
            }
            return true;
        }
        catch (IOException iOException) {
            return false;
        }
    }

    @Override
    public boolean write() {
        try {
            List<AltAccount> list;
            if (!this.getFile().exists()) {
                this.getFile().createNewFile();
            }
            //add code
            list = Client.a.getAltManager().getAccounts();
            JsonObject jsonObject = new JsonObject();
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("version", "6");
            jsonObject2.addProperty("creationDate", aHE.format(new Date()));
            jsonObject.add("Metadata", jsonObject2);
            JsonArray jsonArray = new JsonArray();
            for (AltAccount altAccount : list) {
                jsonArray.add(altAccount.sf());
                System.out.println("writing account: " + altAccount.getName());
            }
            jsonObject.add("data", jsonArray);
            FileWriter fileWriter = new FileWriter(this.getFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            this.A().toJson((JsonElement)jsonObject, (Appendable)bufferedWriter);
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.flush();
            fileWriter.close();
            return true;
        }
        catch (IOException iOException) {
            return false;
        }
    }
}
