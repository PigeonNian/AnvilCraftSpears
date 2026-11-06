package dev.anvilcraft.addon.spears.mixin;

import dev.anvilcraft.addon.spears.HeavyHalberdSpearMode;
import dev.dubhe.anvilcraft.item.HeavyHalberdItem;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeavyHalberdItem.class)
public abstract class HeavyHalberdItemMixin {
    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    private void anvilcraftspears$startSpearCharge(
        Level level,
        Player player,
        InteractionHand hand,
        CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (cir.getReturnValue().getResult() != InteractionResult.PASS) return;
        if (!HeavyHalberdSpearMode.isSpearMode(stack)) return;
        // 先补组件，再进入使用状态：客户端 getUseItem() 会捕获此刻的栈，
        // 若此时栈上没有 USE_EFFECTS，矛模组抵消 0.2 减速、允许疾跑的逻辑就不会生效。
        HeavyHalberdSpearMode.sync(stack);
        player.startUsingItem(hand);
        cir.setReturnValue(InteractionResultHolder.consume(stack));
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void anvilcraftspears$spearUseDuration(
        ItemStack stack,
        LivingEntity entity,
        CallbackInfoReturnable<Integer> cir
    ) {
        if (HeavyHalberdSpearMode.isSpearMode(stack)) cir.setReturnValue(72000);
    }

    @Inject(method = "setMode", at = @At("TAIL"))
    private static void anvilcraftspears$syncOnModeSwitch(Player player, InteractionHand hand, int mode, CallbackInfo ci) {
        HeavyHalberdSpearMode.sync(player.getItemInHand(hand));
    }

    @Inject(method = "verifyComponentsAfterLoad", at = @At("TAIL"))
    private void anvilcraftspears$syncOnLoad(ItemStack stack, CallbackInfo ci) {
        HeavyHalberdSpearMode.sync(stack);
    }

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void anvilcraftspears$syncOnTick(
        ItemStack stack,
        Level level,
        Entity entity,
        int slotId,
        boolean isSelected,
        CallbackInfo ci
    ) {
        HeavyHalberdSpearMode.sync(stack);
    }

    @Inject(method = "isEnchantmentActive", at = @At("HEAD"), cancellable = true)
    private static void anvilcraftspears$lungeOnlyInSpearMode(
        ItemStack stack,
        Holder<Enchantment> enchantment,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (!enchantment.isBound()) return;
        if (!HeavyHalberdSpearMode.isLungeEnchantment(enchantment)) return;
        cir.setReturnValue(
            HeavyHalberdSpearMode.isSpearMode(stack)
                && stack.is(enchantment.value().definition().supportedItems())
        );
    }
}
