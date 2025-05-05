package com.quackers.plugin.mixin.quackers;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.message.SentMessage;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> implements RecipeBookProvider {
	public InventoryScreenMixin(PlayerScreenHandler container, PlayerInventory playerInventory, Text name) {
		super(container, playerInventory, name);
	}
    String serverVersion = MinecraftClient.getInstance().getVersionType();

	@Inject(method = {"init"}, at = { @At("TAIL") })
	protected void init(final CallbackInfo ci) {
        if (serverVersion.contains("1.17")){
            addDrawableChild(new ButtonWidget.Builder(Text.literal("XD [Dupe]"), button -> dupe())
                .position((this.width - 48) / 2 + 35, 160)
                .size(100, 20)
                .build()
            );
        }
	}

	private void dupe()
	{
		Slot outputSlot = handler.slots.get(0);
		onMouseClick(outputSlot, outputSlot.id, 0, SlotActionType.THROW);
	}
}
