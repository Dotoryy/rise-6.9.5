package com.alan.clients.newevent.impl.input;

import com.alan.clients.newevent.Event;
import lombok.Generated;

public class InputStateEvent implements Event {
    private boolean jh;
    private boolean ji;
    private boolean cp;
    private boolean co;
    private boolean jump;
    private boolean jj;
    private boolean sprint;

    @Generated
    public boolean cR() {
        return this.jh;
    }

    @Generated
    public boolean cS() {
        return this.ji;
    }

    @Generated
    public boolean cT() {
        return this.cp;
    }

    @Generated
    public boolean cU() {
        return this.co;
    }

    @Generated
    public boolean isJump() {
        return this.jump;
    }

    @Generated
    public boolean cV() {
        return this.jj;
    }

    @Generated
    public boolean isSprint() {
        return this.sprint;
    }

    @Generated
    public void f(boolean var1) {
        this.jh = var1;
    }

    @Generated
    public void g(boolean var1) {
        this.ji = var1;
    }

    @Generated
    public void h(boolean var1) {
        this.cp = var1;
    }

    @Generated
    public void i(boolean var1) {
        this.co = var1;
    }

    @Generated
    public void setJump(boolean jump) {
        this.jump = jump;
    }

    @Generated
    public void j(boolean var1) {
        this.jj = var1;
    }

    @Generated
    public void setSprint(boolean sprint) {
        this.sprint = sprint;
    }

    @Generated
    public InputStateEvent(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
        this.jh = var1;
        this.ji = var2;
        this.cp = var3;
        this.co = var4;
        this.jump = var5;
        this.jj = var6;
        this.sprint = var7;
    }
}
