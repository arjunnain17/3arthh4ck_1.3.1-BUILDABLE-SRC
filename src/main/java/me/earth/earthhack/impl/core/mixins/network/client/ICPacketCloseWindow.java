package me.earth.earthhack.impl.core.mixins.network.client;

import net.minecraft.network.play.client.CPacketCloseWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={CPacketCloseWindow.class})
public interface ICPacketCloseWindow {
    @Accessor(value="windowId")
    public int getWindowId();
}
