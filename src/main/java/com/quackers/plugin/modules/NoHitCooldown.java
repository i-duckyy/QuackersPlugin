package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.systems.modules.Module;

public class NoHitCooldown extends Module {
    public NoHitCooldown() {
        super(QuackersPlugin.CATEGORY, "no-hit-cooldown", "Removes the cooldown between hits.");
    }
}
