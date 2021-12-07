package me.earth.earthhack.impl.core.ducks.block;

import net.minecraft.block.state.IBlockState;

public interface IBlock {
    public void setHarvestLevelNonForge(String var1, int var2);

    public String getHarvestToolNonForge(IBlockState var1);

    public int getHarvestLevelNonForge(IBlockState var1);
}
