package me.cross.custom.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.cross.Cross;
import me.cross.handler.MessageHandler;
import me.cross.handler.RunningHandler;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public final class ModCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // execute startMod
        dispatcher.register(literal("start").executes(context -> {
            Cross.startMod();
            return Command.SINGLE_SUCCESS;
        }));

        // execute stopMod
        dispatcher.register(literal("stop").executes(context -> {
            Cross.stopMod();
            return Command.SINGLE_SUCCESS;
        }));

        // get result
        dispatcher.register(literal("result").executes(context -> {
            MessageHandler.broadcast(RunningHandler.getTotalResult());
            return Command.SINGLE_SUCCESS;
        }));
    }
}
