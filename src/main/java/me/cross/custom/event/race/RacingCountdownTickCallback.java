package me.cross.custom.event.race;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface RacingCountdownTickCallback {
    // countdown for tick
    Event<RacingCountdownTickCallback> COUNTDOWN_TICK = EventFactory.createArrayBacked(RacingCountdownTickCallback.class,
            (listeners) -> (nowTime) -> {
                for (RacingCountdownTickCallback listener : listeners) {
                    ActionResult result = listener.interact(nowTime);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(long nowTime);
}
