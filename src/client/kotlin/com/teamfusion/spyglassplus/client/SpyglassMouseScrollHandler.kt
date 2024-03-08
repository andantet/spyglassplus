package com.teamfusion.spyglassplus.client

import com.teamfusion.spyglassplus.client.event.ClientMouseScrollCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.option.Perspective
import net.minecraft.item.ItemStack
import net.minecraft.item.SpyglassItem
import net.minecraft.util.ActionResult

class SpyglassMouseScrollHandler : ClientMouseScrollCallback {
    override fun onMouseScroll(client: MinecraftClient, player: ClientPlayerEntity, delta: Double): ActionResult {
        // if not in first person, spyglass does not function
        if (client.options.perspective != Perspective.FIRST_PERSON) {
            return ActionResult.PASS
        }

        // check for scroll movement
        if (delta == 0.0) {
            return ActionResult.PASS
        }

        // check for item use
        if (!player.isUsingItem) {
            return ActionResult.PASS
        }

        // check for correct item in use
        val itemStack = player.activeItem
        val item = itemStack.item
        if (item !is SpyglassItem) {
            return ActionResult.PASS
        }

        onSpyglassUse(client, player, delta, itemStack, item)
        return ActionResult.FAIL
    }

    private fun onSpyglassUse(
        client: MinecraftClient,
        player: ClientPlayerEntity,
        delta: Double,
        itemStack: ItemStack,
        item: SpyglassItem
    ) {
        // adjust scrutiny
    }
}
