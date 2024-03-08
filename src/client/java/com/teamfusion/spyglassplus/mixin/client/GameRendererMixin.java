package com.teamfusion.spyglassplus.mixin.client;

import com.teamfusion.spyglassplus.client.event.FovMultiplierUpdateCallback;
import com.teamfusion.spyglassplus.event.TypedEventResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final
    MinecraftClient client;

    @Shadow private float fovMultiplier;

    @Inject(method = "updateFovMultiplier", at = @At("TAIL"))
    private void onUpdateFovMultiplier(CallbackInfo ci) {
        TypedEventResult<Float> typedResult = FovMultiplierUpdateCallback.Companion.getEVENT().invoker().getMultiplier(this.client, this.client.player, this.fovMultiplier);
        typedResult.ifCancelled(value -> this.fovMultiplier = value);
    }
}
