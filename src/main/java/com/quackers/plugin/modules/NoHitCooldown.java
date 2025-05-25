/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.systems.modules.Module;

public class NoHitCooldown extends Module {
    public NoHitCooldown() {
        super(QuackersPlugin.Main, "no-hit-cooldown", "Removes the cooldown between hits.");
    }
}
