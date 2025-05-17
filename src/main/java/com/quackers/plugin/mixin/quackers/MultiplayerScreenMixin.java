package com.quackers.plugin.mixin.quackers;

import com.quackers.plugin.gui.screens.QuackersScreen;
import com.quackers.plugin.QuackersPlugin;
import com.quackers.plugin.mixin.IMultiplayerScreenAccessor;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
    @Unique
    private String text;
    @Unique
    private int textColor;
    @Shadow
    protected MultiplayerServerListWidget serverListWidget;

    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        textColor = Color.fromRGBA(255, 200, 0, 255);
        text = "[" +QuackersPlugin.NAME + " - " + QuackersPlugin.VER + "] (" + QuackersPlugin.ACCESS_TYPE + ")";
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        String displayText = text;
        int screenWidth = context.getScaledWindowWidth();
        int textWidth = mc.textRenderer.getWidth(displayText);
        int x = (screenWidth - textWidth) / 2;
        int y = 5;
        context.drawTextWithShadow(mc.textRenderer, text, x, y, textColor);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addCopyIpButton(CallbackInfo ci) {
        MultiplayerScreen screen = (MultiplayerScreen) (Object) this;
        int backButtonX = screen.width / 2 - 103;
        int backButtonWidth = 200;

        ButtonWidget copyIpButton = ButtonWidget.builder(
                Text.literal("Copy IP"),
                button -> {
                    MultiplayerServerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();
                    if (entry instanceof MultiplayerServerListWidget.ServerEntry serverEntry) {
                        ServerInfo server = serverEntry.getServer();
                        MinecraftClient.getInstance().keyboard.setClipboard(server.address);
                    }
                })
            .dimensions(backButtonX + backButtonWidth + 60, screen.height - 30, 75, 20)
            .build();

        ((IMultiplayerScreenAccessor) screen).invokeAddDrawableChild(copyIpButton);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addQuackersButton(CallbackInfo ci) {
        MultiplayerScreen screen = (MultiplayerScreen) (Object) this;
        int screenWidth = mc.getWindow().getScaledWidth();
        int screenHeight = mc.getWindow().getScaledHeight();

        int buttonWidth = 75;
        int buttonHeight = 20;
        int x = 10;
        int y = screenHeight - buttonHeight - 10;

        ButtonWidget quackersButton = ButtonWidget.builder(
                Text.literal("Quackers"),
                button -> {
                    assert client != null;
                    client.setScreen(new QuackersScreen());
                })
            .dimensions(x, y, buttonWidth, buttonHeight)
            .build();

        ((IMultiplayerScreenAccessor) screen).invokeAddDrawableChild(quackersButton);
    }
}
