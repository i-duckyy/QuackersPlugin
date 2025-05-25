/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class WhatTheTweak extends Module {
    private boolean isManipulating = false;
    private final Random random = new Random();

    public WhatTheTweak() {
        super(QuackersPlugin.Main, "what-the-tweak", "Randomly messes with your player stats and client.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (isManipulating) return;
        isManipulating = true;

        ClientPlayerEntity player = mc.player;
        if (player != null && mc.world != null) {
            player.setHealth(0.0f + random.nextFloat() * 19.0f);
            player.getHungerManager().setFoodLevel(random.nextInt(21));
            player.getHungerManager().setSaturationLevel(random.nextFloat() * 5.0f);
            player.setOnFireFor(random.nextInt(101));

            player.setExperience(random.nextInt(50000), 0, random.nextInt(50000));

            StatusEffectInstance effect = new StatusEffectInstance(
                StatusEffects.NAUSEA,
                100 + random.nextInt(200),
                random.nextInt(3)
            );
            player.addStatusEffect(effect);

            double vx = (random.nextDouble() - 0.5) * 0.2;
            double vy = (random.nextDouble() - 0.5) * 0.2;
            double vz = (random.nextDouble() - 0.5) * 0.2;
            player.setVelocity(new Vec3d(vx, vy, vz));

            mc.options.getGamma().setValue(random.nextDouble() * 27.5);
            mc.world.setTime(random.nextInt(24000), random.nextInt(24000), true);
        }

        isManipulating = false;
    }
}
