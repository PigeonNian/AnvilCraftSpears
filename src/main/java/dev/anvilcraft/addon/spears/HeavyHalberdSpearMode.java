package dev.anvilcraft.addon.spears;

import com.notunanancyowen.spears.Spears;
import com.notunanancyowen.spears.components.AttackRange;
import com.notunanancyowen.spears.components.KineticWeapon;
import com.notunanancyowen.spears.components.PiercingWeapon;
import com.notunanancyowen.spears.components.SwingAnimation;
import com.notunanancyowen.spears.components.UseEffects;
import dev.dubhe.anvilcraft.item.HeavyHalberdItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Optional;

/**
 * 重戟“矛模式”与 Backported Spears 的桥接。
 *
 * <p>矛模组的战斗逻辑全部读物品栈上的数据组件（攻击距离、突刺、冲锋、挥砍动画……），
 * 因此只需在重戟处于矛模式时把这些组件挂上去、离开矛模式时摘掉，
 * 重戟就获得了：长距离突刺、蓄力冲锋（击落骑乘 / 击退 / 增伤）、stab 挥砍动画，
 * 以及矛模组的 {@code lunge}（突刺）附魔。</p>
 *
 * <p>注意：不能把这些组件写进物品的“默认组件”里——重戟的其它三种模式
 * （三叉戟 / 剑 / 重锤）必须保持原样，所以组件是“按模式”动态同步的。</p>
 */
public final class HeavyHalberdSpearMode {
    /** Backported Spears 的突刺附魔所对应的可附魔标签。 */
    public static final TagKey<Item> LUNGE_ENCHANTABLE =
        TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("enchantable/lunge"));

    private HeavyHalberdSpearMode() {
    }

    public static boolean isSpearMode(ItemStack stack) {
        return stack.getItem() instanceof HeavyHalberdItem
            && HeavyHalberdItem.getMode(stack) == HeavyHalberdItem.SPEAR_MODE;
    }

    public static boolean isLungeEnchantment(Holder<Enchantment> enchantment) {
        return enchantment.unwrapKey()
            .map(key -> key.location().equals(ResourceLocation.withDefaultNamespace("lunge")))
            .orElse(false);
    }

    /**
     * 让物品栈上的矛组件与当前模式保持一致（幂等，可以每 tick 调用）。
     */
    public static void sync(ItemStack stack) {
        if (!(stack.getItem() instanceof HeavyHalberdItem)) return;
        if (Spears.ATTACK_RANGE == null) return; // 数据组件尚未注册完成
        if (isSpearMode(stack)) {
            SpearComponents.applyTo(stack);
        } else {
            removeSpearComponents(stack);
        }
    }

    private static void removeSpearComponents(ItemStack stack) {
        if (Spears.USE_EFFECTS != null) stack.remove(Spears.USE_EFFECTS);
        if (Spears.ATTACK_RANGE != null) stack.remove(Spears.ATTACK_RANGE);
        if (Spears.SWING_ANIMATION != null) stack.remove(Spears.SWING_ANIMATION);
        if (Spears.PIERCING_WEAPON != null) stack.remove(Spears.PIERCING_WEAPON);
        if (Spears.KINETIC_WEAPON != null) stack.remove(Spears.KINETIC_WEAPON);
        if (Spears.MINIMUM_ATTACK_CHARGE != null) stack.remove(Spears.MINIMUM_ATTACK_CHARGE);
    }

    /**
     * 矛模式使用的组件数值。重戟比普通矛更重、更慢，但冲锋收益更高。
     *
     * <p>放在内部类里是为了延迟到“游戏已经注册完数据组件”之后再初始化。</p>
     */
    private static final class SpearComponents {
        private static final UseEffects USE_EFFECTS = new UseEffects(1.0F, true, false);
        private static final AttackRange ATTACK_RANGE = new AttackRange(2.0F, 4.5F);
        private static final SwingAnimation SWING_ANIMATION = new SwingAnimation(20, "stab");
        private static final PiercingWeapon PIERCING_WEAPON = new PiercingWeapon(
            0.25F, true, false,
            sound(Spears.SPEAR_ATTACK),
            sound(Spears.SPEAR_HIT)
        );
        private static final KineticWeapon KINETIC_WEAPON = new KineticWeapon(
            0.125F,  // hitboxMargin
            10,      // contactCooldownTicks
            10,      // delayTicks：蓄力 0.5 秒后才能触发冲锋
            KineticWeapon.Condition.ofMinSpeed(60, 6.5F),          // 击落骑乘
            KineticWeapon.Condition.ofMinSpeed(80, 4.8F),          // 击退
            KineticWeapon.Condition.ofMinRelativeSpeed(200, 4.2F), // 额外伤害
            0.38F,   // forwardMovement
            1.35F,   // damageMultiplier
            sound(Spears.SPEAR_USE),
            sound(Spears.SPEAR_HIT)
        );
        private static final Float MINIMUM_ATTACK_CHARGE = 1.0F;

        private static void applyTo(ItemStack stack) {
            put(stack, Spears.USE_EFFECTS, USE_EFFECTS);
            put(stack, Spears.ATTACK_RANGE, ATTACK_RANGE);
            put(stack, Spears.SWING_ANIMATION, SWING_ANIMATION);
            put(stack, Spears.PIERCING_WEAPON, PIERCING_WEAPON);
            put(stack, Spears.KINETIC_WEAPON, KINETIC_WEAPON);
            put(stack, Spears.MINIMUM_ATTACK_CHARGE, MINIMUM_ATTACK_CHARGE);
        }

        private static <T> void put(ItemStack stack, DataComponentType<T> type, T value) {
            if (!value.equals(stack.get(type))) stack.set(type, value);
        }

        private static Optional<Holder<SoundEvent>> sound(SoundEvent sound) {
            if (sound == null) return Optional.empty();
            return BuiltInRegistries.SOUND_EVENT.getResourceKey(sound)
                .flatMap(BuiltInRegistries.SOUND_EVENT::getHolder);
        }
    }
}
