package com.dividedby0.healthscaling.item;

import com.dividedby0.healthscaling.HealthScalingMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HealthScalingMod.MODID);

    public static final RegistryObject<Item> HEART_CONTAINER =
            ITEMS.register("heart_container",
                    () -> new HeartContainerItem(new Item.Properties().stacksTo(16)));
}
