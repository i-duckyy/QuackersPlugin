package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;

public class BoatTweak extends Module {
    private final SettingGroup settingGroup = settings.getDefaultGroup();

    private final Setting<Integer> height = settingGroup.add(new IntSetting.Builder()
            .name("Height")
            .description("How high you go.")
            .defaultValue(200)
            .min(1)
            .sliderRange(1,400)
            .build()
    );

    public BoatTweak() {
        super(QuackersPlugin.Main, "boat-tweak", "Kills people in a boat using funny packets. Patched in Minecraft 1.21.2");
    }

    @Override
    public void onActivate() {
        if (!(mc.player.getVehicle() instanceof BoatEntity boat)) {
            ChatUtils.sendMsg(Text.of("You are required to be in a boat."));
            toggle();
            return;
        }

        Vec3d oPos = boat.getPos();

        for (int i = 0; i < 15; i++) {
            moveTo(oPos);
        }

        moveTo(oPos.add(0,height.get(),0));
        moveTo(oPos.add(0,0.0001,0));

        mc.player.networkHandler.sendPacket(new PlayerInputC2SPacket(new PlayerInput(false, false, false, false, false,true,false)));
        toggle();
    }

    public void moveTo(Vec3d pos){
        mc.player.getVehicle().setPosition(pos);
        mc.player.networkHandler.sendPacket(VehicleMoveC2SPacket.fromVehicle(mc.player.getVehicle()));
    }
}
