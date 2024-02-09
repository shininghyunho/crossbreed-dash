package me.cross;

import me.cross.entity.CubeEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cross implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("cross");

	public static final EntityType<CubeEntity> CUBE = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("cross", "cube"),
			FabricEntityTypeBuilder
					.create(SpawnGroup.CREATURE,CubeEntity::new)
					.dimensions(EntityDimensions.fixed(0.75F, 0.75F))
					.build()
	);

	public static final Item IRON_GOLDEN_SPAWN_EGG = new SpawnEggItem(EntityType.IRON_GOLEM, 0xc4c4c4, 0xadadad, new FabricItemSettings());

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		// add the spawn egg to MISC item group
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> content.add(IRON_GOLDEN_SPAWN_EGG));
		// Register the spawn egg
		Registry.register(Registries.ITEM,new Identifier("cross","iron_golem_spawn_egg"),IRON_GOLDEN_SPAWN_EGG);
		// Register the entity
		FabricDefaultAttributeRegistry.register(CUBE, CubeEntity.createMobAttributes());

	}
}