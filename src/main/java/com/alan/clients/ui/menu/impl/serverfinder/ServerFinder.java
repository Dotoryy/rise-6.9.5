package com.alan.clients.ui.menu.impl.serverfinder;

import com.alan.clients.ui.menu.Menu;
import com.alan.clients.ui.menu.impl.main.MainMenu;
import com.alan.clients.ui.menu.impl.serverfinder.ServerIP;
import com.alan.clients.util.player.ServerUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.StringUtils;

public final class ServerFinder
extends Menu {
    private final Pattern aCK = Pattern.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))");
    private final Pattern aCL = Pattern.compile("([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])");
    private final List<ServerData> servers = new ArrayList<ServerData>();
    private ThreadPoolExecutor executor = null;
    private boolean done;
    private GuiTextField aCO;
    private GuiTextField aCP;
    private GuiTextField aCQ;

    private void rt() {
        String string;
        int n2;
        int n3;
        ServerIP adt2;
        ServerIP adt3;
        block7: {
            block6: {
                block4: {
                    block3: {
                        block1: {
                            block5: {
                                String[] stringArray;
                                block2: {
                                    ((GuiButton)this.buttonList.get((int)0)).displayString = "Stop";
                                    String[] stringArray2 = this.aCO.getText().split("-");
                                    if (!this.aCK.matcher(stringArray2[0]).matches()) break block1;
                                    adt3 = new ServerIP(stringArray2[0]);
                                    adt2 = new ServerIP(stringArray2[0]);
                                    if (stringArray2.length <= 1) break block2;
                                    Matcher matcher = this.aCK.matcher(stringArray2[1]);
                                    if (!matcher.matches()) break block3;
                                    adt2 = new ServerIP(stringArray2[1]);
                                }
                                if (!this.aCL.matcher((stringArray = this.aCP.getText().split("-"))[0]).matches()) break block4;
                                n3 = Integer.parseInt(stringArray[0]);
                                n2 = Integer.parseInt(stringArray[0]);
                                if (stringArray.length <= 1) break block5;
                                Matcher matcher = this.aCL.matcher(stringArray[1]);
                                if (!matcher.matches()) break block6;
                                n2 = Integer.parseInt(stringArray[1]);
                            }
                            if (!StringUtils.isNumeric(string = this.aCQ.getText())) {
                                this.stop();
                                return;
                            }
                            break block7;
                        }
                        this.stop();
                        return;
                    }
                    this.stop();
                    return;
                }
                this.stop();
                return;
            }
            this.stop();
            return;
        }
        int n4 = Integer.parseInt(string);
        this.executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(n4);
        System.out.println("Started with " + n4 + " threads");
        this.servers.clear();
        this.done = false;
        this.a(ServerIP.min(adt3, adt2), ServerIP.max(adt3, adt2), Math.min(n3, n2), Math.max(n3, n2));
    }

    private void a(ServerIP serverIP, ServerIP adt3, int n2, int n3) {
        ServerIP adt4 = new ServerIP(serverIP.getFirst(), serverIP.getSecond(), serverIP.rw(), serverIP.getThird());
        for (int i2 = 0; i2 < 4; ++i2) {
            for (int i3 = serverIP.getPart(i2); i3 <= adt3.getPart(i2); ++i3) {
                adt4.setPart(i2, i3);
                ServerIP adt5 = new ServerIP(adt4.getPart(0), adt4.getPart(1), adt4.getPart(2), adt4.getPart(3));
                int n4 = n2;
                while (n4 <= n3) {
                    int n5 = n4++;
                    this.executor.execute(() -> {
                        if (this.done) {
                            return;
                        }
                        System.out.println("CHECKING " + String.valueOf(adt5) + ":" + n5);
                        ServerData serverData = ServerUtil.isOnline(adt5.toString(), n5, 500);
                        if (serverData != null) {
                            this.servers.add(serverData);
                        }
                        if (adt5.toString().equals(adt3.toString()) && n5 == n3) {
                            this.stop();
                        }
                    });
                }
            }
        }
    }

    private void stop() {
        this.servers.removeIf(serverData -> {
            if (serverData.populationInfo != null) return false;
            return true;
        });
        System.out.println(this.servers.size() + " servers found");
        for (ServerData serverData2 : this.servers) {
            GuiMultiplayer.bph = true;
            GuiMultiplayer.savedServerList.loadServerList();
            GuiMultiplayer.savedServerList.addServerData(serverData2);
            GuiMultiplayer.savedServerList.saveServerList();
        }
        ((GuiButton)this.buttonList.get((int)0)).displayString = "Start";
        this.done = true;
    }

    public void updateScreen() {
        this.aCO.updateCursorCounter();
        this.aCP.updateCursorCounter();
        this.aCQ.updateCursorCounter();
    }

    public void initGui() {
        this.aCO = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 56, 200, 20);
        this.aCO.setMaxStringLength(31);
        this.aCO.setFocused(true);
        this.aCO.setText("1.1.1.1-255.255.255.255");
        this.aCP = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, 96, 200, 20);
        this.aCP.setMaxStringLength(11);
        this.aCP.setText("1-25565");
        this.aCQ = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 136, 200, 20);
        this.aCQ.setMaxStringLength(4);
        this.aCQ.setText("128");
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Start"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96 + 12 + 20 + 4, I18n.format((String)"gui.done", (Object[])new Object[0])));
        super.initGui();
    }

    protected void actionPerformed(GuiButton guiButton) {
        switch (guiButton.id) {
            case 0: {
                block3: {
                    String string = ((GuiButton)this.buttonList.get((int)0)).displayString;
                    switch (string) {
                        case "Start": {
                            this.rt();
                            return;
                        }
                        case "Stop": {
                            break;
                        }
                        default: {
                            return;
                        }
                    }
                }
                this.stop();
                break;
            }
            case 1: {
                ServerFinder.aEg.displayGuiScreen(new GuiMultiplayer(new MainMenu()));
            }
        }
    }

    protected void keyTyped(char c2, int n2) {
        this.aCO.textboxKeyTyped(c2, n2);
        this.aCP.textboxKeyTyped(c2, n2);
        this.aCQ.textboxKeyTyped(c2, n2);
    }

    public void mouseClicked(int n2, int n3, int n4) {
        super.mouseClicked(n2, n3, n4);
        this.aCO.mouseClicked(n2, n3, n4);
        this.aCP.mouseClicked(n2, n3, n4);
        this.aCQ.mouseClicked(n2, n3, n4);
    }

    public void drawScreen(int n2, int n3, float f2) {
        this.drawDefaultBackground();
        this.drawString(this.fontRendererObj, "IP Range", this.width / 2 - 100, 40, 0xA0A0A0);
        this.drawString(this.fontRendererObj, "Port Range", this.width / 2 - 100, 82, 0xA0A0A0);
        this.drawString(this.fontRendererObj, "Threads", this.width / 2 - 100, 122, 0xA0A0A0);
        this.aCO.drawTextBox();
        this.aCP.drawTextBox();
        this.aCQ.drawTextBox();
        super.drawScreen(n2, n3, f2);
    }
}
