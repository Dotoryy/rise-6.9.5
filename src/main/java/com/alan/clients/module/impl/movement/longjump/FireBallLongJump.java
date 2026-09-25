package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.SlotUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class FireBallLongJump extends Mode<LongJump> {
    public ModeValue mode = new ModeValue("Mode", this).add(new SubMode("Custom")).add(new SubMode("Hypixel")).setDefault("Hypixel");
    private final NumberValue height = new NumberValue("Height", this, 1, 0.42, 9, 0.1, () -> !this.mode.wo().getName().equals("Custom"));
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 3, 0.1, () -> !this.mode.wo().getName().equals("Custom"));
    private int tick;
    private double moveSpeed = 0.0;
    private float yawAtDamage;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        label47: {
            {
                String s = this.mode.wo().getName();
                switch (s) {
                    case "Hypixel":
                        if (aEg.thePlayer.hurtTime == 10) {
                            this.yawAtDamage = aEg.thePlayer.pl;
                            aEg.thePlayer.motionY = 1.5;
                            this.moveSpeed = 1.4;
                        }

                        if (aEg.thePlayer.hurtTime == 9) {
                            this.moveSpeed = 2.0 - Math.random() / 100.0;
                        }

                        if (aEg.thePlayer.ae <= 11) {
                            MoveUtil.a(this.moveSpeed, this.yawAtDamage);
                            this.moveSpeed = this.moveSpeed - (this.moveSpeed / 249.9 + Math.random() / 100.0);
                        }
                        break label47;
                    case "Custom":
                        break;
                    default:
                        break label47;
                }
            }

            if (aEg.thePlayer.ae <= 1) {
                aEg.thePlayer.motionY = this.height.wo().doubleValue();
                MoveUtil.strafe(this.speed.wo().doubleValue());
            }
        }

        int i = SlotUtil.findItem(Items.fire_charge);
        if (aEg.thePlayer.cqL == 1) {
            MoveUtil.stop();
        }

        if (i != -1) {
            this.tick++;
            SlotComponent slotcomponent = this.d(SlotComponent.class);
            SlotComponent.setSlot(i);
            if (this.tick == 2) {
                SlotComponent slotcomponent1 = this.d(SlotComponent.class);
                PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));
            }

            RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl, 90.0F), 10.0, MovementFix.OFF);
        }
    };

    public FireBallLongJump(String var1, LongJump longJump) {
        super(var1, longJump);
    }

    @Override
    public void onEnable() {
        this.tick = 0;
        if (this.mode.wo().getName().equals("Hypixel")) {
            ChatUtil.b("Don't enable on bridges, the ac falses, also make sure you take the velocity when on the ground.");
        }
    }
}
