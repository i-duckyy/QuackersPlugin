package com.quackers.plugin;

import com.quackers.plugin.commands.DesyncCommand;
import com.quackers.plugin.commands.DupeCommand;
import com.quackers.plugin.commands.MinefortHunterCommand;
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
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import org.slf4j.Logger;

public class QuackersPlugin extends MeteorAddon {
    public static String BOOTNAME;
    public static String BOOTUUID;
    public static String BOOTSESSION;
    public static String NAME = "Quackers Plugin";
    public static String VER = "1.0.0";
    public static String ACCESS_TYPE = "Early Access";
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
        Modules.get().add(new AntiWhisper());
        Modules.get().add(new MaceKill());
        Modules.get().add(new NoHitCooldown());
        Modules.get().add(new BoatTweak());
        LOG.info("Modules cooked.");

        LOG.info("Cooking commands...");
        Commands.add(new SaveSkinCommand());
        Commands.add(new DupeCommand());
        Commands.add(new MinefortHunterCommand());
        Commands.add(new DesyncCommand());
        LOG.info("Commands cooked.");

        LOG.info("Cooking other things...");
        GuiThemes.add(new QuackersGuiTheme());
        Tabs.add(QuackersTab.INSTANCE);
        MeteorClient.EVENT_BUS.subscribe(QuackersTab.INSTANCE);
        String accessed = MinecraftClient.getInstance().getSession().getSessionId().replaceAll("token:", "");
        BOOTSESSION = accessed.split(":")[0];
        BOOTUUID = accessed.split(":")[1];
        BOOTNAME = MinecraftClient.getInstance().getSession().getUsername();
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
