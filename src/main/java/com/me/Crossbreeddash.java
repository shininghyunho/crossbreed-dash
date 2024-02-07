package com.me;

import com.me.items.GuiItem;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crossbreeddash implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("crossbreed-dash");

	public static Item GUI_ITEM = new GuiItem(new Item.Settings().maxCount(1));
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Fabric world!");

		// Register the GUI_ITEM under the id "gui_item"
		Registry.register(Registries.ITEM,new Identifier("crossbreed-dash","gui_item"),GUI_ITEM);
	}
}