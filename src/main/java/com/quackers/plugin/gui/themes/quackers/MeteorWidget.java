/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

package com.quackers.plugin.gui.themes.quackers;

import com.quackers.plugin.utils.gui.GuiUtils;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.utils.BaseWidget;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.utils.render.color.Color;

public interface MeteorWidget extends BaseWidget {
    default QuackersGuiTheme theme() {
        return (QuackersGuiTheme) getTheme();
    }

    default void renderBackground(GuiRenderer renderer, WWidget widget, boolean pressed, boolean mouseOver) {
        QuackersGuiTheme theme = theme();
        int r = theme.roundAmount();
        double s = theme.scale(2);
        Color outlineColor = theme.outlineColor.get(pressed, mouseOver);
        GuiUtils.quadRounded(renderer, widget.x + s, widget.y + s, widget.width - s * 2, widget.height - s * 2, theme.backgroundColor.get(pressed, mouseOver), r - s);
        GuiUtils.quadOutlineRounded(renderer, widget, outlineColor, r, s);
    }
}
