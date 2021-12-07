package me.earth.earthhack.impl.modules.player.speedmine.mode;

import java.awt.Color;
import me.earth.earthhack.impl.modules.player.speedmine.Speedmine;
import me.earth.earthhack.impl.modules.player.speedmine.mode.ESPMode;
import me.earth.earthhack.impl.util.render.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;

final class ESPMode$3
extends ESPMode {
    @Override
    public void drawEsp(Speedmine module, AxisAlignedBB bb, float damage) {
        RenderUtil.startRender();
        float red = 255.0f - 255.0f * damage;
        float green = 255.0f * damage;
        RenderUtil.drawBox(bb, new Color((int)red, (int)green, 0, module.getBlockAlpha()));
        RenderUtil.endRender();
    }
}
