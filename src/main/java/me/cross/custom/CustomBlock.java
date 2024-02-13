package me.cross.custom;

import me.cross.Cross;
import me.cross.handler.CheckPointBlockHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomBlock {
    // very large float value
    private static final float VERY_LARGE_FLOAT = 6000000F;
    private static final float UNBREAKABLE_BLOCK_HARDNESS = VERY_LARGE_FLOAT;
    private static final float UNBREAKABLE_BLOCK_RESISTANCE = VERY_LARGE_FLOAT;

    // 경기장 바닥
    public static final Block UNBREAKABLE_DIRT_PATH_BLOCK = registerBlock("unbreakable_dirt_path",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    // 경기장 울타리 TODO : 지금은 cube 블록인데 fence 블록으로 변경해야함
    public static final Block UNBREAKABLE_FENCE_BLOCK = registerBlock("unbreakable_fence",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    // 총 10개의 체크포인트 블록을 등록
    public static final Block UNBREAKABLE_CHECKPOINT_BLOCK_0 = registerBlock("unbreakable_checkpoint_0",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    public static final Block UNBREAKABLE_CHECKPOINT_BLOCK_1 = registerBlock("unbreakable_checkpoint_1",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    public static final Block UNBREAKABLE_CHECKPOINT_BLOCK_2 = registerBlock("unbreakable_checkpoint_2",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    public static final Block UNBREAKABLE_CHECKPOINT_BLOCK_3 = registerBlock("unbreakable_checkpoint_3",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    public static final Block UNBREAKABLE_CHECKPOINT_BLOCK_4 = registerBlock("unbreakable_checkpoint_4",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    public static final Block UNBREAKABLE_CHECKPOINT_BLOCK_5 = registerBlock("unbreakable_checkpoint_5",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    public static final Block UNBREAKABLE_CHECKPOINT_BLOCK_6 = registerBlock("unbreakable_checkpoint_6",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    public static final Block UNBREAKABLE_CHECKPOINT_BLOCK_7 = registerBlock("unbreakable_checkpoint_7",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    public static final Block UNBREAKABLE_CHECKPOINT_BLOCK_8 = registerBlock("unbreakable_checkpoint_8",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);
    public static final Block UNBREAKABLE_CHECKPOINT_BLOCK_9 = registerBlock("unbreakable_checkpoint_9",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);

    public static void registerCustomBlock() {
        Cross.LOGGER.info("Registering custom blocks");
        addCheckpointBlocks();
    }
    private static void addCheckpointBlocks() {
        CheckPointBlockHandler.addCheckpointBlock(UNBREAKABLE_CHECKPOINT_BLOCK_0);
        CheckPointBlockHandler.addCheckpointBlock(UNBREAKABLE_CHECKPOINT_BLOCK_1);
        CheckPointBlockHandler.addCheckpointBlock(UNBREAKABLE_CHECKPOINT_BLOCK_2);
        CheckPointBlockHandler.addCheckpointBlock(UNBREAKABLE_CHECKPOINT_BLOCK_3);
        CheckPointBlockHandler.addCheckpointBlock(UNBREAKABLE_CHECKPOINT_BLOCK_4);
        CheckPointBlockHandler.addCheckpointBlock(UNBREAKABLE_CHECKPOINT_BLOCK_5);
        CheckPointBlockHandler.addCheckpointBlock(UNBREAKABLE_CHECKPOINT_BLOCK_6);
        CheckPointBlockHandler.addCheckpointBlock(UNBREAKABLE_CHECKPOINT_BLOCK_7);
        CheckPointBlockHandler.addCheckpointBlock(UNBREAKABLE_CHECKPOINT_BLOCK_8);
        CheckPointBlockHandler.addCheckpointBlock(UNBREAKABLE_CHECKPOINT_BLOCK_9);
    }

    private static Block registerBlock(String name, Block block, RegistryKey<ItemGroup> itemGroupKey) {
        try {
            // register blockItem and add item group key
            Item item = registerBlockItem(name, block);
            ItemGroupEvents.modifyEntriesEvent(itemGroupKey).register(content -> {
                content.add(item);
            });
            // register block
            Registry.register(Registries.BLOCK, new Identifier(Cross.MOD_ID, name), block);
        } catch (Exception e) {
            Cross.LOGGER.error("Failed to register block: " + name, e);
        }
        return block;
    }
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(Cross.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }
}
