package com.teamfusion.spyglassplus.client.handler

import com.teamfusion.spyglassplus.client.SpyglassPlusClient.getActiveSpyglassItemStack
import com.teamfusion.spyglassplus.client.event.ClientMouseScrollCallback
import com.teamfusion.spyglassplus.event.EventResult
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.item.ItemStack

class ScrutinyScrollHandler : ClientMouseScrollCallback {
    override fun onMouseScroll(client: MinecraftClient, player: ClientPlayerEntity, delta: Double): EventResult {
        // check for scroll movement
        if (delta == 0.0) {
            return EventResult.PASS
        }

        val stack = getActiveSpyglassItemStack(client, player) ?: return EventResult.PASS

        onSpyglassUse(client, player, delta, stack)
        return EventResult.CANCEL
    }

    private fun onSpyglassUse(
        client: MinecraftClient,
        player: ClientPlayerEntity,
        delta: Double,
        itemStack: ItemStack
    ) {
        // adjust scrutiny
    }
}
