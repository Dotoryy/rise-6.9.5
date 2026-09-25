package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.combat.TargetComponent;
import java.io.File;
import java.io.FileWriter;
import java.util.Map.Entry;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = "Data", description = "module.other.irc.description", category = Category.PLAYER)
public final class Data extends Module {
    private File dataFile;
    private FileWriter writer;
    private StopWatch writeStopWatch = new StopWatch();
    private Entity target;
    private Vector3d lastTargetPosition = new Vector3d(0.0, 0.0, 0.0);
    private Vector3d lastPlayerPosition = new Vector3d(0.0, 0.0, 0.0);
    private Vector2f lastTargetAngle = new Vector2f(0.0F, 0.0F);
    private Vector2f lastPlayerRotation = new Vector2f(0.0F, 0.0F);
    private Vector2f lastTargetRotation = new Vector2f(0.0F, 0.0F);
    private double playerOffsetDistance;
    @EventLink
    public final Listener<PostMotionEvent> onPostMotion = var1 -> {
        this.target = TargetComponent.e(16.0);
        if (this.target == null) {
            this.writeStopWatch.aX();
        } else {
            EntityPlayerSP entityplayersp = aEg.thePlayer;
            JsonObject jsonobject = new JsonObject();
            Vector2f vector2f = this.getRotationsToEntity(this.target);
            Vector2f vector2f1 = new Vector2f(
                MathHelper.wrapAngleTo180_float(vector2f.getX() - aEg.thePlayer.pl), vector2f.getY() - aEg.thePlayer.rotationPitch
            );
            jsonobject.add("playerOffsetYaw", JsonParser.parseString(Double.toString(vector2f1.getX())));
            jsonobject.add("playerOffsetPitch", JsonParser.parseString(Double.toString(vector2f1.getY())));
            Vector2f vector2f2 = this.getRotationsBetween(this.target, entityplayersp);
            Vector2f vector2f3 = new Vector2f(MathHelper.wrapAngleTo180_float(vector2f2.getX() - this.target.pl), vector2f2.getY() - this.target.rotationPitch);
            jsonobject.add("targetOffsetYaw", JsonParser.parseString(Double.toString(vector2f3.getX())));
            jsonobject.add("targetOffsetPitch", JsonParser.parseString(Double.toString(vector2f3.getY())));
            double d0 = entityplayersp.getDistanceToEntity(this.target);
            jsonobject.add("distance", JsonParser.parseString(Double.toString(d0)));
            double d1 = MathUtil.getDistance(entityplayersp.prevPosX, entityplayersp.posY, entityplayersp.prevPosZ, this.target.posX, entityplayersp.posY, this.target.posZ)
                - MathUtil.getDistance(entityplayersp.posX, entityplayersp.posY, entityplayersp.posZ, this.target.posX, entityplayersp.posY, this.target.posZ);
            jsonobject.add("playerDeltaHorizontalDistance", JsonParser.parseString(Double.toString(d1)));
            System.out.println("Data " + d1);
            double d2 = entityplayersp.prevPosY - this.target.posY - (entityplayersp.posY - this.target.posY);
            jsonobject.add("playerDeltaVerticalDistance", JsonParser.parseString(Double.toString(d2)));
            double d3 = MathUtil.getDistance(this.lastTargetPosition.getX(), this.target.posY, this.lastTargetPosition.getZ(), entityplayersp.posX, this.target.posY, entityplayersp.posZ)
                - MathUtil.getDistance(this.target.posX, this.target.posY, this.target.posZ, entityplayersp.posX, this.target.posY, entityplayersp.posZ);
            jsonobject.add("enemyDeltaHorizontalDistance", JsonParser.parseString(Double.toString(d3)));
            double d4 = this.lastTargetPosition.getY() - entityplayersp.posY - (this.target.posY - entityplayersp.posY);
            jsonobject.add("enemyDeltaVerticalDistance", JsonParser.parseString(Double.toString(d4)));
            double d5 = Math.sqrt(Math.pow(d3, 2.0) + Math.pow(d4, 2.0));
            jsonobject.add("enemyOffsetDistance", JsonParser.parseString(Double.toString(d5)));
            double d6 = MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(vector2f1.getX(), vector2f1.getY())));
            jsonobject.add("twoDimensionalAngleToTarget", JsonParser.parseString(Double.toString(d6)));
            double d41 = this.target.posX - entityplayersp.posX;
            double d7 = this.target.posY - entityplayersp.posY;
            jsonobject.add("distancey", JsonParser.parseString(Double.toString(d7)));
            d41 = this.target.posZ - entityplayersp.posZ;
            double d8 = entityplayersp.posX - this.lastPlayerPosition.getX();
            double d9 = entityplayersp.posY - this.lastPlayerPosition.getY();
            double d10 = entityplayersp.posZ - this.lastPlayerPosition.getZ();
            Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
            double d11 = this.target.posX - this.lastTargetPosition.getX();
            double d12 = this.target.posY - this.lastTargetPosition.getY();
            double d13 = this.target.posZ - this.lastTargetPosition.getZ();
            Math.sqrt(d11 * d11 + d12 * d12 + d13 * d13);
            double d14 = RotationUtil.a(this.getRotationsToEntity(this.target), new Vector2f(this.lastTargetAngle.getX(), 0.0F), 999999.0).getX();
            double d15 = Double.isNaN(d14) ? 0.0 : d14;
            jsonobject.add("deltaHorizontalAngle", JsonParser.parseString(Double.toString(d15)));
            double d16 = this.getRotationsToEntity(this.target).getY() - this.lastTargetAngle.getY();
            jsonobject.add("deltaVerticalAngle", JsonParser.parseString(Double.toString(d16)));
            double d17 = MathHelper.wrapAngleTo180_float((float)(360.0 - Math.atan2(d8, d10) * (180.0 / Math.PI) - 180.0)) + 180.0F;
            d41 = Math.atan2(d12, MoveUtil.speed()) * (180.0 / Math.PI);
            double d18 = MathHelper.wrapAngleTo180_double(360.0 - Math.atan2(d11, d13) * (180.0 / Math.PI) - 180.0) + 180.0;
            double d19 = MathHelper.wrapAngleTo180_double(entityplayersp.pl - d17);
            double d20 = MathHelper.wrapAngleTo180_double(this.target.pl - d18);
            double d21 = Math.abs(d19) > 90.0 ? 90.0 - Math.abs(d19) % 90.0 : Math.abs(d19) % 90.0;
            double d22 = (1.0 - MathHelper.clamp_double(d21 / 90.0, 0.0, 1.0)) * (Math.abs(d19) > 90.0 ? -1 : 1);
            double d23 = MoveUtil.speed() * d22;
            double d24 = MoveUtil.speed() * (1.0 - Math.abs(d22)) * (d19 < 0.0 ? -1 : 1);
            jsonobject.add("forwardVelocityPlayer", JsonParser.parseString(Double.toString(d23)));
            jsonobject.add("lateralVelocityPlayer", JsonParser.parseString(Double.toString(d24)));
            jsonobject.add("verticalVelocityPlayer", JsonParser.parseString(Double.toString(d9)));
            jsonobject.add("verticalVelocityTarget", JsonParser.parseString(Double.toString(d12)));
            double d25 = Math.sqrt(d11 * d11 + d13 * d13);
            double d26 = Math.abs(d20) > 90.0 ? 90.0 - Math.abs(d20) % 90.0 : Math.abs(d20) % 90.0;
            double d27 = (1.0 - MathHelper.clamp_double(d26 / 90.0, 0.0, 1.0)) * (Math.abs(d20) > 90.0 ? -1 : 1);
            double d28 = d25 * d27;
            double d29 = d25 * (1.0 - Math.abs(d27)) * (d20 < 0.0 ? -1 : 1);
            jsonobject.add("forwardVelocityTarget", JsonParser.parseString(Double.toString(d28)));
            jsonobject.add("lateralVelocityTarget", JsonParser.parseString(Double.toString(d29)));
            double d30 = MathHelper.wrapAngleTo180_double(
                RotationUtil.c(new Vector3d(entityplayersp.posX, entityplayersp.posY, entityplayersp.posZ), new Vector3d(this.target.posX, entityplayersp.posY, this.target.posZ)).getX()
                    - RotationUtil.c(
                            new Vector3d(entityplayersp.prevPosX, entityplayersp.posY, entityplayersp.prevPosZ),
                            new Vector3d(this.target.posX, entityplayersp.posY, this.target.posZ)
                        )
                        .getX()
            );
            jsonobject.add("playerDeltaHorizontalMovementAngle", JsonParser.parseString(Double.toString(d30)));
            double d31 = RotationUtil.c(new Vector3d(entityplayersp.posX, entityplayersp.posY, entityplayersp.posZ), new Vector3d(this.target.posX, this.target.posY, this.target.posZ))
                    .getY()
                - RotationUtil.c(new Vector3d(entityplayersp.posX, entityplayersp.prevPosY, entityplayersp.posZ), new Vector3d(this.target.posX, this.target.posY, this.target.posZ)).getY();
            jsonobject.add("playerDeltaVerticalMovementAngle", JsonParser.parseString(Double.toString(d31)));
            double d32 = MathHelper.wrapAngleTo180_double(
                RotationUtil.c(new Vector3d(this.target.posX, this.target.posY, this.target.posZ), new Vector3d(entityplayersp.posX, this.target.posY, entityplayersp.posZ)).getX()
                    - RotationUtil.c(new Vector3d(this.lastTargetPosition.getX(), this.target.posY, this.lastTargetPosition.getZ()), new Vector3d(entityplayersp.posX, this.target.posY, entityplayersp.posZ)).getX()
            );
            jsonobject.add("targetDeltaHorizontalMovementAngle", JsonParser.parseString(Double.toString(d32)));
            double d33 = RotationUtil.c(new Vector3d(entityplayersp.posX, entityplayersp.posY, entityplayersp.posZ), new Vector3d(this.target.posX, this.target.posY, this.target.posZ))
                    .getY()
                - RotationUtil.c(new Vector3d(entityplayersp.posX, entityplayersp.posY, entityplayersp.posZ), new Vector3d(this.target.posX, this.lastTargetPosition.getY(), this.target.posZ)).getY();
            jsonobject.add("targetDeltaVerticalMovementAngle", JsonParser.parseString(Double.toString(d33)));
            double d34 = this.playerOffsetDistance - Math.sqrt(Math.pow(vector2f1.getX(), 2.0) + Math.pow(vector2f1.getY(), 2.0));
            jsonobject.add("deltaPlayerOffsetDistance", JsonParser.parseString(Double.toString(d34)));
            this.playerOffsetDistance = Math.sqrt(Math.pow(vector2f1.getX(), 2.0) + Math.pow(vector2f1.getY(), 2.0));
            jsonobject.add("playerOffsetDistance", JsonParser.parseString(Double.toString(this.playerOffsetDistance)));
            double d35 = Math.sqrt(Math.pow(entityplayersp.pl - this.lastPlayerRotation.getX(), 2.0) + Math.pow(entityplayersp.rotationPitch - this.lastPlayerRotation.getY(), 2.0));
            jsonobject.add("deltaRotation", JsonParser.parseString(Double.toString(d35)));
            double d36 = Math.sqrt(Math.pow(this.target.pl - this.lastTargetRotation.getX(), 2.0) + Math.pow(this.target.rotationPitch - this.lastTargetRotation.getY(), 2.0));
            jsonobject.add("targetDeltaRotation", JsonParser.parseString(Double.toString(d36)));
            double d37 = MathHelper.wrapAngleTo180_float(this.lastPlayerRotation.getX() - entityplayersp.pl);
            jsonobject.add("playerDeltaYaw", JsonParser.parseString(Double.toString(d37)));
            double d38 = this.lastPlayerRotation.getY() - entityplayersp.rotationPitch;
            jsonobject.add("playerDeltaPitch", JsonParser.parseString(Double.toString(d38)));
            double d39 = MathHelper.wrapAngleTo180_float(this.lastTargetRotation.getX() - this.target.pl);
            jsonobject.add("targetDeltaYaw", JsonParser.parseString(Double.toString(d39)));
            double d40 = this.lastTargetRotation.getY() - this.target.rotationPitch;
            jsonobject.add("targetDeltaPitch", JsonParser.parseString(Double.toString(d40)));
            this.lastTargetAngle = this.getRotationsToEntity(this.target);
            this.lastPlayerPosition = new Vector3d(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
            this.lastTargetPosition = new Vector3d(this.target.posX, this.target.posY, this.target.posZ);
            Vector2f vector2f4 = new Vector2f(MathHelper.wrapAngleTo180_float(vector2f.getX() - vector2f1.getX()), vector2f.getY() - vector2f1.getY());
            new Vector2f(MathHelper.wrapAngleTo180_float(vector2f4.getX() - this.lastPlayerRotation.getX()), vector2f4.getY() - this.lastPlayerRotation.getY());
            new Vector2f(MathHelper.wrapAngleTo180_float(aEg.thePlayer.pl - this.lastPlayerRotation.getX()), aEg.thePlayer.rotationPitch - this.lastPlayerRotation.getY());
            try {
                if (this.writeStopWatch.T(1000L)) {
                    for (Entry entry : jsonobject.entrySet()) {
                        this.writer.write(((JsonElement)entry.getValue()).getAsString() + "\t");
                    }

                    this.writer.write("\n");
                }
            } catch (java.io.IOException ioexception) {
            }

            this.lastPlayerRotation = new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
            this.lastTargetRotation = new Vector2f(this.target.pl, this.target.rotationPitch);
        }
    };

    public Data() {
    }

    @Override
    public void onEnable() {
        try {
            this.dataFile = new File("C:\\Users\\alanw\\OneDrive\\Desktop\\data.txt");
            this.dataFile.delete();
            this.dataFile.createNewFile();
            this.writer = new FileWriter("C:\\Users\\alanw\\OneDrive\\Desktop\\data.txt");
            ChatUtil.b("Started writing to file");
        } catch (Exception exception) {
            exception.printStackTrace();
            ChatUtil.b("Failed To Write File");
        }

        this.writeStopWatch.aX();
    }

    @Override
    public void onDisable() {
        try {
            this.writer.close();
            ChatUtil.b("Finished writing to file");
        } catch (Exception exception) {
            exception.printStackTrace();
            ChatUtil.b("Failed to write file");
        }
    }

    public Vector2f getRotationsToEntity(Entity entity) {
        return this.getRotationsBetween(aEg.thePlayer, entity);
    }

    public Vector2f getRotationsBetween(Entity entity, Entity var2) {
        Vector2f vector2f = RotationUtil.c(entity.Ty().v(0.0, entity.getEyeHeight(), 0.0), var2.Ty().v(0.0, aEg.thePlayer.getEyeHeight(), 0.0));
        if (vector2f.y == 0.0F) {
            vector2f.y = Math.abs(vector2f.getY());
        }

        return vector2f;
    }
}
