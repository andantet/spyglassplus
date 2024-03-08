package com.teamfusion.spyglassplus.mixin.client;

import com.teamfusion.spyglassplus.client.event.ClientMouseScrollCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.util.ActionResult;
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
        ActionResult result = ClientMouseScrollCallback.Companion.getEVENT().invoker().onMouseScroll(this.client, this.client.player, this.eventDeltaWheel);
        if (!result.isAccepted() && result != ActionResult.PASS) {
            ci.cancel();
            this.eventDeltaWheel = 0;
        }
    }
}
