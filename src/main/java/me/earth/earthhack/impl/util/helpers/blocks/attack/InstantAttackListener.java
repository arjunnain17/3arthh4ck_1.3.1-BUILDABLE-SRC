package me.earth.earthhack.impl.util.helpers.blocks.attack;

import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.util.helpers.blocks.attack.InstantAttackingModule;
import me.earth.earthhack.impl.util.math.rotation.RotationUtil;
import me.earth.earthhack.impl.util.minecraft.DamageUtil;
import me.earth.earthhack.impl.util.network.PacketUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketSpawnObject;

public class InstantAttackListener<M extends InstantAttackingModule>
extends ModuleListener<M, PacketEvent.Receive<SPacketSpawnObject>> {
    public InstantAttackListener(M module) {
        super(module, PacketEvent.Receive.class, SPacketSpawnObject.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketSpawnObject> event) {
        SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
        if (InstantAttackListener.mc.player == null || packet.getType() != 51 || !((InstantAttackingModule)this.module).getTimer().passed(((InstantAttackingModule)this.module).getBreakDelay()) || Managers.SWITCH.getLastSwitch() < (long)((InstantAttackingModule)this.module).getCooldown() || DamageUtil.isWeaknessed()) {
            return;
        }
        EntityEnderCrystal crystal = new EntityEnderCrystal(InstantAttackListener.mc.world, packet.getX(), packet.getY(), packet.getZ());
        try {
            this.attack(crystal);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    protected void attack(EntityEnderCrystal crystal) throws Throwable {
        if (!((InstantAttackingModule)this.module).shouldAttack(crystal)) {
            return;
        }
        float damage = DamageUtil.calculate(crystal, (EntityLivingBase)RotationUtil.getRotationPlayer());
        if (!((InstantAttackingModule)this.module).getPop().shouldPop(damage, ((InstantAttackingModule)this.module).getPopTime())) {
            return;
        }
        PacketUtil.attack(crystal);
        ((InstantAttackingModule)this.module).postAttack(crystal);
    }
}
