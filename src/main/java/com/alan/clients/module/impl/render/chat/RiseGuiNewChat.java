package com.alan.clients.module.impl.render.chat;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.impl.render.Chat;
import com.alan.clients.module.impl.render.UnlimitedChat;
import com.alan.clients.module.impl.render.chat.ChatImage;
import com.alan.clients.module.impl.render.chat.ChatImageManager;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.a;
import net.minecraft.util.IChatComponent;

public class RiseGuiNewChat extends GuiNewChat
{
    private Chat chatModule;

    public RiseGuiNewChat(final Minecraft minecraft) {
        super(minecraft);
    }

    public void setChatLine(final IChatComponent chatComponent, final int n, final int n2, final boolean b) {
        if (this.chatModule == null) {
            this.chatModule = (Chat)Client.a.g().c((Class)Chat.class);
        }
        System.out.println("[ChatImage] RiseGuiNewChat#setChatLine text=" + chatComponent.getUnformattedText());
        if (n != 0) {
            this.deleteChatLine(n);
        }
        final List a = GuiUtilRenderComponents.a(chatComponent, (int)(this.chatModule.getDragValue().aHe.x - 19.0), this.mc.fontRendererObj, false, false);
        final boolean chatOpen = this.getChatOpen();
        Object obj = Collections.emptyList();
        if ((boolean)this.chatModule.getImageChat().wo()) {
            final String unformattedText = chatComponent.getUnformattedText();
            final ChatImageManager mz = this.chatModule.getImageManager();
            obj = mz.extractUrls(unformattedText);
            System.out.println("[ChatImage] detected urls=" + String.valueOf(obj));
            final Iterator iterator = ((List)obj).iterator();
            while (iterator.hasNext()) {
                mz.queueImage(new ChatImage((String)iterator.next(), n2, n));
            }
        }
        for (int i = 0; i < a.size(); ++i) {
            final IChatComponent chatComponent2 = (IChatComponent)a.get(i);
            if (chatOpen && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }
            this.drawnChatLines.addFirst(new a(n2, chatComponent2, n, chatComponent, !((List)obj).isEmpty() && i == a.size() - 1));
        }
        final Module c = Client.a.g().c((Class)UnlimitedChat.class);
        final int n3 = (c == null || !c.isEnabled()) ? 200 : 10000;
        while (this.drawnChatLines.size() > n3) {
            this.drawnChatLines.removeLast();
        }
        if (!b) {
            this.chatLines.addFirst(new a(n2, chatComponent, n));
            while (this.chatLines.size() > n3) {
                this.chatLines.removeLast();
            }
        }
    }
}
