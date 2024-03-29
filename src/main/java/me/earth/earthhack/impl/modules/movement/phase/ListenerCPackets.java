package me.earth.earthhack.impl.modules.movement.phase;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.CPacketPlayerListener;
import me.earth.earthhack.impl.modules.movement.phase.Phase;
import net.minecraft.network.play.client.CPacketPlayer;

final class ListenerCPackets
extends CPacketPlayerListener {
    private final Phase module;

    public ListenerCPackets(Phase module) {
        this.module = module;
    }

    @Override
    protected void onPacket(PacketEvent.Send<CPacketPlayer> event) {
        this.module.onPacket(event);
    }

    @Override
    protected void onPosition(PacketEvent.Send<CPacketPlayer.Position> event) {
        this.module.onPacket(event);
    }

    @Override
    protected void onRotation(PacketEvent.Send<CPacketPlayer.Rotation> event) {
        this.module.onPacket(event);
    }

    @Override
    protected void onPositionRotation(PacketEvent.Send<CPacketPlayer.PositionRotation> event) {
        this.module.onPacket(event);
    }
}
