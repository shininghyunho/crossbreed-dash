package com.me.crossbreeddash.item;

import com.me.crossbreeddash.CrossbreedDash;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // ITEMS : 아이템을 등록하는 DeferredRegister
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CrossbreedDash.MOD_ID);

    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire",
            () -> new Item(new Item.Properties()));

    // 아이템을 등록하는 메소드
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
