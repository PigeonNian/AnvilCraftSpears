package dev.anvilcraft.addon.spears.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.anvilcraft.addon.spears.HeavyHalberdSpearMode;
import dev.dubhe.anvilcraft.item.HeavyHalberdItem;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(com.notunanancyowen.spears.components.KineticWeapon.class)
abstract class SpearKineticWeaponMixin {
    @WrapOperation(
        method = "usageTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeBaseValue(Lnet/minecraft/core/Holder;)D"
        )
    )
    private double anvilcraftspears$applyHeavyHalberdAttackDamage(
        LivingEntity entity, Holder<Attribute> attribute, Operation<Double> original,
        @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) EquipmentSlot slot
    ) {
        double base = original.call(entity, attribute);
        if (!(stack.getItem() instanceof HeavyHalberdItem) || !HeavyHalberdSpearMode.isSpearMode(stack)) {
            return base;
        }
        return anvilcraftspears$computeAttribute(stack, attribute, base, slot);
    }

    private static double anvilcraftspears$computeAttribute(
        ItemStack stack, Holder<Attribute> attribute, double base, EquipmentSlot slot
    ) {
        double value = base;
        for (ItemAttributeModifiers.Entry entry : stack.getAttributeModifiers().modifiers()) {
            if (!entry.attribute().equals(attribute) || !entry.slot().test(slot)) continue;
            double amount = entry.modifier().amount();
            value += switch (entry.modifier().operation()) {
                case ADD_VALUE -> amount;
                case ADD_MULTIPLIED_BASE -> amount * base;
                case ADD_MULTIPLIED_TOTAL -> amount * value;
            };
        }
        return value;
    }
}
