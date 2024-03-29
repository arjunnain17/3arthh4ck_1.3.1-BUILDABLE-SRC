package me.earth.earthhack.impl.util.math.path;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.util.helpers.blocks.modes.RayTraceMode;
import me.earth.earthhack.impl.util.math.MathUtil;
import me.earth.earthhack.impl.util.math.geocache.GeoCache;
import me.earth.earthhack.impl.util.math.path.BlockingEntity;
import me.earth.earthhack.impl.util.math.path.PathCache;
import me.earth.earthhack.impl.util.math.path.Pathable;
import me.earth.earthhack.impl.util.math.path.TriPredicate;
import me.earth.earthhack.impl.util.math.position.PositionUtil;
import me.earth.earthhack.impl.util.math.raytrace.Ray;
import me.earth.earthhack.impl.util.math.raytrace.RayTraceFactory;
import me.earth.earthhack.impl.util.minecraft.blocks.states.IBlockStateHelper;
import me.earth.earthhack.impl.util.minecraft.entity.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class PathFinder
implements Globals {
    public static final double MAX_RANGE = 7.0;
    public static final GeoCache CACHE;
    public static final TriPredicate<BlockPos, Pathable, Entity> CHECK;
    private static final EnumFacing[] SN;
    private static final EnumFacing[] EW;
    private static final EnumFacing[] UD;
    private static final boolean[][] CHECKS;

    private PathFinder() {
        throw new AssertionError();
    }

    public static void efficient(Pathable p, double pr, List<Entity> es, RayTraceMode mode, IBlockStateHelper world, IBlockState setState, TriPredicate<BlockPos, Pathable, Entity> check, Iterable<BlockPos> limited, BlockPos ... ignore) {
        PathFinder.fastPath(p, pr, es, mode, world, setState, check, limited, ignore);
        if (!p.isValid()) {
            PathFinder.findPath(p, pr, es, mode, world, setState, check, ignore);
        }
    }

    public static void fastPath(Pathable p, double pr, List<Entity> es, RayTraceMode m, IBlockStateHelper world, IBlockState s, TriPredicate<BlockPos, Pathable, Entity> c, Iterable<BlockPos> tc, BlockPos ... ignore) {
        if (pr > 7.0) {
            throw new IllegalArgumentException("Range " + pr + " was bigger than MAX_RANGE: " + 7.0);
        }
        HashSet<BlockPos> ignored = Sets.newHashSet(ignore);
        for (BlockPos pos : tc) {
            if (PathFinder.checkPos(pos, p, ignored, pr, es, m, world, s, c)) break;
        }
    }

    public static void findPath(Pathable pathable, double pr, List<Entity> entities, RayTraceMode mode, IBlockStateHelper world, IBlockState setState, TriPredicate<BlockPos, Pathable, Entity> check, BlockPos ... ignore) {
        if (pr > 7.0) {
            throw new IllegalArgumentException("Range " + pr + " was bigger than MAX_RANGE: " + 7.0);
        }
        HashSet<BlockPos> ignored = Sets.newHashSet(ignore);
        int maxRadius = CACHE.getRadius(pr);
        Vec3i[] offsets = CACHE.array();
        for (int i = 1; i < maxRadius && !PathFinder.checkPos(pathable.getPos().add(offsets[i]), pathable, ignored, pr, entities, mode, world, setState, check); ++i) {
        }
    }

    private static boolean checkPos(BlockPos pos, Pathable pathable, Set<BlockPos> ignored, double pr, List<Entity> entities, RayTraceMode mode, IBlockStateHelper world, IBlockState setState, TriPredicate<BlockPos, Pathable, Entity> c) {
        IBlockState state = world.getBlockState(pos);
        if (state.func_185904_a().isReplaceable()) {
            return false;
        }
        if (pathable.getFrom().getDistanceSq(pos) > MathUtil.square(pr) || ignored.contains(pos)) {
            return false;
        }
        int xDiff = pathable.getPos().getX() - pos.getX();
        int yDiff = pathable.getPos().getY() - pos.getY();
        int zDiff = pathable.getPos().getZ() - pos.getZ();
        for (int i = 0; i < CHECKS.length; ++i) {
            boolean[] check = CHECKS[i];
            EnumFacing[] facings = PathFinder.produceOffsets(check[0], check[1], xDiff, yDiff, zDiff);
            if (facings.length > pathable.getMaxLength()) continue;
            int index = 0;
            boolean valid = true;
            BlockPos current = pos;
            Ray[] path = new Ray[facings.length];
            for (EnumFacing facing : facings) {
                BlockPos offset = current.offset(facing);
                if (PathFinder.check(offset, pr, ignored, pathable, entities, c)) {
                    valid = false;
                    break;
                }
                Ray ray = RayTraceFactory.rayTrace(pathable.getFrom(), current, facing, world, setState, mode == RayTraceMode.Smart ? -1.0 : 2.0);
                if (!ray.isLegit() && mode == RayTraceMode.Smart) {
                    valid = false;
                    break;
                }
                path[index++] = ray;
                current = offset;
            }
            if (valid) {
                pathable.setPath(path);
                pathable.setValid(true);
                return true;
            }
            if (facings.length == 1 || facings.length < 4 && i > 0) break;
            pathable.getBlockingEntities().clear();
        }
        return false;
    }

    private static boolean check(BlockPos pos, double pr, Set<BlockPos> ignored, Pathable pathable, List<Entity> entities, TriPredicate<BlockPos, Pathable, Entity> c) {
        return pathable.getFrom().getDistanceSq(pos) > MathUtil.square(pr) || ignored.contains(pos) || PathFinder.checkEntities(pathable, pos, entities, c);
    }

    private static boolean checkEntities(Pathable data, BlockPos pos, List<Entity> entities, TriPredicate<BlockPos, Pathable, Entity> check) {
        for (Entity entity : entities) {
            if (check.test(pos, data, entity)) continue;
            return true;
        }
        return false;
    }

    public static EnumFacing[] produceOffsets(boolean yFirst, boolean xFirst, int xDiff, int yDiff, int zDiff) {
        EnumFacing[] result = new EnumFacing[Math.abs(xDiff) + Math.abs(yDiff) + Math.abs(zDiff)];
        int index = 0;
        if (yFirst) {
            index = PathFinder.apply(result, yDiff, index, UD);
            if (xFirst) {
                index = PathFinder.apply(result, xDiff, index, EW);
                PathFinder.apply(result, zDiff, index, SN);
            } else {
                index = PathFinder.apply(result, zDiff, index, SN);
                PathFinder.apply(result, xDiff, index, EW);
            }
        } else {
            if (xFirst) {
                index = PathFinder.apply(result, xDiff, index, EW);
                index = PathFinder.apply(result, zDiff, index, SN);
            } else {
                index = PathFinder.apply(result, zDiff, index, SN);
                index = PathFinder.apply(result, xDiff, index, EW);
            }
            PathFinder.apply(result, yDiff, index, UD);
        }
        return result;
    }

    private static int apply(EnumFacing[] array, int diff, int start, EnumFacing[] facings) {
        int i;
        for (i = 0; i < Math.abs(diff); ++i) {
            array[i + start] = diff > 0 ? facings[0] : facings[1];
        }
        return start + i;
    }

    static {
        CHECK = (b, d, e) -> {
            if (e == null || !e.preventEntitySpawning || EntityUtil.isDead(e) || !PositionUtil.intersects(e.getEntityBoundingBox(), b)) {
                return true;
            }
            if (d != null && e instanceof EntityEnderCrystal) {
                d.getBlockingEntities().add(new BlockingEntity((Entity)e, (BlockPos)b));
                return false;
            }
            return false;
        };
        SN = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.NORTH};
        EW = new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST};
        UD = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};
        CHECKS = new boolean[][]{{true, false}, {false, true}, {true, true}, {false, false}};
        CACHE = new PathCache(1365, 8, 7.0);
        CACHE.cache();
    }
}
