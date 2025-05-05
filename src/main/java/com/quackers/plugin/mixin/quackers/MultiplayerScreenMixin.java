package com.quackers.plugin.mixin.quackers;

import com.quackers.plugin.gui.screens.QuackersScreen;
import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
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

    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        textColor = Color.fromRGBA(255, 200, 0, 255);
        text = "[" +QuackersPlugin.NAME + " - " + QuackersPlugin.VER + "] (" + QuackersPlugin.ACCESS_TYPE + ")";

        int x = 10;
        int y = this.height - 30;
        addDrawableChild(ButtonWidget.builder(Text.of(QuackersPlugin.NAME), button -> {
                client.setScreen(new QuackersScreen());
            })
            .position(x, y)
            .size(100, 20)
            .build());
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
}
