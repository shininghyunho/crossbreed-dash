package me.cross.handler.horse;

import me.cross.Cross;
import me.cross.entity.HorseAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;

/**
 * command 생성은 online generator 사용.
 */
public class HorseBookHandler {
    public static void giveHorseBook(PlayerEntity player, HorseAbility horseAbility) {
        // 말 책을 주는 코드
        MinecraftServer server = Cross.server;
        if(server == null) return;

        CommandManager commandManager = server.getCommandManager();
        commandManager.executeWithPrefix(server.getCommandSource(), getHorseBookCommand(player, horseAbility));
    }

    private static String getHorseBookCommand(PlayerEntity player, HorseAbility horseAbility) {
        // 유저 이름
        String playerName = player.getDisplayName().getString();
        // 말 이름
        String horseName = horseAbility.name;
        // 말 속도 문자열 소수점 2자리 까지
        String speed = String.format("%.2f", horseAbility.speedMultiplier);
        // 말 점프력
        String jump = String.format("%.2f", horseAbility.jumpMultiplier);
        // 말 변덕
        String crazy = String.format("%.5f", horseAbility.crazyFactor);
        // 말 책을 주는 명령어를 생성하는 코드
        return "give "+playerName+" written_book{pages:['[[\"\",{\"text\":\""+horseName+"\",\"color\":\"dark_aqua\",\"bold\":true}" +
                ",\"의 능력\\\\n\\\\n속도 : \",{\"text\":\""+speed+"\",\"color\":\"dark_green\"}" +
                ",\"\\\\n점프력 : \",{\"text\":\""+jump+"\",\"color\":\"dark_green\"}" +
                ",\"\\\\n변덕 : \",{\"text\":\""+crazy+"\",\"color\":\"dark_green\"}]]']" +
                ",title:"+horseName+",author:"+playerName+"}";
    }
}
