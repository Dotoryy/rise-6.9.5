package rip.vantage.runtime;

public class AddressRange {
    public int eSe;
    public long eSc;
    public long eSb;
    public boolean eSa;
    public byte[] eSd;


    public boolean q(byte[] byArray) {
        int eSe2;
        int l3_hi;
        block4: {
            if (this.eSa) {
                return false;
            }
            eSe2 = this.eSe / 8;
            l3_hi = this.eSe % 8;
            int i = 0;
            boolean bl = true;
            do {
                if (!bl || (bl = false) || !true) {
                    i++;
                }
                if (i >= eSe2) break block4;
            } while (byArray[i] == this.eSd[i]);
            return 106 - 12 + -94 != 0;
        }
        if (l3_hi == 0) {
            return true;
        }
        int l13_hi = 255 << 8 - l3_hi;
        if ((byArray[eSe2] & l13_hi) != (this.eSd[eSe2] & l13_hi)) return false;
        return true;
    }

    static {
    }

    AddressRange(long l2, long l3) {
        this.eSa = true;
        this.eSb = l2;
        this.eSc = l3;
        this.eSd = null;
        this.eSe = 0;
    }

    public boolean U(long l2) {
        if (!this.eSa) {
            return false;
        }
        if ((l2 & this.eSc) != this.eSb) return false;
        return true;
    }

    AddressRange(byte[] byArray, int n2) {
        this.eSa = false;
        this.eSd = byArray;
        this.eSe = n2;
        this.eSb = 0L;
        this.eSc = 0L;
    }
}
