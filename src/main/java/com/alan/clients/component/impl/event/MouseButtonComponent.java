package com.alan.clients.component.impl.event;

import com.alan.clients.Client;
import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.input.MouseButtonEvent;
import org.lwjgl.input.Mouse;

public class MouseButtonComponent extends Component {
    int[] bF = new int[]{0, 1, 2, 3, 4, 5};
    boolean[] bG = new boolean[]{false, false, false, false, false, false};
    @EventLink(value = 0)
    public final Listener<PreMotionEvent> onPreMotion = var1 -> {
        for (int i : this.bF) {
            if (Mouse.isButtonDown(i)) {
                if (!this.bG[i]) {
                    Client.a.e().d(new MouseButtonEvent(i));
                }

                this.bG[i] = true;
            } else {
                this.bG[i] = false;
            }
        }
    };

    public MouseButtonComponent() {
    }
}
