package me.earth.earthhack.impl.core.ducks.network;

import net.minecraft.network.Packet;

public interface INetworkManager {
    public Packet<?> sendPacketNoEvent(Packet<?> var1);

    public Packet<?> sendPacketNoEvent(Packet<?> var1, boolean var2);
}
