package me.earth.earthhack.impl.core.mixins.network.server;

import net.minecraft.network.play.server.SPacketSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketSetSlot.class})
public interface ISPacketSetSlot {
    @Accessor(value="windowId")
    public void setWindowId(int var1);
}
