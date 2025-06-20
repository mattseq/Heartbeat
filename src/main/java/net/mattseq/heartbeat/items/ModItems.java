package net.mattseq.heartbeat.items;

import net.mattseq.heartbeat.Heartbeat;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Heartbeat.MODID);

    public static final RegistryObject<Item> TEST_WAND = ITEMS.register("test_wand",
            () -> new TestWandItem());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}