package me.earth.earthhack.impl.modules.client.safety;

import me.earth.earthhack.api.module.data.DefaultData;
import me.earth.earthhack.impl.modules.client.safety.Safety;
import me.earth.earthhack.impl.util.minecraft.ICachedDamage;

final class SafetyData
extends DefaultData<Safety> {
    public SafetyData(Safety module) {
        super(module);
        this.register("MaxDamage", "The MaxDamage that counts as unsafe. If a crystal or a position around you can be found that deals more damage than this to you your SafetyState counts as \"unsafe\" which will make modules like AutoTotem more strict.");
        this.register("BedCheck", "Takes beds into account. Use this on servers where beds are common.");
        this.register("1.13+", "Takes changes to minecraft mechanics after 1.13+ into account. Use on ViaVersion servers that are on one of those version.");
        this.register("SafetyPlayer", "Will be less strict if no player in range of a damaging crystal/position can be found. Be careful not to blow yourself up!");
        this.register("Updates", "Tick: will update each tick (~50ms), Delay: will updated in Intervals specified by the Delay setting.");
        this.register("Delay", "The interval in milliseconds to check your position with if Updates is set to Delay. The lower this setting the higher the workload on your CPU.");
        this.register("2x1s", "Deems 2x1 holes safe.");
        this.register("2x2s", "Deems 2x2 holes safe.");
        this.register("Post-Calc", "Updates Safety immediately after we updated our position.");
        this.register(ICachedDamage.SHOULD_CACHE, "Caches some values in order to prevent MultiThreading problems.");
        this.register("Terrain", "If Terrain should be taken into account.");
        this.register("Anvils", "Only use when u expected to get Anvil - AntiSurrounded.");
    }

    @Override
    public int getColor() {
        return -65536;
    }

    @Override
    public String getDescription() {
        return "Manages your Safety, doesn't matter if this module is on or not.";
    }
}
