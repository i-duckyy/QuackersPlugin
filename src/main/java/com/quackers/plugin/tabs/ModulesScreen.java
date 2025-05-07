package com.quackers.plugin.tabs;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;

public class ModulesScreen extends TabScreen {

    public ModulesScreen(GuiTheme theme) {
        super(theme, Tabs.get().getFirst());
    }

    @Override
    public void initWidgets() {
        WVerticalList help = add(theme.verticalList()).bottom().center().widget();
        help.add(theme.label("Test"));
    }
}
