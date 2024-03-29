package me.earth.earthhack.impl.modules.client.media;

import io.netty.buffer.Unpooled;
import me.earth.earthhack.impl.commands.packet.util.BufferUtil;
import me.earth.earthhack.impl.event.events.misc.TickEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.client.media.Media;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

final class ListenerTick
extends ModuleListener<Media, TickEvent> {
    public ListenerTick(Media module) {
        super(module, TickEvent.class);
    }

    @Override
    public void invoke(TickEvent event) {
        if (Media.PING_BYPASS.isEnabled()) {
            if (!((Media)this.module).pingBypassEnabled) {
                ((Media)this.module).cache.clear();
            }
            ((Media)this.module).pingBypassEnabled = true;
            if (ListenerTick.mc.player == null) {
                ((Media)this.module).send = false;
            } else if (!((Media)this.module).send) {
                PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
                buffer.writeShort(0);
                ListenerTick.mc.player.connection.sendPacket(new CPacketCustomPayload("PingBypass", buffer));
                BufferUtil.releaseBuffer(buffer);
                ((Media)this.module).send = true;
            }
        } else if (((Media)this.module).pingBypassEnabled) {
            ((Media)this.module).pingBypassEnabled = false;
            ((Media)this.module).cache.clear();
        }
    }
}
