package com.alan.clients.component.impl.community;

import com.alan.clients.component.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class CapeComponent extends Component {
    public static HashMap<String, String[]> hb = new HashMap<>();

    public CapeComponent() {
    }

    public static void updateFrames() {
        Iterator iterator = UserLookupComponent.dc.entrySet().iterator();

        while (iterator.hasNext()) {
            ClientUser by = (ClientUser)((Entry)iterator.next()).getValue();
            if (by.bT() && by.getCapeTextures() != null) {
                by.setFrame(by.getFrame() + 1);
                if (by.getFrame() >= by.getCapeTextures().length) {
                    by.setFrame(0);
                }
            }
        }
    }

    public static void loadTextures() {
        if (!hb.isEmpty()) {
            for (Entry entry : hb.entrySet()) {
                String s = (String)entry.getKey();
                String[] astring = (String[])entry.getValue();
                ClientUser by = UserLookupComponent.dc.get(s);
                ResourceLocation[] aresourcelocation = new ResourceLocation[astring.length];
                if (!by.ca()) {
                    by.setCapeLoaded(true);

                    for (int i = 0; i < astring.length; i++) {
                        int j = i;
                        String s1 = astring[i];
                        ResourceLocation resourcelocation = new ResourceLocation("capes/" + s + "/" + j);
                        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
                        new Thread(() -> {
                            try {
                                Thread.sleep(100L);
                                ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(null, s1, null, null);
                                texturemanager.loadTexture(resourcelocation, threaddownloadimagedata);
                                aresourcelocation[j] = resourcelocation;
                            } catch (InterruptedException interruptedexception) {
                                interruptedexception.printStackTrace();
                            }
                        }).start();
                    }

                    by.setCapeTextures(aresourcelocation);
                    hb.remove(s);
                }
            }
        }
    }
}
