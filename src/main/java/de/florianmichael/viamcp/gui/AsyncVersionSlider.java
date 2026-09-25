package de.florianmichael.viamcp.gui;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class AsyncVersionSlider extends GuiButton {
    private float dragValue = (float)ViaLoadingBase.PROTOCOLS.indexOf(ViaLoadingBase.getInstance().getTargetVersion()) / (ViaLoadingBase.PROTOCOLS.size() - 1);
    private final List<ProtocolVersion> values = ViaLoadingBase.PROTOCOLS;
    private float sliderValue;
    public boolean dragging;

    public AsyncVersionSlider(int var1, int var2, int var3, int var4, int var5) {
        super(var1, var2, var3, Math.max(var4, 110), var5, "");
        Collections.reverse(this.values);
        this.sliderValue = this.dragValue;
        this.displayString = this.values.get((int)Math.ceil(this.sliderValue * (this.values.size() - 1))).getName();
    }

    @Override
    public void drawButton(Minecraft mc, int var2, int var3) {
        super.drawButton(mc, var2, var3);
    }

    @Override
    protected int getHoverState(boolean var1) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int var2, int var3) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (float)(var2 - (this.xPosition + 4)) / (this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
                this.dragValue = this.sliderValue;
                int i = (int)Math.ceil(this.sliderValue * (this.values.size() - 1));
                this.displayString = this.values.get(i).getName();
                ViaLoadingBase.getInstance().reload(this.values.get(i));
            }

            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int var2, int var3) {
        if (super.mousePressed(mc, var2, var3)) {
            this.sliderValue = (float)(var2 - (this.xPosition + 4)) / (this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
            this.dragValue = this.sliderValue;
            int i = (int)Math.ceil(this.sliderValue * (this.values.size() - 1));
            this.displayString = this.values.get(i).getName();
            ViaLoadingBase.getInstance().reload(this.values.get(i));
            this.dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int var1, int var2) {
        this.dragging = false;
    }

    public void setVersion(int version) {
        this.dragValue = (float)ViaLoadingBase.PROTOCOLS.indexOf(ProtocolVersion.getProtocol(version)) / (ViaLoadingBase.PROTOCOLS.size() - 1);
        this.sliderValue = this.dragValue;
        int i = (int)(this.sliderValue * (this.values.size() - 1) + 0.5F);
        this.displayString = this.values.get(i).getName();
    }
}
