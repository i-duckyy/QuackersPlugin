/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.mixin.quackers;

import meteordevelopment.meteorclient.systems.config.Config;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(SplashTextResourceSupplier.class)
public class SplashTextMixin {
    @Unique
    private int currentIndex = 0;
    @Unique
    private final List<String> Splashes = getSplashes();

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void onApply(CallbackInfoReturnable<SplashTextRenderer> cir) {
        currentIndex = new Random().nextInt(Splashes.size());
        cir.setReturnValue(new SplashTextRenderer(Splashes.get(currentIndex)));
    }

    @Unique
    private static List<String> getSplashes() {
        return List.of(
            "Quackers Plugin",
            "Greif it all!",
            "#AntiCracked",
            "#AntiP2W"
        );
    }
}
