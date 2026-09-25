package rip.vantage.runtime;

import java.net.InetAddress;

public class SocketEndpoint {
    public int eSg;
    public InetAddress eSf;

    SocketEndpoint(InetAddress inetAddress, int var2) {
        this.eSf = inetAddress;
        this.eSg = var2;
    }

    public boolean aLj() {
        return this.eSf.isAnyLocalAddress() || this.eSf.isLoopbackAddress() || this.eSf.isSiteLocalAddress() || this.eSf.isLinkLocalAddress();
    }

    public static SocketEndpoint kq(String var0) {
        String[] astring = var0.split(":");
        if (astring.length < 2) {
            return null;
        }

        String s = astring[0];
        int parseInt2 = Integer.parseInt(astring[1], 16);
        if (s.length() == 8) {
            Long olong = rip.vantage.util.NativeBridge.V(s, 16);
            int l2_hi = (int)((int)(olong & 255L));
            int i3_hi = (int)((int)(olong >> 8 & 255L));
            int j3_lo = (int)((int)(olong >> 16 & 255L));
            int k3_hi = (int)((int)(olong >> 24 & 255L));
            InetAddress inetaddress1 = rip.vantage.util.NativeBridge.v(new byte[]{(byte)l2_hi, (byte)i3_hi, (byte)j3_lo, (byte)k3_hi});
            return inetaddress1 != null ? new SocketEndpoint(inetaddress1, parseInt2) : null;
        }

        if (s.length() != 32) {
            return null;
        }

        byte[] abyte = new byte[16];
        int i2_lo = 0;

        while (i2_lo < 16) {
            int j2_hi = (15 - i2_lo) * 2;
            abyte[i2_lo] = (byte)Integer.parseInt(s.substring(j2_hi, j2_hi + 2), 16);
            i2_lo = i2_lo + 1;
        }

        InetAddress inetaddress = rip.vantage.util.NativeBridge.v(abyte);
        return inetaddress != null ? new SocketEndpoint(inetaddress, parseInt2) : null;
    }


    static {
    }

    public static SocketEndpoint kp(String var0) {
        String s = var0.trim();
        String s1;
        int indexOf3;
        if (s.startsWith("[")) {
            int indexOf2 = s.indexOf(93);
            s1 = s.substring(1, indexOf2);
            indexOf3 = Integer.parseInt(s.substring(s.indexOf(58, indexOf2) + 1));
        } else {
            int lastIndexOf2 = s.lastIndexOf(58);
            if (lastIndexOf2 <= 0) {
                return null;
            }

            s1 = s.substring(0, lastIndexOf2);
            indexOf3 = Integer.parseInt(s.substring(lastIndexOf2 + 1));
        }

        InetAddress inetaddress = rip.vantage.util.NativeBridge.kO(s1);
        return inetaddress != null ? new SocketEndpoint(inetaddress, indexOf3) : null;
    }
}
