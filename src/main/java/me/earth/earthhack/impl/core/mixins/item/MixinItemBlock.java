package me.earth.earthhack.impl.core.mixins.item;

import me.earth.earthhack.api.cache.ModuleCache;
import me.earth.earthhack.impl.modules.Caches;
import me.earth.earthhack.impl.modules.player.freecam.Freecam;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={ItemBlock.class})
public abstract class MixinItemBlock {
    private static final ModuleCache<Freecam> FREECAM = Caches.getModule(Freecam.class);

    @Redirect(method={"onItemUse"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/World;mayPlace(Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/Entity;)Z"))
    private boolean mayPlaceHook1(World world, Block blockIn, BlockPos pos, boolean skip, EnumFacing sidePlacedOn, Entity placer) {
        if (FREECAM.isEnabled()) {
            return this.mayPlace(world, blockIn, pos, skip, sidePlacedOn, placer);
        }
        return world.mayPlace(blockIn, pos, skip, sidePlacedOn, placer);
    }

    @Redirect(method={"canPlaceBlockOnSide"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/World;mayPlace(Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/Entity;)Z"))
    private boolean mayPlaceHook2(World world, Block blockIn, BlockPos pos, boolean skip, EnumFacing sidePlacedOn, Entity placer) {
        if (FREECAM.isEnabled()) {
            return this.mayPlace(world, blockIn, pos, skip, sidePlacedOn, placer);
        }
        return world.mayPlace(blockIn, pos, skip, sidePlacedOn, placer);
    }

    private boolean mayPlace(World world, Block blockIn, BlockPos pos, boolean skip, EnumFacing sidePlacedOn, Entity placer) {
        AxisAlignedBB bb;
        IBlockState state = world.getBlockState(pos);
        AxisAlignedBB axisAlignedBB = bb = skip ? null : blockIn.getDefaultState().func_185890_d(world, pos);
        if (bb != Block.NULL_AABB && bb != null && !this.checkCollision(world, bb.offset(pos), placer)) {
            return false;
        }
        if (state.func_185904_a() == Material.CIRCUITS && blockIn == Blocks.ANVIL) {
            return true;
        }
        return state.getBlock().isReplaceable(world, pos) && blockIn.canPlaceBlockOnSide(world, pos, sidePlacedOn);
    }

    private boolean checkCollision(World world, AxisAlignedBB bb, Entity entityIn) {
        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(null, bb)) {
            if (entity == null || entity.isDead || !entity.preventEntitySpawning || entity.equals(entityIn) || entity.equals(Minecraft.getMinecraft().player) || entityIn != null && entity.isRidingSameEntity(entityIn)) continue;
            return false;
        }
        return true;
    }
}
