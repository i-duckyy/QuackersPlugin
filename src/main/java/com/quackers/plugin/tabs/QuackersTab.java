package com.quackers.plugin.tabs;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.WindowTabScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.settings.*;

import net.minecraft.client.gui.screen.Screen;

public class QuackersTab extends Tab {
    public static final QuackersTab INSTANCE = new QuackersTab();

    private static final Settings settings = new Settings();
    private static final SettingGroup sgSettings = settings.createGroup("Quackers Tools");

    private static final Setting<Boolean> smallDuck = sgSettings.add(new BoolSetting.Builder()
        .name("Small Duck [B]")
        .description("Makes iDucky__ (the developer) smaller than usual.")
        .defaultValue(false)
        .build()
    );

    private QuackersTab() {
        super("Quackers");
        MeteorClient.EVENT_BUS.subscribe(this);
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new QuackersTabScreen(theme);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof QuackersTabScreen;
    }

    private class QuackersTabScreen extends WindowTabScreen {
        public QuackersTabScreen(GuiTheme theme) {
            super(theme, QuackersTab.this);
        }

        @Override
        protected void init() {
            super.init();
            initWidgets();
        }

        @Override
        public void initWidgets() {
            clear();
            WTable table = add(theme.table()).expandX().widget();
            table.add(theme.settings(settings));
            table.row();
        }
    }
}
