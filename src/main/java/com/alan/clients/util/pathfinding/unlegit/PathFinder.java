package com.alan.clients.util.pathfinding.unlegit;

import com.alan.clients.util.pathfinding.unlegit.NodeComparator;
import com.alan.clients.util.pathfinding.unlegit.Node;
import com.alan.clients.util.pathfinding.unlegit.Vec3;
import com.alan.clients.util.player.PlayerUtil;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockSnowBlock;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public final class PathFinder {
    private final ArrayList<Node> hubsToWork = new ArrayList();
    private ArrayList<Vec3> path = new ArrayList();
    private final ArrayList<Node> hubs = new ArrayList();
    private final double minDistanceSquared = 9.5;
    private final boolean nearest = true;
    private final Vec3 aNQ;
    private final Vec3 aNR;
    private static final Vec3[] aNS = new Vec3[]{new Vec3(1.0, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0), new Vec3(0.0, 0.0, 1.0), new Vec3(0.0, 0.0, -1.0)};

    public PathFinder(Vec3 ahy2, Vec3 ahy3) {
        this.aNQ = ahy2.n(0.0, 0.0, 0.0).uX();
        this.aNR = ahy3.n(0.0, 0.0, 0.0).uX();
    }

    public ArrayList<Vec3> getPath() {
        return this.path;
    }

    public void uR() {
        this.compute(1000, 4);
    }

    public void compute(int n2, int n3) {
        this.path.clear();
        this.hubsToWork.clear();
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        arrayList.add(this.aNQ);
        this.hubsToWork.add(new Node(this.aNQ, null, arrayList, this.aNQ.c(this.aNR), 0.0, 0.0));
        block0: for (int i2 = 0; i2 < n2; ++i2) {
            Vec3 ahy2;
            Node ahx2;
            Vec3 ahy3;
            this.hubsToWork.sort(new NodeComparator());
            int n4 = 0;
            if (this.hubsToWork.size() == 0) break;
            Iterator<Node> iterator = new ArrayList<Node>(this.hubsToWork).iterator();
            do {
                if (!iterator.hasNext()) continue block0;
                ahx2 = iterator.next();
                if (++n4 > n3) {
                    continue block0;
                }
                this.hubsToWork.remove(ahx2);
                this.hubs.add(ahx2);
                for (Vec3 ahy4 : aNS) {
                    Vec3 ahy5 = ahx2.uS().d(ahy4).uX();
                    if (PathFinder.checkPositionValidity(ahy5, false) && this.a(ahx2, ahy5, 0.0)) break block0;
                }
            } while ((!PathFinder.checkPositionValidity(ahy3 = ahx2.uS().n(0.0, 1.0, 0.0).uX(), false) || !this.a(ahx2, ahy3, 0.0)) && (!PathFinder.checkPositionValidity(ahy2 = ahx2.uS().n(0.0, -1.0, 0.0).uX(), false) || !this.a(ahx2, ahy2, 0.0)));
        }
        this.hubs.sort(new NodeComparator());
        this.path = this.hubs.get(0).uQ();
    }

    public static boolean checkPositionValidity(Vec3 ahy2, boolean bl) {
        return PathFinder.a((int)ahy2.getX(), (int)ahy2.getY(), (int)ahy2.getZ(), bl);
    }

    public static boolean a(int n2, int n3, int n4, boolean bl) {
        BlockPos blockPos = new BlockPos(n2, n3 - 1, n4);
        if (PathFinder.isBlockSolid(new BlockPos(n2, n3, n4))) return false;
        if (PathFinder.isBlockSolid(new BlockPos(n2, n3 + 1, n4))) return false;
        if (!PathFinder.isBlockSolid(blockPos)) {
            if (bl) return false;
        }
        if (!PathFinder.isSafeToWalkOn(blockPos)) return false;
        return true;
    }

    private static boolean isBlockSolid(BlockPos blockPos) {
        Block block = PlayerUtil.block(blockPos);
        if (block.isFullBlock()) return true;
        if (block instanceof BlockSlab) return true;
        if (block instanceof BlockStairs) return true;
        if (block instanceof BlockCactus) return true;
        if (block instanceof BlockChest) return true;
        if (block instanceof BlockEnderChest) return true;
        if (block instanceof BlockSkull) return true;
        if (block instanceof BlockPane) return true;
        if (block instanceof BlockFence) return true;
        if (block instanceof BlockWall) return true;
        if (block instanceof BlockGlass) return true;
        if (block instanceof BlockPistonBase) return true;
        if (block instanceof BlockPistonExtension) return true;
        if (block instanceof BlockPistonMoving) return true;
        if (block instanceof BlockStainedGlass) return true;
        if (block instanceof BlockTrapDoor) return true;
        if (block instanceof BlockEndPortalFrame) return true;
        if (block instanceof BlockEndPortal) return true;
        if (block instanceof BlockBed) return true;
        if (block instanceof BlockWeb) return true;
        if (block instanceof BlockBarrier) return true;
        if (block instanceof BlockLadder) return true;
        if (block instanceof BlockLeaves) return true;
        if (block instanceof BlockSnow) return true;
        if (block instanceof BlockSnowBlock) return true;
        if (block instanceof BlockCarpet) return true;
        if (block instanceof BlockDoor) return true;
        if (block instanceof BlockVine) return true;
        if (!(block instanceof BlockLilyPad)) return false;
        return true;
    }

    private static boolean isSafeToWalkOn(BlockPos blockPos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
        if (block instanceof BlockFence) return false;
        if (block instanceof BlockWall) return false;
        return true;
    }

    public Node a(Vec3 ahy2) {
        for (Node ahx2 : this.hubs) {
            if (ahx2.uS().getX() != ahy2.getX() || ahx2.uS().getY() != ahy2.getY() || ahx2.uS().getZ() != ahy2.getZ()) continue;
            return ahx2;
        }
        for (Node ahx3 : this.hubsToWork) {
            if (ahx3.uS().getX() != ahy2.getX() || ahx3.uS().getY() != ahy2.getY() || ahx3.uS().getZ() != ahy2.getZ()) continue;
            return ahx3;
        }
        return null;
    }

    public boolean a(Node ahx2, Vec3 ahy2, double d2) {
        Node ahx3 = this.a(ahy2);
        double d3 = d2;
        if (ahx2 != null) {
            d3 += ahx2.uW();
        }
        if (ahx3 != null) {
            if (!(ahx3.uV() > d2)) return false;
            ArrayList<Vec3> arrayList = new ArrayList<Vec3>(ahx2.uQ());
            arrayList.add(ahy2);
            ahx3.b(ahy2);
            ahx3.a(ahx2);
            ahx3.e(arrayList);
            ahx3.Y(ahy2.c(this.aNR));
            ahx3.Z(d2);
            ahx3.aa(d3);
            return false;
        }
        if (!(ahy2.getX() == this.aNR.getX() && ahy2.getY() == this.aNR.getY() && ahy2.getZ() == this.aNR.getZ() || ahy2.c(this.aNR) <= 9.5)) {
            ArrayList<Vec3> arrayList = new ArrayList<Vec3>(ahx2.uQ());
            arrayList.add(ahy2);
            this.hubsToWork.add(new Node(ahy2, ahx2, arrayList, ahy2.c(this.aNR), d2, d3));
            return false;
        }
        if (ahx2 == null) return false;
        this.path.clear();
        this.path = ahx2.uQ();
        this.path.add(ahy2);
        return true;
    }
}
