package rip.vantage.util;

import com.alan.clients.module.Module;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URI;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.json.JSONObject;

public class NativeBridge {
    public NativeBridge() {
    }

    public static boolean a(SecurityManager securityManager, int var1) {
        return false;
    }

    public static boolean b(Class<?> type, String var1, Class<?>... var2) {
        return false;
    }

    public static void kF(int var0) {
    }

    public static void kG(int var0) {
    }

    public static Map<String, String> aMV() {
        return null;
    }

    public static List<String> aMW() {
        return null;
    }

    public static Class<?> kL(String var0) {
        return null;
    }

    public static Object v(Class<?> type) {
        return null;
    }

    public static List<String> H(Object var0) {
        return null;
    }

    public static void aMI() {
    }

    public static void k(long var0) {
    }

    public static JSONObject kM(String var0) {
        return null;
    }

    public static InetAddress[] kN(String var0) {
        return null;
    }

    public static InetAddress kO(String var0) {
        return null;
    }

    public static HttpsURLConnection kP(String var0) {
        return null;
    }

    public static void d(HttpsURLConnection connection) {
    }

    public static int e(HttpsURLConnection connection) {
        return 0;
    }

    public static boolean f(HttpsURLConnection connection) {
        return false;
    }

    public static boolean l(String var0, int var1, int var2) {
        return false;
    }

    public static int m(String var0, int var1, int var2) {
        return 0;
    }

    public static byte[] c(Path path) {
        return null;
    }

    public static List<String> d(Path path) {
        return null;
    }

    public static void a(WebSocketContainer webSocketContainer, Object var1, URI uri) {
    }

    public static void b(Session session) {
    }

    public static byte[] t(byte[] var0) {
        return null;
    }

    public static void aMX() {
    }

    public static void aMY() {
    }

    public static void aMZ() {
    }

    public static void a(Object var0, Class<?> type, Object var2) {
    }

    public static void kQ(String var0) {
    }

    public static MessageDigest kR(String var0) {
        return null;
    }

    public static ByteArrayOutputStream aNa() {
        return null;
    }

    public static DataOutputStream a(OutputStream out) {
        return null;
    }

    public static byte[] a(MessageDigest messageDigest, byte[] var1) {
        return null;
    }

    public static Constructor<?>[] w(Class<?> type) {
        return null;
    }

    public static Method[] x(Class<?> type) {
        return null;
    }

    public static Field[] y(Class<?> type) {
        return null;
    }

    public static Object a(Field field, Object var1) {
        return null;
    }

    public static void a(DataOutputStream dataOutputStream, boolean var1) {
    }

    public static void a(DataOutputStream dataOutputStream, int var1) {
    }

    public static void a(DataOutputStream dataOutputStream, long var1) {
    }

    public static void a(DataOutputStream dataOutputStream, double var1) {
    }

    public static void a(DataOutputStream dataOutputStream, float var1) {
    }

    public static void a(DataOutputStream dataOutputStream, byte[] var1) {
    }

    public static String aM(String var0, String var1) {
        return null;
    }

    public static long aNb() {
        return 0L;
    }

    public static Process t(String... var0) {
        return null;
    }

    public static BufferedReader o(InputStream in) {
        return null;
    }

    public static String a(BufferedReader reader) {
        return null;
    }

    public static void c(Reader reader) {
    }

    public static void a(Writer writer) {
    }

    public static InputStream h(Class<?> type, String var1) {
        return null;
    }

    public static int a(InputStream in, byte[] var1) {
        return 0;
    }

    public static void p(InputStream in) {
    }

    public static void e(Path path) {
    }

    public static BufferedWriter f(Path path) {
        return null;
    }

    public static void a(BufferedWriter writer, String var1) {
    }

    public static BufferedReader g(Path path) {
        return null;
    }

    public static void a(Path path, Path var1) {
    }

    public static boolean h(Path path) {
        return false;
    }

    public static void a(MessageDigest messageDigest, byte[] var1, int var2, int var3) {
    }

    public static String u(byte[] var0) {
        return null;
    }

    public static byte[] kS(String var0) {
        return null;
    }

    public static byte[] i(Class<?> type, String var1) {
        return null;
    }

    public static Certificate[] g(HttpsURLConnection connection) {
        return null;
    }

    public static Method a(Class<?> type, String var1, Class<?>... var2) {
        return null;
    }

    public static void a(Path path, byte[] var1) {
    }

    public static void a(Path path, String var1, Object var2) {
    }

    public static void a(Path path, Iterable<? extends CharSequence> var1) {
    }

    public static InetAddress v(byte[] var0) {
        return null;
    }

    public static String[] n(Module module) {
        return null;
    }

    public static Collection<StackTraceElement[]> aNc() {
        return null;
    }

    public static String u(String... var0) {
        return null;
    }

    public static String[] v(String... var0) {
        return null;
    }

    public static String kT(String var0) {
        return null;
    }

    public static long V(String var0, int var1) {
        return 0L;
    }

    public static Class<?> a(Method method) {
        return null;
    }

    public static void a(Task var0) {
    }

    public static String aNd() {
        return null;
    }

    public static String kU(String var0) {
        return null;
    }

    public static void aN(String var0, String var1) {
    }

    public static String aNe() {
        return null;
    }

    public static String aNf() {
        return null;
    }

    public static String aNg() {
        return null;
    }

    private static String w(byte[] var0) {
        return null;
    }

    public static String aNh() {
        return null;
    }

    public static String aNi() {
        return null;
    }

    public static String aNj() {
        return null;
    }

    public static int aou() {
        return 0;
    }

    public static long aNk() {
        return 0L;
    }

    public static long aNl() {
        return 0L;
    }

    public static String aNm() {
        return null;
    }

    public static String aNn() {
        return null;
    }

    public static String aNo() {
        return null;
    }

    public static String aNp() {
        return null;
    }

    public static int aNq() {
        return 0;
    }

    public static String aNr() {
        return null;
    }

    public static int aNs() {
        return 0;
    }

    public static int aNt() {
        return 0;
    }

    public static String aNu() {
        return null;
    }

    public static NetworkInterfaceInfo aNv() {
        return null;
    }

    public static String aO(String var0, String var1) {
        return null;
    }
}
