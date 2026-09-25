package com.alan.clients.module.impl.render.nametags;

import com.alan.clients.component.impl.render.ProjectionComponent;
import com.alan.clients.module.impl.render.NameTags;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.font.impl.minecraft.FontRenderer;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.util.social.FriendManager;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Vector4d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class VanillaNameTags
extends Mode<NameTags> {
    private final BooleanValue showTeamTag = new BooleanValue("Show Team Tag", (Mode<?>)this, (Boolean)false);
    private final BooleanValue showTargetTag = new BooleanValue("Show Target Tag", (Mode<?>)this, (Boolean)false);
    private final BooleanValue showFriendTag = new BooleanValue("Show Friend Tag", (Mode<?>)this, (Boolean)false);
    private final BooleanValue shortenedTags = new BooleanValue("Shortened Tags", (Mode<?>)this, (Boolean)false);
    @EventLink
    public final Listener<Render2DEvent> onRender2D = render2DEvent -> {
        FontRenderer agd2 = VanillaNameTags.aEg.fontRendererObj;
        GlStateManager.pushMatrix();
        List<EntityLivingBase> list = TargetComponent.b((Boolean)((NameTags)this.getParent()).player.wo(), (Boolean)((NameTags)this.getParent()).invisibles.wo(), (Boolean)((NameTags)this.getParent()).animals.wo(), (Boolean)((NameTags)this.getParent()).mobs.wo(), (Boolean)((NameTags)this.getParent()).playerTeammates.wo(), true);
        if (VanillaNameTags.aEg.gameSettings.thirdPersonView != 0) {
            list.add((EntityLivingBase)VanillaNameTags.aEg.thePlayer);
        }
        Iterator<EntityLivingBase> iterator = list.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                GlStateManager.popMatrix();
                return;
            }
            EntityLivingBase entityLivingBase = iterator.next();
            Object object = entityLivingBase.getDisplayName().getUnformattedText();
            if (((Boolean)this.showTeamTag.wo()).booleanValue() && PlayerUtil.sameTeam(entityLivingBase)) {
                object = "\u00a7a\u00a7l" + ((Boolean)this.shortenedTags.wo() != false ? "[TM]" : "[TEAM]") + "\u00a7r " + (String)object;
            }
            if (((Boolean)this.showTargetTag.wo()).booleanValue() && FriendManager.n(entityLivingBase.getName())) {
                object = "\u00a74\u00a7l" + ((Boolean)this.shortenedTags.wo() != false ? "[T]" : "[TARGET]") + "\u00a7r " + (String)object;
            }
            if (((Boolean)this.showFriendTag.wo()).booleanValue() && FriendManager.isFriend(entityLivingBase.getName())) {
                object = "\u00a7b\u00a7l" + ((Boolean)this.shortenedTags.wo() != false ? "[F]" : "[FRIEND]") + "\u00a7r " + (String)object;
            }
            entityLivingBase.Tc();
            Vector4d vector4d = ProjectionComponent.e((Entity)entityLivingBase);
            if (vector4d == null) continue;
            float f2 = 2.0f;
            int n2 = 8;
            float f3 = ((NameTags)this.getParent()).a((String)object, agd2);
            float f4 = (float)(vector4d.x + (vector4d.z - vector4d.x) / 2.0);
            float f5 = (float)vector4d.y - (float)n2;
            this.b(ShaderQueueType.BLOOM).c(() -> RenderUtil.d(f4 - f3 / 2.0f - f2, f5 - f2 - 3.0f, f3 + f2 * 2.0f, (float)n2 + f2 * 2.0f, this.rz().rE()));
            this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.d(f4 - f3 / 2.0f - f2, f5 - f2 - 3.0f, f3 + f2 * 2.0f, (float)n2 + f2 * 2.0f, Color.BLACK));
            Object object2 = object;
            this.b(ShaderQueueType.REGULAR).c(() -> this.drawNameTag(f4, f3, f2, f5, n2, agd2, (String)object2));
        }
    };

    public VanillaNameTags(String string, NameTags nameTags) {
        super(string, nameTags);
    }

    private  void drawNameTag(float f2, float f3, float f4, float f5, int n2, Font agc2, String string) {
        double d2 = f2 - f3 / 2.0f - f4;
        double d3 = f5 - f4 - 3.0f;
        double d4 = f3 + f4 * 2.0f;
        double d5 = (float)n2 + f4 * 2.0f;
        this.rz();
        RenderUtil.d(d2, d3, d4, d5, Themes.rK());
        float f6 = f2 - f3 / 2.0f;
        agc2.b(string, f6 + 0.5f, f5 - 2.0f, Color.WHITE.getRGB());
    }
}
