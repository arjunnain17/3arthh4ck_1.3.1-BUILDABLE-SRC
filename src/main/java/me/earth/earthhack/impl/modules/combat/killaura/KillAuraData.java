package me.earth.earthhack.impl.modules.combat.killaura;

import me.earth.earthhack.impl.modules.combat.killaura.KillAura;
import me.earth.earthhack.impl.util.minecraft.entity.module.EntityTypeData;

final class KillAuraData
extends EntityTypeData<KillAura> {
    public KillAuraData(KillAura module) {
        super(module);
        this.register(module.passengers, "Attacks your own Passengers.");
        this.register(module.targetMode, "-Closest will target the closest Player, Takes Health and Armor into account.\n-Angle will target the closest Player to where you aim.");
        this.register(module.prioEnemies, "Prioritizes Enemies over armor, health and distance.");
        this.register(module.range, "Range in which targets will be hit.");
        this.register(module.wallRange, "Range in which targets will be hit through walls.");
        this.register(module.swordOnly, "Only attacks if you hold a Sword or Axe.");
        this.register(module.delay, "If on applies 1.9+ Delays to all weapons. Otherwise applies the CPS Setting.");
        this.register(module.cps, "Clicks/second when not using Delay or using a 32k.");
        this.register(module.rotate, "Rotates to the targets.");
        this.register(module.stopSneak, "Stops sneaking when attacking.");
        this.register(module.stopSprint, "Stops sprinting when attacking.");
        this.register(module.stopShield, "Hold rightclick to block.");
        this.register(module.whileEating, "Allows you to hit while eating.");
        this.register(module.stay, "Keeps rotations. Recommended when using Soft or Rotation-Ticks.");
        this.register(module.rotationTicks, "Ticks to stay rotated towards the entity before attacking it.");
        this.register(module.soft, "Soft Rotations. Maximum Angle to rotate per tick.");
        this.register(module.auraTeleport, "- None won't teleport.\n-Smart only teleports so the target is in Range.\n-Full teleports into the target.");
        this.register(module.teleportRange, "Range for teleports.");
        this.register(module.yTeleport, "Teleports you down/up.");
        this.register(module.movingTeleport, "If Off: Won't teleport you if you hold any of the WASD keys.");
        this.register(module.swing, "-None won't swing at all.\n-Packet will swing on the server.\n-Full will swing both on client and server.\n-Client will only swing client-sided.");
        this.register(module.tps, "Syncs your attacks with the TPS.");
        this.register(module.t2k, "Ignores Delay and uses the CPS Setting when attacking with a 32k.");
        this.register(module.health, "Targets with a health lower than this will be prioritized.");
        this.register(module.armor, "Targets wearing armor with a durability lower than this value will be prioritized.");
        this.register(module.targetRange, "Only players within this range will be targeted.");
        this.register(module.multi32k, "Attacks multiple targets at the same time when using 32ks.");
        this.register(module.packets, "Can send multiple Packets when using a 32k.");
        this.register(module.height, "Part of the targets Hitbox to attack. The higher the value the higher the target will be attacked. A value of 1 means the head will be attacked.");
        this.register(module.ridingTeleports, "Allows teleports when riding an Entity.");
        this.register(module.efficient, "Very smart.");
        this.register(module.cancelEntityEquip, "Cancels SPacketEntityEquipment.");
        this.register(module.tpInfo, "Displays ArrayList info even when not attacking.");
    }
}
