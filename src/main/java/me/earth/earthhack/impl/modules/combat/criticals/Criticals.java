package me.earth.earthhack.impl.modules.combat.criticals;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.modules.combat.criticals.CriticalsData;
import me.earth.earthhack.impl.modules.combat.criticals.ListenerUseEntity;
import me.earth.earthhack.impl.modules.combat.criticals.mode.CritMode;
import me.earth.earthhack.impl.util.math.StopWatch;

public class Criticals
extends Module {
    protected final Setting<CritMode> mode = this.register(new EnumSetting<CritMode>("Mode", CritMode.Packet));
    protected final Setting<Boolean> noDesync = this.register(new BooleanSetting("NoDesync", true));
    protected final Setting<Integer> delay = this.register(new NumberSetting<Integer>("Delay", 250, 0, 1000));
    protected final StopWatch timer = new StopWatch();

    public Criticals() {
        super("Criticals", Category.Combat);
        this.listeners.add(new ListenerUseEntity(this));
        this.setData(new CriticalsData(this));
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.getValue().name();
    }
}
