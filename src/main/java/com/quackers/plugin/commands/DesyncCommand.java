package com.quackers.plugin.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class DesyncCommand extends Command {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    private Entity entity = null;

    public DesyncCommand() {
        super("leave", "Disconnect.", "kms", "bye", "rage-quit");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            mc.player.networkHandler.getConnection().disconnect(Text.of("Quackers Plugin disconnected you!"));
            return SINGLE_SUCCESS;
        });
    }
}
