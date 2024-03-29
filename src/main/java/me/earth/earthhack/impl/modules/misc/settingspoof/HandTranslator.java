package me.earth.earthhack.impl.modules.misc.settingspoof;

import net.minecraft.util.EnumHandSide;

public enum HandTranslator {
    Left(EnumHandSide.LEFT),
    Right(EnumHandSide.RIGHT);

    private final EnumHandSide handSide;

    private HandTranslator(EnumHandSide visibility) {
        this.handSide = visibility;
    }

    public EnumHandSide getHandSide() {
        return this.handSide;
    }
}
