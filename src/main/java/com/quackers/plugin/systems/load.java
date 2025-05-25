/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.systems;

import com.quackers.plugin.commands.*;
import com.quackers.plugin.modules.*;
import com.quackers.plugin.modules.exploits.*;
import com.quackers.plugin.tabs.QuackersTab;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.systems.modules.Modules;

import static meteordevelopment.meteorclient.MeteorClient.LOG;
import static meteordevelopment.meteorclient.MeteorClient.mc;

import java.util.Objects;

public class load {
    public static void modules() {
        // Exploits
        Modules.get().add(new BowInstaKill());
        Modules.get().add(new CrashPrevention());
        Modules.get().add(new FourbbJGhostTrack());
        Modules.get().add(new FourjjBCoordExploit());
        Modules.get().add(new MaceKill());

        // Main
        Modules.get().add(new AlwaysHotbar());
        Modules.get().add(new AntiRun());
        Modules.get().add(new AntiSheild());
        Modules.get().add(new AntiWhisper());
        Modules.get().add(new BetterBoats());
        Modules.get().add(new BoatTweak());
        Modules.get().add(new CustomFOV());
        Modules.get().add(new DiscordLogger());
        Modules.get().add(new DrSpin());
        Modules.get().add(new eGirlMode());
        Modules.get().add(new GameSettings());
        Modules.get().add(new InvUtils());
        Modules.get().add(new NoCollision());
        Modules.get().add(new NoHitCooldown());
        Modules.get().add(new NoPause());
        Modules.get().add(new OfflineSpammer());
        Modules.get().add(new OnlineSpammer());
        Modules.get().add(new PlayerFucker());
        Modules.get().add(new PortalGodMode());
        Modules.get().add(new RideFlight());
        Modules.get().add(new SmoothDoors());
        Modules.get().add(new WhatTheTweak());
        Modules.get().add(new WhenDucksFly());
    }

    public static void commands() {
        Commands.add(new ClientCommand());
        Commands.add(new DesyncCommand());
        Commands.add(new InventoryCheckCommand());
        Commands.add(new MinefortHunterCommand());
        Commands.add(new SaveSkinCommand());
    }

    public static void privacy() {
        LOG.warn("Checking if you are allowed to use this mod...");
        if (!Objects.equals(mc.getSession().getUsername(), "Abbelixus")
            && !Objects.equals(mc.getSession().getUsername(), "BoyKisser12")
            && !Objects.equals(mc.getSession().getUsername(), "SpectroProto")
            && !Objects.equals(mc.getSession().getUsername(), "WiabobberYT")
            && !Objects.equals(mc.getSession().getUsername(), "iDucky__")) {
            for (int i = 0; i < 50; i++) {
                LOG.error("Your not allowed to use this version of QuackersPlugin.");
                LOG.error("Please use a version that you're allowed to use.");
            }
            mc.close();
        }
    }

    public static void tabs() {
        Tabs.add(QuackersTab.INSTANCE);
        MeteorClient.EVENT_BUS.subscribe(QuackersTab.INSTANCE);
    }
}
