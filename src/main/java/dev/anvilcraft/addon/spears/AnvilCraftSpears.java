package dev.anvilcraft.addon.spears;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(AnvilCraftSpears.MOD_ID)
public class AnvilCraftSpears {
    public static final String MOD_ID = "anvilcraft_spears";
    public static final String MOD_NAME = "AnvilCraft: Spears";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AnvilCraftSpears(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("AnvilCraft heavy halberd spear mode is now backed by Backported Spears");
    }
}
