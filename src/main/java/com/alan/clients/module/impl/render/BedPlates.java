package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.player.Breaker;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.ui.click.standard.UIColors;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.module.impl.render.bedplates.BedPlateEntry;
import com.alan.clients.module.impl.render.bedplates.BedPosition;
import com.alan.clients.module.impl.render.bedplates.BedPlateInfo;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed.EnumPartType;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

@ModuleInfo(aliases = {"module.render.bedplates.name", "Bed Plates"}, description = "module.render.bedplates.description", category = Category.RENDER)
public class BedPlates extends Module {
    private final BooleanValue whiteListOwnBed = new BooleanValue("Whitelist Own Bed", this, true);
    private final BooleanValue overlaysBloomBlur = new BooleanValue("Overlays (Bloom/Blur)", this, true);
    private final BooleanValue minimal = new BooleanValue("Minimal", this, false);
    private final BooleanValue showGradient = new BooleanValue("Show Gradient", this, false);
    private final BooleanValue showDistance = new BooleanValue("Show Distance", this, true);
    private final BooleanValue distanceScale = new BooleanValue("Distance Scale", this, true);
    private final NumberValue range = new NumberValue("Range", this, 200, 20, 200, 10);
    private final NumberValue refreshTicks = new NumberValue("Refresh Ticks", this, 1, 1, 20, 1);
    private final Font nameFont = FontManager.MAIN.a(15, FontWeight.MEDIUM);
    private final Font distanceLabelFont = FontManager.MAIN.a(11, FontWeight.BOLD);
    private final Font distanceFont = FontManager.MAIN.a(11, FontWeight.REGULAR);
    private final List<BedPlateEntry> entries = new ArrayList<>();
    private final List<BedPosition> bedPositions = new CopyOnWriteArrayList<>();
    private int refreshCounter = 0;
    private int lastTick = -1;
    private int blockCheckTicks = 0;
    private double gradientProgress = 0.5;
    private boolean hasCachedView;
    private double lastRenderPosX;
    private double lastRenderPosY;
    private double lastRenderPosZ;
    private int lastScaleFactor;
    private int lastDisplayHeight;
    private float lastYaw;
    private float lastPitch;
    private volatile boolean scanning = false;
    private long lastScanTime = 0L;
    private static final long SCAN_INTERVAL = 5000L;
    private static final EnumFacing[] SURROUNDING_FACES = new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        if (!this.isInWorld()) {
            this.entries.clear();
            this.bedPositions.clear();
            this.scanning = false;
        } else {
            int i = aEg.thePlayer.ticksExisted;
            if (i != this.lastTick) {
                this.lastTick = i;
                this.refreshCounter++;
                this.tryStartScan();
                if (this.refreshCounter >= this.refreshTicks.wo().intValue() || this.entries.isEmpty()) {
                    this.refreshCounter = 0;
                    this.updateEntries();
                }
            }

            this.renderPlates();
        }
    };

    public BedPlates() {
    }

    @Override
    public void onDisable() {
        this.entries.clear();
        this.bedPositions.clear();
        this.refreshCounter = 0;
        this.lastTick = -1;
        this.blockCheckTicks = 0;
        this.hasCachedView = false;
        this.scanning = false;
        this.lastScanTime = 0L;
    }

    @Override
    public void onEnable() {
        this.entries.clear();
        this.bedPositions.clear();
        this.refreshCounter = 0;
        this.lastTick = -1;
        this.blockCheckTicks = 0;
        this.hasCachedView = false;
        this.scanning = false;
        this.lastScanTime = 0L;
    }

    private boolean isInWorld() {
        return aEg.theWorld != null && aEg.thePlayer != null;
    }

    private void tryStartScan() {
        if (this.isInWorld()) {
            long now = System.currentTimeMillis();
            if (!this.scanning) {
                if (now - this.lastScanTime >= 5000L) {
                    this.lastScanTime = now;
                    this.startBedScan();
                }
            }
        }
    }

    private void startBedScan() {
        if (this.isInWorld()) {
            synchronized (this) {
                if (this.scanning) {
                    return;
                }

                this.scanning = true;
            }

            WorldClient worldclient = aEg.theWorld;
            if (worldclient == null) {
                this.scanning = false;
            } else {
                BlockPos blockpos = new BlockPos(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
                int i = this.range.wo().intValue();
                int j = Math.min(100, Math.max(4, i / 2));
                double range = this.range.wo().doubleValue();
                double d1 = range * range;
                double d2 = aEg.thePlayer.posX;
                double d3 = aEg.thePlayer.posY;
                double d4 = aEg.thePlayer.posZ;
                double d5 = blockpos.getX() + 0.5 - d2;
                double d6 = blockpos.getZ() + 0.5 - d4;
                boolean flag = this.whiteListOwnBed.wo();
                new Thread(() -> {
                    try {
                        ArrayList arraylist = new ArrayList();
                        MutableBlockPos mutableblockpos = new MutableBlockPos();

                        for (int k = -i; k <= i; k++) {
                            int l = blockpos.getX() + k;
                            double d7 = k + d5;
                            double d8 = d7 * d7;

                            for (int i1 = -i; i1 <= i; i1++) {
                                double d9 = i1 + d6;
                                double d10 = d8 + d9 * d9;
                                if (!(d10 > d1)) {
                                    int j1 = blockpos.getZ() + i1;

                                    for (int k1 = -j; k1 <= j; k1++) {
                                        int l1 = blockpos.getY() + k1;
                                        if (l1 >= 0 && l1 <= 255) {
                                            mutableblockpos.set(l, l1, j1);
                                            IBlockState iblockstate = worldclient.getBlockState(mutableblockpos);
                                            if (iblockstate.getBlock() instanceof BlockBed && iblockstate.getValue(BlockBed.PART) == EnumPartType.HEAD) {
                                                double d11 = l1 + 0.5 - d3;
                                                if (!(d10 + d11 * d11 > d1)) {
                                                    BlockPos blockpos1 = new BlockPos(l, l1, j1);
                                                    EnumFacing enumfacing = iblockstate.getValue(BlockBed.FACING);
                                                    if ((!flag || !this.a(blockpos1, enumfacing)) && !a(arraylist, blockpos1)) {
                                                        arraylist.add(new BedPosition(blockpos1));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        this.bedPositions.clear();
                        this.bedPositions.addAll(arraylist);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    } finally {
                        this.scanning = false;
                    }
                }, "BedPlates-AsyncBedScan").start();
            }
        }
    }

    private void updateEntries() {
        this.entries.clear();
        if (this.isInWorld() && !this.bedPositions.isEmpty()) {
            double range = this.range.wo().doubleValue();
            double d1 = range * range;
            double d2 = aEg.thePlayer.posX;
            double d3 = aEg.thePlayer.posY;
            double d4 = aEg.thePlayer.posZ;
            WorldClient worldclient = aEg.theWorld;
            Iterator iterator = this.bedPositions.iterator();

            while (iterator.hasNext()) {
                BlockPos blockpos = ((BedPosition)iterator.next()).getPosition();
                IBlockState iblockstate = worldclient.getBlockState(blockpos);
                if (iblockstate.getBlock() instanceof BlockBed && iblockstate.getValue(BlockBed.PART) == EnumPartType.HEAD) {
                    EnumFacing enumfacing = iblockstate.getValue(BlockBed.FACING);
                    if (!this.whiteListOwnBed.wo() || !this.a(blockpos, enumfacing)) {
                        BlockPos blockpos1 = blockpos.offset(enumfacing.getOpposite());
                        double d5 = (blockpos.getX() + blockpos1.getX() + 1.0) / 2.0;
                        double d6 = blockpos.getY() + 0.5;
                        double d7 = (blockpos.getZ() + blockpos1.getZ() + 1.0) / 2.0;
                        double d8 = d5 - d2;
                        double d9 = d6 - d3;
                        double d10 = d7 - d4;
                        double d11 = d8 * d8 + d9 * d9 + d10 * d10;
                        if (d11 <= d1) {
                            BedPlateInfo wk = this.a(blockpos, enumfacing, d5, d6, d7);
                            if (wk != null) {
                                Vector3d aka = new Vector3d(d5, d6, d7);
                                this.entries.add(new BedPlateEntry(aka, d11, wk));
                            }
                        }
                    }
                }
            }

            this.entries.sort(Comparator.comparingDouble(BedPlateEntry::getDistanceSquared));
        }
    }

    private boolean a(BlockPos pos, EnumFacing facing) {
        Vector3d akaxx = Breaker.home;
        if (akaxx == null) {
            return false;
        }

        Vector3d akax = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        BlockPos blockpos = pos.offset(facing.getOpposite());
        Vector3d akaxx2 = new Vector3d(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);
        double d0 = 4.5;
        return akax.g(akaxx2) <= d0 || akaxx2.g(akaxx2) <= d0;
    }

    private BedPlateInfo a(BlockPos pos, EnumFacing facing, double var3, double var5, double var7) {
        ItemStack itemstack = null;
        double d0 = Double.MAX_VALUE;
        double d1 = 0.0;
        MapColor mapcolor = null;
        Vector3d aka = null;
        int i = 0;
        BlockPos blockpos = pos.offset(facing.getOpposite());
        BlockPos[] ablockpos = new BlockPos[]{pos, blockpos};
        double d2 = aEg.thePlayer.posX;
        double d3 = aEg.thePlayer.posY;
        double d4 = aEg.thePlayer.posZ;
        WorldClient worldclient = aEg.theWorld;

        for (BlockPos blockpos1 : ablockpos) {
            for (EnumFacing enumfacing : SURROUNDING_FACES) {
                BlockPos blockpos2 = blockpos1.offset(enumfacing);
                IBlockState iblockstate = worldclient.getBlockState(blockpos2);
                Block block = iblockstate.getBlock();
                if (!(block instanceof BlockBed) && !(block instanceof BlockAir) && !(block instanceof BlockLiquid) && block.getMaterial().isSolid()) {
                    double d5 = block.getBlockHardness(worldclient, blockpos2);
                    if (!(d5 < 0.0)) {
                        i++;
                        if (d5 < d0) {
                            Item item = block.getItem(worldclient, blockpos2);
                            if (item != null) {
                                ItemStack itemstack1 = new ItemStack(item, 1, block.getDamageValue(worldclient, blockpos2));
                                if (itemstack1.getItem() != null) {
                                    double d6 = blockpos2.getX() + 0.5;
                                    double d7 = blockpos2.getY() + 0.5;
                                    double d8 = blockpos2.getZ() + 0.5;
                                    double d9 = d6 - d2;
                                    double d10 = d7 - d3;
                                    double d11 = d8 - d4;
                                    itemstack = itemstack1;
                                    d1 = d9 * d9 + d10 * d10 + d11 * d11;
                                    mapcolor = block.getMapColor(iblockstate);
                                    aka = new Vector3d(d6, d7, d8);
                                    d0 = d5;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (i == 0) {
            ItemStack itemstack2 = new ItemStack(Items.bed, 1, 0);
            double d12 = var3 - d2;
            double d13 = var5 - d3;
            double d14 = var7 - d4;
            double d15 = d12 * d12 + d13 * d13 + d14 * d14;
            return new BedPlateInfo(itemstack2, d15, MapColor.airColor, new Vector3d(var3, var5, var7), false, true);
        } else if (itemstack != null) {
            boolean flag = i < 8;
            return new BedPlateInfo(itemstack, d1, mapcolor, aka, flag, false);
        }
        return null;
    }

    private void renderPlates() {
        if (!this.entries.isEmpty()) {
            if (aEg.currentScreen == null) {
                this.gradientProgress = Math.sin(System.currentTimeMillis() / 600.0) * 0.5 + 0.5;
                this.blockCheckTicks++;
                boolean flag = this.blockCheckTicks >= 20;
                if (flag) {
                    this.blockCheckTicks = 0;
                }

                int i = aEg.jY.getScaleFactor();
                int j = Display.getHeight();
                double d0 = RenderManager.bUO;
                double d1 = RenderManager.bUP;
                double d2 = RenderManager.bUQ;
                float f = aEg.thePlayer.pl;
                float rotationPitch = aEg.thePlayer.rotationPitch;
                boolean flag1 = !this.hasCachedView
                    || d0 != this.lastRenderPosX
                    || d1 != this.lastRenderPosY
                    || d2 != this.lastRenderPosZ
                    || i != this.lastScaleFactor
                    || j != this.lastDisplayHeight
                    || f != this.lastYaw
                    || rotationPitch != this.lastPitch;
                if (flag1) {
                    this.hasCachedView = true;
                    this.lastRenderPosX = d0;
                    this.lastRenderPosY = d1;
                    this.lastRenderPosZ = d2;
                    this.lastScaleFactor = i;
                    this.lastDisplayHeight = j;
                    this.lastYaw = f;
                    this.lastPitch = rotationPitch;
                }

                for (BedPlateEntry wi : this.entries) {
                    if (flag) {
                        Vector3d aka = wi.getPosition();
                        BlockPos blockpos = new BlockPos(aka.getX(), aka.getY(), aka.getZ());
                        if (!(aEg.theWorld.getBlockState(blockpos).getBlock() instanceof BlockBed)) {
                            wi.v(false);
                            continue;
                        }

                        wi.v(true);
                    }

                    if (wi.isVisible()) {
                        double[] adouble = wi.getProjectedPosition();
                        wi.incrementCacheAge();
                        if (adouble == null || flag1 || wi.getCacheAge() > 60) {
                            adouble = this.a(wi.getPosition(), d0, d1, d2, i, j);
                            if (adouble == null) {
                                continue;
                            }

                            wi.a(adouble);
                        }

                        if (!(adouble[2] < 0.0) && !(adouble[2] > 1.0)) {
                            this.a(wi.getInfo(), adouble[0], adouble[1], 0);
                        }
                    }
                }
            }
        }
    }

    private void a(BedPlateInfo var1, double var2, double var4, int var6) {
        ItemStack itemstack = var1.getStack();
        if (itemstack != null && itemstack.getItem() != null) {
            boolean flag = this.minimal.wo();
            if (flag && (var1.isIncomplete() || var1.isNotProtected())) {
                itemstack = new ItemStack(Items.bed, 1, 0);
            }

            double d0 = var1.getDistance();
            double d1 = 1.0;
            if (this.distanceScale.wo() && d0 > 10.0) {
                d1 = Math.max(0.5, 1.0 - (d0 - 10.0) / 80.0);
            }

            boolean showDistance = this.showDistance.wo();
            double d2 = 4.0 * d1;
            double d3 = 16.0 * d1;
            double d4 = 1.05 * d1;
            double d5 = 8.0 * d1;
            double d7;
            double d8;
            if (flag) {
                double d6 = showDistance ? this.distanceFont.getStringWidth(var1.getDistanceText()) * d1 : 0.0;
                d7 = Math.max(d3 + d2 * 2.0, d6 + d2 * 4.0);
                d8 = d3 + d2 * 1.0;
            } else {
                double d20 = this.nameFont.height() * d1;
                double d21 = Math.max(d3, d20);
                double d22 = this.nameFont.getStringWidth(var1.getDisplayName()) * d1;
                double d23 = showDistance ? (this.distanceLabelFont.getStringWidth("distance: ") + this.distanceFont.getStringWidth(var1.getDistanceText())) * d1 : 0.0;
                double d24 = d3 + d2 * 3.0 + d22;
                d7 = Math.max(d24, d23 + d2 * 4.0);
                d8 = d21 + d2 * 1.0;
            }

            double d9 = var2 - d7 / 2.0;
            double d10 = var4 - d8 - d5 - 8.0 - var6 * (d8 + d5 + 6.0);
            int i = this.getRound();
            Color color = Themes.rK();
            Color color1 = this.a(color, var1.getMapColor());
            Color color2 = this.rz().rA();
            Color color3 = new Color(
                Math.min(255, color1.getRed() + color2.getRed() / 26),
                Math.min(255, color1.getGreen() + color2.getGreen() / 26),
                Math.min(255, color1.getBlue() + color2.getBlue() / 26),
                180
            );
            Color color4 = this.rz().rA();
            Color color5 = this.rz().rB();
            Color color6 = ColorUtil.a(color4, color5, this.gradientProgress);
            Color color7 = ColorUtil.a(color5, color4, this.gradientProgress);
            ItemStack itemstack1 = itemstack;
            double d11 = d9;
            double d12 = d10;
            double d13 = d5;
            double d14 = d7;
            double d15 = d8;
            double d16 = d2;
            double d17 = d3;
            double d18 = d4;
            double d19 = d1;
            Color color8 = ColorUtil.withAlpha(UIColors.SECONDARY.pV(), 105);
            Color color9 = color3;
            Color color10 = color6;
            Color color11 = color7;
            String s = var1.getDisplayName();
            String s1 = var1.getDistanceText();
            boolean flag2 = showDistance;
            boolean flag3 = flag;
            if (this.overlaysBloomBlur.wo()) {
                this.b(ShaderQueueType.BLUR).c(() -> {
                    RenderUtil.a(d11, d12, d14, d13, i, UIColors.SECONDARY.pV(), true, true, false, false);
                    RenderUtil.a(d11, d12 + d13, d14, d15, i, UIColors.BACKGROUND.pV(), false, false, true, true);
                });
                this.b(ShaderQueueType.BLOOM).c(() -> {
                    RenderUtil.roundedRectangle(d11, d12, d14, d15 + d13, i + 1, this.rz().rE());
                    if (flag2 && !flag3) {
                        String s2 = "distance: ";
                        double d25 = this.distanceLabelFont.getStringWidth(s2) * d19;
                        double d26 = this.distanceFont.getStringWidth(s1) * d19;
                        double d27 = d25 + d26;
                        double d28 = d11 + (d14 - d27) / 2.0;
                        double d29 = d12 + d13 / 2.0 - 1.2 * d19;
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(d28, d29, 0.0);
                        GlStateManager.scale(d19, d19, d19);
                        this.distanceLabelFont.a(s2, 0.0, 0.0, ColorUtil.withAlpha(this.rz().rA(), 240).getRGB());
                        GlStateManager.popMatrix();
                    }
                });
            }

            this.b(ShaderQueueType.REGULAR).c(() -> {
                int j = Math.max(2, i - 1);
                RenderUtil.a(d11, d12, d14, d13, j, color8, true, true, false, false);
                RenderUtil.a(d11, d12 + d13, d14, d15, j, color9, false, false, true, true);
                if (this.showGradient.wo()) {
                    double d25 = 2.5 * d19;
                    RenderUtil.horizontalCenteredGradient(d11, d12, d14, d25, color10, color11);
                }

                if (flag2) {
                    if (flag3) {
                        double d26 = this.distanceFont.getStringWidth(s1) * d19;
                        double d27 = d11 + (d14 - d26) / 2.0;
                        double d28 = d12 + d13 / 2.0 - 1.2 * d19;
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(d27, d28, 0.0);
                        GlStateManager.scale(d19, d19, d19);
                        this.distanceFont.a(s1, 0.0, 0.0, UIColors.TEXT.pW());
                        GlStateManager.popMatrix();
                    } else {
                        String s2 = "distance: ";
                        double d33 = this.distanceLabelFont.getStringWidth(s2) * d19;
                        double d34 = this.distanceFont.getStringWidth(s1) * d19;
                        double d35 = d33 + d34;
                        double d36 = d11 + (d14 - d35) / 2.0;
                        double d37 = d12 + d13 / 2.0 - 1.2 * d19;
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(d36, d37, 0.0);
                        GlStateManager.scale(d19, d19, d19);
                        this.distanceLabelFont.a(s2, 0.0, 0.0, this.rz().rD().getRGB());
                        this.distanceFont.a(s1, (float)(d33 / d19), 0.0, UIColors.TEXT.pW());
                        GlStateManager.popMatrix();
                    }
                }

                double d29 = d12 + d13;
                if (flag3) {
                    double d30 = 16.0 * d18;
                    double d31 = d11 + (d14 - d30) / 2.0;
                    double d32 = d29 + (d15 - d30) / 2.0;
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.translate(d31, d32, 0.0);
                    GlStateManager.scale(d18, d18, d18);
                    RenderHelper.enableGUIStandardItemLighting();
                    aEg.getRenderItem().b(itemstack1, 0.0, 0.0);
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.disableLighting();
                    GlStateManager.popMatrix();
                } else {
                    double d38 = 16.0 * d18;
                    double d39 = d11 + d16;
                    double d40 = d29 + (d15 - d38) / 2.0;
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.translate(d39, d40, 0.0);
                    GlStateManager.scale(d18, d18, d18);
                    RenderHelper.enableGUIStandardItemLighting();
                    aEg.getRenderItem().b(itemstack1, 0.0, 0.0);
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.disableLighting();
                    GlStateManager.popMatrix();
                    double d41 = d11 + d16 * 2.0 + d17;
                    double d42 = d29 + d15 / 2.0 - (this.nameFont.height() / 2.0 - 1.5) * d19;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(d41, d42, 0.0);
                    GlStateManager.scale(d19, d19, d19);
                    this.nameFont.a(s, 0.0, 0.0, -2038554);
                    GlStateManager.popMatrix();
                }
            });
        }
    }

    private double[] project(Vector3d var1) {
        return this.projectPosition(var1.getX(), var1.getY(), var1.getZ());
    }

    private double[] projectPosition(double var1, double var3, double var5) {
        int i = aEg.jY.getScaleFactor();
        double d0 = RenderManager.bUO;
        double d1 = RenderManager.bUP;
        double d2 = RenderManager.bUQ;
        int j = Display.getHeight();
        return this.a(var1, var3, var5, d0, d1, d2, i, j);
    }

    private double[] a(Vector3d var1, double var2, double var4, double var6, int var8, int var9) {
        return this.a(var1.getX(), var1.getY(), var1.getZ(), var2, var4, var6, var8, var9);
    }

    private double[] a(double var1, double var3, double var5, double var7, double var9, double var11, int var13, int var14) {
        double d0 = var1 - var7;
        double d1 = var3 - var9;
        double d2 = var5 - var11;
        if (GLU.gluProject(
            (float)d0, (float)d1, (float)d2, ActiveRenderInfo.MODELVIEW, ActiveRenderInfo.PROJECTION, ActiveRenderInfo.VIEWPORT, ActiveRenderInfo.OBJECTCOORDS
        )) {
            float f = ActiveRenderInfo.OBJECTCOORDS.get(0);
            float f1 = ActiveRenderInfo.OBJECTCOORDS.get(1);
            float f2 = ActiveRenderInfo.OBJECTCOORDS.get(2);
            double d3 = f / var13;
            double d4 = (var14 - f1) / var13;
            return new double[]{d3, d4, f2};
        } else {
            return null;
        }
    }

    private int getRound() {
        return this.rz().getRound();
    }

    private Color a(Color var1, MapColor mapColor) {
        if (mapColor == null) {
            return var1;
        }

        Color color = new Color(mapColor.colorValue | 0xFF000000, true);
        Color color1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), var1.getAlpha());
        return a(var1, color1, 0.35F);
    }

    private static Color a(Color color, Color var1, float var2) {
        float f = Math.max(0.0F, Math.min(1.0F, var2));
        int i = (int)(color.getRed() + (var1.getRed() - color.getRed()) * f);
        int j = (int)(color.getGreen() + (var1.getGreen() - color.getGreen()) * f);
        int k = (int)(color.getBlue() + (var1.getBlue() - color.getBlue()) * f);
        int l = (int)(color.getAlpha() + (var1.getAlpha() - color.getAlpha()) * f);
        return new Color(i, j, k, l);
    }

    private static boolean a(List<BedPosition> var0, BlockPos pos) {
        Iterator iterator = var0.iterator();

        while (iterator.hasNext()) {
            if (((BedPosition)iterator.next()).getPosition().equals(pos)) {
                return true;
            }
        }

        return false;
    }
}
