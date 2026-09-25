package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.render.LookEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.util.rotation.RotationUtil;
import java.util.function.Function;
import lombok.Generated;
import net.minecraft.util.MathHelper;

public final class RotationComponent extends Component {
    private static boolean dj;
    private static boolean fj;
    public static Vector2f fk;
    public static Vector2f fl = new Vector2f(0.0F, 0.0F);
    public static Vector2f fm;
    public static Vector2f fn;
    private static double rotationSpeed;
    private static MovementFix correctMovement;
    private static Function<Vector2f, Boolean> fq;
    private static boolean fr = true;
    private static boolean fs = true;
    private static float ft;
    private static final Vector2f fu = new Vector2f(0.0F, 0.0F);
    @EventLink(value = 0)
    public final Listener<PreUpdateEvent> onPreUpdate = var0 -> {
        if (!dj || fk == null || fl == null || fm == null || fn == null) {
            fk = fl = fm = fn = new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
        }

        if (dj) {
            bJ();
        }

        if (correctMovement == MovementFix.BACKWARDS_SPRINT && dj && Math.abs(fk.x % 360.0F - Math.toDegrees(MoveUtil.direction()) % 360.0) > 45.0) {
            aEg.gameSettings.cgG.setPressed(false);
            aEg.thePlayer.setSprinting(false);
        }
    };
    @EventLink(value = 1)
    public final Listener<MoveInputEvent> onMove = var0 -> {
        if (dj && correctMovement == MovementFix.NORMAL && fk != null) {
            float f = fk.x;
            MoveUtil.fixMovement(var0, f);
        }
    };
    @EventLink(value = 0)
    public final Listener<LookEvent> onLook = var0 -> {
        if (dj && fk != null && fs) {
            var0.setRotation(fk);
        }
    };
    @EventLink(value = 0)
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (dj && (correctMovement == MovementFix.NORMAL || correctMovement == MovementFix.TRADITIONAL) && fk != null) {
            var0.setYaw(fk.x);
        }
    };
    @EventLink(value = 0)
    public final Listener<JumpEvent> onJump = var0 -> {
        if (dj
            && (correctMovement == MovementFix.NORMAL || correctMovement == MovementFix.TRADITIONAL || correctMovement == MovementFix.BACKWARDS_SPRINT)
            && fk != null) {
            var0.setYaw(fk.x);
        }
    };
    @EventLink(value = 0)
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (dj && fk != null) {
            float f = fk.x;
            float f1 = fk.y;
            var1.setYaw(f);
            var1.setPitch(f1);
            aEg.thePlayer.rotationYawHead = f;
            aEg.thePlayer.po = f1;
            if (!fr) {
                aEg.thePlayer.pl = f;
                aEg.thePlayer.rotationPitch = f1;
            }

            fn = new Vector2f(f, f1);
            boolean flag = Math.abs((fk.x - aEg.thePlayer.pl) % 360.0F) < 1.0F && Math.abs(fk.y - aEg.thePlayer.rotationPitch) < 1.0F;
            if (flag) {
                dj = false;
                if (fr) {
                    this.bI();
                }
            }

            fl = fk;
        } else {
            fl = new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
        }

        fm = new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
        fj = false;
    };

    public RotationComponent() {
    }

    public static void setRotations(Vector2f vec2, double var1, MovementFix movementFix) {
        a(vec2, var1, movementFix, null, true);
    }

    public static void a(Vector2f vec2, double var1, MovementFix movementFix, Function<Vector2f, Boolean> function) {
        a(vec2, var1, movementFix, function, true);
    }

    public static void a(Vector2f vec2, double var1, MovementFix movementFix, Function<Vector2f, Boolean> function, boolean var5) {
        a(vec2, var1, movementFix, function, var5, true);
    }

    public static void a(Vector2f vec2, double var1, MovementFix movementFix, Function<Vector2f, Boolean> function, boolean var5, boolean var6) {
        fm = vec2;
        rotationSpeed = var1 * 36.0;
        correctMovement = movementFix;
        fq = function;
        fr = var5;
        fs = var6;
        dj = true;
        bJ();
    }

    public static Vector2f bH() {
        return dj && fl != null ? fl : new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
    }

    private void bI() {
        Vector2f vector2f = RotationUtil.o(RotationUtil.applySensitivityPatch(new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch), fl));
        aEg.thePlayer.pl = vector2f.x;
        aEg.thePlayer.rotationPitch = vector2f.y;
    }

    public static void bJ() {
        if (!fj) {
            float f = fm.x;
            float f1 = fm.y;
            if (fq != null && (Math.abs(f - fk.x) > 5.0F || Math.abs(f1 - fk.y) > 5.0F)) {
                Vector2f vector2f = new Vector2f(fm.getX(), fm.getY());
                double d0 = Math.random() * Math.random() * Math.random() * 20.0;
                ft = ft
                    + (float)(
                        (20.0 + (Math.random() - 0.5) * (Math.random() * Math.random() * Math.random() * 360.0))
                            * (aEg.thePlayer.ticksExisted / 10 % 2 == 0 ? -1 : 1)
                    );
                fu.setX((float)(fu.getX() + -MathHelper.sin((float)Math.toRadians(ft)) * d0));
                fu.setY((float)(fu.getY() + MathHelper.cos((float)Math.toRadians(ft)) * d0));
                float f2 = f + fu.getX();
                float f3 = f1 + fu.getY();
                if (!fq.apply(new Vector2f(f2, f3))) {
                    ft = (float)Math.toDegrees(Math.atan2(vector2f.getX() - f2, f3 - vector2f.getY())) - 180.0F;
                    float f4 = f;
                    float f5 = f1;
                    fu.setX((float)(fu.getX() + -MathHelper.sin((float)Math.toRadians(ft)) * d0));
                    fu.setY((float)(fu.getY() + MathHelper.cos((float)Math.toRadians(ft)) * d0));
                    f2 = f4 + fu.getX();
                    f3 = f5 + fu.getY();
                }

                if (!fq.apply(new Vector2f(f2, f3))) {
                    fu.setX(0.0F);
                    fu.setY(0.0F);
                    f2 = (float)(fm.x + Math.random() * 2.0);
                    f3 = (float)(fm.y + Math.random() * 2.0);
                }

                f = f2;
                f1 = f3;
            }

            fk = RotationUtil.e(new Vector2f(f, f1), rotationSpeed + Math.random());
            if (correctMovement == MovementFix.NORMAL || correctMovement == MovementFix.TRADITIONAL) {
                aEg.thePlayer.pp = fk.x;
            }

            aEg.thePlayer.pq = fk.x;
        }

        fj = true;
        if (fs) {
            aEg.entityRenderer.getMouseOver(1.0F);
        }
    }

    @Generated
    public static void c(boolean var0) {
        dj = var0;
    }

    @Generated
    public static void d(boolean var0) {
        fj = var0;
    }

    @Generated
    public static boolean bd() {
        return dj;
    }

    @Generated
    public static boolean bK() {
        return fj;
    }
}
