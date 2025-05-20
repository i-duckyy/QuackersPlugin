package com.quackers.plugin.utils;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CapeManager {
    private static final Map<UUID, Identifier> capes = new HashMap<>();

    public static void registerCape(UUID uuid, net.minecraft.util.Identifier capeTexture) {
        capes.put(uuid, capeTexture);
    }

    public static Identifier getCape(UUID uuid) {
        return capes.get(uuid);
    }
}
