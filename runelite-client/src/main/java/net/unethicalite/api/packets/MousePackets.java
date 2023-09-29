package net.unethicalite.api.packets;

import net.runelite.api.packets.PacketBufferNode;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.game.GameThread;
import net.unethicalite.client.Static;

import java.util.Random;

public class MousePackets
{

    public static void queueClickPacket(int x, int y)
    {
        Static.getClient().setMouseLastPressedMillis(System.currentTimeMillis());
        int mousePressedTime = ((int) (Static.getClient().getMouseLastPressedMillis() - Static.getClient().getClientMouseLastPressedMillis()));
        if (mousePressedTime < 0)
        {
            mousePressedTime = 0;
        }
        if (mousePressedTime > 32767)
        {
            mousePressedTime = 32767;
        }

        Static.getClient().setClientMouseLastPressedMillis(Static.getClient().getMouseLastPressedMillis());
        int mouseInfo = (mousePressedTime << 1);

        System.out.println("Last mouse press time: " + Static.getClient().getMouseLastPressedMillis());
        System.out.println("Second-to-last mouse press time: " + Static.getClient().getClientMouseLastPressedMillis());
        System.out.println("mouseInfo: " + mouseInfo);
        MousePackets.queueClickPacket(mouseInfo, x, y);
    }
    public static void queueClickPacket(int mouseInfo, int x, int y)
    {
        GameThread.invoke(() -> createClickPacket(mouseInfo, x, y).send());
    }

    public static void queueClickPacket()
    {
        queueClickPacket(0, 0);
    }

    public static PacketBufferNode createClickPacket(int mouseInfo, int x, int y)
    {
        var client = Static.getClient();
        var clientPacket = Game.getClientPacket();
        var packetBufferNode = Static.getClient().preparePacket(clientPacket.EVENT_MOUSE_CLICK(), client.getPacketWriter().getIsaacCipher());
        packetBufferNode.getPacketBuffer().writeShort(mouseInfo);
        packetBufferNode.getPacketBuffer().writeShort(x);
        packetBufferNode.getPacketBuffer().writeShort(y);
        return packetBufferNode;
    }
}