package com.quackers.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.network.ClientConnection;
import net.minecraft.util.math.BlockPos;

import java.net.InetSocketAddress;

public class ClientCommand extends Command {
    public ClientCommand() {
        super("client", "Displays information about your client.");
        MeteorClient.EVENT_BUS.subscribe(this);
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            displayClientInfo();
            return SINGLE_SUCCESS;
        });
    }

    private void displayClientInfo() {
        if (mc.player == null || mc.world == null) {
            error("Not in a world or server.");
            return;
        }

        info("Username: %s", mc.getSession().getUsername());
        info("XUID: %s", mc.getSession().getXuid());
        info("Client Brand: %s", mc.getNetworkHandler() != null ? mc.getNetworkHandler().getBrand() : "unknown");

        info("Dimension: %s", mc.world.getRegistryKey().getValue().toString());
        BlockPos pos = mc.player.getBlockPos();
        info("Coordinates: X: %.2f Y: %.2f Z: %.2f", pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

        info("Gamemode: %s", mc.interactionManager != null ? mc.interactionManager.getCurrentGameMode().name() : "unknown");

        if (mc.getNetworkHandler() != null && mc.getNetworkHandler().getConnection() != null) {
            ClientConnection conn = mc.getNetworkHandler().getConnection();
            if (conn.getAddress() instanceof InetSocketAddress isa) {
                info("Server Address: %s:%d", isa.getHostString(), isa.getPort());
            }
            info("Ping: %d ms", mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()).getLatency());
        } else {
            info("Ping: %d ms", 0);
        }
    }
}
