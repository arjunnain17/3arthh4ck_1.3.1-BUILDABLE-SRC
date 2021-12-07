package me.earth.earthhack.impl.managers.client.macro;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.keyboard.KeyboardEvent;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.earthhack.impl.managers.client.macro.Macro;
import me.earth.earthhack.impl.managers.client.macro.MacroType;
import me.earth.earthhack.impl.util.text.ChatUtil;

class MacroManager$1
extends EventListener<KeyboardEvent> {
    MacroManager$1(Class target, int priority) {
        super(target, priority);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void invoke(KeyboardEvent event) {
        for (Macro macro : MacroManager.this.getRegistered()) {
            if (macro.getType() == MacroType.DELEGATE || macro.getBind().getKey() != event.getKey() || macro.isRelease() == event.getEventState()) continue;
            try {
                MacroManager.this.safe = true;
                macro.execute(Managers.COMMANDS);
            }
            catch (Throwable t) {
                ChatUtil.sendMessage("\u00a7cAn error occurred while executing macro \u00a7f" + macro.getName() + "\u00a7c" + ": " + (t.getMessage() == null ? t.getClass().getName() : t.getMessage()) + ". I strongly recommend deleting it for now and checking your logic!");
                t.printStackTrace();
            }
            finally {
                MacroManager.this.safe = false;
            }
        }
    }
}
