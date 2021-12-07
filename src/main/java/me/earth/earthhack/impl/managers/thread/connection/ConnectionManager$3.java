package me.earth.earthhack.impl.managers.thread.connection;

import java.util.UUID;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.impl.event.events.network.ConnectionEvent;
import me.earth.earthhack.impl.managers.thread.lookup.LookUp;

class ConnectionManager$3
extends LookUp {
    ConnectionManager$3(LookUp.Type type, UUID uuid) {
        super(type, uuid);
    }

    @Override
    public void onSuccess() {
        Bus.EVENT_BUS.post(new ConnectionEvent.Leave(this.name, this.uuid, null));
    }

    @Override
    public void onFailure() {
    }
}
