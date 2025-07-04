/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.settings;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SetSession {
    public static String username = "";
    public static String accessToken = "";
    public static String UUID = "";
    public static String sessionid = "token:"+accessToken+":"+UUID;
    public static boolean originalSession = true;
    public static MinecraftClient mc = MinecraftClient.getInstance();

    @Nullable
    public static java.util.UUID getUuidOrNull() {
        try {
            return java.util.UUID.fromString(UUID);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static GameProfile getGameProfile() {
        return new GameProfile(Objects.requireNonNull(getUuidOrNull()), username);
    }
}
