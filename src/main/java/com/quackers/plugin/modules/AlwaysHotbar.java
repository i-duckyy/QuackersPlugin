/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.events.meteor.MouseScrollEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixininterface.IClientPlayerInteractionManager;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class AlwaysHotbar extends Module {
    private boolean open;

    public AlwaysHotbar() {
        super(QuackersPlugin.Main, "always-hotbar", "Allows you to scroll in your hotbar while having a screen opened.");
    }

    @Override
    public void onActivate() {
        open = false;
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        open = mc.currentScreen != null;
    }

    @EventHandler
    private void onMouseScroll(MouseScrollEvent event) {
        if (open) {
            int slot = mc.player.getInventory().selectedSlot;

            if (slot <= 0 && -event.value < 0) slot = 8;
            else if (slot >= 8 && -event.value > 0) slot = 0;
            else slot += (int) -event.value;

            mc.player.getInventory().selectedSlot = slot > 8 ? 8 : Math.max(slot, 0);
            ((IClientPlayerInteractionManager) mc.interactionManager).meteor$syncSelected();
        }
    }
}
