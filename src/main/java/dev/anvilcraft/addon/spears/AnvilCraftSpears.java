package dev.anvilcraft.addon.spears;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/**
 * AnvilCraft: Spears。
 *
 * <p>本附属不新增任何物品，只做一件事：把 AnvilCraft「重戟」的矛模式（{@code HeavyHalberdItem.SPEAR_MODE}）
 * 接到 Backported Spears 的矛机制上。</p>
 *
 * <p>实现全部在 {@code mixin} 包里：重戟本质是“一把武器切换四种形态”，
 * 而 Backported Spears 的全部战斗逻辑又都由物品的数据组件驱动，
 * 所以只要在矛模式下把对应的数据组件挂到重戟的物品栈上，重戟就自动变成一把真正的矛。</p>
 */
@Mod(AnvilCraftSpears.MOD_ID)
public class AnvilCraftSpears {
    public static final String MOD_ID = "anvilcraft_spears";
    public static final String MOD_NAME = "AnvilCraft: Spears";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AnvilCraftSpears(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("AnvilCraft heavy halberd spear mode is now backed by Backported Spears");
    }
}
