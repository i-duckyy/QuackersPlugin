package com.quackers.plugin.gui.screens;

import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class BetaTestersScreen extends WindowScreen {

    public BetaTestersScreen() {
        super(GuiThemes.get(), "Beta Testers");
    }

    @Override
    public void initWidgets() {
        WTable t = add(theme.table()).expandX().widget();

        t.add(theme.label("None yet, message me on discord (i.ducky) to become a beta tester!"));
    }
}
