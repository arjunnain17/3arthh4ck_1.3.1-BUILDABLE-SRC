package me.earth.earthhack.impl.modules.render.breadcrumbs;

import me.earth.earthhack.impl.event.events.network.DisconnectEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.modules.render.breadcrumbs.BreadCrumbs;

final class ListenerLogout
extends ModuleListener<BreadCrumbs, DisconnectEvent> {
    public ListenerLogout(BreadCrumbs module) {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void invoke(DisconnectEvent event) {
        if (((BreadCrumbs)this.module).clearL.getValue().booleanValue()) {
            mc.addScheduledTask(((BreadCrumbs)this.module).positions::clear);
        }
    }
}
