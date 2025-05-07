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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MaceKill extends Module {
    private static final Map<Integer, Long> delayedPackets = new ConcurrentHashMap<>(); // Store packet hash and timestamp
    private static final int PACKET_DELAY_TAG = 1337;
    private final SettingGroup specialGroup = settings.createGroup("Values higher than 22 only work on Paper/Spigot [fixed soon]");
    private final SettingGroup testing = settings.createGroup("Testing Modules");

    private final Setting<Boolean> maxPower = specialGroup.add(new BoolSetting.Builder()
        .name("Force Max Power (Paper/Spigot ONLY)")
        .description("Simulates a fall from the highest air gap within 170 blocks")
        .defaultValue(false)
        .build());

    private final Setting<Integer> fallHeight = specialGroup.add(new IntSetting.Builder()
        .name("Mace Power (Fall height)")
        .description("Simulates a fall from this distance")
        .defaultValue(22)
        .sliderRange(1, 200)
        .min(1)
        .max(200)
        .visible(() -> !maxPower.get())
        .build());

    private final Setting<Boolean> packetDisable = specialGroup.add(new BoolSetting.Builder()
        .name("Bypass Block")
        .description("Does not send movement packets if the attack was blocked. (prevents death)")
        .defaultValue(true)
        .build());

    private final Setting<Integer> spoofDelay = testing.add(new IntSetting.Builder()
        .name("Delay (ms) [broken]")
        .description("Delay before spoofing fall movement to sync with ticks or bypass anti-cheat")
        .defaultValue(0)
        .sliderRange(0, 500)
        .min(0)
        .max(500)
        .build());

    public MaceKill() {
        super(QuackersPlugin.CATEGORY, "solid-mace", "A patch of Trouser Streak's MaceKill making it work no matter what.");
    }

    private Vec3d previouspos;
    private LivingEntity currentTarget = null;

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (mc.player == null
            || !(event.packet instanceof PlayerInteractEntityC2SPacket packet)
            || mc.player.getInventory().getMainHandStack().getItem() != Items.MACE
        ) return;

        // Get the packet's unique identifier (hash)
        int packetHash = packet.hashCode();

        // Check if we've already processed this packet and if it's delayed enough
        if (delayedPackets.containsKey(packetHash) && System.currentTimeMillis() - delayedPackets.get(packetHash) < spoofDelay.get()) {
            return; // Ignore this packet if it's too soon to process again
        }

        // Proceed with normal logic if no delay or delay has passed
        Object type = ((IPlayerInteractEntityC2SPacket) packet).meteor$getType();
        if (type == null || !type.toString().equalsIgnoreCase("ATTACK")) return;

        LivingEntity targetEntity = ((IPlayerInteractEntityC2SPacket) packet).meteor$getEntity() instanceof LivingEntity l ? l : null;
        if (targetEntity == null) return;

        if (packetDisable.get() && (targetEntity.isBlocking() || targetEntity.isInvulnerable() || targetEntity.isInCreativeMode())) return;

        event.cancel(); // Cancel original attack packet

        // Mark this packet as being delayed
        delayedPackets.put(packetHash, System.currentTimeMillis());

        int delay = spoofDelay.get();
        new Thread(() -> {
            try {
                if (delay > 0) Thread.sleep(delay);
                spoofFall(targetEntity);

                // After the delay, re-send the packet (and remove the tag)
                mc.execute(() -> mc.player.networkHandler.sendPacket(packet));
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void spoofFall(LivingEntity targetEntity) {
        currentTarget = targetEntity;
        previouspos = mc.player.getPos();
        int blocks = getMaxHeightAbovePlayer();
        int packetsRequired = (int) Math.ceil(Math.abs(blocks / 10));
        if (packetsRequired > 20) packetsRequired = 1;

        BlockPos isopenair1 = mc.player.getBlockPos().add(0, blocks, 0);
        BlockPos isopenair2 = mc.player.getBlockPos().add(0, blocks + 1, 0);
        if (isSafeBlock(isopenair1) && isSafeBlock(isopenair2)) {
            if (blocks <= 22) {
                if (mc.player.hasVehicle()) {
                    for (int i = 0; i < 4; i++) mc.player.networkHandler.sendPacket(VehicleMoveC2SPacket.fromVehicle(mc.player.getVehicle()));
                    double maxHeight = Math.min(mc.player.getVehicle().getY() + 22, mc.player.getVehicle().getY() + blocks);
                    mc.player.getVehicle().setPosition(mc.player.getVehicle().getX(), maxHeight + blocks, mc.player.getVehicle().getZ());
                    mc.player.networkHandler.sendPacket(VehicleMoveC2SPacket.fromVehicle(mc.player.getVehicle()));
                    mc.player.getVehicle().setPosition(previouspos);
                    mc.player.networkHandler.sendPacket(VehicleMoveC2SPacket.fromVehicle(mc.player.getVehicle()));
                } else {
                    for (int i = 0; i < 4; i++) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false, mc.player.horizontalCollision));
                    double maxHeight = Math.min(mc.player.getY() + 22, mc.player.getY() + blocks);
                    PlayerMoveC2SPacket movepacket = new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), maxHeight, mc.player.getZ(), false, mc.player.horizontalCollision);
                    PlayerMoveC2SPacket homepacket = new PlayerMoveC2SPacket.PositionAndOnGround(previouspos.getX(), previouspos.getY(), previouspos.getZ(), false, mc.player.horizontalCollision);
                    ((IPlayerMoveC2SPacket) homepacket).meteor$setTag(1337);
                    ((IPlayerMoveC2SPacket) movepacket).meteor$setTag(1337);
                    mc.player.networkHandler.sendPacket(movepacket);
                    mc.player.networkHandler.sendPacket(homepacket);
                }
            } else {
                if (mc.player.hasVehicle()) {
                    for (int packetNumber = 0; packetNumber < (packetsRequired - 1); packetNumber++)
                        mc.player.networkHandler.sendPacket(VehicleMoveC2SPacket.fromVehicle(mc.player.getVehicle()));
                    double maxHeight = mc.player.getVehicle().getY() + blocks;
                    mc.player.getVehicle().setPosition(mc.player.getVehicle().getX(), maxHeight + blocks, mc.player.getVehicle().getZ());
                    mc.player.networkHandler.sendPacket(VehicleMoveC2SPacket.fromVehicle(mc.player.getVehicle()));
                    mc.player.getVehicle().setPosition(previouspos);
                    mc.player.networkHandler.sendPacket(VehicleMoveC2SPacket.fromVehicle(mc.player.getVehicle()));
                } else {
                    for (int packetNumber = 0; packetNumber < (packetsRequired - 1); packetNumber++)
                        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false, mc.player.horizontalCollision));
                    double maxHeight = mc.player.getY() + blocks;
                    PlayerMoveC2SPacket movepacket = new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), maxHeight, mc.player.getZ(), false, mc.player.horizontalCollision);
                    PlayerMoveC2SPacket homepacket = new PlayerMoveC2SPacket.PositionAndOnGround(previouspos.getX(), previouspos.getY(), previouspos.getZ(), false, mc.player.horizontalCollision);
                    ((IPlayerMoveC2SPacket) homepacket).meteor$setTag(1337);
                    ((IPlayerMoveC2SPacket) movepacket).meteor$setTag(1337);
                    mc.player.networkHandler.sendPacket(movepacket);
                    mc.player.networkHandler.sendPacket(homepacket);
                }
            }
        }
    }

    private int getMaxHeightAbovePlayer() {
        BlockPos playerPos = mc.player.getBlockPos();
        int maxHeight = playerPos.getY() + (maxPower.get() ? 170 : fallHeight.get());

        for (int i = maxHeight; i > playerPos.getY(); i--) {
            BlockPos isopenair1 = new BlockPos(playerPos.getX(), i, playerPos.getZ());
            BlockPos isopenair2 = isopenair1.up(1);
            if (isSafeBlock(isopenair1) && isSafeBlock(isopenair2)) {
                return i - playerPos.getY();
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
