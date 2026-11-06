package dev.anvilcraft.addon.spears;

import com.notunanancyowen.spears.Spears;
import dev.dubhe.anvilcraft.init.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

/** 临时自检：仅在开发环境验证“矛模式 → 组件 → 使用行为”这条链路。 */
@EventBusSubscriber(modid = AnvilCraftSpears.MOD_ID)
public final class DebugProbe {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ItemStack halberd = new ItemStack(ModItems.EMBER_METAL_HEAVY_HALBERD.get());
        AnvilCraftSpears.LOGGER.info("[probe] initial mode={} attRange={}", dev.dubhe.anvilcraft.item.HeavyHalberdItem.getMode(halberd), halberd.get(Spears.ATTACK_RANGE));

        halberd.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(dev.dubhe.anvilcraft.item.HeavyHalberdItem.SPEAR_MODE));
        HeavyHalberdSpearMode.sync(halberd);
        AnvilCraftSpears.LOGGER.info(
            "[probe] spear mode={} range={} piercing={} kinetic={} swing={} charge={} useEffects={}",
            dev.dubhe.anvilcraft.item.HeavyHalberdItem.getMode(halberd),
            halberd.get(Spears.ATTACK_RANGE),
            halberd.get(Spears.PIERCING_WEAPON) != null,
            halberd.get(Spears.KINETIC_WEAPON) != null,
            halberd.get(Spears.SWING_ANIMATION),
            halberd.get(Spears.MINIMUM_ATTACK_CHARGE),
            halberd.get(Spears.USE_EFFECTS) != null
        );
        AnvilCraftSpears.LOGGER.info(
            "[probe] anim={} duration={}",
            halberd.getItem().getUseAnimation(halberd),
            halberd.getItem().getUseDuration(halberd, null)
        );

        halberd.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(dev.dubhe.anvilcraft.item.HeavyHalberdItem.SWORD_MODE));
        HeavyHalberdSpearMode.sync(halberd);
        AnvilCraftSpears.LOGGER.info(
            "[probe] after leaving spear mode: range={} piercing={} anim={}",
            halberd.get(Spears.ATTACK_RANGE),
            halberd.get(Spears.PIERCING_WEAPON) != null,
            halberd.getItem().getUseAnimation(halberd)
        );
    }
}
