package me.earth.earthhack.impl.modules.combat.pistonaura;

import me.earth.earthhack.impl.modules.combat.pistonaura.PistonAura;
import me.earth.earthhack.impl.util.helpers.blocks.data.BlockPlacingData;

final class PistonAuraData
extends BlockPlacingData<PistonAura> {
    public PistonAuraData(PistonAura module) {
        super(module);
        this.register(module.multiDirectional, "Pistons orientate based on where you look. Use this on servers that don't require rotations for placing blocks, then the module can look anywhere to make the piston face the direction we want it to.");
        this.register(module.explode, "Breaks crystals.");
        this.register(module.breakDelay, "Delay in ms that crystals will be blown up with.");
        this.register(module.breakRange, "Range within which crystals will be blown up.");
        this.register(module.breakTrace, "Range within which crystals that are behind walls will be blown up.");
        this.register(module.placeRange, "Range within which Pistons, Redstone and Crystals will be placed.");
        this.register(module.placeTrace, "Rarely a server checks when you place Blocks through walls. Maximum range within which a block will be placed through a wall.");
        this.register(module.coolDown, "Takes switchcooldown after switching your mainhand slot into account.");
        this.register(module.suicide, "Will ignore damage dealt to you.");
        this.register(module.newVer, "Takes 1.13+ mechanics into account. Use on ViaVersion servers.");
        this.register(module.targetMode, "-Closest will target the closest enemy, regardless of him being in a hole or not. \n-FOV will target the player that you are looking at, but only if he is in a hole. \n-Calculates the best enemy in range to target.");
        this.register(module.instant, "Instantly attacks crystals.");
        this.register(module.confirmation, "Time for the server to confirm our blockplacements.");
        this.register(module.newVer, "Maximum time a cycle of Piston, Crystal, Redstone before its aborted and a new one begins.");
        String packet = "Analyzes packets to optimize cycles";
        this.register(module.explosions, packet);
        this.register(module.destroyEntities, packet);
        this.register(module.multiChange, packet);
        this.register(module.change, packet);
    }

    @Override
    public int getColor() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "Uses pistons and redstone to push crystals into enemies. (Needs Rewrite)";
    }
}
