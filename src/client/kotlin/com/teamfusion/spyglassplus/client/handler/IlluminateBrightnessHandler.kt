package com.teamfusion.spyglassplus.client.handler

import com.teamfusion.spyglassplus.client.SpyglassPlusClient.getActiveSpyglassItemStack
import com.teamfusion.spyglassplus.client.event.ClientWorldBrightnessCallback
import com.teamfusion.spyglassplus.enchantment.SpyglassPlusEnchantments
import com.teamfusion.spyglassplus.event.EventResult
import com.teamfusion.spyglassplus.event.TypedEventResult
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack
import kotlin.math.max
import kotlin.math.min

/**
 * Handles world brightness for the illuminate enchantment.
 */
class IlluminateBrightnessHandler : ClientWorldBrightnessCallback {
    /**
     * When the spyglass opened.
     */
    private var progress: Float = 0.0f

    override fun getBrightness(client: MinecraftClient, player: ClientPlayerEntity, originalValue: Float): TypedEventResult<Float> {
        // calculate progress delta
        val delta = 0.0005f * client.tickDelta

        // check stack for illuminate
        val stack = getActiveSpyglassItemStack(client, player)
        if (hasIlluminate(stack)) {
            progress += delta
        } else {
            progress -= delta * 3
        }

        // clamp progress
        progress = min(1.0f, max(0.0f, progress))

        // apply possible progress
        if (stack != null) {
            return TypedEventResult(EventResult.CANCEL, min(1.0f, max(originalValue, progress)))
        }

        return TypedEventResult.pass()
    }

    /**
     * Checks whether a nullable stack has the illuminate enchantment.
     */
    private fun hasIlluminate(stack: ItemStack?): Boolean {
        return stack != null && EnchantmentHelper.getLevel(SpyglassPlusEnchantments.ILLUMINATE, stack) > 0
    }
}
