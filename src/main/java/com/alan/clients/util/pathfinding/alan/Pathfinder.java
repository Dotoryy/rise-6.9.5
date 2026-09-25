package com.alan.clients.util.pathfinding.alan;

import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.pathfinding.alan.PathGrid;
import com.alan.clients.util.pathfinding.alan.api.Path;
import com.alan.clients.util.pathfinding.alan.api.Point;
import com.alan.clients.util.pathfinding.alan.PathValidation;
import com.alan.clients.util.ThreadUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Executors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3i;
import org.lwjgl.opengl.GL11;
import rip.vantage.commons.util.time.StopWatch;

public class Pathfinder extends ArrayList<Path> implements InstanceAccess {
    public static int aNu = 100;
    public Point aNv = new Point((int)aEg.thePlayer.posX, (int)aEg.thePlayer.posY, (int)aEg.thePlayer.posZ);
    public Point aNw;
    private final PathGrid aNx = new PathGrid(100, this.aNv);
    private final PathGrid aNy = new PathGrid(100, this.aNv);
    public Path aNz;
    public Vec3i aNA;
    public int aNB = Integer.MAX_VALUE;
    private final boolean aNC;

    public Pathfinder(Point var1, boolean var2) {
        this.aNw = var1;
        this.aNC = var2;
        this.aNA = new Vec3i(Math.abs(this.aNv.getX() - var1.getX()), Math.abs(this.aNv.getY() - var1.getY()), Math.abs(this.aNv.getZ() - var1.getZ()));
    }

    public Pathfinder(Point var1) {
        this.aNw = var1;
        this.aNC = false;
        this.aNA = new Vec3i(Math.abs(this.aNv.getX() - var1.getX()), Math.abs(this.aNv.getY() - var1.getY()), Math.abs(this.aNv.getZ() - var1.getZ()));
    }

    public void run() {
        Executors.newFixedThreadPool(1).execute(() -> {
            StopWatch a = new StopWatch();

            while (this.aNz == null && !a.T(this.aNC ? 15000L : 50L)) {
                this.uN();
                if (this.size() == 0) {
                    break;
                }

                if (this.aNC) {
                    ThreadUtil.k(200L);
                }
            }

            if (this.aNz == null) {
                ChatUtil.b("Failed to find path.");
            } else {
                ChatUtil.b("Took " + a.getElapsedTime() + " ms.");
            }
        });
    }

    private void uN() {
        try {
            if (this.isEmpty()) {
                Path ahrxxxx = new Path();
                ahrxxxx.add(this.aNv);
                this.add(ahrxxxx);
            } else {
                ArrayList arraylist = new ArrayList();
                ArrayList arraylist1 = new ArrayList();
                PathValidation[] aaht = new PathValidation[]{PathValidation.COLLISIONS, PathValidation.LEGIT};

                for (Path ahrxxx : this) {
                    byte b0 = 1;

                    for (int i = -1; i <= b0; i += b0 * 2) {
                        Path ahrx = this.a(ahrxxx, i, 0, 0);
                        if (ahrx != null && PathValidation.a(ahrx, this, aaht)) {
                            arraylist.add(ahrx);
                        }
                    }

                    for (int j = -b0; j <= b0; j += b0 * 2) {
                        Path ahrx = this.a(ahrxxx, 0, j, 0);
                        if (ahrx != null && PathValidation.a(ahrx, this, aaht)) {
                            arraylist.add(ahrx);
                        }
                    }

                    for (int k = -b0; k <= b0; k += b0 * 2) {
                        Path ahrx = this.a(ahrxxx, 0, 0, k);
                        if (ahrx != null && PathValidation.a(ahrx, this, aaht)) {
                            arraylist.add(ahrx);
                        }
                    }

                    arraylist1.add(ahrxxx);
                }

                this.removeAll(arraylist1);
                this.addAll(arraylist);
                arraylist.forEach(var1 -> {
                    if (((Path)var1).get(((Path)var1).size() - 1).equals(this.aNw)) {
                        this.aNz = (Path)var1;
                    }
                });
                this.sort(Comparator.comparingDouble(var1 -> (int)var1.get(var1.size() - 1).j(this.aNw)));
                int l = (int)this.get(0).get(this.get(0).size() - 1).j(this.aNw);
                this.removeRange(Math.max(1, Math.min(25, this.size() - 1)), this.size());
                if (l < this.aNB) {
                    this.aNB = l;
                }
            }
        } catch (Exception exception) {
            ChatUtil.c("Exception in Console");
            exception.printStackTrace();
            this.clear();
        }
    }

    private Path a(Path var1, int var2, int var3, int var4) {
        Path ahr = new Path(var1);
        Point ahsx = ahr.get(ahr.size() - 1);
        Point ahsx22 = new Point(ahsx.add(var2, var3, var4));
        if (this.aNx.b(ahsx22)) {
            return null;
        }

        ahr.add(ahsx22);
        this.aNx.a(ahsx22);
        return ahr;
    }

    public void cj() {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glDepthMask(false);
        RenderUtil.color(ColorUtil.withAlpha(this.rz().rA(), 100));
        double d0 = 0.6;

        for (Path ahr : new ArrayList<>(this)) {
            if (ahr != null) {
                for (Point ahsxx : ahr) {
                    RenderUtil.drawBoundingBox(
                        new AxisAlignedBB(
                            ahsxx.getX() + (1.0 - d0),
                            ahsxx.getY() + (1.0 - d0),
                            ahsxx.getZ() + (1.0 - d0),
                            ahsxx.getX() + d0,
                            ahsxx.getY() + d0,
                            ahsxx.getZ() + d0
                        )
                    );
                }
            }
        }

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GL11.glDepthMask(true);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
        if (this.aNz != null) {
            Point ahsx = null;

            for (Point ahsx2 : this.aNz) {
                if (ahsx != null) {
                    RenderUtil.drawLine(
                        ahsx.getX() + 0.5, ahsx.getY() + 0.5, ahsx.getZ() + 0.5, ahsx2.getX() + 0.5, ahsx2.getY() + 0.5, ahsx2.getZ() + 0.5, Color.WHITE, 1.0F
                    );
                }

                ahsx = ahsx2;
            }
        }
    }
}
