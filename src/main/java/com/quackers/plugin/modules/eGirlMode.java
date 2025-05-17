package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import com.quackers.plugin.utils.eGirlUtil;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class eGirlMode extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> addFaces = sgGeneral.add(new BoolSetting.Builder()
        .name("add-faces")
        .description("Appends random faces like UwU, OwO.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> addExpressions = sgGeneral.add(new BoolSetting.Builder()
        .name("add-expressions")
        .description("Appends random expressions like nya~ or meow.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> addActions = sgGeneral.add(new BoolSetting.Builder()
        .name("add-actions")
        .description("Appends random actions like *blushes* or *cries*.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> addStutter = sgGeneral.add(new BoolSetting.Builder()
        .name("add-stutter")
        .description("Adds stuttering to some words.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> addSuffix = sgGeneral.add(new BoolSetting.Builder()
        .name("add-suffix")
        .description("Adds cute suffixes to your messages.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> addEmojis = sgGeneral.add(new BoolSetting.Builder()
        .name("add-emojis")
        .description("Adds random kawaii emojis like 🥺, ✨.")
        .defaultValue(true)
        .build()
    );

    public eGirlMode() {
        super(QuackersPlugin.CATEGORY, "e-girl-mode", "Uwuifies your messages with max cuteness.");
    }

    @EventHandler
    private void onMessageSend(SendMessageEvent event) {
        String original = event.message;
        String uwuified = eGirlUtil.uwuify(
            original,
            addFaces.get(),
            addExpressions.get(),
            addActions.get(),
            addStutter.get(),
            addSuffix.get(),
            addEmojis.get()
        );

        if (uwuified.length() > 256) {
            uwuified = eGirlUtil.uwuify(original, false, false, false, false, false, false);
        }

        event.message = uwuified.length() <= 256 ? uwuified : original;
    }
}
