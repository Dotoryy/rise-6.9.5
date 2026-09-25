package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.shader.ShaderQueueType;
import java.util.ConcurrentModificationException;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;

@ModuleInfo(aliases = "module.render.chestesp.name", description = "module.render.chestesp.description", category = Category.RENDER)
public final class ChestESP extends Module {
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1 -> {
        Runnable runnable = () -> {
            try {
                aEg.theWorld.loadedTileEntityList.forEach(var2x -> {
                    if (var2x instanceof TileEntityChest || var2x instanceof TileEntityEnderChest) {
                        RendererLivingEntity.setShaderBrightness(this.rz().rA());
                        TileEntityRendererDispatcher.instance.b(var2x, var1.getPartialTicks());
                        RendererLivingEntity.No();
                    }
                });
            } catch (ConcurrentModificationException concurrentmodificationexception) {
            }
        };
        this.b(ShaderQueueType.BLOOM).c(runnable);
    };

    public ChestESP() {
    }
}
