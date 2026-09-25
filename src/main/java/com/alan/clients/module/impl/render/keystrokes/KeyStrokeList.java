package com.alan.clients.module.impl.render.keystrokes;

import com.alan.clients.module.impl.render.KeyStrokes;
import com.alan.clients.module.impl.render.keystrokes.KeyStroke;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.vector.Vector2f;
import java.util.ArrayList;

public class KeyStrokeList extends ArrayList<KeyStroke> {
    final KeyStrokes keyStrokes;

    public KeyStrokeList(KeyStrokes keyStrokes) {
        this.keyStrokes = keyStrokes;
        this.add(new KeyStroke(new Vector2f(25.0F, 0.0F), InstanceAccess.aEg.gameSettings.keyBindForward));
        this.add(new KeyStroke(new Vector2f(0.0F, 25.0F), InstanceAccess.aEg.gameSettings.keyBindLeft));
        this.add(new KeyStroke(new Vector2f(50.0F, 25.0F), InstanceAccess.aEg.gameSettings.keyBindRight));
        this.add(new KeyStroke(new Vector2f(25.0F, 25.0F), InstanceAccess.aEg.gameSettings.keyBindBack));
        if (this.keyStrokes.space.wo()) {
            this.add(new KeyStroke(new Vector2f(72.0F, 22.0F), new Vector2f(0.0F, 50.0F), "Space", InstanceAccess.aEg.gameSettings.keyBindJump));
        }
    }
}
