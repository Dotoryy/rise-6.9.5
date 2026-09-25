package com.alan.clients.util.pathfinding.unlegit;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
    public NodeComparator() {
    }

    public int a(Node var1, Node var2) {
        return (int)(var1.uU() + var1.uW() - (var2.uU() + var2.uW()));
    }

    public int compare(Node var1, Node var2) {
        return this.a(var1, var2);
    }
}
