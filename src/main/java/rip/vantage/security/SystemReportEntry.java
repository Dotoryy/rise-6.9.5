package rip.vantage.security;

public class SystemReportEntry {
    public int eSr;
    public String eSs;
    public String eSq;

    SystemReportEntry(String var1, int var2, String var3) {
        this.eSq = var1;
        this.eSr = var2;
        this.eSs = var3;
    }
}
