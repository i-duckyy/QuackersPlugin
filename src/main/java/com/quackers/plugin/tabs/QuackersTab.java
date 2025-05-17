package com.quackers.plugin.tabs;

import meteordevelopment.meteorclient.MeteorClient;
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

import static com.quackers.plugin.settings.SetSession.mc;

public class QuackersTab extends Tab {
    public static final QuackersTab INSTANCE = new QuackersTab();

    private final Settings settings = new Settings();
    private final SettingGroup sgOptions = settings.createGroup("Quackers Tools");
    private final SettingGroup sgOptions1 = settings.createGroup("Quackers Player Utils [soon]");
    private final SettingGroup sgOptions2 = settings.createGroup("Quackers Systems [soon]");

    private QuackersTab() {
        super("Quackers");
        MeteorClient.EVENT_BUS.subscribe(this);
    }

    private final Setting<Boolean> spoofBrand = sgOptions.add(new BoolSetting.Builder()
        .name("Quackers Brand")
        .description("Brands your client to 'QuackersPlugin'")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> quickBetterTooltips = sgOptions.add(new BoolSetting.Builder()
        .name("Quick Better Tooltips")
        .description("Forces minecrafts better tooltips to be on.")
        .defaultValue(mc.options.hudHidden)
        .onChanged(this::quickBetterTooltips)
        .build()
    );

    private final Setting<Boolean> noArmSwing = sgOptions.add(new BoolSetting.Builder()
        .name("No Arm Swing")
        .description("Removes arm swing.")
        .defaultValue(false)
        .build()
    );

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
