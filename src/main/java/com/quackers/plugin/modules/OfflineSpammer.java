package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.Http;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;

public class OfflineSpammer extends Module {
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

    public OfflineSpammer() {
        super(QuackersPlugin.Main, "offline-spammer", "Spams a request to meteors API to make it look like there's many people offline.");
    }

    @Override
    public void onActivate() {
        running = true;
        spamThread = new Thread(() -> {
            while (running) {
                MeteorExecutor.execute(() -> Http.post("https://meteorclient.com/api/online/leave")
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
