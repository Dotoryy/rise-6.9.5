package com.alan.clients.module.impl.combat;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.other.GameEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.component.impl.combat.TargetComponent;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@ModuleInfo(aliases = {"module.combat.keeprange.name", "S Tap"}, description = "module.combat.keeprange.description", category = Category.COMBAT)
public final class KeepRange extends Module {
    private final NumberValue range = new NumberValue("Range", this, 3, 0, 6, 0.1);
    private final BooleanValue disableNearEdge = new BooleanValue("Disable Near Edge", this, true);
    private final NumberValue edgeRange = new NumberValue("Edge Range", this, 5, 0, 6, 1, () -> !this.disableNearEdge.wo());
    private final ModeValue mode = new ModeValue("Mode", this).add(new SubMode("BackWards")).add(new SubMode("Stop")).setDefault("Stop");
    private final NumberValue comboToStart = new NumberValue("Combo To Start", this, 2, 0, 6, 1);
    private boolean nearEdge;
    private int comboTicks;
    @EventLink
    public final Listener<GameEvent> onGame = var1 -> {
        if (aEg.thePlayer.onGround) {
            this.nearEdge = false;
            int i = this.edgeRange.wo().intValue();

            for (int j = -i; j <= i; j++) {
                for (int k = -i; k <= i; k++) {
                    for (int l = -5; l <= 0 && PlayerUtil.p(j, l, k) instanceof BlockAir; l++) {
                        if (l == 0) {
                            this.nearEdge = true;
                            return;
                        }
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1 -> {
        EntityLivingBase entitylivingbase = TargetComponent.e(10.0);
        double d0 = this.range.wo().doubleValue();
        if (aEg.thePlayer.aY <= 7) {
            d0 -= 0.2;
        }

        if (entitylivingbase != null && (!this.nearEdge || !this.disableNearEdge.wo())) {
            if (entitylivingbase.hurtTime > 0) {
                this.comboTicks++;
            }

            if (aEg.thePlayer.hurtTime > 0) {
                this.comboTicks = 0;
            }

            if (this.comboTicks > this.comboToStart.wo().intValue() * 8 || this.comboToStart.wo().intValue() <= 0) {
                if (PlayerUtil.v(entitylivingbase) < d0 - 0.05) {
                    float f = var1.getForward();
                    float f1 = var1.getStrafe();
                    double d1 = MathHelper.wrapAngleTo180_double(RotationUtil.y(entitylivingbase).getX() - 180.0F);
                    if (f == 0.0F && f1 == 0.0F) {
                        return;
                    }

                    float f2 = 0.0F;
                    float f3 = 0.0F;
                    float f4 = Float.MAX_VALUE;

                    for (float f5 = -1.0F; f5 <= 1.0F; f5++) {
                        for (float f6 = -1.0F; f6 <= 1.0F; f6++) {
                            if (f6 != 0.0F || f5 != 0.0F) {
                                double d2 = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction(aEg.thePlayer.pl, f5, f6)));
                                double d3 = MathUtil.n(d1, d2);
                                if (d3 < f4) {
                                    f4 = (float)d3;
                                    f2 = f5;
                                    f3 = f6;
                                }
                            }
                        }
                    }

                    {
                        String s = this.mode.wo().getName();
                        switch (s) {
                            case "Stop":
                                if (f2 == f * -1.0F) {
                                    var1.setForward(0.0F);
                                }

                                if (f3 == f1 * -1.0F) {
                                    var1.setStrafe(0.0F);
                                }

                                return;
                            case "BackWards":
                                break;
                            default:
                                return;
                        }
                    }

                    var1.setForward(f2);
                    var1.setStrafe(f3);
                }
            }
        } else {
            this.comboTicks = 0;
        }
    };

    public KeepRange() {
    }
}
