package com.quackers.plugin.mixin.quackers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.Collectors;

import static meteordevelopment.meteorclient.MeteorClient.mc;
import static meteordevelopment.meteorclient.utils.player.ChatUtils.info;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> implements RecipeBookProvider {
    public InventoryScreenMixin(PlayerScreenHandler container, PlayerInventory playerInventory, Text name) {
        super(container, playerInventory, name);
    }

    @Inject(method = {"init"}, at = { @At("TAIL") })
    protected void init(final CallbackInfo ci) {
        addDrawableChild(new ButtonWidget.Builder(Text.literal("x2"), button -> dupe())
            .position(540, 186)
            .size(23, 20)
            .build()
        );

        addDrawableChild(new ButtonWidget.Builder(Text.literal("Empty Inv"), button -> empty())
            .position(380, 350)
            .size(67, 20)
            .build()
        );

        addDrawableChild(new ButtonWidget.Builder(Text.literal("Sort Inv"), button -> sortInventory())
            .position(450, 350)
            .size(67, 20)
            .build()
        );

        addDrawableChild(new ButtonWidget.Builder(Text.literal("Mess Inv"), button -> messUp())
            .position(520, 350)
            .size(67, 20)
            .build()
        );

        addDrawableChild(new ButtonWidget.Builder(Text.literal("Paper Dupe"), button -> bookDupe())
            .position(500, 160)
            .size(67, 20)
            .build()
        );
    }

    @Unique
    private void dupe() {
        MinecraftClient mc = MinecraftClient.getInstance();
        String serverVersion = mc.getVersionType();

        if (!serverVersion.contains("1.17")) {
            info("The server version isn't 1.17, the dupe won't work.");
            return;
        }

        info("Duping...");
        if (!handler.slots.isEmpty()) {
            Slot outputSlot = handler.slots.get(0);
            if (outputSlot != null && outputSlot.hasStack()) {
                assert mc.interactionManager != null;
                mc.interactionManager.clickSlot(
                    handler.syncId,
                    outputSlot.id,
                    0,
                    SlotActionType.THROW,
                    mc.player
                );
            } else {
                info("No item found to dupe.");
            }
        }
    }

    @Unique
    private void empty() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ScreenHandler handler = this.handler;

        if (handler == null || handler.slots.isEmpty()) {
            info("No slots found.");
            return;
        }

        info("Dropping all items...");
        for (Slot slot : handler.slots) {
            assert mc.player != null;
            if (slot.inventory == mc.player.getInventory() && slot.hasStack()) {
                assert mc.interactionManager != null;
                mc.interactionManager.clickSlot(
                    handler.syncId,
                    slot.id,
                    1,
                    SlotActionType.THROW,
                    mc.player
                );
            }
        }
    }

    @Unique
    private void sortInventory() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ScreenHandler handler = this.handler;

        info("Sorting inventory alphabetically...");

        List<Slot> playerSlots = handler.slots.stream()
            .filter(slot -> slot.inventory == mc.player.getInventory())
            .collect(Collectors.toList());

        List<ItemStack> stacks = playerSlots.stream()
            .map(Slot::getStack)
            .collect(Collectors.toList());

        stacks.sort(Comparator.comparing(stack -> stack.getName().getString()));

        for (int i = 0; i < stacks.size(); i++) {
            mc.player.getInventory().setStack(i, stacks.get(i));
        }
    }

    @Unique
    private void messUp() {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerInventory inv = mc.player.getInventory();

        info("Messing up inventory...");

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inv.size(); i++) items.add(inv.getStack(i));

        Collections.shuffle(items);

        for (int i = 0; i < inv.size(); i++) inv.setStack(i, items.get(i));
    }

    @Unique
    private void bookDupe() {
        if (mc.player == null || mc.interactionManager == null) return;

        for (int i = 0; i < mc.player.getInventory().size(); i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);

            if (stack.getItem() == Items.WRITABLE_BOOK) {

                int selectedSlot = mc.player.getInventory().selectedSlot;
                if (i != selectedSlot) {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, selectedSlot, SlotActionType.SWAP, mc.player);
                }
                for (int slot = 9; slot <= 44; slot++) {
                    if (selectedSlot == slot - 36) continue;

                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 10, SlotActionType.THROW, mc.player);
                }
                mc.player.networkHandler.sendPacket(new BookUpdateC2SPacket(mc.player.getInventory().selectedSlot, new ArrayList<>(), Optional.of("BookUpdateC2SPacket.launch(458*defaultVar)")));
                mc.execute(() -> mc.setScreen(new DisconnectedScreen(
                    mc.currentScreen,
                    Text.of("§4Lost connection :/"),
                    Text.of("Quackers plugin attempted to dupe!\nKicked for: BookUpdateC2SPacket.optionTooLong\n\n(Expect another kick screen)")
                )));

                break;
            }
        }
    }
}
