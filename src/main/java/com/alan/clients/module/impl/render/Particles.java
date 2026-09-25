package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;

@ModuleInfo(aliases = "module.render.particles.name", description = "module.render.particles.description", category = Category.RENDER)
public final class Particles extends Module {
    private final NumberValue multiplier = new NumberValue("Multiplier", this, 1, 1, 10, 1);
    private final BooleanValue alwaysCrit = new BooleanValue("Always Crit", this, true);
    private final BooleanValue alwaysSharpness = new BooleanValue("Always Sharpness", this, true);
    @EventLink
    public final Listener<AttackEvent> onAttack = var1 -> {
        EntityLivingBase entitylivingbase = var1.getLiving();
        if (aEg.thePlayer.fallDistance > 0.0F || this.alwaysCrit.wo() || this.alwaysSharpness.wo()) {
            for (int i = 0; i <= this.multiplier.wo().intValue(); i++) {
                if (this.alwaysCrit.wo()) {
                    aEg.thePlayer.onCriticalHit(entitylivingbase);
                }

                if (this.alwaysSharpness.wo()) {
                    aEg.effectRenderer.emitParticleAtEntity(entitylivingbase, EnumParticleTypes.CRIT_MAGIC);
                }
            }
        }
    };

    public Particles() {
    }
}
