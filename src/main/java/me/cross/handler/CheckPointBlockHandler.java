package me.cross.handler;

import me.cross.Cross;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class CheckPointBlockHandler {
    private static final List<String> checkpointBlocks = new ArrayList<>();
    // 체크포인트 블록마다 좌표들 저장
    private static final Map<String, Set<BlockPos>> checkpointBlockPosMap = new HashMap<>();

    // 생성자
    public CheckPointBlockHandler() {
        for(String name : checkpointBlocks) checkpointBlockPosMap.put(name, new HashSet<>());
    }

    public static void addCheckpointBlock(Block block) {
        checkpointBlocks.add(block.getTranslationKey());
        Cross.LOGGER.info("Add checkpoint block : " + block.getTranslationKey());
    }

    public static boolean isCheckpoint(Block block) {
        return checkpointBlocks.contains(block.getTranslationKey());
    }

    // 체크포인트 블록마다 좌표 추가
    public static void addCheckPointPos(Block block, BlockPos blockPos) {
        if(checkpointBlockPosMap.containsKey(block.getTranslationKey())) checkpointBlockPosMap.get(block.getTranslationKey()).add(blockPos);
    }

    // 체크포인트 블록마다 좌표 제거
    public static void removeCheckPointPos(Block block, BlockPos blockPos) {
        if(checkpointBlockPosMap.containsKey(block.getTranslationKey())) checkpointBlockPosMap.get(block.getTranslationKey()).remove(blockPos);
    }
}
