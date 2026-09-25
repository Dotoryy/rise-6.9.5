package com.alan.clients.component.impl.render;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.interfaces.ExecutorAccess;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.render.Projection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.vecmath.Vector4d;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

public class ProjectionComponent
extends Component
implements ExecutorAccess {
    private static final HashMap<Entity, Projection> hK = new HashMap();
    private static HashMap<Entity, Projection> hL = new HashMap();
    @EventLink(value=0)
    public final Listener<Render2DEvent> onRender2D = render2DEvent -> aMR.execute(() -> {
        HashMap<Entity, Projection> hashMap = new HashMap<Entity, Projection>();
        HashMap<Entity, Projection> hashMap2 = hK;
        synchronized (hashMap2) {
            for (Map.Entry<Entity, Projection> entry : hK.entrySet()) {
                Projection ck2 = entry.getValue();
                ck2.hN = this.f(entry.getKey());
                hashMap.put(entry.getKey(), ck2);
            }
            hK.clear();

            hL = hashMap;
            return;
        }
    });

    public static Vector4d e(Entity entity) {
        Projection ck2;
        if (entity == null) {
            return null;
        }
        if (!hK.containsKey(entity)) {
            hK.put(entity, new Projection());
        }
        if ((ck2 = hL.get(entity)) == null) {
            return null;
        }
        return ck2.cm();
    }

    private Vector3d a(int n2, double d2, double d3, double d4) {
        if (GLU.gluProject((float)d2, (float)d3, (float)d4, ActiveRenderInfo.MODELVIEW, ActiveRenderInfo.PROJECTION, ActiveRenderInfo.VIEWPORT, ActiveRenderInfo.OBJECTCOORDS)) {
            return new Vector3d(ActiveRenderInfo.OBJECTCOORDS.get(0) / (float)n2, ((float)Display.getHeight() - ActiveRenderInfo.OBJECTCOORDS.get(1)) / (float)n2, ActiveRenderInfo.OBJECTCOORDS.get(2));
        }
        return null;
    }

    public Vector4d f(Entity entity) {
        aEg.getRenderManager();
        double d2 = RenderManager.bUO;
        aEg.getRenderManager();
        double d3 = RenderManager.bUP;
        aEg.getRenderManager();
        double d4 = RenderManager.bUQ;
        double d5 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)ProjectionComponent.aEg.timer.bWm - d2;
        double d6 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)ProjectionComponent.aEg.timer.bWm - d3;
        double d7 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)ProjectionComponent.aEg.timer.bWm - d4;
        double d8 = ((double)entity.width + 0.14) / 2.0;
        double d9 = (double)entity.height + (entity.isSneaking() ? -0.1 : 0.2) + 0.01;
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d5 - d8, d6, d7 - d8, d5 + d8, d6 + d9, d7 + d8);
        List<Vector3d> list = Arrays.asList(new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));
        Vector4d vector4d = null;
        Iterator<Vector3d> iterator = list.iterator();
        while (iterator.hasNext()) {
            Vector3d aka2 = iterator.next();
            Vector3d aka3 = this.a(ProjectionComponent.aEg.jY.getScaleFactor(), aka2.getX(), aka2.getY(), aka2.getZ());
            if (aka3 == null || !(aka3.getZ() >= 0.0) || !(aka3.getZ() < 1.0)) continue;
            if (vector4d == null) {
                vector4d = new Vector4d(aka3.getX(), aka3.getY(), aka3.getZ(), 0.0);
            }
            vector4d = new Vector4d(Math.min(aka3.getX(), vector4d.x), Math.min(aka3.getY(), vector4d.y), Math.max(aka3.getX(), vector4d.z), Math.max(aka3.getY(), vector4d.w));
        }
        return vector4d;
    }
}
