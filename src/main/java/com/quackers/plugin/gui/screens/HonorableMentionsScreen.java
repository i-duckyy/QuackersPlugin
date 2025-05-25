/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.gui.screens;

import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class HonorableMentionsScreen extends WindowScreen {

    public HonorableMentionsScreen() {
        super(GuiThemes.get(), "Honorable Mentions");
    }

    @Override
    public void initWidgets() {
        WTable t = add(theme.table()).center().widget();

        t.add(theme.label("SalC1 - Got me into anarchy")); t.row();
        t.add(theme.label("FitMC - Also got me into anarchy")); t.row();
        t.add(theme.label("Mountains Of Lava Inc - Gave me the idea to make an addon")); t.row();
        t.add(theme.label("Dupe Anarchy - The anarchy server I play on"));
    }
}
