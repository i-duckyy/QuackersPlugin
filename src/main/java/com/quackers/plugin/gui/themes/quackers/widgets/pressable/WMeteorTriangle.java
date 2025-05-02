/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

package com.quackers.plugin.gui.themes.quackers.widgets.pressable;

import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import com.quackers.plugin.gui.themes.quackers.MeteorWidget;
import meteordevelopment.meteorclient.gui.widgets.pressable.WTriangle;

public class WMeteorTriangle extends WTriangle implements MeteorWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.rotatedQuad(x, y, width, height, rotation, GuiRenderer.TRIANGLE, theme().backgroundColor.get(pressed, mouseOver));
    }
}
