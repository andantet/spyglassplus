package com.teamfusion.spyglassplus.mixin.client;

import com.teamfusion.spyglassplus.client.event.ClientWorldBrightnessCallback;
import com.teamfusion.spyglassplus.event.TypedEventResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    private static void onGetBrightness(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        TypedEventResult<Float> typedResult = ClientWorldBrightnessCallback.Companion.getEVENT().invoker().getBrightness(client, client.player, cir.getReturnValueF());
        typedResult.ifCancelled(cir::setReturnValue);
    }
}
