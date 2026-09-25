package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(aliases = "module.movement.clipper.name", description = "module.movement.clipper.description", category = Category.MOVEMENT)
public class Clipper extends Module {
    private final NumberValue clipHeight = new NumberValue("Clip Height", this, 5, 1, 50, 1);
    private final Set<EnumFacing> clippableFacings = new HashSet<>();
    private int cooldown = 0;
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        boolean flag = this.clippableFacings.contains(EnumFacing.UP);
        boolean flag1 = this.clippableFacings.contains(EnumFacing.DOWN);
        if (flag || flag1) {
            float f = aEg.jY.getScaledWidth() / 2.0F;
            float f1 = aEg.jY.getScaledHeight() / 2.0F;
            int i = this.rz().rA().getRGB();
            String s;
            String s1;
            if (flag && flag1) {
                s = "^ v";
                s1 = "SPACE & SHIFT";
            } else if (flag) {
                s = "^";
                s1 = "SPACE";
            } else {
                s = "v";
                s1 = "SHIFT";
            }

            int j = new Color(0, 0, 0, 200).getRGB();
            if (aEg.thePlayer.cqL > 2) {
                FontManager.MAIN.a(17, FontWeight.LIGHT).drawString(s, f + 1.0F, f1 - 10.0F + 1.0F, j);
                FontManager.MAIN.a(17, FontWeight.LIGHT).drawString(s1, f + 1.0F, f1 + 10.0F + 1.0F, j);
                FontManager.MAIN.a(17, FontWeight.LIGHT).drawString(s, f, f1 - 10.0F, i);
                FontManager.MAIN.a(17, FontWeight.LIGHT).drawString(s1, f, f1 + 10.0F, i);
            }
        }
    };
    @EventLink
    public final Listener<JumpEvent> onJump = var1 -> {
        if (this.clippableFacings.contains(EnumFacing.UP) && aEg.thePlayer.tR > 2 && aEg.gameSettings.keyBindJump.isPressed()) {
            var1.setCancelled();
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1 -> {
        if (this.cooldown > 0) {
            this.cooldown--;
        } else {
            this.clippableFacings.clear();
            if (!this.e(Flight.class).isEnabled()) {
                int i = this.clipHeight.wo().intValue();

                for (EnumFacing enumfacing : new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN}) {
                    this.findClip(enumfacing, i, var2 -> this.clippableFacings.add(enumfacing));
                }

                EnumFacing enumfacing1;
                if (aEg.gameSettings.keyBindSneak.isPressed() && aEg.thePlayer.cqL > 2) {
                    enumfacing1 = EnumFacing.DOWN;
                } else {
                    if (!aEg.gameSettings.keyBindJump.isPressed() || aEg.thePlayer.cqL <= 2) {
                        Object object = null;
                        return;
                    }

                    enumfacing1 = EnumFacing.UP;
                }

                if (this.clippableFacings.contains(enumfacing1)) {
                    this.findClip(enumfacing1, i, var2 -> {
                        aEg.thePlayer.setPositionAndUpdate(aEg.thePlayer.posX, var2.getY(), aEg.thePlayer.posZ);
                        ChatUtil.b("Clipped " + (enumfacing1 == EnumFacing.UP ? "up" : "down"));
                        this.cooldown = 5;
                    });
                }
            }
        }
    };

    public Clipper() {
    }

    private void findClip(EnumFacing facing, int var2, Consumer<BlockPos> consumer) {
        if (var2 > 0) {
            boolean flag = false;
            BlockPos blockpos = aEg.thePlayer.getPosition();

            for (int i = 0; i < var2; i++) {
                blockpos = blockpos.offset(facing);
                if (facing == EnumFacing.DOWN && this.isOverVoid(blockpos, 50)) {
                    return;
                }

                if (flag && this.canFit(blockpos)) {
                    consumer.accept(blockpos);
                    return;
                }

                if (!this.isPassable(blockpos)) {
                    flag = true;
                }
            }
        }
    }

    private boolean isOverVoid(BlockPos pos, int var2) {
        int i = 0;

        for (int j = 1; j <= var2; j++) {
            BlockPos blockpos = pos.down(j);
            if (!aEg.theWorld.isAirBlock(blockpos)) {
                break;
            }

            i++;
        }

        return i >= var2;
    }

    private boolean canFit(BlockPos pos) {
        double d0 = aEg.thePlayer.posX;
        double d1 = aEg.thePlayer.posZ;
        double d2 = pos.getY();
        AxisAlignedBB axisalignedbb = aEg.thePlayer.getEntityBoundingBox().offset(d0 - aEg.thePlayer.posX, d2 - aEg.thePlayer.posY, d1 - aEg.thePlayer.posZ);
        return aEg.theWorld.getCollidingBoundingBoxes(aEg.thePlayer, axisalignedbb).isEmpty();
    }

    private boolean isPassable(BlockPos pos) {
        return aEg.theWorld.getBlockState(pos).getBlock().isPassable(aEg.theWorld, pos);
    }
}
