package rip.vantage.commons.util.time;

public class StopWatch {
    public long millis;

    public StopWatch() {
        this.aX();
    }

    public boolean T(long var1) {
        return System.currentTimeMillis() - var1 >= this.millis;
    }

    public void aX() {
        this.millis = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - this.millis;
    }

    public void setMillis(long var1) {
        this.millis = var1;
    }
}
