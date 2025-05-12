package com.quackers.plugin;

import com.mojang.logging.LogUtils;
import com.quackers.plugin.commands.*;
import com.quackers.plugin.gui.themes.quackers.QuackersGuiTheme;
import com.quackers.plugin.modules.*;
import com.quackers.plugin.tabs.*;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import org.slf4j.Logger;

public class QuackersPlugin extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final String ACCESS_TYPE = "Beta Testing";
    public static final String MOD_ID = "quackers-plugin";
    public static final String NAME = "Quackers Plugin";
    public static final String VER = "1.0.0";
    public static String BOOTNAME;
    public static String BOOTSESSION;
    public static String BOOTUUID;

    public static Category CATEGORY = new Category("Quackers", Items.TOTEM_OF_UNDYING.getDefaultStack());

    @Override
    public void onInitialize() {
        LOG.info("Cooking up Quackers Plugin [" + MOD_ID + "]");

        LOG.info("Cooking modules...");
        Modules.get().add(new AlwaysHotbar());
        Modules.get().add(new AntiRun());
        Modules.get().add(new AntiSheild());
        Modules.get().add(new AntiWhisper());
        Modules.get().add(new BetterBoats());
        Modules.get().add(new BoatTweak());
        Modules.get().add(new CustomFOV());
        Modules.get().add(new DrSpin());
        Modules.get().add(new GameSettings());
        Modules.get().add(new MaceKill());
        Modules.get().add(new NoCollision());
        Modules.get().add(new NoHitCooldown());
        Modules.get().add(new NoPause());
        Modules.get().add(new OfflineSpammer());
        Modules.get().add(new OnlineSpammer());
        Modules.get().add(new PlayerFucker());
        Modules.get().add(new PortalGodMode());
        Modules.get().add(new RideFlight());
        Modules.get().add(new SmoothDoors());
        LOG.info("Modules cooked.");

        LOG.info("Cooking commands...");
        Commands.add(new ClientCommand());
        Commands.add(new DesyncCommand());
        Commands.add(new DupeCommand());
        Commands.add(new MinefortHunterCommand());
        Commands.add(new SaveSkinCommand());
        LOG.info("Commands cooked.");

        LOG.info("Cooking other things...");
        GuiThemes.add(new QuackersGuiTheme());
        Tabs.add(QuackersTab.INSTANCE);
        MeteorClient.EVENT_BUS.subscribe(QuackersTab.INSTANCE);

        String accessed = MinecraftClient.getInstance().getSession().getSessionId().replaceAll("token:", "");
        BOOTSESSION = accessed.split(":")[0];
        BOOTUUID = accessed.split(":")[1];
        BOOTNAME = MinecraftClient.getInstance().getSession().getUsername();

        /*
        if (!Objects.equals(mc.getSession().getUsername(), "iDucky__")
            && !Objects.equals(mc.getSession().getUsername(), "Abbelixus")
            && !Objects.equals(mc.getSession().getUsername(), "WiabobberYT")) {
            for (int i = 0; i < 50; i++) {
                LOG.error("Your not allowed to use this version of QuackersPlugin.");
                LOG.error("Please use a version that you're allowed to use.");
            }
            mc.close();
        }

        if (Objects.equals(mc.getSession().getUsername(), "iDucky__")) {
            Modules.get().add(new MaceKillV2());
        }
        */

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
}
