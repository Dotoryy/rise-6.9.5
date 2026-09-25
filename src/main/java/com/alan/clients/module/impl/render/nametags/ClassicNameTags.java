package com.alan.clients.module.impl.render.nametags;

import com.alan.clients.component.impl.render.ProjectionComponent;
import com.alan.clients.module.impl.player.HealthBypass;
import com.alan.clients.module.impl.render.NameTags;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.impl.minecraft.FontRenderer;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.util.social.FriendManager;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.util.List;
import javax.vecmath.Vector4d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class ClassicNameTags extends Mode<NameTags> {
    private final BooleanValue showTeamTag = new BooleanValue("Show Team Tag", this, false);
    private final BooleanValue showTargetTag = new BooleanValue("Show Target Tag", this, false);
    private final BooleanValue showFriendTag = new BooleanValue("Show Friend Tag", this, false);
    private final BooleanValue shortenedTags = new BooleanValue("Shortened Tags", this, false);
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1x -> {
        FontRenderer agd = aEg.fontRendererObj;
        GlStateManager.pushMatrix();
        List list = TargetComponent.b(this.getParent().player.wo(), this.getParent().invisibles.wo(), this.getParent().animals.wo(), this.getParent().mobs.wo(), this.getParent().playerTeammates.wo(), true);
        if (aEg.gameSettings.thirdPersonView != 0 && this.getParent().player.wo()) {
            list.add(aEg.thePlayer);
        }

        for (EntityLivingBase entitylivingbase : (Iterable<EntityLivingBase>)list) {
            HealthBypass healthbypass = this.e(HealthBypass.class);
            float f = healthbypass != null && healthbypass.isEnabled() ? HealthBypass.getScoreboardHealth(entitylivingbase) : entitylivingbase.getHealth();
            String s = entitylivingbase.getDisplayName().getFormattedText() + " §7[§4❤" + Math.round(f) + "§7]";
            if (this.showTeamTag.wo() && PlayerUtil.sameTeam(entitylivingbase)) {
                s = "§a§l" + (this.shortenedTags.wo() ? "[TM]" : "[TEAM]") + "§r " + s;
            }

            if (this.showTargetTag.wo() && FriendManager.n(entitylivingbase.getName())) {
                s = "§4§l" + (this.shortenedTags.wo() ? "[T]" : "[TARGET]") + "§r " + s;
            }

            if (this.showFriendTag.wo() && FriendManager.isFriend(entitylivingbase.getName())) {
                s = "§b§l" + (this.shortenedTags.wo() ? "[F]" : "[FRIEND]") + "§r " + s;
            }

            entitylivingbase.Tc();
            Vector4d vector4d = ProjectionComponent.e(entitylivingbase);
            if (vector4d != null) {
                float f1 = 2.0F;
                byte b0 = 8;
                float f2 = agd.getStringWidth(s);
                float f3 = (float)(vector4d.x + (vector4d.z - vector4d.x) / 2.0);
                float f4 = (float)vector4d.y - b0;
                String s1 = s;
                this.b(ShaderQueueType.REGULAR).c(() -> {
                    double d0 = f3 - f2 / 2.0F - f1;
                    double d1 = f4 - f1 - 3.0F;
                    double d2 = f2 + f1 * 2.0F;
                    double d3 = b0 + f1 * 2.0F;
                    this.rz();
                    RenderUtil.d(d0, d1, d2, d3, Themes.rK());
                    float f5 = f3 - f2 / 2.0F;
                    agd.b(s1, f5 + 0.5F, f4 - 2.0F, Color.WHITE.getRGB());
                });
            }
        }

        GlStateManager.popMatrix();
    };

    public ClassicNameTags(String var1, NameTags nameTags) {
        super(var1, nameTags);
    }
}
