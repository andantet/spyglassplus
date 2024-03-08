package com.teamfusion.spyglassplus.client.handler

import com.teamfusion.spyglassplus.client.SpyglassPlusClient.getActiveSpyglassItemStack
import com.teamfusion.spyglassplus.client.event.ClientMouseScrollCallback
import com.teamfusion.spyglassplus.client.event.FovMultiplierUpdateCallback
import com.teamfusion.spyglassplus.enchantment.SpyglassEnchantmentHelper
import com.teamfusion.spyglassplus.enchantment.SpyglassEnchantmentHelper.MAX_SCRUTINY_STACK_LEVEL
import com.teamfusion.spyglassplus.enchantment.SpyglassEnchantmentHelper.MIN_SCRUTINY_STACK_LEVEL
import com.teamfusion.spyglassplus.enchantment.SpyglassPlusEnchantments
import com.teamfusion.spyglassplus.event.EventResult
import com.teamfusion.spyglassplus.event.TypedEventResult
import com.teamfusion.spyglassplus.handler.ScrutinyScrollHandler.Companion.SCRUTINY_KEY
import com.teamfusion.spyglassplus.handler.ScrutinyScrollHandler.Companion.SCRUTINY_TOOLTIP_TRANSLATION_KEY
import com.teamfusion.spyglassplus.networking.C2SScrutinyUpdatePacket
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.truncate

class ClientScrutinyHandler : ClientMouseScrollCallback, FovMultiplierUpdateCallback, ItemTooltipCallback {
    override fun onMouseScroll(client: MinecraftClient, player: ClientPlayerEntity, delta: Double): EventResult {
        // check for scroll movement
        if (delta == 0.0) {
            return EventResult.PASS
        }

        val stack = getActiveSpyglassItemStack(client, player) ?: return EventResult.PASS

        onSpyglassScroll(delta, stack)
        return EventResult.CANCEL
    }

    private fun onSpyglassScroll(scrutinyDelta: Double, stack: ItemStack) {
        // check for scrutiny
        val level = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, stack)
        if (level < 1) {
            return
        }

        // get scrutiny
        val nbt = stack.orCreateNbt
        val scrutiny = nbt.getInt(SCRUTINY_KEY)
        val modifiedScrutiny = truncate(scrutiny + scrutinyDelta).toInt()
        val clampedScrutiny = SpyglassEnchantmentHelper.clampScrutiny(modifiedScrutiny)

        // update
        nbt.putInt(SCRUTINY_KEY, clampedScrutiny)
        ClientPlayNetworking.send(C2SScrutinyUpdatePacket(clampedScrutiny))
    }

    override fun getMultiplier(
        client: MinecraftClient,
        player: ClientPlayerEntity,
        originalValue: Float
    ): TypedEventResult<Float> {
        val stack = getActiveSpyglassItemStack(client, player) ?: return TypedEventResult.pass()
        val level = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, stack)
        if (level < 1) {
            return TypedEventResult.pass()
        }

        val scrutiny = stack.nbt?.getInt("scrutiny") ?: return TypedEventResult.pass()
        return TypedEventResult.cancel(
            originalValue * if (scrutiny > 0) {
                FACTOR / scrutiny
            } else {
                1.0f + (FACTOR / abs(MIN_SCRUTINY_STACK_LEVEL) * abs(scrutiny))
            }
        )
    }

    override fun getTooltip(stack: ItemStack, context: TooltipContext, lines: MutableList<Text>) {
        val level = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, stack)
        if (level < 1) {
            return
        }

        val scrutiny = stack.nbt?.getInt(SCRUTINY_KEY) ?: return
        val percentage = abs(scrutiny).toFloat() / MAX_SCRUTINY_STACK_LEVEL
        val progress = FACTOR * percentage

        lines.add(1, Text.empty())
        lines.add(
            1,
            Text.translatable(
                SCRUTINY_TOOLTIP_TRANSLATION_KEY,
                floor(
                    if (scrutiny >= 0) {
                        1.0f + progress
                    } else {
                        1.0f - progress
                    } * 100
                ) / 100
            ).formatted(Formatting.GRAY)
        )
    }

    companion object {
        private const val FACTOR = 0.85f
    }
}
