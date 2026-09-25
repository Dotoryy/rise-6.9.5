package com.alan.clients.ui.menu.impl.account.impl;

import com.alan.clients.ui.menu.impl.account.AccountManagerScreen;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.alan.clients.ui.menu.MenuColors;
import com.alan.clients.ui.menu.component.button.MenuButton;
import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.account.auth.MicrosoftCookieAuth;
import com.alan.clients.util.account.auth.MSAAuthResult;
import com.alan.clients.util.account.localts.LocaltsApi;
import com.alan.clients.util.account.localts.LocaltsOrder;
import com.alan.clients.util.account.localts.LocaltsOrderPage;
import com.alan.clients.util.account.localts.LocaltsProduct;
import com.alan.clients.util.account.localts.LocaltsProducts;
import com.alan.clients.util.account.localts.LocaltsResult;
import com.alan.clients.util.account.localts.LocaltsPurchase;
import com.alan.clients.util.account.localts.LocaltsConfig;
import com.alan.clients.util.account.localts.LocaltsOrderStore;
import com.alan.clients.util.account.localts.LocaltsDelivery;
import com.alan.clients.util.web.CommunityChat;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.gui.textbox.TextAlign;
import com.alan.clients.util.gui.textbox.TextBox;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.shader.RiseShaders;
import com.alan.clients.util.shader.base.ShaderRenderType;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.awt.Desktop.Action;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import rip.vantage.commons.packet.impl.client.community.C2SPacketStoreDelivery;
import rip.vantage.network.core.VantageNetwork;

public class AddLocaltsScreen extends GuiScreen implements MenuColors, InstanceAccess {
    private static final String API_KEY_SETTINGS_URL = "https://localts.store/user/settings";
    private static final Font TITLE_FONT = FontManager.MAIN.a(36, FontWeight.BOLD);
    private static final Font INFO_FONT = FontManager.MAIN.a(16, FontWeight.REGULAR);
    private static final Font LABEL_FONT = FontManager.MAIN.a(14, FontWeight.BOLD);
    private static final Font TOOLTIP_FONT = FontManager.MAIN.a(12, FontWeight.REGULAR);
    private static final Font QUANTITY_FONT = FontManager.MAIN.a(18, FontWeight.REGULAR);
    private static final float CENTER_REF_HEIGHT = FontManager.MAIN.a(24, FontWeight.BOLD).height();
    private static final int PRODUCT_INDEX_NONE = -1;
    private static final int PRODUCTS_PER_PAGE = 4;
    private final MenuButton[] menuButtons = new MenuButton[4];
    private static volatile List<LocaltsProduct> products = Collections.emptyList();
    private static volatile int selectedProductIndex = -1;
    private static final Set<String> BUNDLED_PRODUCT_IMAGES = new HashSet<>(
        Arrays.asList(
            "6900ec1c8f13c00afab7c381",
            "6900ed898f13c00afab7c382",
            "6900eeb78f13c00afab7c383",
            "6900ef8e8f13c00afab7c384",
            "6900f0468f13c00afab7c385",
            "6900f0dd8f13c00afab7c386",
            "6900f1688f13c00afab7c387",
            "6900f29e8f13c00afab7c389",
            "6900f3538f13c00afab7c38a",
            "6904936dfa5bca3b23dfbc57",
            "69049723fa5bca3b23dfbc5b",
            "6904978cfa5bca3b23dfbc5c",
            "690497fdfa5bca3b23dfbc5d",
            "6936f0f50012d41ae47d5599",
            "69396133cf14434b3961e722",
            "6947421b5ed4b81132777692",
            "694768565ed4b811327781af",
            "6a245da8e2ec8410af11582c",
            "6a245e1fe2ec8410af11582d",
            "6a245f29e2ec8410af11582e",
            "6a246a3c62352652aae20bc6"
        )
    );
    private static TextBox apiKeyBox;
    private static TextBox quantityBox;
    private static GuiScreen reference;
    private Animation animation;
    private static volatile String statusMessage = "Loading Localts products...";
    private static volatile Color statusColor = Color.WHITE;
    private static volatile boolean isPurchasing;
    private static volatile String localtsUsername = "";
    private static volatile int localtsCredits = -1;
    private static boolean productModalOpen;
    private static int productPage;
    private static final Runnable API_KEY_BOX_RUNNABLE = () -> {
        apiKeyBox.setSelected(true);
        quantityBox.setSelected(false);
    };
    private static final Runnable QUANTITY_BOX_RUNNABLE = () -> {
        quantityBox.setSelected(true);
        apiKeyBox.setSelected(false);
    };
    private static final Runnable CANCEL_RUNNABLE = () -> aEg.displayGuiScreen(new AccountManagerScreen(reference));
    private static final Runnable OPEN_LATEST_ORDER_RUNNABLE = () -> new Thread(() -> {
        String s = apiKeyBox.text.trim();
        if (s.isEmpty()) {
            statusMessage = "Enter your Localts API key first";
            statusColor = Color.RED;
        } else {
            statusMessage = "Looking for your latest packaged Localts order...";
            statusColor = Color.YELLOW;
            LocaltsOrderPage aev = LocaltsApi.b(s, 0, 25);
            if (!aev.aFv) {
                statusMessage = aev.aFB;
                statusColor = Color.RED;
            } else {
                for (JsonElement jsonelement : aev.aFw) {
                    if (jsonelement.isJsonObject()) {
                        JsonObject jsonobject = jsonelement.getAsJsonObject();
                        if (jsonobject.has("id") && !jsonobject.get("id").isJsonNull()) {
                            LocaltsOrder aeu = LocaltsApi.z(s, jsonobject.get("id").getAsString());
                            if (aeu.aFp && "PACKAGED".equalsIgnoreCase(aeu.aFr)) {
                                saveAndOpenOrder(aeu);
                                return;
                            }
                        }
                    }
                }

                statusMessage = "No packaged Localts order is ready yet";
                statusColor = Color.YELLOW;
            }
        }
    }, "Localts latest order thread").start();
    private static final Runnable CYCLE_PRODUCT_RUNNABLE = () -> {
        if (products.isEmpty()) {
            refreshProducts(true);
        } else {
            selectedProductIndex = (selectedProductIndex + 1) % products.size();
            LocaltsConfig.bw(selectedProduct().aFC);
        }
    };
    private static final Runnable CHECK_STATUS_RUNNABLE = () -> new Thread(() -> {
        String s = apiKeyBox.text.trim();
        if (s.isEmpty()) {
            statusMessage = "Enter your Localts API key first";
            statusColor = Color.RED;
        } else {
            refreshAccountSummary(s, true);
        }
    }, "Localts status check thread").start();
    private static final Runnable CHECK_STOCK_RUNNABLE = () -> new Thread(() -> {
        statusMessage = "Refreshing Localts products...";
        statusColor = Color.YELLOW;
        refreshProducts(false);
        LocaltsProduct aew = selectedProduct();
        if (aew != null) {
            statusMessage = aew.aFD + " | Stock: " + aew.aFH + " | " + aew.aFG + " credits each";
            statusColor = aew.aFH > 0 ? Color.GREEN : Color.RED;
        }
    }, "Localts stock check thread").start();
    private static final Runnable PURCHASE_RUNNABLE = () -> {
        String s = apiKeyBox.text.trim();
        if (s.isEmpty()) {
            statusMessage = "Enter your Localts API key first";
            statusColor = Color.RED;
        } else {
            int i;
            try {
                i = Integer.parseInt(quantityBox.text.trim());
                if (i < 1) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException numberformatexception) {
                statusMessage = "Quantity must be a positive number";
                statusColor = Color.RED;
                return;
            }

            LocaltsProduct aew = selectedProduct();
            if (aew == null) {
                statusMessage = "Products are still loading; try again shortly";
                statusColor = Color.YELLOW;
            } else if (isPurchasing) {
                statusMessage = "A Localts purchase is already in progress";
                statusColor = Color.YELLOW;
            } else {
                LocaltsConfig.bv(s);
                new Thread(() -> purchase(s, aew, i), "Localts purchase thread").start();
            }
        }
    };
    private static final Runnable BACKGROUND_RUNNABLE = () -> {
        ScaledResolution scaledresolution = new ScaledResolution(aEg);
        RenderUtil.d(0.0, 0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), Color.BLACK);
    };

    private static float centeredTextY(double var0, double var2, Font var4) {
        return (float)(var0 + var2 / 2.0 - 4.0 * var4.height() / CENTER_REF_HEIGHT);
    }

    private static void purchase(String var0, LocaltsProduct var1, int var2) {
        isPurchasing = true;

        try {
            purchaseAndDeliver(var0, var1, var2);
        } finally {
            isPurchasing = false;
            refreshAccountSummary(var0, false);
        }
    }

    private static void purchaseAndDeliver(String var0, LocaltsProduct var1, int var2) {
        int i = var2;
        LocaltsDelivery afd = null;
        JsonArray jsonarray = new JsonArray();

        for (int j = 0; j < 2 && i > 0; j++) {
            int k = i;
            statusMessage = (j == 0 ? "Purchasing " : "Reordering ") + k + "x " + var1.aFD + "...";
            statusColor = Color.YELLOW;
            LocaltsResult aey = LocaltsApi.c(var0, var1.aFC, k);
            if (!aey.aFM) {
                statusMessage = "Purchase failed: " + aey.aFO;
                statusColor = Color.RED;
                return;
            }

            statusMessage = "Order " + aey.aFN + " is being packaged...";
            LocaltsOrder aeu = waitForPackage(var0, aey.aFN);
            if (!aeu.aFp) {
                statusMessage = "Order created, but could not be retrieved: " + aeu.aFu;
                statusColor = Color.YELLOW;
                return;
            }

            if (!"PACKAGED".equalsIgnoreCase(aeu.aFr)) {
                statusMessage = "Order " + aey.aFN + " is " + aeu.aFr + ". Check Localts orders shortly.";
                statusColor = Color.YELLOW;
                return;
            }

            LocaltsDelivery afdx = afd == null ? LocaltsOrderStore.a(aeu) : LocaltsOrderStore.a(aeu, afd.aGa);
            if (afdx == null) {
                statusMessage = "Order packaged, but saving it locally failed";
                statusColor = Color.YELLOW;
                return;
            }

            if (afd == null) {
                afd = afdx;
            }

            addDeliveredItems(aeu.aFt, jsonarray);
            if (afdx.aGb > 0) {
                sendLocaltsDeliveryPacket(var1, aey.aFN, k, j > 0);
            }

            i -= afdx.aGb;
            if (i <= 0) {
                openDelivery(afd, jsonarray);
                return;
            }

            if (j == 0) {
                statusMessage = "Order " + aey.aFN + " delivered " + afdx.aGb + " of " + var2 + ". Reordering " + i + " missing item(s)...";
                statusColor = Color.YELLOW;
            }
        }

        openDelivery(afd, jsonarray);
        statusMessage = "Only " + jsonarray.size() + " of " + var2 + " item(s) were delivered. No further replacement was purchased.";
        statusColor = Color.RED;
    }

    private static void addDeliveredItems(JsonArray var0, JsonArray var1) {
        for (JsonElement jsonelement : var0) {
            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject = jsonelement.getAsJsonObject();
                if (jsonobject.has("content") && !jsonobject.get("content").isJsonNull() && !jsonobject.get("content").getAsString().trim().isEmpty()) {
                    var1.add(jsonobject);
                }
            }
        }
    }

    private static void sendLocaltsDeliveryPacket(LocaltsProduct var0, String var1, int var2, boolean var3) {
        try {
            VantageNetwork aInstance = VantageNetwork.aKB();
            if (aInstance.aKK() == null || aInstance.bX() == null || aInstance.bX().trim().isEmpty()) {
                return;
            }

            long i = (long)var0.aFG * var2;
            int j = i > 2147483647L ? Integer.MAX_VALUE : (int)Math.max(0L, i);
            aInstance.aKK().sendMessage(new C2SPacketStoreDelivery(aInstance.bX(), var0.aFD, j, var2, var1, var3).aJk());
        } catch (Exception exception) {
        }
    }

    private static LocaltsOrder waitForPackage(String var0, String var1) {
        LocaltsOrder aeu = LocaltsApi.z(var0, var1);

        for (int i = 0; i < 60 && aeu.aFp && !"PACKAGED".equalsIgnoreCase(aeu.aFr); i++) {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException interruptedexception) {
                Thread.currentThread().interrupt();
                break;
            }

            aeu = LocaltsApi.z(var0, var1);
        }

        return aeu;
    }

    private static void saveAndOpenOrder(LocaltsOrder var0) {
        LocaltsDelivery afd = LocaltsOrderStore.a(var0);
        if (afd == null) {
            statusMessage = "Order packaged, but saving it locally failed";
            statusColor = Color.YELLOW;
        } else {
            JsonArray jsonarray = new JsonArray();
            addDeliveredItems(var0.aFt, jsonarray);
            openDelivery(afd, jsonarray);
        }
    }

    private static void openDelivery(LocaltsDelivery var0, JsonArray var1) {
        if (var0 != null) {
            MSAAuthResult aes = MicrosoftCookieAuth.a(var1);
            String s = aes.aFj;

            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(var0.aGa);
                }

                if (!aes.sr()) {
                    statusMessage = "Order saved, but automatic login failed. Opened delivery folder.";
                    statusColor = Color.YELLOW;
                } else {
                    statusMessage = "Logged in as " + s + " | added " + aes.aFk + " account(s) to the alt manager";
                    statusColor = Color.GREEN;
                }
            } catch (Exception exception) {
                statusMessage = !aes.sr()
                    ? "Order saved, but automatic login failed: " + var0.aGa.getAbsolutePath()
                    : "Logged in as " + s + " | added " + aes.aFk + " account(s). Order saved: " + var0.aGa.getAbsolutePath();
                statusColor = !aes.sr() ? Color.YELLOW : Color.GREEN;
            }
        }
    }

    private static void refreshProducts(boolean var0) {
        LocaltsProducts aex = LocaltsApi.ss();
        if (!aex.aFJ) {
            if (var0) {
                statusMessage = aex.aFL;
                statusColor = Color.RED;
            }
        } else {
            ArrayList arraylist = new ArrayList();

            for (LocaltsProduct aew : aex.aFK) {
                if (aew.aFH > 0 && (isNfa(aew) || isCookie(aew))) {
                    arraylist.add(aew);
                }
            }

            Collections.sort(arraylist, new AddLocaltsScreen$1());
            products = arraylist;
            String s = LocaltsConfig.sw();
            selectedProductIndex = -1;

            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).aFC.equals(s)) {
                    selectedProductIndex = i;
                    break;
                }
            }

            if (!products.isEmpty() && selectedProductIndex == -1) {
                selectedProductIndex = 0;
            }

            productPage = selectedProductIndex < 0 ? 0 : selectedProductIndex / 4;
            if (var0) {
                statusMessage = products.isEmpty()
                    ? "No in-stock NFA or Cookie products are currently available"
                    : "Loaded " + products.size() + " in-stock NFA/Cookie product(s)";
                statusColor = products.isEmpty() ? Color.YELLOW : Color.GREEN;
            }
        }
    }

    private static LocaltsProduct selectedProduct() {
        return selectedProductIndex >= 0 && selectedProductIndex < products.size() ? products.get(selectedProductIndex) : null;
    }

    private static void refreshAccountSummary(String var0, boolean var1) {
        LocaltsPurchase afa = LocaltsApi.bu(var0);
        if (afa.aFS) {
            localtsUsername = afa.aFT;
            localtsCredits = afa.aFU;
            if (var1) {
                statusMessage = "Localts: " + afa.aFT + " | Credits: " + afa.aFU;
                statusColor = Color.GREEN;
            }
        } else if (var1) {
            statusMessage = afa.aFV;
            statusColor = Color.RED;
        }
    }

    static int productPriority(LocaltsProduct var0) {
        if (isCookie(var0)) {
            return 0;
        }
        return isNfa(var0) ? 1 : 2;
    }

    private static boolean isNfa(LocaltsProduct var0) {
        String s = (var0.aFD + " " + var0.aFE + " " + var0.aFF).toLowerCase();
        return s.contains("nfa") || s.contains("non-full") || s.contains("refresh token");
    }

    private static boolean isCookie(LocaltsProduct var0) {
        return (var0.aFD + " " + var0.aFE + " " + var0.aFF).toLowerCase().contains("cookie");
    }

    public AddLocaltsScreen() {
        reference = this;
        LocaltsConfig.init();
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        RiseShaders.aPL.a(ShaderRenderType.OVERLAY, var3, null);
        this.b(ShaderQueueType.BLUR).c(BACKGROUND_RUNNABLE);
        this.b(ShaderQueueType.REGULAR).c(() -> {
            this.drawStorefront(var1, var2);
            apiKeyBox.draw();
        });
        MenuButton[] aadh = this.menuButtons;
        int i = aadh.length;

        for (int j = 0; j < i; j++) {
            aadh[j].draw(var1, var2, var3);
        }

        if (productModalOpen) {
            this.b(ShaderQueueType.REGULAR).c(() -> this.drawProductModal(var1, var2));
        }
    }

    @Override
    public void mouseClicked(int var1, int var2, int var3) {
        if (var3 == 0 && this.isHelpHovered(var1, var2)) {
            openApiDocs();
        } else if (productModalOpen) {
            this.handleModalClick(var1, var2, var3);
        } else {
            apiKeyBox.click(var1, var2, var3);
            if (!this.isPrimaryClick(var3) || !this.selectProductAt(var1, var2)) {
                if (this.isPrimaryClick(var3) && MouseUtil.isHovered(this.panelX() + 12, this.cardsY() + 230, 30.0, 18.0, var1, var2)) {
                    productPage = Math.max(0, productPage - 1);
                } else if (this.isPrimaryClick(var3) && MouseUtil.isHovered(this.panelX() + this.panelWidth() - 42, this.cardsY() + 230, 30.0, 18.0, var1, var2)) {
                    productPage = Math.min(this.maxProductPage(), productPage + 1);
                } else {
                    for (MenuButton menuButton : this.menuButtons) {
                        if (MouseUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.oM(), menuButton.da(), var1, var2)) {
                            menuButton.runAction();
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void keyTyped(char var1, int var2) {
        if (var2 == 1) {
            this.closeWithEscape();
        } else {
            if (apiKeyBox.isSelected()) {
                apiKeyBox.key(var1, var2);
            } else if (productModalOpen && quantityBox.isSelected()) {
                quantityBox.key(var1, var2);
            }
        }
    }

    private void closeWithEscape() {
        productModalOpen = false;
        quantityBox.setSelected(false);
        apiKeyBox.setSelected(false);
        aEg.displayGuiScreen(new AccountManagerScreen(reference));
    }

    @Override
    public void initGui() {
        String s = LocaltsConfig.sv();
        apiKeyBox = new TextBox(
            new Vector2d(this.width / 2, this.panelY() + 91), FontManager.MAIN.a(16, FontWeight.REGULAR), Color.WHITE, TextAlign.CENTER, s.isEmpty() ? "API Key" : s, 330.0F
        );
        if (!s.isEmpty()) {
            apiKeyBox.bW(s);
        }

        quantityBox = new TextBox(new Vector2d(this.width / 2, this.height / 2), QUANTITY_FONT, Color.WHITE, TextAlign.CENTER, "1", 42.0F);
        quantityBox.bW("1");
        float f = (this.panelWidth() - 42) / 4.0F;
        float f1 = this.cardsY() + 278;
        this.menuButtons[0] = new MenuTextButton(this.panelX() + 12, f1, f, 22.0, CHECK_STATUS_RUNNABLE, "Balance");
        this.menuButtons[1] = new MenuTextButton(this.panelX() + 18 + f, f1, f, 22.0, CHECK_STOCK_RUNNABLE, "Refresh");
        this.menuButtons[2] = new MenuTextButton(this.panelX() + 24 + f * 2.0F, f1, f, 22.0, OPEN_LATEST_ORDER_RUNNABLE, "Open Latest");
        this.menuButtons[3] = new MenuTextButton(this.panelX() + 30 + f * 3.0F, f1, f, 22.0, CANCEL_RUNNABLE, "Close");
        statusMessage = "Loading Localts products...";
        statusColor = Color.WHITE;
        isPurchasing = false;
        productModalOpen = false;
        productPage = 0;
        new Thread(() -> refreshProducts(true), "Localts product load thread").start();
        if (!s.isEmpty()) {
            new Thread(() -> refreshAccountSummary(s, false), "Localts account load thread").start();
        }
    }

    private boolean isHelpHovered(int var1, int var2) {
        return MouseUtil.isHovered(this.apiHelpX() - 12.0F, this.apiHelpY() - 12.0F, 24.0, 24.0, var1, var2);
    }

    private float apiHelpX() {
        return this.width / 2.0F + LABEL_FONT.getStringWidth("Localts API Key") / 2.0F + 12.0F;
    }

    private float apiHelpY() {
        return this.panelY() + 70 + LABEL_FONT.height() / 2.0F;
    }

    private boolean isPrimaryClick(int var1) {
        return var1 == 0 || var1 == 1;
    }

    private int panelWidth() {
        return Math.min(700, Math.max(440, this.width - 32));
    }

    private int panelHeight() {
        return Math.min(450, Math.max(425, this.height - 38));
    }

    private int panelX() {
        return this.width / 2 - this.panelWidth() / 2;
    }

    private int panelY() {
        return this.height / 2 - this.panelHeight() / 2;
    }

    private int cardsY() {
        return this.panelY() + 128;
    }

    private int cardWidth() {
        return (this.panelWidth() - 34) / 4;
    }

    private int maxProductPage() {
        return Math.max(0, (products.size() - 1) / 4);
    }

    private int cardsOnPage() {
        return Math.max(0, Math.min(4, products.size() - productPage * 4));
    }

    private int cardX(int var1) {
        int i = this.cardsOnPage();
        int j = i * this.cardWidth() + Math.max(0, i - 1) * 2;
        return this.panelX() + (this.panelWidth() - j) / 2 + var1 * (this.cardWidth() + 2);
    }

    private void drawStorefront(int var1, int var2) {
        int i = this.panelX();
        int j = this.panelY();
        int k = this.panelWidth();
        int l = this.panelHeight();
        RenderUtil.roundedRectangle(i, j, k, l, 12.0, new Color(19, 23, 34, 242));
        RenderUtil.roundedRectangle(i + 1, j + 1, k - 2, 112.0, 11.0, new Color(36, 43, 63, 245));
        TITLE_FONT.a("Localts Marketplace", i + 18, j + 16, Color.WHITE.getRGB());
        INFO_FONT.a("Choose an account type, view its preview, then buy inside Rise.", i + 19, j + 49, new Color(176, 187, 212).getRGB());
        if (localtsUsername.isEmpty()) {
            INFO_FONT.drawCenteredString("5% off in Rise", i + k - 18, j + 18, new Color(118, 221, 178).getRGB());
            TOOLTIP_FONT.drawCenteredString("In stock: NFA • Cookies", i + k - 18, j + 38, new Color(176, 187, 212).getRGB());
        } else {
            LABEL_FONT.drawCenteredString(localtsUsername, i + k - 18, j + 15, Color.WHITE.getRGB());
            INFO_FONT.drawCenteredString(localtsCredits < 0 ? "Checking credits..." : localtsCredits + " credits", i + k - 18, j + 36, new Color(118, 221, 178).getRGB());
            TOOLTIP_FONT.drawCenteredString("5% off in Rise", i + k - 18, j + 53, new Color(176, 187, 212).getRGB());
        }

        LABEL_FONT.drawString("Localts API Key", this.width / 2, j + 70, new Color(176, 187, 212).getRGB());
        RenderUtil.roundedRectangle(this.width / 2 - 176, j + 84, 352.0, 22.0, 7.0, new Color(9, 12, 20, 150));
        apiKeyBox.setPosition(new Vector2d(this.width / 2, centeredTextY(j + 84, 22.0, INFO_FONT)));
        float f = this.apiHelpX();
        float f1 = this.apiHelpY();
        RenderUtil.c(f, f1, 7.0, this.isHelpHovered(var1, var2) ? new Color(109, 160, 255) : new Color(92, 107, 141));
        TOOLTIP_FONT.drawString("?", f, centeredTextY(f1, 0.0, TOOLTIP_FONT), Color.WHITE.getRGB());
        if (products.isEmpty()) {
            INFO_FONT.drawString("Loading Localts products...", this.width / 2, this.cardsY() + 85, new Color(176, 187, 212).getRGB());
        } else {
            for (int j1 = 0; j1 < 4; j1++) {
                int k1 = productPage * 4 + j1;
                if (k1 >= products.size()) {
                    break;
                }

                this.drawProductCard(products.get(k1), k1, j1, var1, var2);
            }
        }

        int i1 = this.cardsY() + 230;
        float f2 = centeredTextY(i1, 18.0, INFO_FONT);
        RenderUtil.roundedRectangle(i + 12, i1, 30.0, 18.0, 6.0, new Color(49, 58, 84, 225));
        INFO_FONT.drawString("‹", i + 27, f2, Color.WHITE.getRGB());
        RenderUtil.roundedRectangle(i + k - 42, i1, 30.0, 18.0, 6.0, new Color(49, 58, 84, 225));
        INFO_FONT.drawString("›", i + k - 27, f2, Color.WHITE.getRGB());
        INFO_FONT.drawString(productPage + 1 + " / " + (this.maxProductPage() + 1), this.width / 2, f2, new Color(176, 187, 212).getRGB());
        INFO_FONT.drawString(statusMessage, this.width / 2, this.cardsY() + 256, statusColor.getRGB());
        if (this.isHelpHovered(var1, var2)) {
            float f3 = 198.0F;
            float f4 = Math.max(i + 12, Math.min(this.apiHelpX() - f3 / 2.0F, i + k - 12 - f3));
            float f5 = j + 109;
            RenderUtil.roundedRectangle(f4, f5, f3, 32.0, 6.0, new Color(9, 12, 20, 250));
            TOOLTIP_FONT.drawString("Opens Localts Settings to", f4 + f3 / 2.0F, f5 + 7.0F, Color.WHITE.getRGB());
            TOOLTIP_FONT.drawString("create or manage your API key", f4 + f3 / 2.0F, f5 + 19.0F, new Color(176, 187, 212).getRGB());
        }
    }

    private void drawProductCard(LocaltsProduct var1, int var2, int var3, int var4, int var5) {
        int i = this.cardX(var3);
        int j = this.cardsY();
        boolean flag = var2 == selectedProductIndex;
        boolean flag1 = MouseUtil.isHovered(i, j, this.cardWidth(), 220.0, var4, var5);
        Color color = flag ? new Color(50, 76, 111, 245) : (flag1 ? new Color(44, 52, 77, 245) : new Color(31, 37, 55, 245));
        RenderUtil.roundedRectangle(i, j, this.cardWidth(), 220.0, 9.0, color);
        int k = Math.min(this.cardWidth() - 10, 108);
        int l = i + this.cardWidth() / 2 - k / 2;
        RenderUtil.roundedRectangle(l, j + 5, k, k, 6.0, new Color(13, 16, 27, 230));
        ResourceLocation resourcelocation = this.productImage(var1);
        if (resourcelocation != null) {
            RenderUtil.image(resourcelocation, l, j + 5, k, k, Color.WHITE);
        } else {
            INFO_FONT.drawString(this.productTag(var1), i + this.cardWidth() / 2, j + 47, new Color(145, 164, 210).getRGB());
        }

        LABEL_FONT.a(shorten(var1.aFD, 22), i + 8, j + 122, Color.WHITE.getRGB());
        TOOLTIP_FONT.a(shorten(var1.aFE.isEmpty() ? var1.aFF : var1.aFE, 34), i + 8, j + 139, new Color(175, 185, 208).getRGB());
        this.drawTag(i + 8, j + 158, this.productTag(var1), this.productTagColor(var1));
        INFO_FONT.a(var1.aFG + " credits", i + 8, j + 186, new Color(118, 221, 178).getRGB());
        INFO_FONT.drawCenteredString(
            var1.aFH + " stock", i + this.cardWidth() - 8, j + 186, var1.aFH > 0 ? new Color(176, 187, 212).getRGB() : new Color(235, 125, 125).getRGB()
        );
        TOOLTIP_FONT.drawString("Click to view", i + this.cardWidth() / 2, j + 204, new Color(175, 185, 208).getRGB());
    }

    private void drawTag(float var1, float var2, String var3, Color color) {
        float f = TOOLTIP_FONT.getStringWidth(var3) + 20;
        RenderUtil.roundedRectangle(var1, var2, f, 15.0, 7.0, ColorUtil.withAlpha(color, 55));
        TOOLTIP_FONT.a(var3, var1 + 10.0F, centeredTextY(var2, 15.0, TOOLTIP_FONT), color.getRGB());
    }

    private String productTag(LocaltsProduct var1) {
        return isCookie(var1) ? "COOKIE" : (isNfa(var1) ? "NFA" : "MINECRAFT");
    }

    private Color productTagColor(LocaltsProduct var1) {
        return isCookie(var1) ? new Color(244, 184, 94) : (isNfa(var1) ? new Color(132, 166, 255) : new Color(154, 225, 175));
    }

    private ResourceLocation productImage(LocaltsProduct var1) {
        return BUNDLED_PRODUCT_IMAGES.contains(var1.aFC) ? new ResourceLocation("rise/images/localts_products/" + var1.aFC + ".png") : null;
    }

    private boolean selectProductAt(int var1, int var2) {
        for (int i = 0; i < 4; i++) {
            int j = productPage * 4 + i;
            if (j >= products.size()) {
                break;
            }

            if (MouseUtil.isHovered(this.cardX(i), this.cardsY(), this.cardWidth(), 220.0, var1, var2)) {
                selectedProductIndex = j;
                LocaltsConfig.bw(products.get(j).aFC);
                productModalOpen = true;
                apiKeyBox.setSelected(false);
                quantityBox.setSelected(false);
                return true;
            }
        }

        return false;
    }

    private void drawProductModal(int var1, int var2) {
        LocaltsProduct aew = selectedProduct();
        if (aew != null) {
            RenderUtil.d(0.0, 0.0, this.width, this.height, new Color(0, 0, 0, 150));
            int i = Math.min(570, this.width - 30);
            int j = Math.min(350, this.height - 30);
            int k = this.width / 2 - i / 2;
            int l = this.height / 2 - j / 2;
            RenderUtil.roundedRectangle(k, l, i, j, 12.0, new Color(28, 34, 50, 252));
            RenderUtil.roundedRectangle(k + 14, l + 14, 166.0, 166.0, 8.0, new Color(13, 16, 27, 235));
            ResourceLocation resourcelocation = this.productImage(aew);
            if (resourcelocation != null) {
                RenderUtil.image(resourcelocation, k + 14, l + 14, 166.0F, 166.0F, Color.WHITE);
            } else {
                INFO_FONT.drawString(this.productTag(aew), k + 97, l + 83, new Color(145, 164, 210).getRGB());
            }

            TITLE_FONT.a(shorten(aew.aFD, 28), k + 198, l + 21, Color.WHITE.getRGB());
            this.drawTag(k + 200, l + 63, this.productTag(aew), this.productTagColor(aew));
            INFO_FONT.a(
                aew.aFH + " in stock  •  Instant delivery",
                k + 198,
                l + 92,
                aew.aFH > 0 ? new Color(118, 221, 178).getRGB() : new Color(235, 125, 125).getRGB()
            );
            this.drawWrapped(
                aew.aFE.isEmpty() ? "Delivered automatically by Localts after purchase." : aew.aFE,
                k + 198,
                l + 120,
                i - 218,
                4,
                new Color(185, 195, 216).getRGB()
            );
            RenderUtil.roundedRectangle(k + 14, l + 199, i - 28, 1.0, 0.0, new Color(67, 78, 108, 180));
            int i1 = k + i / 2 - 54;
            int j1 = l + 213;
            LABEL_FONT.drawCenteredString("Quantity", i1 - 16, centeredTextY(j1, 25.0, LABEL_FONT), new Color(176, 187, 212).getRGB());
            RenderUtil.roundedRectangle(i1 + 31, j1, 54.0, 25.0, 6.0, new Color(13, 16, 27, 220));
            quantityBox.setPosition(new Vector2d(i1 + 58, centeredTextY(j1, 25.0, QUANTITY_FONT)));
            quantityBox.draw();
            RenderUtil.roundedRectangle(i1 + 90, j1, 26.0, 25.0, 6.0, new Color(49, 58, 84, 225));
            INFO_FONT.drawString("+", i1 + 103, centeredTextY(j1, 25.0, INFO_FONT), Color.WHITE.getRGB());
            RenderUtil.roundedRectangle(i1, j1, 26.0, 25.0, 6.0, new Color(49, 58, 84, 225));
            INFO_FONT.drawString("−", i1 + 13, centeredTextY(j1, 25.0, INFO_FONT), Color.WHITE.getRGB());
            INFO_FONT.drawString("Total: " + this.purchaseTotal(aew) + " credits", k + i / 2, l + 251, new Color(118, 221, 178).getRGB());
            RenderUtil.roundedRectangle(k + 14, l + j - 54, i - 28, 36.0, 7.0, aew.aFH > 0 ? new Color(62, 120, 98) : new Color(77, 59, 68));
            INFO_FONT.drawString(
                aew.aFH > 0 ? "Purchase " + this.purchaseTotal(aew) + " credits" : "Out of stock",
                this.width / 2,
                centeredTextY(l + j - 54, 36.0, INFO_FONT),
                Color.WHITE.getRGB()
            );
            INFO_FONT.drawString("×", k + i - 22, centeredTextY(l + 8, 28.0, INFO_FONT), Color.WHITE.getRGB());
            TOOLTIP_FONT.drawString("Click outside or × to close", k + i / 2, l + j - 74, new Color(146, 159, 190).getRGB());
        }
    }

    private void handleModalClick(int var1, int var2, int var3) {
        int i = Math.min(570, this.width - 30);
        int j = Math.min(350, this.height - 30);
        int k = this.width / 2 - i / 2;
        int l = this.height / 2 - j / 2;
        if (this.isPrimaryClick(var3)) {
            if (MouseUtil.isHovered(k, l, i, j, var1, var2) && !MouseUtil.isHovered(k + i - 36, l + 8, 28.0, 28.0, var1, var2)) {
                int i1 = k + i / 2 - 54;
                if (MouseUtil.isHovered(i1, l + 213, 26.0, 25.0, var1, var2)) {
                    this.setQuantity(Math.max(1, this.quantity() - 1));
                }

                if (MouseUtil.isHovered(i1 + 90, l + 213, 26.0, 25.0, var1, var2)) {
                    this.setQuantity(this.quantity() + 1);
                }

                quantityBox.click(var1, var2, var3);
                if (MouseUtil.isHovered(k + 14, l + j - 54, i - 28, 36.0, var1, var2)) {
                    LocaltsProduct aew = selectedProduct();
                    if (aew != null && aew.aFH > 0) {
                        productModalOpen = false;
                        quantityBox.setSelected(false);
                        PURCHASE_RUNNABLE.run();
                    }
                }
            } else {
                productModalOpen = false;
                quantityBox.setSelected(false);
            }
        }
    }

    private int quantity() {
        try {
            return Math.max(1, Integer.parseInt(quantityBox.text.trim()));
        } catch (Exception exception) {
            return 1;
        }
    }

    private void setQuantity(int var1) {
        quantityBox.bW(String.valueOf(var1));
        quantityBox.ar(quantityBox.text.length());
    }

    private int purchaseTotal(LocaltsProduct var1) {
        long i = (long)var1.aFG * this.quantity();
        return i > 2147483647L ? Integer.MAX_VALUE : (int)i;
    }

    private void drawWrapped(String var1, float var2, float var3, float var4, int var5, int var6) {
        String[] astring = var1.replace('\n', ' ').split("\\s+");
        String s = "";
        int i = 0;

        for (String s1 : astring) {
            String s2 = s.isEmpty() ? s1 : s + " " + s1;
            if (INFO_FONT.getStringWidth(s2) > var4 && !s.isEmpty()) {
                INFO_FONT.a(s, var2, var3 + i * 15, var6);
                s = s1;
                if (++i >= var5) {
                    return;
                }
            } else {
                s = s2;
            }
        }

        if (!s.isEmpty() && i < var5) {
            INFO_FONT.a(s, var2, var3 + i * 15, var6);
        }
    }

    private static void openApiDocs() {
        CommunityChat.A("api_settings", "https://localts.store/user/settings");

        try {
            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Action.BROWSE)) {
                throw new IOException("Browser opening is unavailable");
            }

            Desktop.getDesktop().browse(new URI("https://localts.store/user/settings"));
            statusMessage = "Opened Localts API-key settings in your browser";
            statusColor = Color.GREEN;
        } catch (Exception exception) {
            statusMessage = "Could not open Localts settings: https://localts.store/user/settings";
            statusColor = Color.YELLOW;
        }
    }

    private static String shorten(String var0, int var1) {
        return var0.length() <= var1 ? var0 : var0.substring(0, var1 - 3) + "...";
    }
}
