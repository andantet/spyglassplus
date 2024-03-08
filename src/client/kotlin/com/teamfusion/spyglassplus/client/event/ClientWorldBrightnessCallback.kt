package com.teamfusion.spyglassplus.client.event

import com.teamfusion.spyglassplus.event.EventResult
import com.teamfusion.spyglassplus.event.TypedEventResult
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity

fun interface ClientWorldBrightnessCallback {
    /**
     * A callback for when the client requests the brightness level.
     */
    fun getBrightness(client: MinecraftClient, player: ClientPlayerEntity, originalValue: Float): TypedEventResult<Float>

    companion object {
        val EVENT: Event<ClientWorldBrightnessCallback> = EventFactory.createArrayBacked(ClientWorldBrightnessCallback::class.java) { callbacks ->
            ClientWorldBrightnessCallback { client, player, originalValue ->
                callbacks.forEach { callback ->
                    val typedResult = callback.getBrightness(client, player, originalValue)
                    val result = typedResult.result
                    if (result == EventResult.CANCEL) {
                        return@ClientWorldBrightnessCallback typedResult
                    }
                }

                TypedEventResult(EventResult.PASS)
            }
        }
    }
}
