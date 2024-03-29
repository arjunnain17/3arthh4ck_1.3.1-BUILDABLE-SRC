package me.earth.earthhack.impl.managers.thread.safety;

import me.earth.earthhack.api.event.events.Stage;
import me.earth.earthhack.impl.event.events.network.MotionUpdateEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.managers.thread.safety.SafetyManager;

final class ListenerMotionUpdate
extends ModuleListener<SafetyManager, MotionUpdateEvent> {
    public ListenerMotionUpdate(SafetyManager module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void invoke(MotionUpdateEvent event) {
        if (event.getStage() == Stage.POST && ((SafetyManager)this.module).post.getValue().booleanValue()) {
            ((SafetyManager)this.module).runThread();
        }
    }
}
