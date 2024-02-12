package me.cross.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface HorseBondWithPlayerCallback {
    Event<HorseBondWithPlayerCallback> EVENT = EventFactory.createArrayBacked(HorseBondWithPlayerCallback.class,
            (listeners) -> (player, horse) -> {
                for (HorseBondWithPlayerCallback listener : listeners) {
                    ActionResult result = listener.interact(player, horse);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player, AbstractHorseEntity horse);
}
