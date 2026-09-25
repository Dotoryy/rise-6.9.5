package com.alan.clients.module.impl.render.chat;

import com.alan.clients.module.impl.render.chat.ChatImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ChatImageManager {
    private static final byte[] PNG_MAGIC = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
    private static final byte[] art = new byte[]{-1, -40, -1};
    private static final byte[] GIF87A_MAGIC = new byte[]{71, 73, 70, 56, 55, 97};
    private static final byte[] GIF89A_MAGIC = new byte[]{71, 73, 70, 56, 57, 97};
    private static final long DECODE_TIMEOUT_MS = 15000L;
    private static volatile boolean sandboxInitialized = false;
    private static final Pattern URL_PATTERN = Pattern.compile("(https?://[^\\s<>\\\"']+)", 2);
    private static final Pattern META_TAG_PATTERN = Pattern.compile("(?is)<meta\\b[^>]*>");
    private static final Pattern META_CONTENT_PATTERN = Pattern.compile("(?is)\\bcontent\\s*=\\s*[\"']([^\"']+)[\"']");
    private static final String[] IMAGE_EXTENSIONS = new String[]{".png", ".jpg", ".jpeg", ".gif", ".webp", ".bmp"};
    private static final String[] VIDEO_EXTENSIONS = new String[]{".mp4", ".webm", ".mov"};
    private static final int MAX_RESOLVE_DEPTH = 3;
    private static final int MIN_HEADER_BYTES = 8;
    private static final int MAX_DOWNLOAD_BYTES = 10485760;
    private static final int MAX_GIF_FRAMES = 200;
    private static final int MAX_IMAGE_DIMENSION = 4096;
    private static final String[] ALLOWED_DOMAINS = new String[]{
        "discord.com",
        "discordapp.com",
        "media.discordapp.net",
        "cdn.discordapp.com",
        "imgur.com",
        "i.imgur.com",
        "giphy.com",
        "media.giphy.com",
        "media0.giphy.com",
        "media1.giphy.com",
        "media2.giphy.com",
        "media3.giphy.com",
        "media4.giphy.com",
        "tenor.com",
        "media.tenor.com",
        "tenor.googleapis.com",
        "imgflip.com",
        "i.imgflip.com",
        "reddit.com",
        "i.redd.it",
        "preview.redd.it",
        "styles.redditmedia.com",
        "external-preview.redd.it",
        "twimg.com",
        "x.com",
        "gyazo.com",
        "i.gyazo.com",
        "scdn.co",
        "prnt.sc",
        "prntscr.com",
        "imgbox.com",
        "th.imgbox.com",
        "freeimagehost.com",
        "servimg.com",
        "tinypic.com",
        "tinypic.org",
        "streamable.com",
        "cloudinary.com",
        "res.cloudinary.com",
        "imgbb.com",
        "i.ibb.co",
        "postimages.org",
        "i.postimg.cc",
        "imagebb.com",
        "i.ibb.co",
        "swapzd.com",
        "lhosti.com",
        "ifunny.co",
        "pifunny.com",
        "meme-kingdom.com",
        "lomdei.com"
    };
    private final Map<String, ChatImage> images = new ConcurrentHashMap<>();
    private final Set<String> queuedUrls = ConcurrentHashMap.newKeySet();
    private final ExecutorService downloadExecutor = Executors.newFixedThreadPool(3);
    private static final ExecutorService decodeExecutor = Executors.newCachedThreadPool(var0 -> {
        Thread thread = new Thread(var0, "ChatImage-Decode");
        thread.setDaemon(true);
        thread.setPriority(1);
        return thread;
    });
    private final Minecraft mc = Minecraft.getMinecraft();

    public ChatImageManager() {
    }

    private static synchronized void initSandbox() {
        if (!sandboxInitialized) {
            sandboxInitialized = true;
            System.out.println("[ChatImage] Security sandbox initialized (format validation + timeout protection)");
        }
    }

    private static boolean decodeWithTimeout(byte[] var0, ImageFormat var1, String var2, DecodeState var3) {
        initSandbox();
        DecodedImage[] ayi = new DecodedImage[1];
        Exception[] aexception = new Exception[1];
        Callable callable = () -> {
            if (var1 == ImageFormat.GIF) {
                GifFrames yg = h(var0);
                return yg != null ? new DecodedImage(yg.images, yg.delays, yg.images.length) : null;
            }
            BufferedImage bufferedimage = ImageIO.read(new ByteArrayInputStream(var0));
            return bufferedimage != null ? new DecodedImage(new BufferedImage[]{bufferedimage}, new int[]{100}, 1) : null;
        };
        Future future = decodeExecutor.submit(callable);

        try {
            DecodedImage yi = (DecodedImage)future.get(15000L, TimeUnit.MILLISECONDS);
            if (yi == null) {
                return false;
            }

            var3.images = yi.frames;
            var3.delays = yi.delays;
            var3.frameCount = yi.frameCount;
            return true;
        } catch (TimeoutException timeoutexception) {
            future.cancel(true);
            System.out.println("[ChatImage] decode timed out for: " + var2);
            return false;
        } catch (Exception exception) {
            System.out.println("[ChatImage] decode threw: " + exception.getClass().getSimpleName() + ": " + exception.getMessage());
            return false;
        }
    }

    private static ImageFormat detectFormat(byte[] var0) {
        if (var0 == null || var0.length < 8) {
            return ImageFormat.UNKNOWN;
        } else if (startsWith(var0, PNG_MAGIC)) {
            return ImageFormat.PNG;
        } else if (startsWith(var0, art)) {
            return ImageFormat.JPEG;
        } else if (startsWith(var0, GIF87A_MAGIC) || startsWith(var0, GIF89A_MAGIC)) {
            return ImageFormat.GIF;
        } else if (var0.length >= 12
            && var0[0] == 82
            && var0[1] == 73
            && var0[2] == 70
            && var0[3] == 70
            && var0[8] == 87
            && var0[9] == 69
            && var0[10] == 66
            && var0[11] == 80) {
            return ImageFormat.WEBP;
        }
        return var0[0] == 66 && var0[1] == 77 ? ImageFormat.BMP : ImageFormat.UNKNOWN;
    }

    private static boolean startsWith(byte[] var0, byte[] var1) {
        if (var0.length < var1.length) {
            return false;
        }

        for (int i = 0; i < var1.length; i++) {
            if (var0[i] != var1[i]) {
                return false;
            }
        }

        return true;
    }

    private static boolean validateStructure(byte[] var0, ImageFormat var1) {
        switch (var1) {
            case PNG:
                return isValidPng(var0);
            case JPEG:
                return isValidJpeg(var0);
            case GIF:
                return isValidGif(var0);
            case BMP:
                return isValidBmp(var0);
            case WEBP:
                return isValidWebp(var0);
            default:
                return false;
        }
    }

    private static boolean isValidPng(byte[] var0) {
        if (var0.length < 29) {
            return false;
        }

        if (var0[12] == 73 && var0[13] == 72 && var0[14] == 68 && var0[15] == 82) {
            int i = (var0[16] & 255) << 24 | (var0[17] & 255) << 16 | (var0[18] & 255) << 8 | var0[19] & 255;
            int j = (var0[20] & 255) << 24 | (var0[21] & 255) << 16 | (var0[22] & 255) << 8 | var0[23] & 255;
            if (i > 0 && j > 0 && i <= 4096 && j <= 4096) {
                int k = var0[25] & 255;
                if (k != 3 && k != 4) {
                    boolean flag = false;
                    boolean flag1 = false;
                    int l = 8;

                    while (l + 12 <= var0.length) {
                        int i1 = (var0[l] & 255) << 24 | (var0[l + 1] & 255) << 16 | (var0[l + 2] & 255) << 8 | var0[l + 3] & 255;
                        if (l + 12 + i1 > var0.length) {
                            break;
                        }

                        String s = new String(var0, l + 4, 4, StandardCharsets.US_ASCII);
                        if ("IDAT".equals(s)) {
                            flag = true;
                        }

                        if ("IEND".equals(s)) {
                            flag1 = true;
                            break;
                        }

                        int j1 = l + 12 + i1;
                        if (j1 < l) {
                            break;
                        }

                        l = j1;
                    }

                    return flag && flag1;
                }
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean isValidJpeg(byte[] var0) {
        if (var0.length >= 2 && var0[0] == -1 && var0[1] == -40) {
            boolean flag = false;
            int i = 2;

            label63:
            while (true) {
                while (i < var0.length - 1) {
                    if (var0[i] != -1) {
                        i++;
                        continue label63;
                    }

                    byte b0 = var0[i + 1];
                    if (b0 == -1) {
                        i++;
                    } else {
                        if (b0 != -64 && b0 != -63 && b0 != -62) {
                            if (b0 != -38 && i + 3 <= var0.length) {
                                int l = (var0[i + 2] & 255) << 8 | var0[i + 3] & 255;
                                int i1 = i + 2 + l;
                                if (i1 >= i && i1 <= var0.length) {
                                    i = i1;
                                    continue;
                                }
                            }
                            break;
                        }

                        if (i + 9 > var0.length) {
                            return false;
                        }

                        flag = true;
                        int j = (var0[i + 5] & 255) << 8 | var0[i + 6] & 255;
                        int k = (var0[i + 7] & 255) << 8 | var0[i + 8] & 255;
                        if (k <= 0 || j <= 0 || k > 4096 || j > 4096) {
                            return false;
                        }
                        break;
                    }
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    private static boolean isValidGif(byte[] var0) {
        if (var0.length < 14) {
            return false;
        }

        if (!new String(var0, 0, 6, StandardCharsets.US_ASCII).startsWith("GIF8")) {
            return false;
        }

        int i = var0[6] & 255 | (var0[7] & 255) << 8;
        int j = var0[8] & 255 | (var0[9] & 255) << 8;
        if (i > 0 && j > 0 && i <= 4096 && j <= 4096) {
            int k = 0;
            int l = 13;
            int i1 = var0[10] & 255;
            if ((i1 & 128) != 0) {
                int j1 = 3 * (1 << (i1 & 7) + 1);
                if (l + j1 < l || l + j1 > var0.length) {
                    return false;
                }

                l += j1;
            }

            while (l < var0.length - 1 && k <= 200) {
                int k1 = var0[l] & 255;
                if (k1 == 33) {
                    if (l + 1 >= var0.length) {
                        break;
                    }

                    l += 2;

                    while (l < var0.length) {
                        int l1 = var0[l] & 255;
                        if (l1 == 0) {
                            l++;
                            break;
                        }

                        l += 1 + l1;
                        if (l > var0.length) {
                            break;
                        }
                    }
                } else if (k1 == 44) {
                    k++;
                    l += 10;
                    if (l < var0.length) {
                        l++;
                    }

                    while (l < var0.length) {
                        int i2 = var0[l] & 255;
                        if (i2 == 0) {
                            l++;
                            break;
                        }

                        l += 1 + i2;
                        if (l > var0.length) {
                            break;
                        }
                    }
                } else {
                    if (k1 == 59) {
                        break;
                    }

                    l++;
                }
            }

            return k > 0 && k <= 200;
        }
        return false;
    }

    private static boolean isValidWebp(byte[] var0) {
        if (var0.length < 30) {
            return false;
        }

        if (var0[0] == 82 && var0[1] == 73 && var0[2] == 70 && var0[3] == 70) {
            if (var0[8] == 87 && var0[9] == 69 && var0[10] == 66 && var0[11] == 80) {
                int i = 12;
                boolean flag = false;
                int j = 0;
                int k = 0;

                while (i + 8 <= var0.length) {
                    int l = var0[i + 4] & 255 | (var0[i + 5] & 255) << 8 | (var0[i + 6] & 255) << 16 | (var0[i + 7] & 255) << 24;
                    String s = new String(var0, i, 4, StandardCharsets.US_ASCII);
                    if ("VP8 ".equals(s) && i + 14 <= var0.length) {
                        flag = true;
                        j = var0[i + 10] & 255 | (var0[i + 11] & 255) << 8;
                        k = var0[i + 12] & 255 | (var0[i + 13] & 255) << 8;
                    } else if ("VP8L".equals(s) && i + 14 <= var0.length) {
                        flag = true;
                        if ((var0[i + 8] & 255 | (var0[i + 9] & 255) << 8) == 47) {
                            int i1 = var0[i + 10] & 255 | (var0[i + 11] & 255) << 8;
                            int j1 = var0[i + 12] & 255 | (var0[i + 13] & 255) << 8;
                            j = (i1 & 16383) + 1;
                            k = (j1 & 16383) + 1;
                        }
                    } else if ("VP8X".equals(s) && i + 18 <= var0.length) {
                        flag = true;
                        j = (var0[i + 12] & 255 | (var0[i + 13] & 255) << 8 | (var0[i + 14] & 255) << 16) + 1;
                        k = (var0[i + 15] & 255 | (var0[i + 16] & 255) << 8 | (var0[i + 17] & 255) << 16) + 1;
                    }

                    i += 8 + l;
                    if (l % 2 != 0) {
                        i++;
                    }
                }

                return !flag ? false : j > 0 && k > 0 && j <= 4096 && k <= 4096;
            }
            return false;
        } else {
            return false;
        }
    }

    private static boolean isValidBmp(byte[] var0) {
        if (var0.length >= 54 && var0[0] == 66 && var0[1] == 77) {
            int i;
            int j;
            if ((var0[14] & 255 | (var0[15] & 255) << 8 | (var0[16] & 255) << 16 | (var0[17] & 255) << 24) == 12) {
                i = var0[18] & 255 | (var0[19] & 255) << 8;
                j = var0[20] & 255 | (var0[21] & 255) << 8;
            } else {
                i = var0[18] & 255 | (var0[19] & 255) << 8 | (var0[20] & 255) << 16 | (var0[21] & 255) << 24;
                j = Math.abs(var0[22] & 255 | (var0[23] & 255) << 8 | (var0[24] & 255) << 16 | (var0[25] & 255) << 24);
            }

            return i > 0 && j > 0 && i <= 4096 && j <= 4096;
        }
        return false;
    }

    private static GifFrames h(byte[] var0) throws java.io.IOException {
        int i = countGifFrames(var0);
        if (i > 0 && i <= 200) {
            ImageInputStream imageinputstream = ImageIO.createImageInputStream(new ByteArrayInputStream(var0));
            if (imageinputstream == null) {
                return null;
            }

            try {
                Iterator iterator = ImageIO.getImageReaders(imageinputstream);
                if (!iterator.hasNext()) {
                    return null;
                }

                ImageReader imagereader = (ImageReader)iterator.next();

                try {
                    imagereader.setInput(imageinputstream, false, false);
                    int j = imagereader.getNumImages(true);
                    if (j > 200) {
                        return null;
                    }

                    BufferedImage[] abufferedimage = new BufferedImage[j];
                    int[] aint = new int[j];

                    for (int k = 0; k < j; k++) {
                        abufferedimage[k] = imagereader.read(k);
                        if (!isValidSize(abufferedimage[k])) {
                            return null;
                        }

                        aint[k] = getFrameDelay(imagereader.getImageMetadata(k));
                    }

                    return new GifFrames(abufferedimage, aint);
                } finally {
                    imagereader.dispose();
                }
            } finally {
                imageinputstream.close();
            }
        } else {
            return null;
        }
    }

    private static int countGifFrames(byte[] var0) {
        int i = 0;
        int j = 13;
        int k = var0[10] & 255;
        if ((k & 128) != 0) {
            int l = 3 * (1 << (k & 7) + 1);
            if (j + l < j || j + l > var0.length) {
                return 0;
            }

            j += l;
        }

        while (j < var0.length - 1 && i <= 200) {
            int i1 = var0[j] & 255;
            if (i1 == 33) {
                if (j + 1 >= var0.length) {
                    break;
                }

                j += 2;

                while (j < var0.length) {
                    int j1 = var0[j] & 255;
                    if (j1 == 0) {
                        j++;
                        break;
                    }

                    j += 1 + j1;
                    if (j > var0.length) {
                        break;
                    }
                }
            } else if (i1 == 44) {
                i++;
                j += 10;
                if (j < var0.length) {
                    j++;
                }

                while (j < var0.length) {
                    int k1 = var0[j] & 255;
                    if (k1 == 0) {
                        j++;
                        break;
                    }

                    j += 1 + k1;
                    if (j > var0.length) {
                        break;
                    }
                }
            } else {
                if (i1 == 59) {
                    break;
                }

                j++;
            }
        }

        return i;
    }

    public List<String> extractUrls(String var1) {
        ArrayList arraylist = new ArrayList();
        Matcher matcher = URL_PATTERN.matcher(var1);

        while (matcher.find()) {
            String s = this.trimTrailingPunctuation(matcher.group(1));
            if (s != null && this.isHttpUrl(s)) {
                arraylist.add(s);
            }
        }

        return arraylist;
    }

    public boolean hasUrl(String var1) {
        return !this.extractUrls(var1).isEmpty();
    }

    public String getFirstUrl(String var1) {
        List list = this.extractUrls(var1);
        return list.isEmpty() ? null : (String)list.get(0);
    }

    public void queueImage(ChatImage var1) {
        String s = var1.getUrl();
        this.images.putIfAbsent(s, var1);
        if (!this.isSafeUrl(s)) {
            System.out.println("[ChatImage] blocked unsafe url " + s);
            this.markFailed(s);
        } else {
            if (this.queuedUrls.add(s)) {
                System.out.println("[ChatImage] queued " + s);
                this.downloadExecutor.submit(() -> this.resolveAndLoad(s, s, 0, new LinkedHashSet<>()));
            }
        }
    }

    private void resolveAndLoad(String var1, String var2, int var3, Set<String> var4) {
        if (var3 > 3 || !var4.add(var2)) {
            System.out.println("[ChatImage] stopping resolve depth for " + var2);
            if (var3 == 0) {
                this.markFailed(var1);
                this.queuedUrls.remove(var1);
            }
        } else if (!this.isSafeUrl(var2)) {
            System.out.println("[ChatImage] blocked unsafe target " + var2);
            if (var3 == 0) {
                this.markFailed(var1);
                this.queuedUrls.remove(var1);
            }
        } else {
            try {
                for (String s : this.getCandidates(var2)) {
                    if (this.isSafeUrl(s)) {
                        try {
                            System.out.println("[ChatImage] downloading " + s);
                            DownloadResult yjx = this.download(s);
                            if (yjx != null) {
                                System.out.println("[ChatImage] downloaded " + s + " status=" + yjx.statusCode + " contentType=" + yjx.contentType + " resolved=" + yjx.resolvedUrl);
                                if (this.tryDecode(var1, yjx)) {
                                    System.out.println("[ChatImage] decoded " + var1);
                                    return;
                                }

                                for (String s1 : this.getFallbackUrls(yjx.resolvedUrl, yjx.contentType)) {
                                    if (!this.isSafeUrl(s1)) {
                                        System.out.println("[ChatImage] blocked unsafe fallback " + s1);
                                    } else {
                                        System.out.println("[ChatImage] retrying decode fallback " + s1);
                                        DownloadResult yjx2 = this.download(s1);
                                        if (yjx2 != null) {
                                            System.out
                                                .println(
                                                    "[ChatImage] fallback downloaded "
                                                        + s1
                                                        + " status="
                                                        + yjx2.statusCode
                                                        + " contentType="
                                                        + yjx2.contentType
                                                        + " resolved="
                                                        + yjx2.resolvedUrl
                                                );
                                            if (this.tryDecode(var1, yjx2)) {
                                                System.out.println("[ChatImage] decoded via fallback " + var1);
                                                return;
                                            }
                                        }
                                    }
                                }

                                if (this.isHtml(yjx)) {
                                    List list = this.extractMetaUrls(yjx);
                                    System.out.println("[ChatImage] html media candidates=" + list);

                                    for (String s2 : (Iterable<String>)list) {
                                        this.resolveAndLoad(var1, s2, var3 + 1, var4);
                                        ChatImage ye = this.images.get(var1);
                                        if (ye != null && ye.isLoaded() && !ye.isFailed()) {
                                            return;
                                        }
                                    }
                                }

                                System.out.println("[ChatImage] decode failed " + var1);
                            }
                        } catch (Exception exception) {
                            System.out.println("[ChatImage] failed " + s + " " + exception.getClass().getSimpleName() + ": " + exception.getMessage());
                        }
                    } else {
                        System.out.println("[ChatImage] blocked unsafe candidate " + s);
                    }
                }

                System.out.println("[ChatImage] no candidate succeeded " + var1);
                if (var3 == 0) {
                    this.markFailed(var1);
                }
            } finally {
                if (var3 == 0) {
                    this.queuedUrls.remove(var1);
                }
            }
        }
    }

    private DownloadResult download(String var1) throws java.io.IOException {
        IOException ioexception = null;

        for (Proxy proxy : this.getProxies()) {
            HttpURLConnection httpurlconnection = null;

            try {
                httpurlconnection = (HttpURLConnection)new URL(var1).openConnection(proxy);
                httpurlconnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
                httpurlconnection.setConnectTimeout(10000);
                httpurlconnection.setReadTimeout(10000);
                httpurlconnection.setDoInput(true);
                httpurlconnection.connect();
                int i = httpurlconnection.getResponseCode();
                if (i / 100 == 2) {
                    try (
                        InputStream inputstream = httpurlconnection.getInputStream();
                        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                    ) {
                        byte[] abyte = new byte[8192];

                        int j;
                        while ((j = inputstream.read(abyte)) != -1) {
                            if (bytearrayoutputstream.size() > 10485760) {
                                throw new IOException("File too large");
                            }

                            bytearrayoutputstream.write(abyte, 0, j);
                        }

                        if (bytearrayoutputstream.size() > 10485760) {
                            throw new IOException("File too large");
                        }

                        return new DownloadResult(bytearrayoutputstream.toByteArray(), httpurlconnection.getURL().toString(), httpurlconnection.getContentType(), i);
                    }
                }
            } catch (IOException ioexception1) {
                ioexception = ioexception1;
            } finally {
                if (httpurlconnection != null) {
                    httpurlconnection.disconnect();
                }
            }
        }

        if (ioexception != null) {
            throw ioexception;
        }
        return null;
    }

    private boolean tryDecode(String var1, DownloadResult var2) {
        byte[] abyte = var2.data;
        ImageFormat yk = detectFormat(abyte);
        if (yk == yk.UNKNOWN) {
            System.out.println("[ChatImage] unknown format, no magic bytes match");
            return false;
        }

        if (!validateStructure(abyte, yk)) {
            System.out.println("[ChatImage] structure validation failed: " + yk);
            return false;
        }

        System.out.println("[ChatImage] format=" + yk + " validated, decoding with timeout...");
        DecodeState yh = new DecodeState();
        if (!decodeWithTimeout(abyte, yk, var1, yh)) {
            return false;
        }

        for (int i = 0; i < yh.frameCount; i++) {
            if (!isValidSize(yh.images[i])) {
                System.out.println("[ChatImage] post-decode dimension check failed");
                return false;
            }
        }

        ChatImage ye = this.images.get(var1);
        if (ye != null) {
            if (yh.frameCount == 1) {
                ye.setImage(yh.images[0]);
            } else {
                ye.a(yh.images, yh.delays);
            }
        }

        return true;
    }

    public void uploadTextures(ChatImage var1) {
        if (var1 != null && var1.needsUpload()) {
            synchronized (var1) {
                if (var1.needsUpload()) {
                    BufferedImage[] abufferedimage = var1.getFrames();
                    if (abufferedimage != null && abufferedimage.length != 0) {
                        int[] aint = new int[abufferedimage.length];

                        for (int i = 0; i < abufferedimage.length; i++) {
                            aint[i] = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), abufferedimage[i], true, true);
                        }

                        var1.setTextureIds(aint);
                        System.out.println("[ChatImage] uploaded " + aint.length + " texture(s) for " + var1.getUrl());
                    }
                }
            }
        }
    }

    private void markFailed(String var1) {
        ChatImage ye = this.images.get(var1);
        if (ye != null) {
            ye.markFailed();
        }

        System.out.println("[ChatImage] marked failed " + var1);
    }

    public ChatImage getImage(String var1) {
        return this.images.get(var1);
    }

    private String trimTrailingPunctuation(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            String s;
            for (s = var1.trim(); !s.isEmpty(); s = s.substring(0, s.length() - 1)) {
                char c0 = s.charAt(s.length() - 1);
                if (c0 != ')' && c0 != ']' && c0 != '}' && c0 != ',' && c0 != '.' && c0 != '!' && c0 != '?') {
                    break;
                }
            }

            return s.isEmpty() ? null : s;
        }
        return null;
    }

    private boolean isHttpUrl(String var1) {
        try {
            URI uri = URI.create(var1);
            String s = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase();
            return "http".equals(s) || "https".equals(s);
        } catch (IllegalArgumentException illegalargumentexception) {
            return false;
        }
    }

    private List<String> getCandidates(String var1) {
        LinkedHashSet linkedhashset = new LinkedHashSet();
        linkedhashset.add(var1);

        try {
            URI uri = URI.create(var1);
            String s = uri.getHost() == null ? "" : uri.getHost().toLowerCase();
            String path = uri.getPath();
            if ((s.endsWith("discordapp.com") || s.endsWith("discord.com")) && path != null && path.contains("/attachments/")) {
                linkedhashset.add("https://media.discordapp.net" + path);
                linkedhashset.add("https://cdn.discordapp.com" + path);
            }
        } catch (IllegalArgumentException illegalargumentexception) {
        }

        return new ArrayList<>(linkedhashset);
    }

    private List<String> getFallbackUrls(String var1, String var2) {
        LinkedHashSet linkedhashset = new LinkedHashSet();

        try {
            URI uri = URI.create(var1);
            String s = uri.getHost() == null ? "" : uri.getHost().toLowerCase();
            String path = uri.getPath();
            String rawQuery = this.stripQueryParams(uri.getRawQuery());
            boolean flag = var2 != null && var2.toLowerCase().contains("image/webp");
            boolean flag1 = s.endsWith("discordapp.net") && path != null && path.contains("/attachments/");
            if (flag && flag1) {
                linkedhashset.add(this.buildUrl("https://cdn.discordapp.com", path, rawQuery));
                linkedhashset.add(this.buildUrl("https://media.discordapp.net", path, rawQuery));
            }
        } catch (IllegalArgumentException illegalargumentexception) {
        }

        return new ArrayList<>(linkedhashset);
    }

    private String stripQueryParams(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            StringBuilder stringbuilder = new StringBuilder();

            for (String s : var1.split("&")) {
                if (s != null && !s.isEmpty()) {
                    int i = s.indexOf(61);
                    String s1 = i >= 0 ? s.substring(0, i) : s;
                    if (!"format".equalsIgnoreCase(s1) && !"quality".equalsIgnoreCase(s1) && !"width".equalsIgnoreCase(s1) && !"height".equalsIgnoreCase(s1)) {
                        if (stringbuilder.length() > 0) {
                            stringbuilder.append('&');
                        }

                        stringbuilder.append(s);
                    }
                }
            }

            return stringbuilder.length() == 0 ? null : stringbuilder.toString();
        }
        return null;
    }

    private String buildUrl(String var1, String var2, String var3) {
        return var3 != null && !var3.isEmpty() ? var1 + var2 + "?" + var3 : var1 + var2;
    }

    private List<Proxy> getProxies() {
        ArrayList arraylist = new ArrayList(1);
        Proxy proxy = this.mc.getProxy();
        if (proxy != null) {
            arraylist.add(proxy);
        } else {
            arraylist.add(Proxy.NO_PROXY);
        }

        return arraylist;
    }

    public void shutdown() {
        Iterator iterator = this.images.values().iterator();

        while (iterator.hasNext()) {
            for (int i : ((ChatImage)iterator.next()).getTextureIds()) {
                if (i >= 0) {
                    TextureUtil.deleteTexture(i);
                }
            }
        }

        this.images.clear();
        this.downloadExecutor.shutdown();
        decodeExecutor.shutdown();
    }

    private boolean isHtml(DownloadResult var1) {
        if (var1.contentType != null && var1.contentType.toLowerCase().contains("html")) {
            return true;
        }

        String s = new String(var1.data, 0, Math.min(var1.data.length, 256), StandardCharsets.UTF_8).toLowerCase();
        return s.contains("<html") || s.contains("<meta");
    }

    private List<String> extractMetaUrls(DownloadResult var1) {
        LinkedHashSet linkedhashset = new LinkedHashSet();
        String s = new String(var1.data, StandardCharsets.UTF_8);

        URI uri;
        try {
            uri = URI.create(var1.resolvedUrl);
        } catch (IllegalArgumentException illegalargumentexception) {
            return new ArrayList<>(linkedhashset);
        }

        Matcher matcher = META_TAG_PATTERN.matcher(s);

        while (matcher.find()) {
            String group = matcher.group();
            String s2 = group.toLowerCase();
            if (s2.contains("og:image")
                || s2.contains("twitter:image")
                || s2.contains("og:video")
                || s2.contains("twitter:player:stream")
                || s2.contains("twitter:image:src")) {
                Matcher matcher1 = META_CONTENT_PATTERN.matcher(group);
                if (matcher1.find()) {
                    this.addCandidate(linkedhashset, uri, matcher1.group(1));
                    if (linkedhashset.size() >= 8) {
                        break;
                    }
                }
            }
        }

        return new ArrayList<>(linkedhashset);
    }

    private void addCandidate(Set<String> var1, URI uri, String var3) {
        String s = this.trimTrailingPunctuation(var3);
        if (s != null) {
            try {
                String s1 = uri.resolve(s).toString();
                if (this.isImageUrl(s1) && this.isSafeUrl(s1)) {
                    var1.add(s1);
                }
            } catch (IllegalArgumentException illegalargumentexception) {
            }
        }
    }

    private boolean isImageUrl(String var1) {
        try {
            URI uri = URI.create(var1);
            String s = uri.getPath() == null ? "" : uri.getPath().toLowerCase();
            String s1 = uri.getHost() == null ? "" : uri.getHost().toLowerCase();

            for (String s2 : IMAGE_EXTENSIONS) {
                if (s.endsWith(s2)) {
                    return true;
                }
            }

            for (String s3 : VIDEO_EXTENSIONS) {
                if (s.endsWith(s3)) {
                    return true;
                }
            }

            return (s1.endsWith("discordapp.com") || s1.endsWith("discord.com") || s1.endsWith("discordapp.net")) && s.contains("/attachments/");
        } catch (IllegalArgumentException illegalargumentexception) {
            return false;
        }
    }

    private static int getFrameDelay(IIOMetadata iioMetadata) {
        if (iioMetadata == null) {
            return 100;
        }

        try {
            Node node = findNode(iioMetadata.getAsTree("javax_imageio_gif_image_1.0"), "GraphicControlExtension");
            if (node == null) {
                return 100;
            }

            NamedNodeMap namednodemap = node.getAttributes();
            Node node1 = namednodemap == null ? null : namednodemap.getNamedItem("delayTime");
            return node1 == null ? 100 : Math.max(Integer.parseInt(node1.getNodeValue()) * 10, 20);
        } catch (Exception exception) {
            return 100;
        }
    }

    private static Node findNode(Node var0, String var1) {
        if (var0 == null) {
            return null;
        }

        if (var1.equals(var0.getNodeName())) {
            return var0;
        }

        for (Node node = var0.getFirstChild(); node != null; node = node.getNextSibling()) {
            Node node1 = findNode(node, var1);
            if (node1 != null) {
                return node1;
            }
        }

        return null;
    }

    private boolean isSafeUrl(String var1) {
        try {
            URI uri = URI.create(var1);
            String s = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase();
            if (!"http".equals(s) && !"https".equals(s)) {
                return false;
            }
            String s1 = uri.getHost() == null ? "" : uri.getHost().toLowerCase();
            if (!isAllowedDomain(s1)) {
                System.out.println("[ChatImage] domain not in allowlist: " + s1);
                return false;
            }
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private static boolean isAllowedDomain(String var0) {
        if (var0 != null && !var0.isEmpty()) {
            for (String s : ALLOWED_DOMAINS) {
                if (var0.equals(s) || var0.endsWith("." + s)) {
                    return true;
                }
            }

            return false;
        }
        return false;
    }

    private static boolean isValidSize(BufferedImage image) {
        return image.getWidth() > 0 && image.getHeight() > 0 && image.getWidth() <= 4096 && image.getHeight() <= 4096;
    }
}
