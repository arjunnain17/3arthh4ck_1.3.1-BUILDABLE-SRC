package me.earth.earthhack.impl.modules.player.automine.util;

import me.earth.earthhack.impl.modules.player.automine.util.IConstellation;
import me.earth.earthhack.impl.util.math.position.PositionUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class Constellation
implements IConstellation {
    private final EntityPlayer player;
    private final BlockPos playerPos;
    private final IBlockState state;
    private final IBlockState playerState;
    private final BlockPos pos;

    public Constellation(IBlockAccess world, EntityPlayer player, BlockPos pos, BlockPos playerPos, IBlockState state) {
        this.player = player;
        this.pos = pos;
        this.playerPos = playerPos;
        this.playerState = world.getBlockState(playerPos);
        this.state = state;
    }

    @Override
    public boolean isAffected(BlockPos pos, IBlockState state) {
        return this.pos.equals(pos) && !this.state.equals(state);
    }

    @Override
    public boolean isValid(IBlockAccess world, boolean checkPlayerState) {
        return PositionUtil.getPosition(this.player).equals(this.playerPos) && world.getBlockState(this.pos).equals(this.state) && (!checkPlayerState || world.getBlockState(this.playerPos).equals(this.playerState));
    }
}
