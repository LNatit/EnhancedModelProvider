package com.lnatit.ccw;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CandyWorkshop.MODID)
public class CandyWorkshop {
    public static final String MODID = "ccw";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CandyWorkshop(IEventBus modEventBus, ModContainer modContainer) {

    }
}
