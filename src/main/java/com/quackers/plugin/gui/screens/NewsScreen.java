/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.gui.screens;

import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class NewsScreen extends WindowScreen {

    public NewsScreen() {
        super(GuiThemes.get(), "News");
    }

    @Override
    public void initWidgets() {
        WTable t = add(theme.table()).center().widget();

        t.add(theme.label("News")).centerX(); t.row();
        t.add(theme.label("Quackers Plugin has left BETA and is now a Public (closed-source) addon")); t.row();
        t.add(theme.label("with over 20 modules and loads of built in features.")); t.row();
        t.add(theme.label("All exploits work!")); t.row();
        t.add(theme.label("Report ANY bug/glitch/error to i.ducky on discord.")); t.row();
        t.add(theme.label("Quackers Plugin will soon be ported to a fork of Meteor Client")); t.row();
        t.add(theme.label("with more features, additions, patches and some modules from Quackers")); t.row();
        t.add(theme.label("Plugin within it!")); t.row();
        t.add(theme.label("")); t.row();
        t.add(theme.label("Updates")).centerX(); t.row();
        t.add(theme.label("New module: DiscordLogger - logs events to a discord webhook (this")); t.row();
        t.add(theme.label("webhook is not pre set, you set it yourself)")); t.row();
    }
}
