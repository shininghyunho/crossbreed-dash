package me.cross.custom.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.cross.handler.HorseSummonHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HorseBreedCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // '/breed horseName1 horseName2 ' 하면 새로운 말 소환됨
        dispatcher.register(literal("breed")
                .then(argument("name1", StringArgumentType.word())
                        .executes(context -> Command.SINGLE_SUCCESS)
                        .then(argument("name2", StringArgumentType.word())
                                .executes(context -> {
                                    // get parameters
                                    String horseName1 = StringArgumentType.getString(context, "name1");
                                    String horseName2 = StringArgumentType.getString(context, "name2");

                                    ServerCommandSource source = (ServerCommandSource) context.getSource();
                                    ServerPlayerEntity player = source.getPlayer();
                                    UUID playerUUID = Objects.requireNonNull(player).getUuid();

                                    HorseSummonHandler.summonBreedHorse(horseName1, horseName2, playerUUID);
                                    return Command.SINGLE_SUCCESS;
                                }))));

        /*dispatcher.register(literal("breed")
                .then(argument("name1", StringArgumentType.string()))
                .then(argument("name2", StringArgumentType.string()))
                .executes(context -> {
                    // get parameters
                    String horseName1 = StringArgumentType.getString(context, "name1");
                    String horseName2 = StringArgumentType.getString(context, "name2");

                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayer();
                    UUID playerUUID = Objects.requireNonNull(player).getUuid();

                    HorseSummonHandler.summonBreedHorse(horseName1, horseName2, playerUUID);
                    return Command.SINGLE_SUCCESS;
                }));*/
    }
}
