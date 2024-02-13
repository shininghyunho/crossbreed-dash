package me.cross.custom.event.race;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface RacingCallback {
    Event<RacingCallback> READY = EventFactory.createArrayBacked(RacingCallback.class,
            (listeners) -> () -> {
                for (RacingCallback listener : listeners) {
                    ActionResult result = listener.interact();
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact();
}
