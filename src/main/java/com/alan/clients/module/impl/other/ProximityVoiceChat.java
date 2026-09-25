package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.chat.ChatUtil;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import org.lwjgl.input.Keyboard;

@ModuleInfo(aliases = "module.other.proximityvoicechat.name", description = "module.other.proximityvoicechat.description", category = Category.PLAYER)
public class ProximityVoiceChat extends Module {
    private final BooleanValue listenToYourself = new BooleanValue("Listen to yourself", this, false);
    private final NumberValue sampleRate = new NumberValue("Sample Rate", this, 16000, 8000, 192000, 4000);
    private final NumberValue sampleSizeInBits = new NumberValue("Sample Size in bits", this, 16, 8, 32, 1);
    public final ModeValue channelDont = new ModeValue("Channel (dont)", this)
        .add(new SubMode("Mono"))
        .add(new SubMode("Stereo"))
        .add(new SubMode("Quadraphonic"))
        .add(new SubMode("Surround Sound"))
        .setDefault("Mono");
    private Thread audioThread;
    private SourceDataLine outputLine;
    private TargetDataLine microphoneLine;
    private boolean talking = false;
    private int talkTicks = 0;
    @EventLink
    public final Listener<TickEvent> onTick = var1 -> {
        if (Keyboard.isKeyDown(45)) {
            if (!this.talking) {
                this.talking = true;
                this.talkTicks = 0;
                this.startAudio();
                ChatUtil.b("Start Talking");
            } else {
                ChatUtil.b("Talking");
                this.talkTicks++;
            }
        } else if (this.talking) {
            ChatUtil.b("Stop talking");
            this.talking = false;
            this.disable();
        }
    };

    public ProximityVoiceChat() {
    }

    private void startAudio() {
        if (this.audioThread != null) {
            this.audioThread.interrupt();
            this.microphoneLine.stop();
            this.microphoneLine.close();
            this.outputLine.stop();
            this.outputLine.close();
        }

        this.audioThread = new Thread(() -> {
            try {
                AudioFormat audioformat = this.getAudioFormat();
                Info info = new Info(TargetDataLine.class, audioformat);
                this.microphoneLine = (TargetDataLine)AudioSystem.getLine(info);
                this.microphoneLine.open(audioformat);
                this.microphoneLine.start();
                Info info1 = new Info(SourceDataLine.class, audioformat);
                this.outputLine = (SourceDataLine)AudioSystem.getLine(info1);
                this.outputLine.open(audioformat);
                this.outputLine.start();
                byte[] abyte = new byte[4096];

                while (true) {
                    int i = this.microphoneLine.read(abyte, 0, abyte.length);
                    if (this.listenToYourself.wo()) {
                        this.outputLine.write(abyte, 0, i);
                    }
                }
            } catch (LineUnavailableException lineunavailableexception) {
                lineunavailableexception.printStackTrace();
            }
        });
        this.audioThread.start();
    }

    private void disable() {
        if (this.audioThread != null) {
            this.audioThread.interrupt();
        }

        if (this.microphoneLine != null) {
            this.microphoneLine.stop();
            this.microphoneLine.close();
        }

        if (this.outputLine != null) {
            this.outputLine.stop();
            this.outputLine.close();
        }
    }

    @Override
    public void onDisable() {
        this.disable();
    }

    private AudioFormat getAudioFormat() {
        label44: {
            {
                String s = this.channelDont.wo().getName();
                switch (s) {
                    case "Stereo":
                        byte b5 = 2;
                        return new AudioFormat(this.sampleRate.wo().floatValue(), this.sampleSizeInBits.wo().intValue(), b5, true, false);
                    case "Quadraphonic":
                        break label44;
                    case "Surround Sound":
                        break;
                    default:
                        byte b2 = 1;
                        return new AudioFormat(this.sampleRate.wo().floatValue(), this.sampleSizeInBits.wo().intValue(), b2, true, false);
                }
            }

            byte b3 = 6;
            return new AudioFormat(this.sampleRate.wo().floatValue(), this.sampleSizeInBits.wo().intValue(), b3, true, false);
        }

        byte b4 = 4;
        return new AudioFormat(this.sampleRate.wo().floatValue(), this.sampleSizeInBits.wo().intValue(), b4, true, false);
    }
}
