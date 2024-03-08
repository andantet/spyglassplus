package com.teamfusion.spyglassplus.client.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.util.ActionResult

fun interface ClientMouseScrollCallback {
    /**
     * A callback for when the client scrolls with the mouse.
     */
    fun onMouseScroll(client: MinecraftClient, player: ClientPlayerEntity, delta: Double): ActionResult

    companion object {
        val EVENT: Event<ClientMouseScrollCallback> = EventFactory.createArrayBacked(ClientMouseScrollCallback::class.java) { callbacks ->
            ClientMouseScrollCallback { client, player, delta ->
                callbacks.forEach { callback ->
                    val result = callback.onMouseScroll(client, player, delta)
                    if (result != ActionResult.PASS) {
                        return@ClientMouseScrollCallback result
                    }
                }

                return@ClientMouseScrollCallback ActionResult.PASS
            }
        }
    }
}
