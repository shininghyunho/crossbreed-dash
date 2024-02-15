package me.cross.custom.event.horse;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface HorseBondWithPlayerCallback {
    Event<HorseBondWithPlayerCallback> EVENT = EventFactory.createArrayBacked(HorseBondWithPlayerCallback.class,
            (listeners) -> (player, horse) -> {
                for (HorseBondWithPlayerCallback listener : listeners) {
                    listener.interact(player, horse);
                }
            });

    void interact(PlayerEntity player, AbstractHorseEntity horse);
}
