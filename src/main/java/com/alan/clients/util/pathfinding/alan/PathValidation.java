package com.alan.clients.util.pathfinding.alan;

import com.alan.clients.util.pathfinding.alan.Pathfinder;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.pathfinding.alan.api.Path;
import com.alan.clients.util.pathfinding.alan.api.Point;
import java.util.Arrays;
import java.util.function.Function;
import net.minecraft.block.material.Material;
import net.minecraft.util.Tuple;

public enum PathValidation {
    COLLISIONS(var0 -> PlayerUtil.a(var0.getFirst().get(var0.getFirst().size() - 1)).getMaterial() == Material.air),
    LEGIT(var0 -> {
        Point ahs = var0.getFirst().get(var0.getFirst().size() - 1);
        return PlayerUtil.a(ahs.add(0, -1, 0)).getMaterial() != Material.air || PlayerUtil.a(ahs.add(0, -2, 0)).getMaterial() != Material.air;
    });

    final Function<Tuple<Path, Pathfinder>, Boolean> aNJ;
    private static final PathValidation[] $VALUES = uP();

    PathValidation(Function<Tuple<Path, Pathfinder>, Boolean> function) {
        this.aNJ = function;
    }

    public static boolean a(Path var0, Pathfinder var1, PathValidation... var2) {
        return Arrays.stream(var2).allMatch(var2x -> var2x.aNJ.apply(new Tuple<>(var0, var1)));
    }

    private static PathValidation[] uP() {
        return new PathValidation[]{COLLISIONS, LEGIT};
    }
}
