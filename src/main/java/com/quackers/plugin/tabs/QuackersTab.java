/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.tabs;

import meteordevelopment.meteorclient.MeteorClient;
import static meteordevelopment.meteorclient.MeteorClient.mc;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.WindowTabScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.BrandCustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.util.Identifier;

public class QuackersTab extends Tab {
    public static final QuackersTab INSTANCE = new QuackersTab();

    private final Settings settings = new Settings();
    private final SettingGroup sgOptions = settings.createGroup("Quackers Tools");
    private final SettingGroup sgOptions1 = settings.createGroup("Quackers Player Utils");

    private QuackersTab() {
        super("Quackers");
        MeteorClient.EVENT_BUS.subscribe(this);
    }

    // Quackers Tools
    private final Setting<Boolean> spoofBrand = sgOptions.add(new BoolSetting.Builder()
        .name("Quackers Rebrander")
        .description("Brands your client to 'QuackersPlugin'")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> quickBetterTooltips = sgOptions.add(new BoolSetting.Builder()
        .name("Fast Quick Better Tooltips")
        .description("Forces minecrafts better tooltips to be on.")
        .defaultValue(mc.options.hudHidden)
        .onChanged(this::quickBetterTooltips)
        .build()
    );

    private final Setting<Boolean> noArmSwing = sgOptions.add(new BoolSetting.Builder()
        .name("Remove Arm Swing")
        .description("Removes arm swing.")
        .defaultValue(false)
        .build()
    );

    // Quackers Player Utils
    private final Setting<Boolean> invincibleClientSide = sgOptions1.add(new BoolSetting.Builder()
        .name("Invincibility [cs]")
        .description("Makes you invincible on the client side")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> noHungerClientSide = sgOptions1.add(new BoolSetting.Builder()
        .name("No Hunger [cs]")
        .description("Makes you not loose hunger on the client side")
        .defaultValue(false)
        .build()
    );

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (invincibleClientSide.get()) {
            assert mc.player != null;
            mc.player.setHealth(20);
        }

        if (noHungerClientSide.get()) {
            assert mc.player != null;
            mc.player.getHungerManager().setSaturationLevel(20);
            mc.player.getHungerManager().setFoodLevel(20);
        }
    }


    @EventHandler
    private void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof CustomPayloadC2SPacket(net.minecraft.network.packet.CustomPayload payload)) {
            Identifier id = payload.getId().id();
            if (spoofBrand.get() && id.equals(BrandCustomPayload.ID.id())) {
                CustomPayloadC2SPacket spoofedPacket = new CustomPayloadC2SPacket(new BrandCustomPayload("Quackers Plugin"));
                event.connection.send(spoofedPacket, null, true);
                event.cancel();
            }
        }
        if (event.packet instanceof HandSwingC2SPacket && !noArmSwing.get()) {
            event.cancel();
        }
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (noArmSwing.get()){
            if (event.packet instanceof EntityAnimationS2CPacket packet && noArmSwing.get()
                && (packet.getAnimationId() == EntityAnimationS2CPacket.SWING_MAIN_HAND
                || packet.getAnimationId() == EntityAnimationS2CPacket.SWING_OFF_HAND)) {

                event.cancel();
            }
        }
    }

    @EventHandler
    private void onPostTick(TickEvent.Post event) {
        if (noArmSwing.get()){
            if (mc.player == null) return;

            mc.player.handSwinging = false;
            mc.player.handSwingProgress = 0;
            mc.player.lastHandSwingProgress = 0;
            mc.player.handSwingTicks = 0;
            mc.player.preferredHand = null;
        }
    }

    private void quickBetterTooltips(Boolean b) {
        mc.options.advancedItemTooltips = b;
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new QuackersTabScreen(theme);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof QuackersTabScreen;
    }

    private class QuackersTabScreen extends WindowTabScreen {
        public QuackersTabScreen(GuiTheme theme) {
            super(theme, QuackersTab.this);
        }

        @Override
        protected void init() {
            super.init();
            initWidgets();
        }

        @Override
        public void initWidgets() {
            clear();
            WTable table = add(theme.table()).expandX().widget();
            table.add(theme.settings(settings));
            table.row();
        }
    }
}
