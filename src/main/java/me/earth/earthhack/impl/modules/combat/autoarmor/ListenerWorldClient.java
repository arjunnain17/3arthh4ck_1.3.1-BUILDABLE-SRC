package me.earth.earthhack.impl.modules.combat.autoarmor;

import me.earth.earthhack.impl.event.events.network.WorldClientEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.combat.autoarmor.AutoArmor;

final class ListenerWorldClient
extends ModuleListener<AutoArmor, WorldClientEvent.Load> {
    public ListenerWorldClient(AutoArmor module) {
        super(module, WorldClientEvent.Load.class);
    }

    @Override
    public void invoke(WorldClientEvent.Load event) {
        ((AutoArmor)this.module).putBackClick = null;
    }
}
