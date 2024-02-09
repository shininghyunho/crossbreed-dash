package me.cross;

import me.cross.entity.CubeEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
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
	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		FabricDefaultAttributeRegistry.register(CUBE, CubeEntity.createMobAttributes());
	}
}