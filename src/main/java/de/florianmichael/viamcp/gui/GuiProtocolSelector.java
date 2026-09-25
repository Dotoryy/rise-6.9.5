package de.florianmichael.viamcp.gui;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.protocolinfo.ProtocolInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

public class GuiProtocolSelector extends GuiScreen {
    private final GuiScreen parent;
    public GuiProtocolSelector.SlotList list;

    public GuiProtocolSelector(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 25, 200, 20, "Back"));
        this.list = new GuiProtocolSelector.SlotList(this, aEg, this.width, this.height, 32, this.height - 32);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        this.list.actionPerformed(button);
        if (button.id == 1) {
            aEg.displayGuiScreen(this.parent);
        }
    }

    @Override
    public void handleMouseInput() {
        this.list.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        this.list.drawScreen(var1, var2, var3);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0, 2.0, 2.0);
        String s = EnumChatFormatting.BOLD + "ViaMCP";
        this.drawString(this.fontRendererObj, s, (this.width - 4) / 4, 5, -1);
        GlStateManager.popMatrix();
        this.drawString(this.fontRendererObj, "by EnZaXD/Flori2007", 1, 1, -1);
        this.drawString(this.fontRendererObj, "Discord: EnZaXD#6257", 1, 11, -1);
        ProtocolInfo protocolinfo = ProtocolInfo.fromProtocolVersion(ViaLoadingBase.getInstance().getTargetVersion());
        String s1 = "Version: " + ViaLoadingBase.getInstance().getTargetVersion().getName() + (protocolinfo != null ? " - " + protocolinfo.getName() : "");
        String s2 = protocolinfo != null ? "Released: " + protocolinfo.getReleaseDate() : "";
        int i = (5 + 9) * 2 + 2;
        this.drawString(this.fontRendererObj, "" + EnumChatFormatting.GRAY + EnumChatFormatting.BOLD + "Version Information", (this.width - 4) / 2, i, -1);
        this.drawString(this.fontRendererObj, s1, 4, i + 9, -1);
        this.drawString(this.fontRendererObj, s2, 4, i + 18, -1);
        super.drawScreen(var1, var2, var3);
    }

    public static class SlotList extends GuiSlot {
        final GuiProtocolSelector this$0;

        public SlotList(GuiProtocolSelector guiProtocolSelector, Minecraft mc, int var3, int var4, int var5, int var6) {
            super(mc, var3, var4, var5 + 30, var6, 18);
            this.this$0 = guiProtocolSelector;
        }

        @Override
        protected int getSize() {
            return ViaLoadingBase.getProtocols().size();
        }

        @Override
        protected void elementClicked(int var1, boolean var2, int var3, int var4) {
            ProtocolVersion protocolversion = ViaLoadingBase.getProtocols().get(var1);
            ViaLoadingBase.getInstance().reload(protocolversion);
        }

        @Override
        protected boolean isSelected(int var1) {
            return false;
        }

        @Override
        protected void drawBackground() {
            this.this$0.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
            this.this$0
                .drawCenteredString(
                    this.mc.fontRendererObj,
                    (
                            ViaLoadingBase.PROTOCOLS.indexOf(ViaLoadingBase.getInstance().getTargetVersion()) == var1
                                ? EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD
                                : EnumChatFormatting.GRAY.toString()
                        )
                        + ViaLoadingBase.getProtocols().get(var1).getName(),
                    this.width / 2,
                    var3 + 2,
                    -1
                );
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            this.this$0
                .drawCenteredString(
                    this.mc.fontRendererObj, "PVN: " + ViaLoadingBase.getProtocols().get(var1).getVersion(), this.width, (var3 + 2) * 2 + 20, -1
                );
            GlStateManager.popMatrix();
        }
    }
}
