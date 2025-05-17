package com.quackers.plugin.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class ServerListMixin {
    @Shadow
    protected MultiplayerServerListWidget serverListWidget;

    @Inject(method = "init", at = @At("TAIL"))
    private void addCopyIpButton(CallbackInfo ci) {
        MultiplayerScreen screen = (MultiplayerScreen) (Object) this;
        int backButtonX = screen.width / 2 - 100;
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
}
