package dev.anvilcraft.addon.spears.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.client.renderer.item.CrabClawItemInHandRenderer;
import dev.dubhe.anvilcraft.item.HeavyHalberdItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrabClawItemInHandRenderer.class)
public abstract class CrabClawItemInHandRendererMixin {
    @Unique
    private static final TagKey<Item> SPEARS_TAG =
        TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("spears"));

    @Inject(method = "isSpearLike", at = @At("RETURN"), cancellable = true)
    private static void anvilcraftspears$recognizeSpearTag(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && anvilcraftspears$isTaggedSpear(stack)) {
            cir.setReturnValue(true);
        }
    }

    @WrapOperation(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
            ordinal = 4
        )
    )
    private void anvilcraftspears$shrinkTaggedSpearOffset(
        PoseStack pose, float x, float y, float z, Operation<Void> original,
        @Local(argsOnly = true) ItemStack stack
    ) {
        if (anvilcraftspears$isTaggedSpear(stack)) {
            // 原参数是 -0.23 * sign，这里只改绝对值，符号仍跟随左右手。
            float sign = x > 0.0F ? -1.0F : 1.0F;
            original.call(pose, 0.1F * sign, y, z);
        } else {
            original.call(pose, x, y, z);
        }
    }

    @Unique
    private static boolean anvilcraftspears$isTaggedSpear(ItemStack stack) {
        return stack.is(SPEARS_TAG) && !(stack.getItem() instanceof HeavyHalberdItem);
    }
}
