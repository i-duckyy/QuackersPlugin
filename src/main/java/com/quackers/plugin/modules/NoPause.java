/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.systems.modules.Module;

public class NoPause extends Module {

	public NoPause() {
		super(QuackersPlugin.Main, "no-pause", "Doesnt pause your game when you switch tab.");
		mc.options.pauseOnLostFocus = !isActive();
	}

	@Override
	public void onActivate() {
		mc.options.pauseOnLostFocus = false;
	}

	@Override
	public void onDeactivate() {
		mc.options.pauseOnLostFocus = true;
	}

}
