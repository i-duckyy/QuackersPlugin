package com.quackers.plugin.mixin.quackers;

import com.quackers.plugin.modules.NoHitCooldown;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class NoHitCooldownMixin{
    @Inject(method = "getAttackCooldownProgress", at = @At("HEAD"), cancellable = true)
    private void onGetAttackCooldownProgress(float baseTime, CallbackInfoReturnable<Float> cir){
        if (Modules.get().isActive(NoHitCooldown.class)){
            cir.setReturnValue(1.0F);
        }
    }
}
