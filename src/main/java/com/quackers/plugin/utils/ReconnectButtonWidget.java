package com.quackers.plugin.utils;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;

public class ReconnectButtonWidget extends ButtonWidget {

    public ReconnectButtonWidget(int x, int y, int width, int height, MutableText message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }
}
