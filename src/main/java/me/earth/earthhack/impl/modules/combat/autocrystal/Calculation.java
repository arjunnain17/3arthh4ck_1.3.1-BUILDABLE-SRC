package me.earth.earthhack.impl.modules.combat.autocrystal;

import java.util.List;
import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.modules.combat.autocrystal.AbstractCalculation;
import me.earth.earthhack.impl.modules.combat.autocrystal.AutoCrystal;
import me.earth.earthhack.impl.modules.combat.autocrystal.util.CrystalData;
import me.earth.earthhack.impl.modules.combat.autocrystal.util.IBreakHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class Calculation
extends AbstractCalculation<CrystalData>
implements Globals {
    public Calculation(AutoCrystal module, List<Entity> entities, List<EntityPlayer> players, BlockPos ... blackList) {
        super(module, entities, players, blackList);
    }

    public Calculation(AutoCrystal module, List<Entity> entities, List<EntityPlayer> players, boolean breakOnly, boolean noBreak, BlockPos ... blackList) {
        super(module, entities, players, breakOnly, noBreak, blackList);
    }

    @Override
    protected IBreakHelper<CrystalData> getBreakHelper() {
        return this.module.breakHelper;
    }
}
