package com.me.cross.item;

import com.me.cross.Cross;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Cross.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CROSS_TAB = CREATIVE_MODE_TABS.register("cross_tab",
            () -> CreativeModeTab.builder()
                    .icon(()->new ItemStack(ModItems.SAPPHIRE.get()))
                    .title(Component.translatable("creativetab.cross_tab"))
                    .displayItems((pParameters,pOutput) -> {
                        pOutput.accept(ModItems.SAPPHIRE.get());
                        pOutput.accept(ModItems.YOUTUBE.get());
                        // exisiting items
                        pOutput.accept(Items.DIAMOND);
                    })
                    .build());
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
