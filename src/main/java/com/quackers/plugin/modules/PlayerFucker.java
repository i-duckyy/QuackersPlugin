package com.quackers.plugin.modules;

import com.mojang.authlib.GameProfile;
import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.events.meteor.MouseButtonEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

public class PlayerFucker extends Module{

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgPos = settings.createGroup("Sex Position");
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<Mode> targetMode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("Mode")
        .description("The mode at which to follow the player.")
        .defaultValue(Mode.Automatic)
        .onChanged(onChanged -> {
            target = null;
        })
        .build()
    );

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("Range")
        .description("The maximum range to set target.")
        .defaultValue(6)
        .min(0)
        .sliderMax(6)
        .build()
    );

    private final Setting<Integer> sexDelay = sgPos.add(new IntSetting.Builder()
        .name("Speed")
        .description("Delay for sex movements in ticks")
        .defaultValue(3)
        .sliderRange(0, 20)
        .build()
    );

    private final Setting<Style> sexStyle = sgPos.add(new EnumSetting.Builder<Style>()
        .name("Position")
        .description("The style for sticking to player.")
        .defaultValue(Style.SuckySucky)
        .build()
    );

    private final Setting<Boolean> isRender = sgRender.add(new BoolSetting.Builder()
        .name("Rendering")
        .description("Render the target.")
        .defaultValue(true)
        .build()
    );

    public PlayerFucker() {
        super(QuackersPlugin.CATEGORY, "player-fucker", "Automatic Minecraft Sex RP.");
    }
    private final List<Entity> targets = new ArrayList<>();
    String regex = "[A-Za-z0-9_]+";
    private int messageI, timer, timerSex, sexI;
    static double renderY;
    double addition = 0.0;
    Entity target = null;

    @Override
    public void onActivate() {
        if(targetMode.get() == Mode.Automatic){
            setTarget();
        }
    }

    private boolean entityCheck(Entity entity) {
        if (entity.equals(mc.player) || entity.equals(mc.cameraEntity)) return false;
        if ((entity instanceof LivingEntity && ((LivingEntity) entity).isDead()) || !entity.isAlive()) return false;
        if (!PlayerUtils.isWithin(entity, range.get())) return false;
        if (!PlayerUtils.canSeeEntity(entity) && !PlayerUtils.isWithin(entity, range.get())) return false;
        if (Pattern.matches(regex, EntityUtils.getName(entity))) return true;
        return entity.isPlayer();
    }

    @EventHandler
    private void onMouseButton(MouseButtonEvent event) {
        if(targetMode.get() == Mode.MiddleClick) {
            if (event.action == KeyAction.Press && event.button == GLFW_MOUSE_BUTTON_MIDDLE && mc.currentScreen == null) {
                if (mc.targetedEntity instanceof PlayerEntity) {
                    target = mc.targetedEntity;

                } else  {
                    target = null;
                    assert mc.player != null;
                    mc.player.getAbilities().flying = false;
                }
            }
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (target == null) return;

        checkEntity();

        assert mc.player != null;
        mc.player.getAbilities().flying = true;

        if (timerSex <= 0) {
            if (sexStyle.get() == Style.SuckySucky) {
                Rotations.rotate(Rotations.getYaw(target), 45);
                if (sexI == 0) {
                    Position head = target.raycast(0.2, 1f / 20f, false).getPos();
                    mc.player.setPosition(head.getX(), head.getY() - 0.5, head.getZ());
                    sexI = 1;
                } else {
                    Position head = target.raycast(0.5, 1f / 20f, false).getPos();
                    mc.player.setPosition(head.getX(), head.getY() - 0.5, head.getZ());
                    sexI = 0;
                }
            }

            if (sexStyle.get() == Style.Anal) {
                Rotations.rotate(Rotations.getYaw(target), 25);
                if (sexI == 0) {
                    Position head = target.raycast(-0.2, 1f / 20f, false).getPos();
                    mc.player.setPosition(head.getX(), target.getY(), head.getZ());
                    sexI = 1;
                } else {
                    Position head = target.raycast(-0.5, 1f / 20f, false).getPos();
                    mc.player.setPosition(head.getX(), target.getY(), head.getZ());
                    sexI = 0;
                }
            }

            if (sexStyle.get() == Style.PussyFuck) {
                Rotations.rotate(Rotations.getYaw(target), 20);
                if (sexI == 0) {
                    Position pos = target.raycast(-0.3, 1f / 20f, false).getPos();
                    mc.player.setPosition(pos.getX(), target.getY() - 0.2, pos.getZ());
                    sexI = 1;
                } else {
                    Position pos = target.raycast(-0.6, 1f / 20f, false).getPos();
                    mc.player.setPosition(pos.getX(), target.getY() - 0.2, pos.getZ());
                    sexI = 0;
                }
            }

            if (sexStyle.get() == Style.TitJob) {
                Rotations.rotate(Rotations.getYaw(target), 40);
                if (sexI == 0) {
                    Position pos = target.raycast(0.3, 1f / 20f, false).getPos();
                    mc.player.setPosition(pos.getX(), pos.getY() - 0.3, pos.getZ());
                    sexI = 1;
                } else {
                    Position pos = target.raycast(0.6, 1f / 20f, false).getPos();
                    mc.player.setPosition(pos.getX(), pos.getY() - 0.3, pos.getZ());
                    sexI = 0;
                }
            }

            timerSex = sexDelay.get();
        } else {
            timerSex--;
        }
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (target == null || !isRender.get()) return;

        Vec3d last = null;
        if (addition > 360) addition = 0;
        for (int i = 0; i < 360; i ++) {
            Color c1 = new Color(255, 0, 255, 255);;
            Vec3d tp = target.getPos();

            double rad = Math.toRadians(i);
            double sin = Math.sin(rad);
            double cos = Math.cos(rad);
            Vec3d c = new Vec3d(tp.x + sin, tp.y + getRenderY(), tp.z + cos);
            if (last != null) event.renderer.line(last.x, last.y, last.z, c.x, c.y, c.z, c1);
            last = c;
        }
    }

    @Override
    public void onDeactivate() {
        target = null;
        assert mc.player != null;
        mc.player.getAbilities().flying = false;
    }

    private void checkEntity() {
        assert mc.player != null;
        List <String> playerNamesList = mc.player.networkHandler.getPlayerList().stream()
            .map(PlayerListEntry::getProfile)
            .map(GameProfile::getName)
            .toList();

        if (!playerNamesList.contains(EntityUtils.getName(target)) && targetMode.get() == Mode.Automatic) {
            target = null;
        }

        if (target == null && targetMode.get() == Mode.Automatic) {
            setTarget();
        }
    }

    private void setTarget() {
        TargetUtils.getList(targets, this::entityCheck, SortPriority.LowestDistance, 1);
        if(targets.isEmpty()) return;
        target = targets.getFirst();
    }

    private static double getRenderY() {
        Random rand = new Random();
        double randomValue = 0.2 + (0.0 - 0.05) * rand.nextDouble();
        if (renderY >= 0.3) {
            renderY = 0;
        }
        renderY+= randomValue;
        return renderY;
    }

    private static boolean shouldCum() {
        double chance = Math.random();
        return chance <= 0.1;
    }

    private enum Mode {
        MiddleClick,
        Automatic
    }

    private enum Style {
        SuckySucky,
        Anal,
        PussyFuck,
        TitJob
    }
}
