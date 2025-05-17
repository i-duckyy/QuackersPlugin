package com.quackers.plugin.mixin.quackers;

import com.quackers.plugin.utils.ReconnectButtonWidget;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class ReconnectButtonMixin extends Screen {
    protected ReconnectButtonMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgets")
    private void addReconnectButton(CallbackInfo ci) {

        assert this.client != null;
        boolean inSingleplayer = this.client.isInSingleplayer();
        boolean inRealms = false;

        ServerInfo currentServer = this.client.getCurrentServerEntry();
        if (currentServer != null && currentServer.address != null && currentServer.isRealm()) {
            inRealms = true;
        }

        if (!inSingleplayer && !inRealms) {
            MutableText text = (MutableText) Text.of("<");
            this.addDrawableChild(new ReconnectButtonWidget(
                this.width / 2 - 102 + 208,
                this.height / 4 + 120 + -16,
                20,
                20,
                text,
                (button) -> {
                    assert currentServer != null;
                    ServerAddress serverIp = ServerAddress.parse(currentServer.address);
                    button.active = false;
                    assert this.client.world != null;
                    this.client.world.disconnect();
                    this.client.disconnect();
                    ConnectScreen.connect(null, this.client, serverIp, currentServer, true, null);
                },
                (narrationSupplier) -> Text.translatable("narration.reconnect_button")
            ));
        }
    }
}
