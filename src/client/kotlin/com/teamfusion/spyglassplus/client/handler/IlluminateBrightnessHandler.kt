package com.teamfusion.spyglassplus.client.handler

import com.teamfusion.spyglassplus.client.SpyglassPlusClient.getActiveSpyglassItemStack
import com.teamfusion.spyglassplus.client.event.ClientWorldBrightnessCallback
import com.teamfusion.spyglassplus.enchantment.SpyglassPlusEnchantments
import com.teamfusion.spyglassplus.event.EventResult
import com.teamfusion.spyglassplus.event.TypedEventResult
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.util.Util
import kotlin.math.max
import kotlin.math.min

class IlluminateBrightnessHandler : ClientWorldBrightnessCallback {
    private var openedLastTick = false
    private var millisOpened: Long = -1

    override fun getBrightness(client: MinecraftClient, player: ClientPlayerEntity, originalValue: Float): TypedEventResult<Float> {
        val stack = getActiveSpyglassItemStack(client, player)
        if (stack != null) {
            val level = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.ILLUMINATE, stack)
            if (level > 0) {
                val ms = Util.getMeasuringTimeMs()
                if (!openedLastTick) {
                    millisOpened = ms
                    openedLastTick = true
                }

                val diff = ms - millisOpened
                return TypedEventResult(EventResult.CANCEL, min(1.0f, max(originalValue, diff / 500.0f)))
            }
        }

        openedLastTick = false
        return TypedEventResult.pass()
    }
}
