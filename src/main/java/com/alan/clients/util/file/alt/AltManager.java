package com.alan.clients.util.file.alt;

import com.alan.clients.util.account.localts.LocaltsConfig;
import com.alan.clients.util.file.FileManager;
import com.alan.clients.util.file.FileType;
import com.alan.clients.util.file.account.AccountFile;
import com.alan.clients.util.account.AltAccount;
import com.alan.clients.util.account.localts.LocaltsOrderStore;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AltManager {
    public static final File ALT_DIRECTORY = new File(FileManager.DIRECTORY, "alts");
    private final List<AltAccount> accounts = new ArrayList<>();

    public AltManager() {
    }

    public void init() {
        if (!ALT_DIRECTORY.exists()) {
            ALT_DIRECTORY.mkdir();
        }

        LocaltsOrderStore.init();
        LocaltsConfig.init();
    }

    public AccountFile tj() {
        return new AccountFile(this.sK(), FileType.ACCOUNT);
    }

    public boolean tk() {
        return this.tj().te();
    }

    public boolean update() {
        return this.tj().write();
    }

    private File sK() {
        return new File(ALT_DIRECTORY, "alts.json");
    }

    public List<AltAccount> getAccounts() {
        return this.accounts;
    }
}
