/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.gui.themes.quackers.widgets.pressable;

import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import com.quackers.plugin.gui.themes.quackers.MeteorWidget;
import meteordevelopment.meteorclient.gui.widgets.pressable.WPlus;

public class WMeteorPlus extends WPlus implements MeteorWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        com.quackers.plugin.gui.themes.quackers.QuackersGuiTheme theme = theme();
        double pad = pad();
        double s = theme.scale(3);

        renderBackground(renderer, this, pressed, mouseOver);
        renderer.quad(x + pad, y + height / 2 - s / 2, width - pad * 2, s, theme.plusColor.get());
        renderer.quad(x + width / 2 - s / 2, y + pad, s, height - pad * 2, theme.plusColor.get());
    }
}
