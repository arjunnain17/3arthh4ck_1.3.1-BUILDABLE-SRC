package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal.ServerAutoCrystal;
import me.earth.earthhack.impl.util.minecraft.Swing;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

final class ListenerRenderPos
extends ModuleListener<ServerAutoCrystal, PacketEvent.Receive<SPacketSpawnExperienceOrb>> {
    public ListenerRenderPos(ServerAutoCrystal module) {
        super(module, PacketEvent.Receive.class, Integer.MAX_VALUE, SPacketSpawnExperienceOrb.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketSpawnExperienceOrb> event) {
        SPacketSpawnExperienceOrb packet = (SPacketSpawnExperienceOrb)event.getPacket();
        if (packet.getEntityID() == -1337) {
            mc.addScheduledTask(() -> {
                if (packet.getXPValue() == -1337) {
                    ((ServerAutoCrystal)this.module).renderPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                } else if (packet.getXPValue() == -1338 && ListenerRenderPos.mc.player != null) {
                    Swing.Client.swing(EnumHand.MAIN_HAND);
                }
            });
            event.setCancelled(true);
        }
    }
}
