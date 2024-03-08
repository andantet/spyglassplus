package com.teamfusion.spyglassplus.client.event

import com.teamfusion.spyglassplus.event.EventResult
import com.teamfusion.spyglassplus.event.TypedEventResult
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity

fun interface FovMultiplierUpdateCallback {
    /**
     * A callback for when the client updates the FOV multiplier.
     */
    fun getMultiplier(client: MinecraftClient, player: ClientPlayerEntity, originalValue: Float): TypedEventResult<Float>

    companion object {
        val EVENT: Event<FovMultiplierUpdateCallback> = EventFactory.createArrayBacked(FovMultiplierUpdateCallback::class.java) { callbacks ->
            FovMultiplierUpdateCallback { client, player, originalValue ->
                callbacks.forEach { callback ->
                    val typedResult = callback.getMultiplier(client, player, originalValue)
                    val result = typedResult.result
                    if (result == EventResult.CANCEL) {
                        return@FovMultiplierUpdateCallback typedResult
                    }
                }

                TypedEventResult(EventResult.PASS)
            }
        }
    }
}
