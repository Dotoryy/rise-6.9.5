import com.alan.clients.Client;
import java.io.File;
import java.util.Arrays;
import net.minecraft.client.main.Main;

public class Start {
    public Start() {
    }

    public static void main(String[] var0) {
        String s = System.getProperty("user.home", ".");
        String s1 = System.getenv("APPDATA");
        String s2 = s1 != null ? s1 : s;
        //add code
        String gameDir = System.getProperty("rise.gameDir");
        File file1 = gameDir != null && !gameDir.isEmpty()
                ? new File(gameDir)
                : new File(s2, ".minecraft/");
        boolean flag = !System.getProperty("os.name").toLowerCase().contains("mac");
        Main.main(
            concat(
                new String[]{
                    "--version",
                    Client.b,
                    "--accessToken",
                    "0",
                    "--gameDir",
                    flag ? new File(file1, ".").getAbsolutePath() : "",
                    "--assetsDir",
                    flag ? new File(file1, "assets/").getAbsolutePath() : "assets",
                    "--assetIndex",
                    "1.8",
                    "--userProperties",
                    "{}"
                },
                var0
            )
        );
    }

    public static <T> T[] concat(T[] var0, T[] var1) {
        Object[] aobject = Arrays.copyOf(var0, var0.length + var1.length);
        System.arraycopy(var1, 0, aobject, var0.length, var1.length);
        return (T[])aobject;
    }
}
