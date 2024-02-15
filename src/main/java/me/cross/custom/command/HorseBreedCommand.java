package me.cross.custom.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.cross.handler.HorseSummonHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HorseBreedCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // '/breed horseName1 horseName2 ' 하면 새로운 말 소환됨
        dispatcher.register(literal("breed")
                        .executes(context -> {
                            // 사용법 안내
                            context.getSource().sendFeedback(()-> Text.of("Usage: /breed <horseName1> <horseName2>"), false);
                            return Command.SINGLE_SUCCESS;
                        })
                .then(argument("name1", StringArgumentType.string()))
                .executes(context -> {
                    // 사용법 안내
                    context.getSource().sendFeedback(()-> Text.of("Usage: /breed <horseName1> <horseName2>"), false);
                    return Command.SINGLE_SUCCESS;
                })
                .then(argument("name2", StringArgumentType.string()))
                .executes(context -> {
                    String horseName1 = StringArgumentType.getString(context, "name1");
                    String horseName2 = StringArgumentType.getString(context, "name2");
                    HorseSummonHandler.summonHorse(horseName1, horseName2);
                    return Command.SINGLE_SUCCESS;
                }));
    }
}
