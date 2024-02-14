package me.cross.handler;

import me.cross.Cross;
import me.cross.custom.CustomBlock;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class CheckPointBlockHandler {
    private static final String CHECKPOINT_BLOCK_POS_MAP_KEY = "CheckpointBlockPosMap";
    private static final List<String> checkpointBlocks = new ArrayList<>();
    // 체크포인트 블록마다 좌표들 저장
    private static final Map<String, Set<BlockPos>> checkpointBlockPosMap = new HashMap<>();

    public static void addCheckpointBlock(Block block) {
        checkpointBlocks.add(block.getTranslationKey());
        Cross.LOGGER.info("Add checkpoint block : " + block.getTranslationKey());
    }

    public static void initCheckPointBlockPosMap() {
        addCheckpointBlock(CustomBlock.UNBREAKABLE_CHECKPOINT_BLOCK_0);
        addCheckpointBlock(CustomBlock.UNBREAKABLE_CHECKPOINT_BLOCK_1);
        addCheckpointBlock(CustomBlock.UNBREAKABLE_CHECKPOINT_BLOCK_2);
        addCheckpointBlock(CustomBlock.UNBREAKABLE_CHECKPOINT_BLOCK_3);
        addCheckpointBlock(CustomBlock.UNBREAKABLE_CHECKPOINT_BLOCK_4);
        addCheckpointBlock(CustomBlock.UNBREAKABLE_CHECKPOINT_BLOCK_5);
        addCheckpointBlock(CustomBlock.UNBREAKABLE_CHECKPOINT_BLOCK_6);
        addCheckpointBlock(CustomBlock.UNBREAKABLE_CHECKPOINT_BLOCK_7);
        addCheckpointBlock(CustomBlock.UNBREAKABLE_CHECKPOINT_BLOCK_8);
        addCheckpointBlock(CustomBlock.UNBREAKABLE_CHECKPOINT_BLOCK_9);
        for(String name : checkpointBlocks) checkpointBlockPosMap.put(name, new HashSet<>());
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

    public static boolean isPlayerAtIdxPoint(int x, int z, int idx) {
        if(idx < 0 || idx >= checkpointBlocks.size()) return false;
        String blockName = checkpointBlocks.get(idx);
        if(!checkpointBlockPosMap.containsKey(blockName)) return false;
        return checkpointBlockPosMap.get(blockName).stream().anyMatch(pos -> pos.getX() == x && pos.getZ() == z);
    }

    // 블록 좌표를 nbt 에 저장
    public static void writeToNbt(NbtCompound nbt) {
        NbtCompound checkpointBlockPosMapNbt = new NbtCompound();
        for(String name : checkpointBlockPosMap.keySet()) {
            NbtCompound blockPosSetNbt = new NbtCompound();
            for(BlockPos blockPos : checkpointBlockPosMap.get(name)) {
                NbtCompound blockPosNbt = new NbtCompound();
                blockPosNbt.putInt("x", blockPos.getX());
                blockPosNbt.putInt("y", blockPos.getY());
                blockPosNbt.putInt("z", blockPos.getZ());
                blockPosSetNbt.put(blockPos.toShortString(), blockPosNbt);
            }
            checkpointBlockPosMapNbt.put(name, blockPosSetNbt);
        }
        nbt.put(CHECKPOINT_BLOCK_POS_MAP_KEY, checkpointBlockPosMapNbt);
    }

    public static void readFromNbt(NbtCompound nbt) {
        NbtCompound checkpointBlockPosMapNbt = nbt.getCompound(CHECKPOINT_BLOCK_POS_MAP_KEY);
        for(String name : checkpointBlockPosMapNbt.getKeys()) {
            NbtCompound blockPosSetNbt = checkpointBlockPosMapNbt.getCompound(name);
            for(String blockPosString : blockPosSetNbt.getKeys()) {
                NbtCompound blockPosNbt = blockPosSetNbt.getCompound(blockPosString);
                BlockPos blockPos = new BlockPos(blockPosNbt.getInt("x"), blockPosNbt.getInt("y"), blockPosNbt.getInt("z"));
                checkpointBlockPosMap.get(name).add(blockPos);
            }
        }
    }
}
