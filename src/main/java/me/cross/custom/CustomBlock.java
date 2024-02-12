package me.cross.custom;

import me.cross.Cross;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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

    // register unbreakable dirt path block
    public static final Block UNBREAKABLE_DIRT_PATH_BLOCK = registerBlock("unbreakable_dirt_path",
            new Block(FabricBlockSettings.create().strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE)),
            ItemGroups.BUILDING_BLOCKS);

    public static void registerCustomBlock() {
        Cross.LOGGER.info("Registering custom blocks");
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
