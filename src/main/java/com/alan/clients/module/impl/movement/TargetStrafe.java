package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.math.MathInterpolation;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.util.shader.ShaderQueueType;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@ModuleInfo(aliases = "module.movement.targetstrafe.name", description = "module.movement.targetstrafe.description", category = Category.MOVEMENT)
public class TargetStrafe extends Module {
    private final NumberValue range = new NumberValue("Range", this, 1, 0.2, 6, 0.1);
    private final BooleanValue holdJump = new BooleanValue("Hold Jump", this, true);
    private final BooleanValue holdJumpSpeedOrFlightOnly = new BooleanValue("Hold Jump Speed or Flight only", this, true, () -> !this.holdJump.wo());
    private final BooleanValue autoThirdPersonCamera = new BooleanValue("Auto third person camera", this, false);
    private final BooleanValue circle = new BooleanValue("Circle", this, true);
    private final BooleanValue glow = new BooleanValue("Glow", this, false);
    private final NumberValue dots = new NumberValue("Dots", this, 1, 1, 20, 1);
    private final NumberValue thickness = new NumberValue("Thickness", this, 1.5, 1, 10, 0.5);
    private final BooleanValue behind = new BooleanValue("Behind", this, false);
    private float yaw;
    private EntityLivingBase target;
    private boolean left;
    private boolean colliding;
    private boolean active;
    private boolean forcedThirdPerson = false;
    @EventLink(value = 1)
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> {
        this.forcedThirdPerson = false;
        aEg.gameSettings.thirdPersonView = 0;
    };
    @EventLink(value = 1)
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        if (this.target == null) {
            if (aEg.gameSettings.thirdPersonView == 1 && this.forcedThirdPerson) {
                this.forcedThirdPerson = false;
                aEg.gameSettings.thirdPersonView = 0;
            }
        } else {
            if (this.autoThirdPersonCamera.wo()) {
                aEg.gameSettings.thirdPersonView = 1;
                this.forcedThirdPerson = true;
            }
        }
    };
    @EventLink(value = 3)
    public final Listener<JumpEvent> onJump = var1 -> {
        if (this.target != null && this.active) {
            var1.setYaw(this.yaw);
        }
    };
    @EventLink(value = 3)
    public final Listener<StrafeEvent> onStrafe = var1 -> {
        if (this.target != null && this.active) {
            var1.setYaw(this.yaw);
        }
    };
    @EventLink(value = 3)
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        Module module = this.e(Scaffold.class);
        KillAura killaura = this.e(KillAura.class);
        if (module != null && !module.isEnabled() && killaura != null && killaura.isEnabled()) {
            this.active = true;
            Module module1 = this.e(Speed.class);
            Module module2 = this.e(Flight.class);
            if ((!this.holdJump.wo() || aEg.gameSettings.keyBindJump.isKeyDown())
                && (!this.holdJumpSpeedOrFlightOnly.wo() || module1 != null && module1.isEnabled() || module2 != null && module2.isEnabled())) {
                if (this.holdJump.wo() || module1 != null && module1.isEnabled() || module2 != null && module2.isEnabled()) {
                    List list = TargetComponent.f(this.range.wo().doubleValue() + 6.0);
                    if (list.isEmpty()) {
                        this.target = null;
                    } else {
                        if (this.autoThirdPersonCamera.wo()) {
                            aEg.gameSettings.thirdPersonView = 1;
                            this.forcedThirdPerson = true;
                        }

                        this.target = (EntityLivingBase)list.get(0);
                        if (this.target != null) {
                            boolean flag = aEg.gameSettings.keyBindLeft.isKeyDown();
                            boolean flag1 = aEg.gameSettings.keyBindRight.isKeyDown();
                            if (flag && !flag1) {
                                this.left = true;
                            } else if (flag1 && !flag) {
                                this.left = false;
                            }

                            if (this.behind.wo()) {
                                this.yaw = this.target.pl + 180.0F;
                            } else {
                                this.yaw = RotationUtil.y(this.target).getX() + 135 * (this.left ? -1 : 1);
                            }

                            double d0 = this.range.wo().doubleValue() + Math.random() / 100.0;
                            double d1 = -MathHelper.sin((float)Math.toRadians(this.yaw)) * d0 + this.target.posX;
                            double d2 = MathHelper.cos((float)Math.toRadians(this.yaw)) * d0 + this.target.posZ;
                            this.yaw = RotationUtil.d(new Vector3d(d1, this.target.posY, d2)).getX();
                            aEg.thePlayer.pp = this.yaw;
                        }
                    }
                } else {
                    this.target = null;
                }
            } else {
                this.target = null;
            }
        } else {
            this.active = false;
            this.target = null;
        }
    };
    @EventLink(value = 1)
    public final Listener<Render3DEvent> onRender3D = var1 -> {
        if (this.circle.wo() && this.target != null && this.active) {
            this.drawCircle(var1.getPartialTicks());
        }
    };

    public TargetStrafe() {
    }

    @Override
    public void onEnable() {
        this.forcedThirdPerson = false;
    }

    private void drawCircle(float var1) {
        double d0 = MathInterpolation.interpolate(this.target.posX, this.target.lastTickPosX, var1) - aEg.getRenderManager().viewerPosX;
        double d1 = MathInterpolation.interpolate(this.target.posY, this.target.lastTickPosY, var1) - aEg.getRenderManager().viewerPosY;
        double d2 = MathInterpolation.interpolate(this.target.posZ, this.target.lastTickPosZ, var1) - aEg.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableDepth();
        GL11.glPointSize(this.thickness.wo().floatValue());
        GL11.glEnable(2832);
        GL11.glHint(3153, 4354);
        GL11.glBegin(0);
        ColorUtil.glColor(this.rz().rD());
        double range = this.range.wo().doubleValue();
        double d4 = Math.PI * 2;

        for (int i = 0; i < 360; i += this.dots.wo().intValue()) {
            double d5 = i * d4 / 360.0;
            double d6 = d0 + Math.sin(d5) * range;
            double d7 = d2 + Math.cos(d5) * range;
            GL11.glVertex3d(d6, d1, d7);
        }

        GL11.glEnd();
        GL11.glDisable(2832);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
        this.b(ShaderQueueType.BLOOM).c(() -> {
            if (this.glow.wo()) {
                GlStateManager.pushMatrix();
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.disableDepth();
                GL11.glPointSize(this.thickness.wo().floatValue() + 2.0F);
                GL11.glEnable(2832);
                GL11.glHint(3153, 4354);
                GL11.glBegin(0);
                ColorUtil.glColor(this.rz().rD());

                for (int j = 0; j < 360; j += this.dots.wo().intValue()) {
                    double d8 = j * d4 / 360.0;
                    double d9 = d0 + Math.sin(d8) * range;
                    double d10 = d2 + Math.cos(d8) * range;
                    GL11.glVertex3d(d9, d1, d10);
                }

                GL11.glEnd();
                GL11.glDisable(2832);
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        });
    }
}
