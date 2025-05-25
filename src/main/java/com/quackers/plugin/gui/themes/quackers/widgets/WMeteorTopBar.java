/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.gui.themes.quackers.widgets;

import com.quackers.plugin.utils.gui.GuiUtils;
import net.minecraft.client.gui.screen.Screen;

import com.quackers.plugin.gui.themes.quackers.MeteorWidget;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.gui.widgets.WTopBar;
import meteordevelopment.meteorclient.gui.widgets.pressable.WPressable;
import meteordevelopment.meteorclient.utils.render.color.Color;

import static meteordevelopment.meteorclient.MeteorClient.mc;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

public class WMeteorTopBar extends WTopBar implements MeteorWidget {
    @Override
    protected Color getButtonColor(boolean pressed, boolean hovered) {
        return theme().backgroundColor.get(pressed, hovered);
    }

    @Override
    protected Color getNameColor() {
        return theme().textColor.get();
    }

    @Override
    public void init() {
        for (Tab tab : Tabs.get()) {
            add(new com.quackers.plugin.gui.themes.quackers.widgets.WMeteorTopBar.WTopBarButton(tab));
        }
    }

    protected int getState(com.quackers.plugin.gui.themes.quackers.widgets.WMeteorTopBar.WTopBarButton btn) {
        int a = 0;
        if (btn.equals(cells.get(0).widget()))
            a |= 1;
        if (btn.equals(cells.get(cells.size() - 1).widget()))
            a |= 2;
        return a;
    }

    protected class WTopBarButton extends WPressable {
        private final Tab tab;

        public WTopBarButton(Tab tab) {
            this.tab = tab;
        }

        @Override
        protected void onCalculateSize() {
            double pad = pad();

            width = pad + theme.textWidth(tab.name) + pad;
            height = pad + theme.textHeight() + pad;
        }

        @Override
        protected void onPressed(int button) {
            Screen screen = mc.currentScreen;

            if (!(screen instanceof TabScreen) || ((TabScreen) screen).tab != tab) {
                double mouseX = mc.mouse.getX();
                double mouseY = mc.mouse.getY();

                tab.openScreen(theme);
                glfwSetCursorPos(mc.getWindow().getHandle(), mouseX, mouseY);
            }
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            double pad = pad();
            Color color = getButtonColor(pressed || (mc.currentScreen instanceof TabScreen && ((TabScreen) mc.currentScreen).tab == tab), mouseOver);

            //renderer.quad(x, y, width, height, color);
            switch (getState(this)) {
                case 1:
                    GuiUtils.quadRoundedSide(renderer, this, color, ((com.quackers.plugin.gui.themes.quackers.QuackersGuiTheme)theme).roundAmount(), false);
                    break;
                case 2:
                    GuiUtils.quadRoundedSide(renderer, this, color, ((com.quackers.plugin.gui.themes.quackers.QuackersGuiTheme)theme).roundAmount(), true);
                    break;
                case 3:
                    GuiUtils.quadRounded(renderer, this, color, ((com.quackers.plugin.gui.themes.quackers.QuackersGuiTheme)theme).roundAmount());
                    break;
                default:
                    renderer.quad(this, color);
                    break;
            }
            renderer.text(tab.name, x + pad, y + pad, getNameColor(), false);
        }
    }
}
