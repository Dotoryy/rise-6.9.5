package com.alan.clients.util.pathfinding.alan.api;

import com.alan.clients.util.pathfinding.alan.api.Point;
import java.util.ArrayList;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public class Path extends ArrayList<Point> {
    public Path(@NotNull Collection<? extends Point> var1) {
        super(var1);
    }

    public Path() {
    }
}
