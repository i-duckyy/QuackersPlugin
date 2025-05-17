package com.quackers.plugin.gui.screens;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.systems.accounts.Accounts;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class QuackersScreen extends WindowScreen {
    public QuackersScreen() {
        super(GuiThemes.get(), QuackersPlugin.NAME);
    }

    @Override
    public void initWidgets() {
        WVerticalList l = add(theme.verticalList()).center().widget();

        addButton(l, "SessionLogin", () -> mc.setScreen(new SessionIDScreen()));
        addButton(l, "Credits", () -> mc.setScreen(new CreditsScreen()));
    }

    private void addButton(WContainer c, String text, Runnable action) {
        WButton button = c.add(theme.button(text)).expandX().widget();
        button.action = action;
    }

    @Override
    public boolean toClipboard() {
        return NbtUtils.toClipboard(Accounts.get());
    }

    @Override
    public boolean fromClipboard() {
        return NbtUtils.fromClipboard(Accounts.get());
    }
}
