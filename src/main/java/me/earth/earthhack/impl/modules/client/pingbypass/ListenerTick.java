package me.earth.earthhack.impl.modules.client.pingbypass;

import me.earth.earthhack.impl.core.ducks.util.IContainer;
import me.earth.earthhack.impl.event.events.misc.TickEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketKeepAlive;

final class ListenerTick
extends ModuleListener<PingBypass, TickEvent> {
    public ListenerTick(PingBypass module) {
        super(module, TickEvent.class);
    }

    @Override
    public void invoke(TickEvent event) {
        if (((PingBypass)this.module).timer.passed(((PingBypass)this.module).pings.getValue() * 1000)) {
            NetHandlerPlayClient connection = mc.getConnection();
            if (connection != null) {
                CPacketClickWindow container = new CPacketClickWindow(1, -1337, 1, ClickType.PICKUP, ItemStack.EMPTY, ((IContainer)((Object)ListenerTick.mc.player.openContainer)).getTransactionID());
                CPacketKeepAlive alive = new CPacketKeepAlive(-1337L);
                ((PingBypass)this.module).startTime = System.currentTimeMillis();
                ((PingBypass)this.module).handled = false;
                connection.sendPacket(container);
                connection.sendPacket(alive);
            }
            ((PingBypass)this.module).timer.reset();
        }
    }
}
