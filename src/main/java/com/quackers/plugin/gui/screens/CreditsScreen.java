package com.quackers.plugin.gui.screens;

import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class CreditsScreen extends WindowScreen {

    public CreditsScreen() {
        super(GuiThemes.get(), "Credits");
    }

    @Override
    public void initWidgets() {
        WTable t = add(theme.table()).expandX().widget();

        t.add(theme.label("iDucky__ - Main Dev")); t.row();
        t.add(theme.label("Trouser Streak - Dupes")); t.row();
        t.add(theme.label("Meteor Rejects - Theme"));
        t.add(theme.horizontalSeparator());
        WHorizontalList l = add(theme.horizontalList()).expandX().widget();
        addButton(l,"Beta Testers", ()  -> mc.setScreen(new BetaTestersScreen()));
        addButton(l,"Honorable Mentions", ()  -> mc.setScreen(new HonorableMentionsScreen()));
    }

    private void addButton(WContainer c, String text, Runnable action) {
        WButton button = c.add(theme.button(text)).expandX().widget();
        button.action = action;
    }
}
