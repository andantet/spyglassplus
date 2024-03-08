package com.teamfusion.spyglassplus.mixin;

import com.teamfusion.spyglassplus.event.ItemUsageCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "usageTick", at = @At("TAIL"))
    private void onUsageTick(World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;
        ItemUsageCallback.Companion.getEVENT().invoker().onUsageTick(world, user, stack, remainingUseTicks);
    }
}
