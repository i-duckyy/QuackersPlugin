package com.quackers.plugin.gui.screens;

import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;

public class BetaTestersScreen extends WindowScreen {

    public BetaTestersScreen() {
        super(GuiThemes.get(), "Beta Testers");
    }

    @Override
    public void initWidgets() {
        WTable t = add(theme.table()).center().widget();

        t.add(theme.label("WiabobberYT")); t.row();
        t.add(theme.label("Abbelixus")); t.row();
        t.add(theme.label("SpectroProto"));
    }
}
