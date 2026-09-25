package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.KeyboardInputEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.ui.palette.CommandPalette;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.type.EvictingList;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.gui.ScrollUtil;
import com.alan.clients.util.gui.textbox.TextAlign;
import com.alan.clients.util.gui.textbox.TextBox;
import com.alan.clients.util.ime.PinyinInputHandler;
import com.alan.clients.util.ime.PinyinImeState;
import com.alan.clients.util.localization.Localization;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.newevent.impl.input.GuiKeyEvent;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.module.impl.render.chat.ChatImage;
import com.alan.clients.module.impl.render.chat.ChatImageManager;
import com.alan.clients.module.impl.render.chat.RiseGuiNewChat;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.optifine.gui.c;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import rip.vantage.network.core.VantageNetwork;

@ModuleInfo(aliases={"module.render.chat.name"}, description="module.render.chat.description", category=Category.RENDER, autoEnabled=true, allowDisable=false)
public final class Chat
extends Module {
    private static final float IMAGE_PADDING = 4.0f;
    private final NumberValue openHeight = new NumberValue("Open Height", this, (Number)130, (Number)0, (Number)200, (Number)1);
    private final NumberValue width = new NumberValue("Width", this, (Number)320, (Number)40, (Number)320, (Number)1);
    private final NumberValue maxClosedHeight = new NumberValue("Max Closed Height", this, (Number)130, (Number)0, (Number)500, (Number)1);
    private final NumberValue messageDisappearanceSpeed = new NumberValue("Message disappearance speed", this, (Number)5000, (Number)0, (Number)5000, (Number)1);
    private final BooleanValue background = new BooleanValue("Background", (Module)this, (Boolean)true);
    private final BooleanValue hidePlayerSourceMessages = new BooleanValue("Hide Player Source Messages", (Module)this, (Boolean)false);
    private final BooleanValue imageChat = new BooleanValue("Image Chat", (Module)this, (Boolean)true);
    private final ChatImageManager imageManager = new ChatImageManager();
    private final DragValue dragValue = new DragValue("", this, new Vector2d(200.0, 200.0), false, true);
    private final Animation scrollAnimation = new Animation(Easing.EASE_OUT_EXPO, 500L);
    private final Animation openAnimation = new Animation(Easing.EASE_OUT_ELASTIC, 400L);
    private final Animation alphaAnimation = new Animation(Easing.LINEAR, 400L);
    private final Animation heightAnimation = new Animation(Easing.EASE_OUT_EXPO, 500L);
    private final ScrollUtil scrollHelper = new ScrollUtil();
    private final Font chatFont;
    private final Font inputFont;
    private static final String clientPrefix = ChatUtil.getPrefix();
    private static final String RISE_PREFIX = "[Rise] ";
    private static final PinyinInputHandler pinyinHandler = new PinyinInputHandler();
    private final BooleanValue pinyinChineseIME;
    private static final String ALLOWED_CHARACTERS = Chat.buildAllowedCharacters();
    private static final int MAX_MESSAGE_LENGTH = 100;
    private int historyIndex;
    private String savedText;
    private TextBox textBox;
    private int lastLineCount;
    private int visibleLines;
    private float lineHeight;
    public boolean lastMouseDown;
    public boolean chatOpen;
    private rip.vantage.commons.util.time.StopWatch messageTimer;
    private EvictingList<String> sentMessages;
    private ArrayList<String> tabCompletions;
    private Interface interfaceModule;
    @EventLink
    public final Listener<KeyboardInputEvent> onKeyboardInput;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive;
    @EventLink
    public final Listener<GuiKeyEvent> onGuiKey;
    @EventLink
    public final Listener<Render2DEvent> onRender2D;

    public Chat() {
        this.chatFont = Chat.aEg.fontRendererObj;
        this.inputFont = Chat.aEg.fontRendererObj;
        this.pinyinChineseIME = new BooleanValue("Pinyin Chinese IME", (Module)this, (Boolean)false);
        this.historyIndex = -1;
        this.savedText = "";
        this.textBox = new TextBox(new Vector2d(0.0, 0.0), this.inputFont, Color.WHITE, TextAlign.LEFT, "", 1000.0f, ALLOWED_CHARACTERS);
        this.messageTimer = new rip.vantage.commons.util.time.StopWatch();
        this.sentMessages = new EvictingList(20);
        this.onKeyboardInput = keyboardInputEvent -> {
            char c2 = keyboardInputEvent.cP();
            if (c2 == '.' || c2 == '\u3002') {
                String string;
                if (keyboardInputEvent.getGuiScreen() == null) {
                    aEg.displayGuiScreen(new CommandPalette(null));
                    keyboardInputEvent.setCancelled();
                    return;
                }
                if (keyboardInputEvent.getGuiScreen() instanceof c && ((string = this.getTextBox().getText()) == null || string.trim().isEmpty())) {
                    aEg.displayGuiScreen(new CommandPalette(Chat.aEg.currentScreen));
                    keyboardInputEvent.setCancelled();
                    return;
                }
            }
            if (c2 == '/' || c2 == '.') {
                this.textBox.setSelected(true);
                this.textBox.key(c2, keyboardInputEvent.getKeyCode());
            }
        };
        this.onPacketReceive = packetReceiveEvent -> {
            Packet<?> packet = packetReceiveEvent.getPacket();
            if (packet instanceof S3APacketTabComplete) {
                this.tabCompletions = new ArrayList<String>(Arrays.asList(((S3APacketTabComplete)packet).func_149630_c()));
                for (String string : this.tabCompletions) {
                    ChatUtil.b(string, new Object[0]);
                }
            }
        };
        this.onGuiKey = dt2 -> {
            int n2 = dt2.cO();
            char c2 = dt2.cP();
            boolean bl = Chat.aEg.currentScreen instanceof c;
            if (Character.getType(c2) == 18 || c2 >= '\uf700' && c2 <= '\uf8ff') {
                c2 = '\u0000';
            }
            if (bl && ((Boolean)this.pinyinChineseIME.wo()).booleanValue() && PinyinImeState.isEnabled()) {
                this.textBox.selected = true;
                if (pinyinHandler.a(this.textBox, c2, n2)) {
                    return;
                }
            }
            switch (n2) {
                case 28: {
                    if (this.textBox.getText().isEmpty()) {
                        return;
                    }
                    String string = this.textBox.getText();
                    if (!string.startsWith("#") && string.length() > 100 && this.imageManager.hasUrl(string)) {
                        ChatUtil.d(ChatUtil.getPrefix() + String.valueOf((Object)EnumChatFormatting.RED) + "Vanilla chat only sends 100 characters. Long image URLs will be truncated.", new Object[0]);
                    }
                    if (!string.startsWith("#") && string.length() > 100) {
                        string = string.substring(0, 100);
                    }
                    Chat.aEg.thePlayer.sendChatMessage(string);
                    if (this.sentMessages.isEmpty() || !string.equals(this.sentMessages.get(this.sentMessages.size() - 1))) {
                        this.sentMessages.add(string);
                    }
                    this.textBox.text = "";
                    this.textBox.aJk = 0;
                    this.historyIndex = -1;
                    this.savedText = "";
                    aEg.displayGuiScreen(null);
                    return;
                }
                case 200: {
                    if (this.sentMessages.isEmpty()) {
                        return;
                    }
                    if (this.historyIndex == -1) {
                        this.savedText = this.textBox.text;
                        this.historyIndex = 0;
                    } else {
                        this.historyIndex = Math.min(this.historyIndex + 1, this.sentMessages.size() - 1);
                    }
                    int n3 = this.sentMessages.size() - 1 - this.historyIndex;
                    this.textBox.text = (String)this.sentMessages.get(n3);
                    this.textBox.aJk = this.textBox.text.length();
                    return;
                }
                case 208: {
                    if (this.sentMessages.isEmpty()) return;
                    if (this.historyIndex == -1) {
                        return;
                    }
                    --this.historyIndex;
                    if (this.historyIndex < 0) {
                        this.textBox.text = this.savedText;
                        this.textBox.aJk = this.textBox.text.length();
                        this.savedText = "";
                        return;
                    }
                    int n4 = this.sentMessages.size() - 1 - this.historyIndex;
                    this.textBox.text = (String)this.sentMessages.get(n4);
                    this.textBox.aJk = this.textBox.text.length();
                    return;
                }
                case 15: {
                    if (this.tabCompletions == null) {
                        String string = this.getTextBox().getText();
                        if (!string.startsWith(".") && !string.isEmpty()) {
                            BlockPos blockPos = null;
                            if (Chat.aEg.objectMouseOver != null && Chat.aEg.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                                blockPos = Chat.aEg.objectMouseOver.getBlockPos();
                            }
                            Chat.aEg.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(string, blockPos));
                        }
                        this.tabCompletions = new ArrayList();
                        return;
                    }
                    if (this.tabCompletions.isEmpty()) return;
                    CharSequence[] charSequenceArray = this.textBox.getText().split(" ");
                    int n5 = this.tabCompletions.indexOf(charSequenceArray[charSequenceArray.length - 1]);
                    charSequenceArray[charSequenceArray.length - 1] = this.tabCompletions.get(this.tabCompletions.size() > n5 + 1 ? n5 + 1 : 0);
                    this.textBox.text = String.join((CharSequence)" ", charSequenceArray);
                    this.textBox.aJk = this.textBox.getText().length();
                    return;
                }
            }
            this.textBox.selected = bl;
            this.textBox.key(c2, n2);
        };
        this.onRender2D = render2DEvent -> {
            this.renderChat();
            this.renderInputBox();
        };
        this.installChatHook();
    }

    public void renderChat() {
        this.installChatHook();
        this.rz();
        if (this.interfaceModule == null) {
            this.interfaceModule = this.e(Interface.class);
        }
        boolean bl = ((Mode)this.interfaceModule.getInformationType().wo()).getName().equals("Rise");
        this.chatOpen = Chat.aEg.currentScreen instanceof c;
        this.lineHeight = this.chatFont.height();
        ArrayList<net.minecraft.client.gui.a> arrayList = new ArrayList<net.minecraft.client.gui.a>(Chat.aEg.ingameGUI.getChatGUI().drawnChatLines);
        int n2 = this.countDisplayLines(arrayList);
        float f2 = Math.min((float)((Number)(this.chatOpen ? this.openHeight : this.maxClosedHeight).wo()).intValue(), this.lineHeight * (float)n2 + (float)(n2 == 0 ? 0 : 7));
        this.heightAnimation.Q(Math.min(f2, this.lineHeight * (this.chatOpen ? f2 : (float)this.visibleLines) + (float)(this.lineHeight * (this.chatOpen ? f2 : (float)this.visibleLines) == 0.0f ? 0 : 7)));
        this.dragValue.aHe = new Vector2d(((Number)this.width.wo()).doubleValue(), this.heightAnimation.getValue());
        this.dragValue.apP = new Vector2d(this.rz().qd(), (double)(Chat.aEg.jY.getScaledHeight() - 10 - 20) - this.dragValue.aHe.y);
        Vector2d vector2d = new Vector2d(this.dragValue.apP.x + 5.0, this.dragValue.apP.y + this.dragValue.aHe.y - 1.5);
        Vector2d vector2d2 = new Vector2d(vector2d.getX(), vector2d.getY());
        double d2 = Math.min(this.dragValue.aHe.y / 5.0, 6.0);
        if (((Boolean)this.background.wo()).booleanValue()) {
            if (bl) {
                double d3 = this.interfaceModule != null ? this.interfaceModule.getRoundingRadius() : d2;
                this.b(ShaderQueueType.REGULAR, 1).c(() -> RenderUtil.roundedRectangle(this.dragValue.apP.x, this.dragValue.apP.y, this.dragValue.aHe.x, this.dragValue.aHe.y, d3, Themes.rK()));
                this.b(ShaderQueueType.BLOOM).c(() -> RenderUtil.roundedRectangle(this.dragValue.apP.x + 1.0, this.dragValue.apP.y + 1.0, this.dragValue.aHe.x - 2.0, this.dragValue.aHe.y - 2.0, d3, this.rz().rE()));
                this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.roundedRectangle(this.dragValue.apP.x, this.dragValue.apP.y, this.dragValue.aHe.x, this.dragValue.aHe.y, d3, Color.BLACK));
            } else {
                this.b(ShaderQueueType.REGULAR, 1).c(() -> RenderUtil.d(this.dragValue.apP.x, this.dragValue.apP.y, this.dragValue.aHe.x, this.dragValue.aHe.y, new Color(0, 0, 0, 130)));
            }
        }
        this.b(ShaderQueueType.REGULAR, 1).c(() -> {
            block29: {
                block30: {
                    GL11.glEnable(3089);
                    RenderUtil.g(this.dragValue.apP.x, this.dragValue.apP.y, this.dragValue.aHe.x, this.dragValue.aHe.y);
                    this.scrollAnimation.Q(0.0);
                    vector2d.setY(vector2d.getY() + this.scrollAnimation.getValue() - this.scrollHelper.tE());
                    this.scrollHelper.E(this.chatOpen);
                    this.scrollHelper.H(true);
                    this.scrollHelper.V((double)(-this.lineHeight * (float)n2) + this.dragValue.aHe.y - 6.0);
                    if (!this.chatOpen) {
                        this.scrollHelper.U(0.0);
                    }
                    if (arrayList.size() > 150) {
                        List<net.minecraft.client.gui.a> list2 = Chat.aEg.ingameGUI.getChatGUI().drawnChatLines;
                        while (list2.size() > 100) {
                            list2.remove(list2.size() - 1);
                        }
                    }
                    if (this.lastLineCount >= n2) break block29;
                    if (!((Boolean)this.hidePlayerSourceMessages.wo()).booleanValue()) break block30;
                    Chat.aEg.ingameGUI.getChatGUI().drawnChatLines.removeIf(a2 -> a2.Dx().getFormattedText().contains(":"));
                    if (this.lastLineCount >= Chat.aEg.ingameGUI.getChatGUI().drawnChatLines.size()) break block29;
                }
                this.scrollAnimation.setValue(this.scrollAnimation.getValue() + (double)((float)(n2 - this.lastLineCount) * this.lineHeight));
                this.scrollAnimation.Q(this.scrollAnimation.getValue());
                this.visibleLines += n2 - this.lastLineCount;
                this.visibleLines = (int)Math.max(Math.min((float)((Number)this.maxClosedHeight.wo()).intValue() / this.lineHeight, (float)this.visibleLines), 0.0f);
            }
            this.lastLineCount = n2;
            if (this.messageTimer.T(((Number)this.messageDisappearanceSpeed.wo()).intValue()) || this.visibleLines == 0) {
                this.messageTimer.aX();
                this.visibleLines = Math.max(0, this.visibleLines - 1);
            }
            IChatComponent iChatComponent = null;
            Iterator iterator = arrayList.iterator();
            block1: while (true) {
                float f2x;
                float f3;
                float f4;
                ChatImage ye2;
                net.minecraft.client.gui.a a3;
                if (iterator.hasNext()) {
                    a3 = (net.minecraft.client.gui.a)iterator.next();
                    vector2d.setX(vector2d2.getX());
                    ye2 = this.a(a3);
                    f4 = this.a(ye2);
                    float f5 = f4 > 0.0f ? 4.0f : 0.0f;
                    float f6 = this.lineHeight + f5 + f4;
                    vector2d.setY(vector2d.getY() - (double)f6);
                    f3 = (float)(vector2d.getY() + (double)f4 + (double)f5);
                    f2x = (float)vector2d.getY();
                    double d = this.dragValue.apP.y - (double)this.lineHeight + (double)(this.chatOpen ? 0 : 6);
                    double d2x = this.dragValue.apP.y + this.dragValue.aHe.y;
                    if (vector2d.y + (double)f6 < d || vector2d.y > d2x) continue;
                } else {
                    if (iChatComponent != null) {
                        Vector2d vector2d3 = MouseUtil.rU();
                        Chat.aEg.currentScreen.handleComponentHover(iChatComponent, (int)vector2d3.getX(), (int)vector2d3.getY());
                        if (!this.lastMouseDown && Mouse.isButtonDown(0)) {
                            Chat.aEg.currentScreen.handleComponentClick(iChatComponent);
                        }
                        this.lastMouseDown = Mouse.isButtonDown(0);
                    }
                    if (((Boolean)this.background.wo()).booleanValue()) {
                        this.scrollHelper.a(new Vector2d(this.dragValue.apP.x + this.dragValue.aHe.x - 5.0, this.dragValue.apP.y + 5.0), this.dragValue.aHe.y - 10.0);
                    }
                    GL11.glDisable(3089);
                    return;
                }
                Iterator iterator2 = a3.Dx().iterator();
                block2: while (true) {
                    float f7;
                    float f8;
                    EnumChatFormatting enumChatFormatting;
                    String string;
                    IChatComponent iChatComponent2;
                    if (iterator2.hasNext()) {
                        iChatComponent2 = (IChatComponent)iterator2.next();
                        string = iChatComponent2.getUnformattedTextForChat();
                        enumChatFormatting = iChatComponent2.getChatStyle().getColor() == null ? EnumChatFormatting.RESET : iChatComponent2.getChatStyle().getColor();
                        f8 = (float)vector2d.getX();
                        f7 = f3;
                        if (string.startsWith(clientPrefix) || string.startsWith(RISE_PREFIX)) {
                            String string2 = string.startsWith(clientPrefix) ? clientPrefix : RISE_PREFIX;
                            this.a(this.chatFont, string2, f8, f7);
                            float f9 = this.chatFont.getStringWidth(string2);
                            if (this.chatOpen && MouseUtil.e(f8, f7, f9, this.lineHeight)) {
                                iChatComponent = iChatComponent2;
                            }
                            vector2d.x += (double)f9;
                            string = string.substring(string2.length());
                            f8 = (float)vector2d.getX();
                        }
                    } else {
                        int n2x;
                        n2x = ye2 == null ? -1 : ye2.getCurrentTextureId();
                        if (ye2 == null || !(f4 > 0.0f) || n2x < 0) continue block1;
                        int n4 = ye2.getRenderWidth(this.getMaxImageWidth());
                        int n5 = ye2.getRenderHeight(this.getMaxImageWidth());
                        RenderUtil.d(vector2d2.getX(), f2x, n4, n5, new Color(30, 30, 30, 200));
                        RenderUtil.a(n2x, (float)vector2d2.getX(), f2x, n4, n5, Color.WHITE);
                        continue block1;
                    }
                    String string3 = string.toLowerCase();
                    String[] stringArray = new String[]{"billionaire2", "billionaire", VantageNetwork.aKB().bX()};
                    int n6 = -1;
                    String string4 = null;
                    String[] stringArray2 = stringArray;
                    int length = stringArray2.length;
                    int n8 = 0;
                    while (true) {
                        block32: {
                            block31: {
                                if (n8 >= length) break block31;
                                String string5 = stringArray2[n8];
                                int n9 = string3.indexOf(string5);
                                if (n9 == -1) break block32;
                                n6 = n9;
                                string4 = string5;
                            }
                            if (n6 == -1 || string4 == null) break;
                            String string6 = string.substring(0, n6);
                            String string7 = string.substring(n6, n6 + string4.length());
                            String string8 = string.substring(n6 + string4.length());
                            if (!string6.isEmpty()) {
                                this.chatFont.b(String.valueOf((Object)enumChatFormatting) + string6, f8, f7, 0xFFFFFF);
                                float f10 = this.chatFont.getStringWidth(string6);
                                if (this.chatOpen && MouseUtil.e(f8, f7, f10, this.lineHeight)) {
                                    iChatComponent = iChatComponent2;
                                }
                                vector2d.x += (double)f10;
                                f8 = (float)vector2d.getX();
                            }
                            this.a(this.chatFont, string7, f8, f7);
                            float f11 = this.chatFont.getStringWidth(string7);
                            if (this.chatOpen && MouseUtil.e(f8, f7, f11, this.lineHeight)) {
                                iChatComponent = iChatComponent2;
                            }
                            vector2d.x += (double)f11;
                            float f12 = (float)vector2d.getX();
                            if (string8.isEmpty()) continue block2;
                            this.chatFont.b(String.valueOf((Object)enumChatFormatting) + string8, f12, f7, 0xFFFFFF);
                            float f13 = this.chatFont.getStringWidth(string8);
                            if (this.chatOpen && MouseUtil.e(f12, f7, f13, this.lineHeight)) {
                                iChatComponent = iChatComponent2;
                            }
                            vector2d.x += (double)f13;
                            continue block2;
                        }
                        ++n8;
                    }
                    float f14 = (float)this.chatFont.b(String.valueOf((Object)enumChatFormatting) + string, f8, f7, 0xFFFFFF) - f8;
                    if (this.chatOpen && MouseUtil.e(f8, f7, f14, this.lineHeight)) {
                        iChatComponent = iChatComponent2;
                    }
                    vector2d.x += (double)f14;
                }
            }
        });
    }

    private int countDisplayLines(List<net.minecraft.client.gui.a> list) {
        int n2 = list.size();
        for (net.minecraft.client.gui.a a2 : list) {
            float f2 = this.a(this.a(a2));
            if (!(f2 > 0.0f)) continue;
            n2 += Math.max(1, (int)Math.ceil(f2 / this.lineHeight));
        }
        return n2;
    }

    private float a(ChatImage ye2) {
        if (!((Boolean)this.imageChat.wo()).booleanValue() || ye2 == null || ye2.isFailed() || !ye2.isLoaded()) {
            return 0.0f;
        }
        this.imageManager.uploadTextures(ye2);
        return ye2.getRenderHeight(this.getMaxImageWidth());
    }

    private float getMaxImageWidth() {
        return Math.max(1.0f, (float)((Number)this.width.wo()).doubleValue() - 10.0f);
    }

    private ChatImage a(net.minecraft.client.gui.a a2) {
        if ((Boolean)this.imageChat.wo() == false) return null;
        if (a2 == null) return null;
        if (!a2.DB()) {
            return null;
        }
        IChatComponent iChatComponent = a2.DA() == null ? a2.Dx() : a2.DA();
        String string = this.imageManager.getFirstUrl(iChatComponent.getUnformattedText());
        if (string == null) {
            return null;
        }
        ChatImage ye2 = this.imageManager.getImage(string);
        if (ye2 != null) return ye2;
        this.imageManager.queueImage(new ChatImage(string, a2.Dy(), a2.getChatLineID()));
        return this.imageManager.getImage(string);
    }

    private void installChatHook() {
        if (aEg == null || Chat.aEg.ingameGUI == null) {
            return;
        }
        if (Chat.aEg.ingameGUI.getChatGUI() instanceof RiseGuiNewChat) {
            return;
        }
        GuiNewChat guiNewChat = Chat.aEg.ingameGUI.getChatGUI();
        RiseGuiNewChat yl2 = new RiseGuiNewChat(aEg);
        if (guiNewChat != null) {
            yl2.getSentMessages().addAll(guiNewChat.getSentMessages());
            yl2.EK().addAll(guiNewChat.EK());
            yl2.drawnChatLines.addAll(guiNewChat.drawnChatLines);
            yl2.scrollPos = guiNewChat.scrollPos;
            yl2.isScrolled = guiNewChat.isScrolled;
        }
        Chat.aEg.ingameGUI.persistantChatGUI = yl2;
        System.out.println("[ChatImage] Installed RiseGuiNewChat hook");
    }

    public void renderInputBox() {
        this.rz();
        if (this.interfaceModule == null) {
            this.interfaceModule = this.e(Interface.class);
        }
        boolean bl = ((Mode)this.interfaceModule.getInformationType().wo()).getName().equals("Rise");
        if (!((Boolean)this.pinyinChineseIME.wo()).booleanValue()) {
            pinyinHandler.aX();
            PinyinImeState.K(false);
            PinyinImeState.setEnabled(false);
        } else {
            PinyinImeState.K(true);
        }
        if (!this.chatOpen) {
            pinyinHandler.aX();
        }
        if (bl) {
            this.openAnimation.setDuration(!this.chatOpen ? 300L : 850L);
            this.openAnimation.setEasing(!this.chatOpen ? Easing.EASE_IN_EXPO : Easing.EASE_OUT_ELASTIC);
        } else {
            this.openAnimation.setDuration(0L);
            this.openAnimation.setEasing(!this.chatOpen ? Easing.EASE_IN_EXPO : Easing.EASE_OUT_ELASTIC);
        }
        this.openAnimation.Q(!this.chatOpen ? 0.0 : 1.0);
        this.alphaAnimation.Q(this.chatOpen ? 255.0 : 0.0);
        double d2 = this.openAnimation.getValue() > 1.0 ? 1.0 + (this.openAnimation.getValue() - 1.0) * 0.4 : this.openAnimation.getValue();
        this.textBox.bX("");
        if (this.openAnimation.getValue() <= 0.0) {
            return;
        }
        Vector2d vector2d = new Vector2d(this.rz().qd(), (float)Chat.aEg.jY.getScaledHeight() - this.rz().qd() - FontManager.MAIN.a(20, FontWeight.REGULAR).height() - 3.0f);
        Vector2d vector2d2 = new Vector2d(this.dragValue.aHe.x, (double)this.inputFont.height() + 7.5);
        Runnable runnable = () -> {
            GlStateManager.pushMatrix();
            GlStateManager.translate((vector2d.x + vector2d2.x / 2.0) * (1.0 - d2), (vector2d.y + vector2d2.y / 2.0) * (1.0 - d2), 0.0);
            GlStateManager.scale(d2, d2, 0.0);
        };
        if (bl) {
            double d3 = this.interfaceModule != null ? this.interfaceModule.getRoundingRadius() : 6.0;
            this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                runnable.run();
                RenderUtil.roundedRectangle(vector2d.x, vector2d.y, vector2d2.x, vector2d2.y, d3, ColorUtil.withAlpha(Themes.rK(), Math.min((int)this.alphaAnimation.getValue(), Themes.rK().getAlpha())));
                GlStateManager.popMatrix();
            });
            this.b(ShaderQueueType.BLOOM).c(() -> {
                runnable.run();
                RenderUtil.roundedRectangle(vector2d.x, vector2d.y, vector2d2.x, vector2d2.y, d3, this.rz().rE());
                GlStateManager.popMatrix();
            });
            this.b(ShaderQueueType.BLUR).c(() -> {
                runnable.run();
                RenderUtil.roundedRectangle(vector2d.x, vector2d.y, vector2d2.x, vector2d2.y, d3, Color.BLACK);
                GlStateManager.popMatrix();
            });
        } else {
            this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                runnable.run();
                RenderUtil.d(vector2d.x, vector2d.y, vector2d2.x, vector2d2.y, new Color(0, 0, 0, Math.min((int)this.alphaAnimation.getValue(), 130)));
                GlStateManager.popMatrix();
            });
        }
        this.textBox.setColor(ColorUtil.withAlpha(Color.WHITE, (int)this.alphaAnimation.getValue()));
        this.textBox.c(this.inputFont);
        this.textBox.setPosition(new Vector2d(this.rz().qd() + 5.0f, (float)Chat.aEg.jY.getScaledHeight() - this.inputFont.height() - this.rz().qd()));
        this.b(ShaderQueueType.REGULAR, 1).c(() -> {
            runnable.run();
            this.textBox.draw();
            if (this.chatOpen && ((Boolean)this.pinyinChineseIME.wo()).booleanValue()) {
                int n = Math.min(255, Math.max(0, (int)this.alphaAnimation.getValue()));
                String string2 = Localization.ce(PinyinImeState.isEnabled() ? "ui.chat.pinyin_ime.hint.on" : "ui.chat.pinyin_ime.hint.off");
                float f2 = (float)vector2d.x + 6.0f;
                float f3 = (float)vector2d.y - this.inputFont.height() - 2.0f;
                ArrayList<String> arrayList = new ArrayList<String>(3);
                arrayList.add(string2);
                if (PinyinImeState.isEnabled()) {
                    arrayList.add(Localization.ce("ui.chat.pinyin_ime.hint.help"));
                    try {
                        boolean blx;
                        File file2 = Minecraft.getMinecraft().mcDataDir;
                        File file3 = file2 == null ? null : new File(file2, "Rise");
                        File file4 = file3 == null ? null : new File(file3, "pinyin_dict.properties");
                        File file5 = file3 == null ? null : new File(file3, "pinyin_dict.tsv");
                        File file6 = file3 == null ? null : new File(file3, "pinyin_dict.txt");
                        boolean bl2 = file4 != null && file4.isFile();
                        boolean bl3 = bl2 | (file5 != null && file5.isFile());
                        boolean bl4 = bl3 | (file6 != null && file6.isFile());
                        if (!bl4 && file3 != null && file3.isDirectory()) {
                            File[] fileArray = file3.listFiles((file, string) -> {
                                if (string == null) return false;
                                if (!string.toLowerCase(Locale.ROOT).endsWith(".dict.yaml")) return false;
                                return true;
                            });
                            bl4 = fileArray != null && fileArray.length > 0;
                        }
                        blx = !bl4;
                        if (blx) {
                            arrayList.add(Localization.ce("ui.chat.pinyin_ime.dict.help"));
                        }
                    } catch (Throwable throwable) {
                    }
                }
                double d = 4.0;
                double d3 = this.inputFont.height();
                double d4 = 0.0;
                for (String string3 : arrayList) {
                    if (string3 == null || string3.isEmpty()) continue;
                    d4 = Math.max(d4, (double)this.inputFont.getStringWidth(string3));
                }
                int n2 = Math.max(1, arrayList.size());
                double d5 = d4 + 8.0;
                double d6 = (double)n2 * d3 + 4.0 + (double)(n2 - 1);
                double d7 = (double)f2 - 4.0;
                double d8 = (double)f3 - (double)(n2 - 1) * (d3 + 1.0);
                double d9 = d8 - 2.0;
                this.b(ShaderQueueType.BLUR).c(() -> {
                    runnable.run();
                    RenderUtil.roundedRectangle(d7, d9, d5, d6, 6.0, Color.BLACK);
                    GlStateManager.popMatrix();
                });
                this.b(ShaderQueueType.BLOOM).c(() -> {
                    runnable.run();
                    RenderUtil.roundedRectangle(d7, d9, d5, d6, 6.0, this.rz().rE());
                    GlStateManager.popMatrix();
                });
                RenderUtil.roundedRectangle(d7, d9, d5, d6, 6.0, ColorUtil.withAlpha(Themes.rK(), Math.min(n, Themes.rK().getAlpha())));
                for (int i = 0; i < n2; ++i) {
                    String string4 = (String)arrayList.get(i);
                    if (string4 == null || string4.isEmpty()) continue;
                    float f4 = (float)((double)f3 - (double)i * (d3 + 1.0));
                    int n3 = i == 0 ? 180 : (i == 1 ? 140 : 120);
                    this.inputFont.b(string4, f2, f4, ColorUtil.withAlpha(Color.WHITE, Math.min(n, n3)).hashCode());
                }
            }
            if (this.chatOpen && ((Boolean)this.pinyinChineseIME.wo()).booleanValue() && PinyinImeState.isEnabled() && pinyinHandler.uc()) {
                String string5 = pinyinHandler.uo();
                List<String> list = pinyinHandler.up();
                if (string5 != null && !string5.isEmpty()) {
                    int n = Math.min(255, Math.max(0, (int)this.alphaAnimation.getValue()));
                    float f5 = this.textBox.tL();
                    float f6 = this.textBox.tM();
                    this.inputFont.b(string5, f5, f6, ColorUtil.withAlpha(new Color(220, 220, 220), n).hashCode());
                    double d = this.inputFont.getStringWidth(string5);
                    RenderUtil.d(f5, f6 + this.inputFont.height() + 1.0f, d, 1.0, ColorUtil.withAlpha(Color.WHITE, Math.min(n, 180)));
                    if (list != null && !list.isEmpty()) {
                        float f7 = (float)this.textBox.getPosition().x;
                        float f8 = f6 - this.inputFont.height() - 3.0f;
                        StringBuilder stringBuilder = new StringBuilder();
                        int n4 = Math.min(9, list.size());
                        int n5 = pinyinHandler.un();
                        for (int i = 0; i < n4; ++i) {
                            if (i > 0) {
                                stringBuilder.append("  ");
                            }
                            String string6 = i + 1 + "." + list.get(i);
                            if (i == n5) {
                                stringBuilder.append('[').append(string6).append(']');
                                continue;
                            }
                            stringBuilder.append(string6);
                        }
                        stringBuilder.append(pinyinHandler.ut());
                        String string7 = stringBuilder.toString();
                        double d11 = this.inputFont.getStringWidth(string7) + 6;
                        double d12 = this.inputFont.height() + 4.0f;
                        RenderUtil.d(f7 - 2.0f, f8 - 2.0f, d11, d12, ColorUtil.withAlpha(new Color(0, 0, 0), Math.min(n, 140)));
                        this.inputFont.b(string7, f7 + 1.0f, f8, ColorUtil.withAlpha(Color.WHITE, n).hashCode());
                    }
                }
            }
            GlStateManager.popMatrix();
            if (!this.chatOpen) {
                this.textBox.text = "";
            }
        });
        if (!this.chatOpen) {
            this.textBox.text = "";
            this.tabCompletions = null;
        }
    }

    private void a(Font agc2, String string, float f2, float f3) {
        String string2 = EnumChatFormatting.getTextWithoutFormattingCodes(string);
        Color color = this.rz().rA();
        Color color2 = this.rz().rB();
        long now = System.currentTimeMillis();
        double d2 = 0.005;
        float f4 = f2;
        int n2 = string2.length();
        for (int i = 0; i < n2; ++i) {
            char c2 = string2.charAt(i);
            double d3 = (double)i / (double)n2 * Math.PI * 2.0;
            float f5 = (float)((Math.sin((double)now * d2 + d3) + 1.0) * 0.5);
            int n3 = (int)((float)color.getRed() + (float)(color2.getRed() - color.getRed()) * f5);
            int n4 = (int)((float)color.getGreen() + (float)(color2.getGreen() - color.getGreen()) * f5);
            int n5 = (int)((float)color.getBlue() + (float)(color2.getBlue() - color.getBlue()) * f5);
            int n6 = (int)((float)color.getAlpha() + (float)(color2.getAlpha() - color.getAlpha()) * f5);
            agc2.b(String.valueOf(c2), f4, f3, new Color(n3, n4, n5, n6).getRGB());
            f4 += (float)agc2.getStringWidth(String.valueOf(c2));
        }
    }

    private static String buildAllowedCharacters() {
        StringBuilder stringBuilder = new StringBuilder(4096);
        for (char c2 = '\u0000'; c2 < '\uffff'; c2 = (char)(c2 + '\u0001')) {
            if (!ChatAllowedCharacters.isAllowedCharacter(c2) && c2 != 32) continue;
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }

    @Generated
    public NumberValue getOpenHeight() {
        return this.openHeight;
    }

    @Generated
    public NumberValue getWidth() {
        return this.width;
    }

    @Generated
    public NumberValue getMaxClosedHeight() {
        return this.maxClosedHeight;
    }

    @Generated
    public NumberValue getMessageDisappearanceSpeed() {
        return this.messageDisappearanceSpeed;
    }

    @Generated
    public BooleanValue getBackground() {
        return this.background;
    }

    @Generated
    public BooleanValue getHidePlayerSourceMessages() {
        return this.hidePlayerSourceMessages;
    }

    @Generated
    public BooleanValue getImageChat() {
        return this.imageChat;
    }

    @Generated
    public ChatImageManager getImageManager() {
        return this.imageManager;
    }

    @Generated
    public DragValue getDragValue() {
        return this.dragValue;
    }

    @Generated
    public Animation getScrollAnimation() {
        return this.scrollAnimation;
    }

    @Generated
    public Animation getOpenAnimation() {
        return this.openAnimation;
    }

    @Generated
    public Animation getAlphaAnimation() {
        return this.alphaAnimation;
    }

    @Generated
    public Animation getHeightAnimation() {
        return this.heightAnimation;
    }

    @Generated
    public ScrollUtil getScrollHelper() {
        return this.scrollHelper;
    }

    @Generated
    public Font getChatFont() {
        return this.chatFont;
    }

    @Generated
    public Font getInputFont() {
        return this.inputFont;
    }

    @Generated
    public BooleanValue getPinyinChineseIME() {
        return this.pinyinChineseIME;
    }

    @Generated
    public int getHistoryIndex() {
        return this.historyIndex;
    }

    @Generated
    public String getSavedText() {
        return this.savedText;
    }

    @Generated
    public TextBox getTextBox() {
        return this.textBox;
    }

    @Generated
    public int getLastLineCount() {
        return this.lastLineCount;
    }

    @Generated
    public int getVisibleLines() {
        return this.visibleLines;
    }

    @Generated
    public float getLineHeight() {
        return this.lineHeight;
    }

    @Generated
    public boolean wasMouseDown() {
        return this.lastMouseDown;
    }

    @Generated
    public boolean isOpen() {
        return this.chatOpen;
    }

    @Generated
    public rip.vantage.commons.util.time.StopWatch getMessageTimer() {
        return this.messageTimer;
    }

    @Generated
    public EvictingList<String> getSentMessages() {
        return this.sentMessages;
    }

    @Generated
    public ArrayList<String> getTabCompletions() {
        return this.tabCompletions;
    }

    @Generated
    public Interface getInterfaceModule() {
        return this.interfaceModule;
    }

    @Generated
    public Listener<KeyboardInputEvent> getOnKeyboardInput() {
        return this.onKeyboardInput;
    }

    @Generated
    public Listener<PacketReceiveEvent> getOnPacketReceive() {
        return this.onPacketReceive;
    }

    @Generated
    public Listener<GuiKeyEvent> getKeyListener() {
        return this.onGuiKey;
    }

    @Generated
    public Listener<Render2DEvent> getOnRender2D() {
        return this.onRender2D;
    }
}
