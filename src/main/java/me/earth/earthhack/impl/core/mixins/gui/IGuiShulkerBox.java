package me.earth.earthhack.impl.core.mixins.gui;

import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={GuiShulkerBox.class})
public interface IGuiShulkerBox {
    @Accessor(value="inventory")
    public IInventory getInventory();

    @Accessor(value="playerInventory")
    public InventoryPlayer getPlayerInventory();
}
