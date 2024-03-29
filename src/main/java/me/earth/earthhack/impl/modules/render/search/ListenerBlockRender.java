package me.earth.earthhack.impl.modules.render.search;

import me.earth.earthhack.impl.event.events.render.BlockRenderEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.render.search.Search;
import me.earth.earthhack.impl.modules.render.search.SearchResult;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

final class ListenerBlockRender
extends ModuleListener<Search, BlockRenderEvent> {
    public ListenerBlockRender(Search module) {
        super(module, BlockRenderEvent.class);
    }

    @Override
    public void invoke(BlockRenderEvent event) {
        if (((Search)this.module).toRender.size() >= 100000) {
            ((Search)this.module).toRender.clear();
        }
        BlockPos mut = event.getPos();
        Block block = event.getState().getBlock();
        if (ListenerBlockRender.mc.player.getDistanceSq(mut) <= 65536.0 && block != Blocks.AIR && ((Search)this.module).isValid(block.getLocalizedName())) {
            BlockPos pos = mut.toImmutable();
            IBlockState state = event.getState();
            AxisAlignedBB bb = state.func_185900_c(ListenerBlockRender.mc.world, pos).offset(pos.getX(), pos.getY(), pos.getZ());
            int stateColor = ((Search)this.module).getColor(state);
            float r = (float)(stateColor >> 24 & 0xFF) / 255.0f;
            float g = (float)(stateColor >> 16 & 0xFF) / 255.0f;
            float b = (float)(stateColor >> 8 & 0xFF) / 255.0f;
            float a = (float)(stateColor & 0xFF) / 255.0f;
            ((Search)this.module).toRender.put(pos, new SearchResult(pos, bb, r, g, b, a));
        }
    }
}
