package com.alan.clients.script.api;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GameSettingsAPI extends API {
    public GameSettingsAPI() {
    }

    public boolean isKeyBindForwardDown() {
        return MC.gameSettings.keyBindForward.isKeyDown();
    }

    public boolean isKeyBindBackDown() {
        return MC.gameSettings.keyBindBack.isKeyDown();
    }

    public boolean isKeyBindLeftDown() {
        return MC.gameSettings.keyBindLeft.isKeyDown();
    }

    public boolean isKeyBindRightDown() {
        return MC.gameSettings.keyBindRight.isKeyDown();
    }

    public boolean isKeyBindJumpDown() {
        return MC.gameSettings.keyBindJump.isKeyDown();
    }

    public boolean isKeyBindSneakDown() {
        return MC.gameSettings.keyBindSneak.isKeyDown();
    }

    public boolean isKeyBindAttackDown() {
        return MC.gameSettings.cgK.isKeyDown();
    }

    public boolean isKeyBindUseItemDown() {
        return MC.gameSettings.cgI.isKeyDown();
    }

    public boolean isKeyBindDropDown() {
        return MC.gameSettings.keyBindDrop.isKeyDown();
    }

    public boolean isKeyBindInventoryDown() {
        return MC.gameSettings.keyBindInventory.isKeyDown();
    }

    public boolean isKeyBindChatDown() {
        return MC.gameSettings.keyBindChat.isKeyDown();
    }

    public boolean isKeyBindPlayerListDown() {
        return MC.gameSettings.keyBindPlayerList.isKeyDown();
    }

    public boolean isKeyBindCommandDown() {
        return MC.gameSettings.cgO.isKeyDown();
    }

    public boolean isKeyBindScreenshotDown() {
        return MC.gameSettings.cgP.isKeyDown();
    }

    public boolean isKeyBindTogglePerspectiveDown() {
        return MC.gameSettings.cgQ.isKeyDown();
    }

    public boolean isKeyBindSmoothCameraDown() {
        return MC.gameSettings.cgR.isKeyDown();
    }

    public boolean isKeyBindFullscreenDown() {
        return MC.gameSettings.cgS.isKeyDown();
    }

    public boolean isKeyBindSpectatorOutlinesDown() {
        return MC.gameSettings.keyBindSpectatorOutlines.isKeyDown();
    }

    public void setKeyBindForward(boolean keyBindForward) {
        MC.gameSettings.keyBindForward.setPressed(keyBindForward);
    }

    public void setKeyBindBack(boolean keyBindBack) {
        MC.gameSettings.keyBindBack.setPressed(keyBindBack);
    }

    public void setKeyBindLeft(boolean keyBindLeft) {
        MC.gameSettings.keyBindLeft.setPressed(keyBindLeft);
    }

    public void setKeyBindRight(boolean keyBindRight) {
        MC.gameSettings.keyBindRight.setPressed(keyBindRight);
    }

    public void setKeyBindJump(boolean keyBindJump) {
        MC.gameSettings.keyBindJump.setPressed(keyBindJump);
    }

    public void setKeyBindSneak(boolean keyBindSneak) {
        MC.gameSettings.keyBindSneak.setPressed(keyBindSneak);
    }

    public void setKeyBindAttack(boolean keyBindAttack) {
        MC.gameSettings.cgK.setPressed(keyBindAttack);
    }

    public void setKeyBindUseItem(boolean keyBindUseItem) {
        MC.gameSettings.cgI.setPressed(keyBindUseItem);
    }

    public void setKeyBindDrop(boolean keyBindDrop) {
        MC.gameSettings.keyBindDrop.setPressed(keyBindDrop);
    }

    public void setKeyBindInventory(boolean keyBindInventory) {
        MC.gameSettings.keyBindInventory.setPressed(keyBindInventory);
    }

    public void setKeyBindChat(boolean keyBindChat) {
        MC.gameSettings.keyBindChat.setPressed(keyBindChat);
    }

    public void setKeyBindPlayerList(boolean keyBindPlayerList) {
        MC.gameSettings.keyBindPlayerList.setPressed(keyBindPlayerList);
    }

    public void setKeyBindCommand(boolean keyBindCommand) {
        MC.gameSettings.cgO.setPressed(keyBindCommand);
    }

    public void setKeyBindScreenshot(boolean keyBindScreenshot) {
        MC.gameSettings.cgP.setPressed(keyBindScreenshot);
    }

    public void setKeyBindTogglePerspective(boolean var1) {
        MC.gameSettings.cgQ.setPressed(var1);
    }

    public void setKeyBindSmoothCamera(boolean keyBindSmoothCamera) {
        MC.gameSettings.cgR.setPressed(keyBindSmoothCamera);
    }

    public void setKeyBindFullscreen(boolean keyBindFullscreen) {
        MC.gameSettings.cgS.setPressed(keyBindFullscreen);
    }

    public void setKeyBindSpectatorOutlines(boolean var1) {
        MC.gameSettings.keyBindSpectatorOutlines.setPressed(var1);
    }

    public boolean isKeyDown(int var1) {
        return Keyboard.isKeyDown(var1);
    }

    public boolean isMouseDown(int var1) {
        return Mouse.isButtonDown(var1);
    }
}
