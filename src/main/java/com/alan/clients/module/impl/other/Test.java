package com.alan.clients.module.impl.other;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.ClickEvent;
import com.alan.clients.newevent.impl.input.GuiClickEvent;
import com.alan.clients.newevent.impl.input.GuiMouseReleaseEvent;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.motion.WaterEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.newevent.impl.render.MouseOverEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.value.impl.StringValue;
import com.alan.clients.util.type.EvictingList;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.TimedPacket;
import com.alan.clients.util.pathfinding.alan.Pathfinder;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.value.impl.SupplierValue;
import com.alan.clients.util.math.MathOperation;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases={"module.other.test.name"}, description="module.other.test.description", category=Category.MOVEMENT)
public final class Test
extends Module {
    private final ArrayList<Integer> integerArrayList = new ArrayList();
    private final ArrayList<Double> doubleArrayList = new ArrayList();
    private final ArrayList<Float> floatArrayList = new ArrayList();
    private final ArrayList<Packet<?>> packetArrayOutList = new ArrayList();
    private final ArrayList<Packet<?>> packetArrayInList = new ArrayList();
    private int anInt;
    private int attackCount;
    private boolean aBoolean;
    private boolean aBoolean2;
    private boolean delay;
    private double aDouble;
    private double aDouble2;
    private double serverPosX;
    private double serverPosY;
    private double serverPosZ;
    private float lastYaw;
    private float lastPitch;
    private Vec3 lastPosition;
    private Vec3 currentPosition;
    private Vec3 lastCurrentPosition;
    private Entity entity;
    private Packet<?> aPacket;
    private EntityOtherPlayerMP otherEntity;
    private final List<Packet<?>> packets = new ArrayList();
    private final ConcurrentLinkedQueue<TimedPacket> timedPackets = new ConcurrentLinkedQueue();
    private final StopWatch timerUtil = new StopWatch();
    private double startPosY;
    private double distance;
    private double moveSpeed;
    private double y;
    private double lastX;
    private double lastY;
    private double lastZ;
    private int stage;
    private int bestBlockStack;
    public static boolean set;
    public static boolean doFly;
    private BlockPos startPos;
    private final DecimalFormat format = new DecimalFormat("0.0");
    private Vec3 targetBlock;
    private BlockPos blockFace;
    private final DragValue positionValue = new DragValue("Position", (Module)this, new Vector2d(255.0, 255.0));
    public Vec3 position = new Vec3(0.0, 0.0, 0.0);
    private final StringValue runIf = new StringValue("Run If () ->", (Module)this, "onGround");
    private final SupplierValue testCurve = new SupplierValue("Test Curve", this);
    private final ArrayList<Entity> entities = new ArrayList();
    private final ArrayList<Vector3d> positions = new ArrayList();
    private World world;
    private boolean reset;
    private double speed;
    private boolean placeHolder;
    Executor threadPool = Executors.newFixedThreadPool(1);
    Pathfinder pathfinder;
    EntityPlayerSP playerSP;
    EvictingList<BlockPos> blockHistory = new EvictingList(2);
    private final HashMap<Double, HashMap<Integer, Tuple<Double, Boolean>>> dataMap = new HashMap();
    private Runnable aRunnable;
    private Runnable aRunnable2;
    List<Vector2d> curvePoints = new ArrayList<Vector2d>();
    Vector2d draggedPoint = null;
    @EventLink
    public final Listener<MouseOverEvent> onMouseOver = mouseOverEvent -> {
        this.placeHolder = true;
    };
    @EventLink(value=0)
    public final Listener<PacketReceiveEvent> receive = packetReceiveEvent -> {
        this.placeHolder = true;
        Packet<?> packet = packetReceiveEvent.getPacket();
        if (packet instanceof S20PacketEntityProperties) {
            Iterator iterator = ((S20PacketEntityProperties)packet).func_149441_d().iterator();
            while (iterator.hasNext()) {
                Iterator iterator2 = ((S20PacketEntityProperties.Snapshot)iterator.next()).func_151408_c().iterator();
                while (iterator2.hasNext()) {
                    if (!((AttributeModifier)iterator2.next()).getID().equals(Test.aEg.thePlayer.getUniqueID())) continue;
                    ChatUtil.c("s", new Object[0]);
                }
            }
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> send = packetSendEvent -> {
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<TeleportEvent> teleport = teleportEvent -> {
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<BlockAABBEvent> blockAABB = blockAABBEvent -> {
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<Render2DEvent> render2D = render2DEvent -> {
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<Render3DEvent> render3D = render3DEvent -> {
        this.placeHolder = true;
    };
    private HashMap<Integer, Integer> rotationDeltaCounts = new HashMap();
    public static double YV;
    @EventLink
    public final Listener<PreUpdateEvent> preUpdate = preUpdateEvent -> {
        this.placeHolder = true;
        Test.aEg.gameSettings.keyBindSneak.setPressed(PlayerUtil.p(0.0, MoveUtil.predictedMotion(Test.aEg.thePlayer.motionY), 0.0) instanceof BlockSlime && Test.aEg.thePlayer.motionY < -0.1);
    };
    @EventLink
    public final Listener<WaterEvent> water = waterEvent -> {
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<AttackEvent> attack = attackEvent -> {
        ++this.attackCount;
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<WorldChangeEvent> worldChange = worldChangeEvent -> {
        this.blockFace = null;
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = preMotionEvent -> {
        this.placeHolder = true;
        if (Test.aEg.thePlayer.tR == 0) {
            preMotionEvent.setPosY(Test.aEg.thePlayer.posY - 0.01);
        }
    };
    @EventLink
    public final Listener<GuiMouseReleaseEvent> onGuiMouseRelease = guiMouseReleaseEvent -> {
        this.draggedPoint = null;
    };
    @EventLink
    public final Listener<GuiClickEvent> onGuiClick = guiClickEvent -> {
        int n2 = guiClickEvent.getMouseX();
        int n3 = guiClickEvent.getMouseY();
        ArrayList<Vector2d> arrayList = new ArrayList<Vector2d>(this.curvePoints);
        arrayList.sort((vector2d, vector2d2) -> (int)(MathOperation.EUCLIDEAN_DISTANCE.a(vector2d.getX() - (double)n2, vector2d.getY() - (double)n3) - MathOperation.EUCLIDEAN_DISTANCE.a(vector2d2.getX() - (double)n2, vector2d2.getY() - (double)n3)));
        this.draggedPoint = (Vector2d)arrayList.stream().findFirst().get();
    };
    @EventLink
    public final Listener<ClickEvent> onClick = clickEvent -> {
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<PostMotionEvent> onPostMotionEvent = postMotionEvent -> {
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<TickEvent> onTick = tickEvent -> {
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = strafeEvent -> {
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<PostStrafeEvent> onPostStrafe = postStrafeEvent -> {
        this.placeHolder = true;
    };
    public final Listener<JumpEvent> onJump = jumpEvent -> {
        this.placeHolder = true;
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = moveInputEvent -> {
        this.placeHolder = true;
    };

    @Override
    public void onDisable() {
        double d2 = 0.0;
        for (double d3 : this.doubleArrayList) {
            d2 += d3;
        }
        double d4 = d2 / (double)this.doubleArrayList.size();
        this.placeHolder = true;
        Test.aEg.gameSettings.keyBindSneak.setPressed(Keyboard.isKeyDown(Test.aEg.gameSettings.keyBindSneak.getKeyCode()));
        Test.aEg.gameSettings.keyBindJump.setPressed(Keyboard.isKeyDown(Test.aEg.gameSettings.keyBindJump.getKeyCode()));
        Test.aEg.gameSettings.keyBindRight.setPressed(Keyboard.isKeyDown(Test.aEg.gameSettings.keyBindRight.getKeyCode()));
        Test.aEg.gameSettings.keyBindLeft.setPressed(Keyboard.isKeyDown(Test.aEg.gameSettings.keyBindLeft.getKeyCode()));
    }

    @Override
    public void onEnable() {
        this.doubleArrayList.clear();
        this.placeHolder = true;
        this.aBoolean = false;
        this.blockFace = null;
        this.blockHistory.clear();
        this.anInt = 0;
        this.attackCount = 0;
        this.curvePoints.clear();
        this.curvePoints.add(new Vector2d(100.0, 500.0));
        this.curvePoints.add(new Vector2d(200.0, 100.0));
        this.curvePoints.add(new Vector2d(600.0, 100.0));
        this.curvePoints.add(new Vector2d(700.0, 500.0));
    }

    private void drawCurve(List<Vector2d> list) {
        for (Vector2d vector2d : list) {
            RenderUtil.c(vector2d.x, vector2d.y, 3.0, this.rz().rA());
        }
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(3);
        float f2 = 0.0f;
        while ((double)f2 <= 1.0) {
            Vector2d vector2d = this.getBezierPoint(f2, list);
            GL11.glVertex2f((float)vector2d.x, (float)vector2d.y);
            f2 += 0.01f;
        }
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        RenderUtil.color(Color.WHITE);
    }

    private Vector2d getBezierPoint(float f2, List<Vector2d> list) {
        float f3 = 1.0f - f2;
        float f4 = (float)(Math.pow(f3, 3.0) * list.get((int)0).x + 3.0 * Math.pow(f3, 2.0) * (double)f2 * list.get((int)1).x + (double)(3.0f * f3) * Math.pow(f2, 2.0) * list.get((int)2).x + Math.pow(f2, 3.0) * list.get((int)3).x);
        float f5 = (float)(Math.pow(f3, 3.0) * list.get((int)0).y + 3.0 * Math.pow(f3, 2.0) * (double)f2 * list.get((int)1).y + (double)(3.0f * f3) * Math.pow(f2, 2.0) * list.get((int)2).y + Math.pow(f2, 3.0) * list.get((int)3).y);
        return new Vector2d(f4, f5);
    }

    public void run() {
        if (Test.aEg.gameSettings.keyBindSneak.isKeyDown()) {
            this.rotationDeltaCounts.clear();
        }
        if (RayCastUtil.c((Vector2f)RotationComponent.fk, (double)15.0).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            this.lastYaw = Test.aEg.thePlayer.pl;
            this.lastPitch = Test.aEg.thePlayer.rotationPitch;
            return;
        }
        int n4 = (int)Math.round(Math.hypot(Test.aEg.thePlayer.pl - this.lastYaw, Test.aEg.thePlayer.rotationPitch - this.lastPitch));
        this.rotationDeltaCounts.putIfAbsent(n4, 1);
        this.rotationDeltaCounts.put(n4, this.rotationDeltaCounts.get(n4) + 1);
        ChatUtil.b("Outputted", new Object[0]);
        this.rotationDeltaCounts.forEach((n2, n3) -> System.out.println(n3 + "\t" + n2));
        this.lastYaw = Test.aEg.thePlayer.pl;
        this.lastPitch = Test.aEg.thePlayer.rotationPitch;
    }
}
