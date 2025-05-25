/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.mixin;

import net.minecraft.client.gui.Element;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.gui.screen.Screen;

@Mixin(Screen.class)
public interface IMultiplayerScreenAccessor {
    @Invoker("addDrawableChild")
    <T extends Element> T invokeAddDrawableChild(T drawable);
}
