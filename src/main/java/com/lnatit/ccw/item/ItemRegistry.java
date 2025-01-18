package com.lnatit.ccw.item;

import com.lnatit.ccw.CandyWorkshop;
import com.lnatit.ccw.item.sugaring.Sugar;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(
                    Registries.DATA_COMPONENT_TYPE,
                    CandyWorkshop.MODID
            );

    public static final Supplier<DataComponentType<Sugar>> SUGAR_DCTYPE =
            DATA_COMPONENTS.registerComponentType(
                    "sugar",
                    sugarBuilder -> sugarBuilder.persistent(Sugar.CODEC).networkSynchronized(Sugar.STREAM_CODEC).cacheEncoding()
            );
    public static final DeferredItem<GummyItem> GUMMY_ITEM =
            ITEMS.registerItem(
                    "gummy",
                    GummyItem::new,
                    new Item.Properties().component(SUGAR_DCTYPE, Sugar.VANILLA)
            );
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(CandyWorkshop.MODID);

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
