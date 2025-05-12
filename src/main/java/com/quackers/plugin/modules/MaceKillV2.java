package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.mixininterface.IPlayerInteractEntityC2SPacket;
import meteordevelopment.meteorclient.mixininterface.IPlayerMoveC2SPacket;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.server.MinecraftServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MaceKillV2 extends Module {
    private static final int MAX_SAFE_FALL_HEIGHT = 22;
    private static final int MAX_FALL_HEIGHT_PAPER = 170;
    private static final int PACKET_HEIGHT_STEP = 5; // Height per packet step for smooth movement

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> maxPower = sgGeneral.add(new BoolSetting.Builder()
        .name("Force Max Power")
        .description("Simulates a fall from the highest air gap within 200 blocks")
        .defaultValue(false)
        .build());

    private final Setting<Integer> fallHeight = sgGeneral.add(new IntSetting.Builder()
        .name("Mace Power (Fall height)")
        .description("Simulates a fall from this distance")
        .defaultValue(MAX_SAFE_FALL_HEIGHT)
        .sliderRange(1, 200)
        .min(1)
        .max(500)
        .visible(() -> !maxPower.get())
        .build());

    private final Setting<Boolean> packetDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("Disable When Blocked")
        .description("Does not send movement packets if the attack was blocked. (prevents death)")
        .defaultValue(true)
        .build());

    private final Setting<Integer> packetDelayMs = sgGeneral.add(new IntSetting.Builder()
        .name("Packet Delay (ms)")
        .description("Delay between movement packets to simulate natural movement")
        .defaultValue(50)
        .sliderRange(10, 200)
        .min(10)
        .max(200)
        .build());

    private Vec3d previousPos;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public MaceKillV2() {
        super(QuackersPlugin.CATEGORY, "solid-mace-v2", "A fix of Quacker Plugin's SolidMace.");
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (mc.player == null) return;

        if (mc.player.getInventory().getMainHandStack().getItem() != Items.MACE) return;

        if (!(event.packet instanceof PlayerInteractEntityC2SPacket packet)) return;

        Object type = ((IPlayerInteractEntityC2SPacket) packet).meteor$getType();
        if (type == null || !type.toString().equalsIgnoreCase("ATTACK")) return;

        try {
            if (!(((IPlayerInteractEntityC2SPacket) packet).meteor$getEntity() instanceof LivingEntity targetEntity)) return;

            if (packetDisable.get() && (targetEntity.isBlocking() || targetEntity.isInvulnerable() || targetEntity.isInCreativeMode())) return;

            previousPos = mc.player.getPos();

            int maxFallHeight = getMaxFallHeightForServer();
            int fallDistance = getMaxHeightAbovePlayer(maxFallHeight);

            if (fallDistance == 0) return;

            BlockPos airCheckPos1 = mc.player.getBlockPos().add(0, fallDistance, 0);
            BlockPos airCheckPos2 = mc.player.getBlockPos().add(0, fallDistance + 1, 0);

            if (!isSafeBlock(airCheckPos1) || !isSafeBlock(airCheckPos2)) return;

            int packetsRequired = Math.max(1, fallDistance / PACKET_HEIGHT_STEP);

            sendMovementPacketsSmooth(fallDistance, packetsRequired, packetDelayMs.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getMaxFallHeightForServer() {
        MinecraftServer server = mc.getServer();
        if (server == null) return fallHeight.get();

        String serverBrand = server.getServerModName().toLowerCase();
        if (serverBrand.contains("paper") || serverBrand.contains("spigot")) {
            return maxPower.get() ? MAX_FALL_HEIGHT_PAPER : fallHeight.get();
        }
        return fallHeight.get();
    }

    private void sendMovementPacketsSmooth(int fallDistance, int packetsRequired, int delayMs) {
        if (mc.player.hasVehicle()) {
            double startY = mc.player.getVehicle().getY();
            double x = mc.player.getVehicle().getX();
            double z = mc.player.getVehicle().getZ();

            for (int i = 1; i <= packetsRequired; i++) {
                final int step = i;
                scheduler.schedule(() -> {
                    double newY = startY + (fallDistance * step / (double) packetsRequired);
                    mc.player.getVehicle().setPosition(x, newY, z);
                    mc.player.networkHandler.sendPacket(VehicleMoveC2SPacket.fromVehicle(mc.player.getVehicle()));
                    // Update client position to match vehicle position
                    mc.player.setPos(x, newY, z);
                }, delayMs * (i - 1), TimeUnit.MILLISECONDS);
            }

            scheduler.schedule(() -> {
                mc.player.getVehicle().setPosition(previousPos.getX(), previousPos.getY(), previousPos.getZ());
                mc.player.networkHandler.sendPacket(VehicleMoveC2SPacket.fromVehicle(mc.player.getVehicle()));
                mc.player.setPos(previousPos.getX(), previousPos.getY(), previousPos.getZ());
            }, delayMs * packetsRequired, TimeUnit.MILLISECONDS);

        } else {
            double startY = mc.player.getY();
            double x = mc.player.getX();
            double z = mc.player.getZ();

            for (int i = 1; i <= packetsRequired; i++) {
                final int step = i;
                scheduler.schedule(() -> {
                    double newY = startY + (fallDistance * step / (double) packetsRequired);
                    PlayerMoveC2SPacket movePacket = new PlayerMoveC2SPacket.PositionAndOnGround(x, newY, z, false, mc.player.horizontalCollision);
                    ((IPlayerMoveC2SPacket) movePacket).meteor$setTag(1337);
                    mc.player.networkHandler.sendPacket(movePacket);
                    // Update client position to match packet position
                    mc.player.setPos(x, newY, z);
                }, delayMs * (i - 1), TimeUnit.MILLISECONDS);
            }

            scheduler.schedule(() -> {
                PlayerMoveC2SPacket homePacket = new PlayerMoveC2SPacket.PositionAndOnGround(previousPos.getX(), previousPos.getY(), previousPos.getZ(), false, mc.player.horizontalCollision);
                ((IPlayerMoveC2SPacket) homePacket).meteor$setTag(1337);
                mc.player.networkHandler.sendPacket(homePacket);
                mc.player.setPos(previousPos.getX(), previousPos.getY(), previousPos.getZ());
            }, delayMs * packetsRequired, TimeUnit.MILLISECONDS);
        }
    }

    private int getMaxHeightAbovePlayer(int maxHeightLimit) {
        BlockPos playerPos = mc.player.getBlockPos();

        for (int y = playerPos.getY() + maxHeightLimit; y > playerPos.getY(); y--) {
            BlockPos pos1 = new BlockPos(playerPos.getX(), y, playerPos.getZ());
            BlockPos pos2 = pos1.up(1);
            if (isSafeBlock(pos1) && isSafeBlock(pos2)) {
                return y - playerPos.getY();
            }
        }
        return 0;
    }

    private boolean isSafeBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).isReplaceable()
            && mc.world.getFluidState(pos).isEmpty()
            && !mc.world.getBlockState(pos).isOf(Blocks.POWDER_SNOW);
    }
}
