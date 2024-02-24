package me.cross.custom.event.race;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface RacingRemainTimeCallback {
    Event<RacingRemainTimeCallback> REMAIN_TIME = EventFactory.createArrayBacked(RacingRemainTimeCallback.class,
            (listeners) -> (nowTime) -> {
                for (RacingRemainTimeCallback listener : listeners) {
                    ActionResult result = listener.interact(nowTime);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });
    ActionResult interact(long nowTime);
}
