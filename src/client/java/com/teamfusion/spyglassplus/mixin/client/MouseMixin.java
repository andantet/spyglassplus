package com.teamfusion.spyglassplus.mixin.client;

import com.teamfusion.spyglassplus.client.event.ClientMouseScrollCallback;
import com.teamfusion.spyglassplus.event.EventResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow private double eventDeltaWheel;

    @Inject(
            method = "onMouseScroll",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Mouse;eventDeltaWheel:D",
                    ordinal = 7
            ),
            cancellable = true
    )
    private void onOnMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        EventResult result = ClientMouseScrollCallback.Companion.getEVENT().invoker().onMouseScroll(this.client, this.client.player, this.eventDeltaWheel);
        if (result == EventResult.CANCEL) {
            ci.cancel();
            this.eventDeltaWheel = 0;
        }
    }
}
