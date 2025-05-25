/*
    Copyright - Quackers Plugin 2025
    This file is part of the Quackers Plugin project.
    Unauthorized use, distribution, or modification of this file without explicit permission is strictly prohibited and may be subject to DMCA takedown actions.
*/

package com.quackers.plugin.modules;

import com.quackers.plugin.QuackersPlugin;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;

import static com.quackers.plugin.utils.DiscordWebhookSender.sendWebhook;


public class DiscordLogger extends Module
{
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> webhookURL = sgGeneral.add(new StringSetting.Builder()
        .name("Webhook Link")
        .description("The discord webhook to use, looks like this: https://discord.com/api/webhooks/webhookUserId/webHookTokenOrSomething")
        .defaultValue("")
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("Message Delay (MS)")
        .description("The delay between messages in milliseconds.")
        .defaultValue(2000)
        .build()
    );

    private final Setting<Boolean> queueMessages = sgGeneral.add(new BoolSetting.Builder()
        .name("Queue Messages")
        .description("Will queue messages if they are sent too quickly. This could result in a long delay between messages being logged if the queue gets too big.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> timestamp = sgGeneral.add(new BoolSetting.Builder()
        .name("Timestamp")
        .description("If the message should have a timestamp.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> connections = sgGeneral.add(new BoolSetting.Builder()
        .name("Disconnect")
        .description("If a message should be logged when leaving.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> logAll = sgGeneral.add(new BoolSetting.Builder()
        .name("All Messages")
        .description("Logs all messages.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> queue = sgGeneral.add(new BoolSetting.Builder()
        .name("2b2t Queue")
        .description("If your position in queue should be logged.")
        .defaultValue(false)
        .visible(() -> !logAll.get())
        .build()
    );

    private final Setting<Boolean> whisper = sgGeneral.add(new BoolSetting.Builder()
        .name("Whisper")
        .description("If whispers should be logged.")
        .defaultValue(false)
        .visible(() -> !logAll.get())
        .build()
    );

    private final Setting<Boolean> chat = sgGeneral.add(new BoolSetting.Builder()
        .name("Logs chat messages.")
        .description("Logs all chat messages")
        .defaultValue(false)
        .visible(() -> !logAll.get())
        .build()
    );

    private final Setting<Boolean> commands = sgGeneral.add(new BoolSetting.Builder()
        .name("Commands/Client Info")
        .description("Logs commands and most messages from clients.")
        .defaultValue(false)
        .visible(() -> !logAll.get())
        .build()
    );

    private final Setting<Boolean> deathMessages = sgGeneral.add(new BoolSetting.Builder()
        .name("Death Messages")
        .description("Logs death messages.")
        .defaultValue(false)
        .visible(() -> !logAll.get())
        .build()
    );

    public DiscordLogger()
    {
        super(QuackersPlugin.Main, "discord-logger", "Sends notifications to a discord webhook.");
    }

    @Override
    public void onActivate()
    {
        messageQueue.clear();
        delayTimer = 0;
    }

    private long delayTimer = 0;
    private int lastQueuePos;
    private final Queue<String> messageQueue = new LinkedList<String>();

    @EventHandler
    private void onTick(TickEvent.Post event)
    {
        if (delayTimer > 0)
        {
            delayTimer--;
        }
        else if (queueMessages.get() && !messageQueue.isEmpty())
        {
            sendWebhookMessage(messageQueue.poll());
        }
    }

    @EventHandler(priority = 999)
    private void onReceivePacket(PacketEvent.Receive event) {
        if (event.packet instanceof SubtitleS2CPacket) {
            SubtitleS2CPacket packet = (SubtitleS2CPacket) event.packet;
            String message = packet.text().getString();
            int queueIndex = message.indexOf("Position in queue: ");
            if (queueIndex != -1)
            {
                int queuePos = Integer.parseInt(message.substring(queueIndex + 19));
                if (queuePos != lastQueuePos)
                {
                    handleMessage(message, MessageType.QUEUE);
                    lastQueuePos = queuePos;
                }
            }
        }
    }

    @EventHandler(priority = 999)
    private void onMessageReceive(ReceiveMessageEvent event)
    {
        Text message = event.getMessage();
        for (Text sibling : message.getSiblings())
        {
            TextColor color = sibling.getStyle().getColor();
            if (color != null && color.getRgb() == 11141120)
            {
                handleMessage(message.getString(), MessageType.DEATH);
                return;
            }
        }
        handleMessage(message.getString(), MessageType.NORMAL);
    }

    public void handleMessage(String message, MessageType messageType)
    {
        if (webhookURL.get().isBlank()) return;

        if (logAll.get())
        {
            sendWebhookMessage(message);
        }
        else if (connections.get() && messageType.equals(MessageType.DISCONNECT))
        {
            sendWebhookMessage(message);
        }
        else if (queue.get() && messageType.equals(MessageType.QUEUE))
        {
            sendWebhookMessage(message);
        }
        else if (whisper.get()
            && !message.startsWith("<")
            && message.contains("whispers: ")
            || message.startsWith("to "))
        {
            sendWebhookMessage(message);
        }
        else if (chat.get() && message.startsWith("<"))
        {
            sendWebhookMessage(message);
        }
        else if (deathMessages.get() && messageType.equals(MessageType.DEATH))
        {
            sendWebhookMessage(message);
        }
        else if (commands.get() && !message.startsWith("<")
            && !message.startsWith("to ")
            && messageType.equals(MessageType.NORMAL))
        {
            sendWebhookMessage(message);
        }
    }

    @EventHandler
    private void onDisconnect(GameLeftEvent event)
    {
        handleMessage("Disconnected", MessageType.DISCONNECT);
    }

    private void sendWebhookMessage(String message)
    {
        if (delayTimer > 0)
        {
            if (queueMessages.get()) messageQueue.offer(message);
            return;
        }
        delayTimer = delay.get() / 1000 * 20;
        if (timestamp.get())
        {
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String timestamp = now.format(formatter);
            message = "[" + timestamp + "] " + message;
        }
        String json = "{\n" +
            "\"content\": \"```" + message + "```\"\n" +
            "}";
        new Thread(() -> sendWebhook(webhookURL.get(), json, null)).start();
    }

    public enum MessageType
    {
        NORMAL,
        DEATH,
        QUEUE,
        DISCONNECT
    }
}
