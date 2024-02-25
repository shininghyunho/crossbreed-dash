package me.cross.handler.race;

import me.cross.Cross;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;

import java.util.UUID;

public class ScoreboardHandler {
    public static void init() {
        command("/scoreboard objectives add TOTAL_SCORE dummy \"내 점수\"");
        command("/scoreboard players set @a TOTAL_SCORE 0");
        command("/scoreboard objectives setdisplay sidebar TOTAL_SCORE");
    }

    public static void remove() {
        command("/scoreboard objectives remove TOTAL_SCORE");
    }

    public static void updateScoreboard() {
        // getTotalScore
        for(UUID playerUUID : TotalScoreHandler.getTotalScoreMap().keySet()) {
            int score = TotalScoreHandler.getTotalScore(playerUUID);
            PlayerEntity player = Cross.server.getPlayerManager().getPlayer(playerUUID);
            if(player==null) continue;
            command("/scoreboard players set " + player.getDisplayName().getString() + " TOTAL_SCORE " + score);
        }
    }

    private static void command(String command) {
        MinecraftServer server = Cross.server;
        if(server == null) return;
        CommandManager commandManager = server.getCommandManager();
        commandManager.executeWithPrefix(server.getCommandSource(), command);
    }
}
