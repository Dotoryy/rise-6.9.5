package com.alan.clients.command.impl;

import com.alan.clients.command.Command;
import com.alan.clients.util.chat.ChatUtil;

public final class Clip extends Command {
    public Clip() {
        super("command.clip.description", "clip", "vclip", "hclip");
    }

    @Override
    public void execute(String[] var1) {
        if (var1.length > 1 && !var1[1].isEmpty()) {
            label149: {
                {
                    String s = var1[0].toLowerCase();
                    switch (s) {
                        case "vclip":
                            double d0 = Double.parseDouble(var1[1]);
                            aEg.thePlayer.setPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + d0, aEg.thePlayer.posZ);
                            ChatUtil.b("Clipped you " + (d0 > 0.0 ? "up" : "down") + " " + Math.abs(d0) + " blocks.");
                            return;
                        case "hclip":
                            break;
                        case "clip":
                            break label149;
                        default:
                            this.error();
                            return;
                    }
                }

                double d1 = Double.parseDouble(var1[1]);
                double radians = Math.toRadians(aEg.thePlayer.pl);
                double d3 = Math.sin(radians) * d1;
                double d4 = Math.cos(radians) * d1;
                aEg.thePlayer.setPosition(aEg.thePlayer.posX - d3, aEg.thePlayer.posY, aEg.thePlayer.posZ + d4);
                ChatUtil.b("Clipped you " + (d1 > 0.0 ? "forward" : "back") + " " + Math.abs(d1) + " blocks.");
                return;
            }

            if (var1.length <= 2 || var1[2].isEmpty()) {
                this.error();
            } else {
                label150: {
                    label151: {
                        label152: {
                            label153: {
                                label154: {
                                    {
                                        String s1 = var1[1];
                                        switch (s1) {
                                            case "upward":
                                            case "upwards":
                                            case "up":
                                                break label150;
                                            case "down":
                                            case "downwards":
                                            case "downward":
                                                break label151;
                                            case "forward":
                                            case "forwards":
                                                break label152;
                                            case "back":
                                            case "backwards":
                                            case "backward":
                                                break label153;
                                            case "left":
                                                break label154;
                                            case "right":
                                                break;
                                            default:
                                                this.error();
                                                return;
                                        }
                                    }

                                    double d19 = Double.parseDouble(var1[2]);
                                    double d20 = Math.toRadians(aEg.thePlayer.pl + 90.0F);
                                    double d21 = Math.sin(d20) * d19;
                                    double d22 = Math.cos(d20) * d19;
                                    aEg.thePlayer.setPosition(aEg.thePlayer.posX - d21, aEg.thePlayer.posY, aEg.thePlayer.posZ + d22);
                                    ChatUtil.b("Clipped you right " + d19 + " blocks.");
                                    return;
                                }

                                double d15 = Double.parseDouble(var1[2]);
                                double d16 = Math.toRadians(aEg.thePlayer.pl - 90.0F);
                                double d17 = Math.sin(d16) * d15;
                                double d18 = Math.cos(d16) * d15;
                                aEg.thePlayer.setPosition(aEg.thePlayer.posX - d17, aEg.thePlayer.posY, aEg.thePlayer.posZ + d18);
                                ChatUtil.b("Clipped you left " + d15 + " blocks.");
                                return;
                            }

                            double d11 = Double.parseDouble(var1[2]);
                            double d12 = Math.toRadians(aEg.thePlayer.pl);
                            double d13 = Math.sin(d12) * d11;
                            double d14 = Math.cos(d12) * d11;
                            aEg.thePlayer.setPosition(aEg.thePlayer.posX + d13, aEg.thePlayer.posY, aEg.thePlayer.posZ - d14);
                            ChatUtil.b("Clipped you back " + d11 + " blocks.");
                            return;
                        }

                        double d7 = Double.parseDouble(var1[2]);
                        double d8 = Math.toRadians(aEg.thePlayer.pl);
                        double d9 = Math.sin(d8) * d7;
                        double d10 = Math.cos(d8) * d7;
                        aEg.thePlayer.setPosition(aEg.thePlayer.posX - d9, aEg.thePlayer.posY, aEg.thePlayer.posZ + d10);
                        ChatUtil.b("Clipped you forward " + d7 + " blocks.");
                        return;
                    }

                    double d6 = Double.parseDouble(var1[2]);
                    aEg.thePlayer.setPosition(aEg.thePlayer.posX, aEg.thePlayer.posY - d6, aEg.thePlayer.posZ);
                    ChatUtil.b("Clipped you down " + d6 + " blocks.");
                    return;
                }

                double d5 = Double.parseDouble(var1[2]);
                aEg.thePlayer.setPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + d5, aEg.thePlayer.posZ);
                ChatUtil.b("Clipped you up " + d5 + " blocks.");
            }
        } else {
            this.error(".clip <up/down/forward/back/left/right> <amount>");
        }
    }
}
