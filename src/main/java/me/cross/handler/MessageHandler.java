package me.cross.handler;

import me.cross.Cross;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MessageHandler {
    private static MinecraftServer server;

    public static void broadcast(String message) {
        if(isServerNotAlive()) return;
        PlayerManager playerManager = server.getPlayerManager();
        playerManager.broadcast(Text.of(message), false);
    }

    public static void broadcast(String message, boolean overlay) {
        if(isServerNotAlive()) return;
        PlayerManager playerManager = server.getPlayerManager();
        playerManager.broadcast(Text.of(message), overlay);
    }

    public static void broadcastWithRed(String message, boolean overlay) {
        if(isServerNotAlive()) return;
        PlayerManager playerManager = server.getPlayerManager();

        // 글자 크기를 크게 설정
        Style style = Style.EMPTY
                .withColor(Formatting.RED)
                .withBold(true);
        Text styledText = Text.of(message).copy().setStyle(style);

        playerManager.broadcast(styledText, overlay);
    }

    public static void sendToPlayer(PlayerEntity controllingPlayer, String string) {
        if(isServerNotAlive()) return;
        controllingPlayer.sendMessage(Text.of(string), true);
    }

    private static boolean isServerNotAlive() {
        if(server == null) {
            MessageHandler.server = Cross.server;
            return server == null;
        }
        return false;
    }
}
