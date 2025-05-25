/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.Http;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;

public class OnlineSpammer extends Module {
    private final Setting<Integer> delay = settings.getDefaultGroup().add(new IntSetting.Builder()
        .name("delay-ms")
        .description("Delay between each spam request in milliseconds.")
        .defaultValue(1000)
        .min(10)
        .sliderMax(5000)
        .build()
    );

    private Thread spamThread;
    private boolean running = false;

    public OnlineSpammer() {
        super(QuackersPlugin.Main, "online-spammer", "Spams a request to meteors API to make it look like there's many people online.");
    }

    @Override
    public void onActivate() {
        running = true;
        spamThread = new Thread(() -> {
            while (running) {
                MeteorExecutor.execute(() -> Http.post("https://meteorclient.com/api/online/ping")
                    .ignoreExceptions()
                    .send());

                try {
                    Thread.sleep(delay.get());
                } catch (InterruptedException ignored) {}
            }
        });
        spamThread.start();
    }

    @Override
    public void onDeactivate() {
        running = false;
        if (spamThread != null) {
            spamThread.interrupt();
            spamThread = null;
        }
    }
}
