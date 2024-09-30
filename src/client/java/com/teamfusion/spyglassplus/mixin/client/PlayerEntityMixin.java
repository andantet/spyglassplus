package com.teamfusion.spyglassplus.mixin.client;

import com.teamfusion.spyglassplus.entity.SpyglassStandEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "isUsingSpyglass", at = @At("HEAD"), cancellable = true)
    private void onIsUsingSpyglass(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity that = (PlayerEntity) (Object) this;
        if (that instanceof ClientPlayerEntity) {
            MinecraftClient client = MinecraftClient.getInstance();
            Entity cameraEntity = client.cameraEntity;
            if (cameraEntity instanceof SpyglassStandEntity) {
                cir.setReturnValue(true);
            }
        }
    }
}
