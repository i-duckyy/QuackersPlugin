package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import com.quackers.plugin.utils.eGirlUtil;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class eGirlMode extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> faces = sgGeneral.add(new BoolSetting.Builder()
        .name("add-faces")
        .description("Adds random faces E.g. UwU, OwO.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> expression = sgGeneral.add(new BoolSetting.Builder()
        .name("add-expression")
        .description("Adds random expressions E.g. meow, nya~.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> actions = sgGeneral.add(new BoolSetting.Builder()
        .name("add-actions")
        .description("Adds random actions E.g. *cries*, *blushes*.")
        .defaultValue(true)
        .build()
    );

    public eGirlMode() {
        super(QuackersPlugin.CATEGORY, "e-girl-mode", "Uwuifies your messages.");
    }

    @EventHandler
    private void onMessageSend(SendMessageEvent event) {
        String message = eGirlUtil.uwuify(event.message, faces.get(), expression.get(), actions.get());


        if (message.length() > 256) {
            message = eGirlUtil.uwuify(event.message, false, false,false);
        }

        if (message.length() > 256) {
            message = event.message;
        }

        event.message = message;
    }
}
