package com.alan.clients.module.impl.combat;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.ghost.HitBox;
import com.alan.clients.module.impl.ghost.Reach;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.ClickEvent;
import com.alan.clients.util.RayCastUtil;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;

@ModuleInfo(aliases = {"Peircing", "Piercing"}, description = "Allows attacks to raycast through blocks", category = Category.COMBAT)
public final class Piercing extends Module {
    @EventLink
    public final Listener<ClickEvent> onClick = var1 -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            if (aEg.objectMouseOver == null || aEg.objectMouseOver.typeOfHit != MovingObjectType.ENTITY) {
                MovingObjectPosition movingobjectposition = RayCastUtil.rayCast(RotationComponent.bH(), this.getReachRange(), this.getHitBoxExpand(), aEg.thePlayer, true);
                if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectType.ENTITY) {
                    aEg.objectMouseOver = movingobjectposition;
                    aEg.pointedEntity = movingobjectposition.entityHit;
                }
            }
        }
    };

    public Piercing() {
    }

    public double getReachRange() {
        Reach reach = this.e(Reach.class);
        return reach != null && reach.isEnabled() ? Math.max(reach.range.wo().doubleValue(), reach.range.wA().doubleValue()) : 3.0;
    }

    public float getHitBoxExpand() {
        HitBox hitbox = this.e(HitBox.class);
        return hitbox != null && hitbox.isEnabled() ? hitbox.expand.wo().floatValue() : 0.0F;
    }
}
