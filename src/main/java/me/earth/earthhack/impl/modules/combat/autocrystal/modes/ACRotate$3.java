package me.earth.earthhack.impl.modules.combat.autocrystal.modes;

import me.earth.earthhack.impl.modules.combat.autocrystal.modes.ACRotate;

final class ACRotate$3
extends ACRotate {
    @Override
    public boolean noRotate(ACRotate rotate) {
        return rotate == Break || rotate == None;
    }
}
