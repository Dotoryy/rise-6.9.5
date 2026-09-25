package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.sound.SoundUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;

@ModuleInfo(aliases = "module.render.killeffect.name", description = "module.render.killeffect.description", category = Category.RENDER)
public final class KillEffect extends Module {
    private final BooleanValue lightning = new BooleanValue("Lightning", this, true);
    private final BooleanValue bloodExplosion = new BooleanValue("Blood Explosion", this, true);
    private final BooleanValue explosion = new BooleanValue("Explosion", this, true);
    private EntityLivingBase target;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (this.target != null && !aEg.theWorld.loadedEntityList.contains(this.target)) {
            if (this.lightning.wo()) {
                EntityLightningBolt entitylightningbolt = new EntityLightningBolt(aEg.theWorld, this.target.posX, this.target.posY, this.target.posZ);
                aEg.theWorld.addEntityToWorld((int)(-Math.random() * 100000.0), entitylightningbolt);
                String s = "ambient.weather.thunder";
                String s1 = "random.explode";
                SoundUtil.playSound(s, 10000.0F, 0.95F);
                SoundUtil.playSound(s1, 2.0F, 0.57F);
            }

            if (this.explosion.wo()) {
                for (int i = 0; i <= 8; i++) {
                    aEg.effectRenderer.emitParticleAtEntity(this.target, EnumParticleTypes.FLAME);
                }

                SoundUtil.cm("item.fireCharge.use");
            }

            if (this.bloodExplosion.wo()) {
                double d0 = this.target.posY;
                double d1 = this.target.posY + this.target.height + 0.4;
                double d2 = 0.4;

                for (int j = 0; j < 100; j++) {
                    for (double d3 = d0; d3 <= d1; d3 += d2) {
                        aEg.theWorld
                            .spawnParticle(
                                EnumParticleTypes.BLOCK_CRACK,
                                this.target.posX,
                                d3,
                                this.target.posZ,
                                0.0,
                                0.0,
                                0.0,
                                Block.getStateId(Blocks.redstone_block.getDefaultState())
                            );
                    }
                }

                for (double d4 = d0; d4 <= d1; d4 += d2) {
                    SoundUtil.cm("dig.stone");
                }
            }

            this.target = null;
        }
    };
    @EventLink
    public final Listener<AttackEvent> onAttack = var1 -> {
        EntityLivingBase entitylivingbase = var1.getLiving();
        if (entitylivingbase instanceof EntityLivingBase) {
            this.target = entitylivingbase;
        }
    };

    public KillEffect() {
    }
}
