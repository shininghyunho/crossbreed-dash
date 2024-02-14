package me.cross.custom.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.cross.Cross;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public final class ModCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // execute startMod
        dispatcher.register(literal("startMod").executes(context -> {
            Cross.startMod();
            return Command.SINGLE_SUCCESS;
        }));

        // execute stopMod
        dispatcher.register(literal("stopMod").executes(context -> {
            Cross.stopMod();
            return Command.SINGLE_SUCCESS;
        }));
    }
}
