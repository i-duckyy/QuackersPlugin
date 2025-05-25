/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.utils;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;

public class ReconnectButtonWidget extends ButtonWidget {

    public ReconnectButtonWidget(int x, int y, int width, int height, MutableText message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }
}
