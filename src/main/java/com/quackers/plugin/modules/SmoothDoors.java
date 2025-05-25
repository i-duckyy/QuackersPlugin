package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.events.entity.player.InteractBlockEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class SmoothDoors extends Module {
    boolean isInteracting = false;

    public SmoothDoors() {
        super(QuackersPlugin.Main, "smooth-doors", "Open both doors with one interaction.");
    }

    @EventHandler
    private void onInteract(InteractBlockEvent event) {
        if (isInteracting) return;

        isInteracting = true;
        BlockPos doorPos = event.result.getBlockPos();
        BlockState blockState = mc.world.getBlockState(doorPos);
        if (blockState.getBlock() instanceof DoorBlock) {
            Direction doorFacing = blockState.get(DoorBlock.FACING);

            DoorHinge doorHinge = blockState.get(DoorBlock.HINGE);
            BlockPos otherDoorPos;
            if (doorHinge == DoorHinge.LEFT) {
                otherDoorPos = doorPos.offset(doorFacing.rotateYClockwise());
            } else {
                otherDoorPos = doorPos.offset(doorFacing.rotateYCounterclockwise());
            }

            BlockState otherBlockState = mc.world.getBlockState(otherDoorPos);
            if (otherBlockState.getBlock() instanceof DoorBlock) {
                if (blockState.get(DoorBlock.HALF) == otherBlockState.get(DoorBlock.HALF)
                        && blockState.get(DoorBlock.HINGE) != otherBlockState.get(DoorBlock.HINGE)
                        && blockState.get(DoorBlock.OPEN) == otherBlockState.get(DoorBlock.OPEN)) {
                    BlockUtils.interact(new BlockHitResult(Utils.vec3d(otherDoorPos), Direction.UP, otherDoorPos, false), Hand.MAIN_HAND, false);
                }
            }
        }

        isInteracting = false;
    }
}
