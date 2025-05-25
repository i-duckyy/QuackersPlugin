/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

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
