package me.cross.custom.event.race;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface RacingCallback {
    Event<RacingCallback> READY_FOR_RACING = EventFactory.createArrayBacked(RacingCallback.class,
            (listeners) -> () -> {
                for (RacingCallback listener : listeners) {
                    ActionResult result = listener.interact();
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    Event<RacingCallback> READY_FOR_RUNNING = EventFactory.createArrayBacked(RacingCallback.class,
            (listeners) -> () -> {
                for (RacingCallback listener : listeners) {
                    ActionResult result = listener.interact();
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    Event<RacingCallback> RUNNING = EventFactory.createArrayBacked(RacingCallback.class,
            (listeners) -> () -> {
                for (RacingCallback listener : listeners) {
                    ActionResult result = listener.interact();
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    Event<RacingCallback> FINISHED = EventFactory.createArrayBacked(RacingCallback.class,
            (listeners) -> () -> {
                for (RacingCallback listener : listeners) {
                    ActionResult result = listener.interact();
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    // 이벤트가 완전히 종료
    Event<RacingCallback> END = EventFactory.createArrayBacked(RacingCallback.class,
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