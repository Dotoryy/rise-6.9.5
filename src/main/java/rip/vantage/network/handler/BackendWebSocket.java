package rip.vantage.network.handler;

import java.net.URI;
import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;
import javax.net.ssl.HttpsURLConnection;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.json.JSONObject;

@ClientEndpoint(configurator = WebSocketConfigurator.class)
public class BackendWebSocket {
    private rip.vantage.network.core.VantageNetwork eRA;
    private Session eRB;
    public static rip.vantage.commons.util.time.StopWatch eRC;
    private static volatile long eRD;
    private static volatile long eRE;
    private static volatile long eRF;
    private static SecureRandom eRG;
    private volatile byte[] eRH = null;
    private volatile byte[] eRI = null;
    private volatile boolean eRJ = false;
    private volatile CountDownLatch eRK = new CountDownLatch(1);
    private static final long eRL = 2000L;
    private static volatile boolean eRM;

    public BackendWebSocket() {
    }

    @OnOpen
    public void onOpen(Session session) {
    }

    @OnMessage
    public void onMessage(String var1) {
    }

    public void aKO() {
    }

    public void connect() {
    }

    private URI aKP() {
        return null;
    }

    private void b(URI uri) {
    }

    private void aKQ() {
    }

    private void kl(String var1) {
    }

    private boolean km(String var1) {
        return false;
    }

    private void aKR() {
    }

    public void sendMessage(String var1) {
    }

    public void o(byte[] var1) {
    }

    private boolean b(JSONObject json) {
        return false;
    }

    private void aKS() {
    }

    public void close() {
    }

    public Session aKT() {
        return null;
    }

    private static void aKU() {
    }

    private static boolean aKV() {
        return false;
    }

    private void aKW() {
    }

    public static long aKX() {
        return 0L;
    }

    public static long aKY() {
        return 0L;
    }

    public static void aKZ() {
    }

    private static void aLa() {
    }

    private void a(boolean var1, byte[] var2, byte[] var3) {
    }

    private void a(boolean var1, byte[] var2, String var3) {
    }

    private static void a(HttpsURLConnection connection) {
    }

    private void a(WebSocketContainer webSocketContainer) {
    }

    private void b(WebSocketContainer webSocketContainer) {
    }

    private static void kn(String var0) {
    }

    private void aLb() {
    }
}
