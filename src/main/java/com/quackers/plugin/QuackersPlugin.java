package com.quackers.plugin;

import com.quackers.plugin.commands.SaveSkinCommand;
import com.quackers.plugin.gui.themes.quackers.QuackersGuiTheme;
import com.mojang.logging.LogUtils;
import com.quackers.plugin.modules.*;
import com.quackers.plugin.tabs.QuackersTab;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.item.Items;
import org.slf4j.Logger;

public class QuackersPlugin extends MeteorAddon {
    private static final String MOD_ID = "quackers-plugin";
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Quackers", Items.TOTEM_OF_UNDYING.getDefaultStack());

    @Override
    public void onInitialize() {
        LOG.info("Cooking up Quackers Plugin [" + MOD_ID + "]");

        LOG.info("Cooking modules...");
        Modules.get().add(new AntiRun());
        Modules.get().add(new AntiSheild());
        Modules.get().add(new BetterBoats());
        Modules.get().add(new CustomFOV());
        Modules.get().add(new DrSpin());
        Modules.get().add(new GameSettings());
        Modules.get().add(new NoCollision());
        Modules.get().add(new NoPause());
        Modules.get().add(new SmoothDoors());
        LOG.info("Modules cooked.");

        LOG.info("Cooking commands...");
        Commands.add(new SaveSkinCommand());
        LOG.info("Commands cooked.");

        LOG.info("Cooking other things...");
        GuiThemes.add(new QuackersGuiTheme());
        Tabs.add(QuackersTab.INSTANCE);
        MeteorClient.EVENT_BUS.subscribe(QuackersTab.INSTANCE);
        LOG.info("Other things cooked.");

        LOG.info("Enjoy your freshly cooked addon! - iDucky");
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.quackers.plugin";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("MeteorDevelopment", "meteor-addon-template");
    }
}
