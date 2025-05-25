/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;

import java.util.ArrayList;
import java.util.List;

public class PortalGodMode extends Module {
    private List<TeleportConfirmC2SPacket> packets;

    public PortalGodMode() {
        super(QuackersPlugin.Main, "portal-god-mode", "Makes you invincible after leaving a nether portal.");
    }

    @Override
    public void onActivate() {
        this.packets = new ArrayList<>();
    }

    @Override
    public void onDeactivate() {
        if (!this.packets.isEmpty()) {
            mc.getNetworkHandler().sendPacket(this.packets.get(this.packets.size() - 1));
        }
    }

    @EventHandler
    private void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof TeleportConfirmC2SPacket packet) {
            this.packets.add(packet);
            event.setCancelled(true);
        }
    }
}
