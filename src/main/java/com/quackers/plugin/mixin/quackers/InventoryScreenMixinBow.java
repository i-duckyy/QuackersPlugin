package com.quackers.plugin.mixin.quackers;

import com.quackers.plugin.modules.exploits.BowInstaKill;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixinBow extends Screen{

    protected InventoryScreenMixinBow(Text title){
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void onInit(CallbackInfo ci){
        if(Modules.get().isActive(BowInstaKill.class)) {
            ButtonWidget toggle = new ButtonWidget.Builder(Text.of("MovePackets: " + (BowInstaKill.shouldAddVelocity0 ? "50" : "OFF")), b -> {
                BowInstaKill.shouldAddVelocity0 = !BowInstaKill.shouldAddVelocity0;
                BowInstaKill.mc.setScreen(new InventoryScreen(BowInstaKill.mc.player));
            })
                    .position(1, 1)
                    .size(90, 12)
                    .build();
            this.addDrawableChild(toggle);
        }
        if(Modules.get().isActive(BowInstaKill.class)) {
        ButtonWidget toggle = new ButtonWidget.Builder(Text.of("MovePackets: " + (BowInstaKill.shouldAddVelocity ? "100" : "OFF")), b -> {
            BowInstaKill.shouldAddVelocity = !BowInstaKill.shouldAddVelocity;
            BowInstaKill.mc.setScreen(new InventoryScreen(BowInstaKill.mc.player));
        })
                .position(1, 13)
                .size(90, 12)
                .build();
        this.addDrawableChild(toggle);
        }
        if(Modules.get().isActive(BowInstaKill.class)) {
            ButtonWidget toggle = new ButtonWidget.Builder(Text.of("MovePackets: " + (BowInstaKill.shouldAddVelocity1 ? "150" : "OFF")), b -> {
                BowInstaKill.shouldAddVelocity1 = !BowInstaKill.shouldAddVelocity1;
                BowInstaKill.mc.setScreen(new InventoryScreen(BowInstaKill.mc.player));
            })
                    .position(1, 25)
                    .size(90, 12)
                    .build();
            this.addDrawableChild(toggle);
        }
        if(Modules.get().isActive(BowInstaKill.class)) {
            ButtonWidget toggle = new ButtonWidget.Builder(Text.of("MovePackets: " + (BowInstaKill.shouldAddVelocity2 ? "200" : "OFF")), b -> {
                BowInstaKill.shouldAddVelocity2 = !BowInstaKill.shouldAddVelocity2;
                BowInstaKill.mc.setScreen(new InventoryScreen(BowInstaKill.mc.player));
            })
                    .position(1, 37)
                    .size(90, 12)
                    .build();
            this.addDrawableChild(toggle);
        }
        if(Modules.get().isActive(BowInstaKill.class)) {
            ButtonWidget toggle = new ButtonWidget.Builder(Text.of("MovePackets: " + (BowInstaKill.shouldAddVelocity3 ? "300" : "OFF")), b -> {
                BowInstaKill.shouldAddVelocity3 = !BowInstaKill.shouldAddVelocity3;
                BowInstaKill.mc.setScreen(new InventoryScreen(BowInstaKill.mc.player));
            })
                    .position(1, 49)
                    .size(90, 12)
                    .build();
            this.addDrawableChild(toggle);
        }
    }
}
