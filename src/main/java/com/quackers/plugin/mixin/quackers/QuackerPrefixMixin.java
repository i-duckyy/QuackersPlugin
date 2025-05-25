/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.mixin.quackers;

import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = ChatUtils.class, remap = false)
public class QuackerPrefixMixin {
    private static String prefix = "&#FF8600Q&#FF9200u&#FF9D00a&#FFA900c&#FFB500k&#FFC000e&#FFCC00r &#FFE300> ";

    @Inject(method = "getPrefix", at = @At("HEAD"), cancellable = true)
    private static void getPrefix(CallbackInfoReturnable<Text> info) {
        info.setReturnValue(parseColorCodeText(prefix));
    }

    private static Text parseColorCodeText(String input) {
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})(.)");
        Matcher matcher = pattern.matcher(input);

        MutableText result = Text.empty();
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                result.append(Text.literal(input.substring(lastEnd, matcher.start())));
            }

            String hexColor = matcher.group(1);
            char character = matcher.group(2).charAt(0);
            int rgb = Integer.parseInt(hexColor, 16);

            result.append(Text.literal(Character.toString(character)).setStyle(Style.EMPTY.withColor(rgb)));
            lastEnd = matcher.end();
        }

        if (lastEnd < input.length()) {
            result.append(Text.literal(input.substring(lastEnd)));
        }

        return result;
    }
}
