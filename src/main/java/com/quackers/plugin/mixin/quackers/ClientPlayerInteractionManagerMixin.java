/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.mixin.quackers;

import com.quackers.plugin.modules.exploits.BowInstaKill;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.player.AntiHunger;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(at = @At("HEAD"), method = "stopUsingItem")
    public void onStopUsingItem(PlayerEntity player, CallbackInfo ci){
        if(Modules.get().isActive(BowInstaKill.class)){
            if(player.getMainHandStack().getItem().equals(Items.BOW)){
                if (Modules.get().get(AntiHunger.class).isActive()) {
                    Modules.get().get(AntiHunger.class).toggle();
                }
                BowInstaKill.addVelocityToPlayer();
            }
            if(player.getMainHandStack().getItem().equals(Items.TRIDENT)){
                if (Modules.get().get(AntiHunger.class).isActive()) {
                    Modules.get().get(AntiHunger.class).toggle();
                }
                BowInstaKill.addVelocityToPlayer();
            }
        }
    }
}
