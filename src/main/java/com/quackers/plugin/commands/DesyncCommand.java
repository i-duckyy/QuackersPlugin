package com.quackers.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;

public class DesyncCommand extends Command {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    private Entity entity = null;

    public DesyncCommand() {
        super("desync", "Desyncs your riding entity from the server.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            assert mc.player != null && mc.world != null;
            if (this.entity == null) {
                if (mc.player.hasVehicle()) {
                    this.entity = mc.player.getVehicle();

                    mc.player.dismountVehicle();
                    mc.world.removeEntity(this.entity.getId(), Entity.RemovalReason.UNLOADED_TO_CHUNK);

                    info("Successfully desynced your vehicle");
                } else {
                    error("You are not riding an entity.");
                }
            } else {
                if (!mc.player.hasVehicle()) {
                    mc.world.addEntity(this.entity);
                    mc.player.startRiding(this.entity, true);

                    this.entity = null;

                    info("Successfully resynced your vehicle");
                } else {
                    error("You are not riding another entity.");
                }
            }

            return SINGLE_SUCCESS;
        });
    }
}
