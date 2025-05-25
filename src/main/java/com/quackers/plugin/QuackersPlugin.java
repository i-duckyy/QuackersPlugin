package com.quackers.plugin;

import com.mojang.logging.LogUtils;
import com.quackers.plugin.gui.themes.quackers.QuackersGuiTheme;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import org.slf4j.Logger;

public class QuackersPlugin extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final String ACCESS_TYPE = "Public";
    public static final String MOD_ID = "quackers-plugin";
    public static final String NAME = "Quackers Plugin";
    public static final String VER = "1.2.1";
    public static String BOOTNAME;
    public static String BOOTSESSION;
    public static String BOOTUUID;

    public static Category Main = new Category("Quackers", Items.TOTEM_OF_UNDYING.getDefaultStack());
    public static Category Exploit = new Category("Exploits", Items.CHEST.getDefaultStack());

    @Override
    public void onInitialize() {
        LOG.info("Cooking up Quackers Plugin [" + MOD_ID + " - " + ACCESS_TYPE + " - " + VER + "]");

        LOG.info("Cooking modules and commands...");
        com.quackers.plugin.systems.load.commands();
        com.quackers.plugin.systems.load.modules();
        LOG.info("Modules and commands cooked.");

        LOG.info("Cooking other things...");
        GuiThemes.add(new QuackersGuiTheme());
        com.quackers.plugin.systems.load.tabs();

        String accessed = MinecraftClient.getInstance().getSession().getSessionId().replaceAll("token:", "");
        BOOTSESSION = accessed.split(":")[0];
        BOOTUUID = accessed.split(":")[1];
        BOOTNAME = MinecraftClient.getInstance().getSession().getUsername();
        //com.quackers.plugin.systems.load.privacy();

        LOG.info("Other things cooked.");
        LOG.info("Enjoy your freshly cooked addon! - iDucky");
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(Main);
        Modules.registerCategory(Exploit);
    }

    @Override
    public String getPackage() {
        return "com.quackers.plugin";
    }
}
