package org.lihe.modulargolemsallembracing.mixin;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.special.PickupGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lihe.modulargolemsmassivestorageupgrade.api.ModCapabilities;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = PickupGoal.class, remap = false)
public class PickupGoalMixin {

    @Shadow
    @Final
    private AbstractGolemEntity<?, ?> golem;

    @Inject(
            method = "handleLeftoverItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void omini$injectStoragePriority(ItemEntity item, @Nullable Player player, CallbackInfo ci) {
        golem.getCapability(ModCapabilities.GOLEM_STORAGE).ifPresent(storage -> {
            ItemStack stack = item.getItem();
            if (stack.isEmpty()) return;
            ItemStack remainder = storage.insertItem(stack, false);
            if (remainder.isEmpty()) {
                item.discard();
                ci.cancel();
            } else {
                item.setItem(remainder);
            }
        });
    }
}