package me.cross.custom;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class CustomBlock {
    // string name of unbreakable_dirt_path
    public static final String UNBREAKABLE_DIRT_PATH_NAME = "unbreakable_dirt_path";
    // very large float value
    private static final float VERY_LARGE_FLOAT = 6000000F;
    private static final float UNBREAKABLE_BLOCK_HARDNESS = VERY_LARGE_FLOAT;
    private static final float UNBREAKABLE_BLOCK_RESISTANCE = VERY_LARGE_FLOAT;

    public static final Block UNBREAKABLE_DIRT_PATH = new Block(FabricBlockSettings
            .copyOf(Blocks.DIRT_PATH)
            .strength(UNBREAKABLE_BLOCK_HARDNESS, UNBREAKABLE_BLOCK_RESISTANCE));
}
