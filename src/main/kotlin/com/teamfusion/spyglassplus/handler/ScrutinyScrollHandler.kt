package com.teamfusion.spyglassplus.handler

import com.teamfusion.spyglassplus.SpyglassPlus
import com.teamfusion.spyglassplus.enchantment.SpyglassEnchantmentHelper
import com.teamfusion.spyglassplus.enchantment.SpyglassPlusEnchantments
import com.teamfusion.spyglassplus.networking.C2SScrutinyUpdatePacket
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPacketHandler
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.SpyglassItem
import net.minecraft.server.network.ServerPlayerEntity

class ScrutinyScrollHandler : PlayPacketHandler<C2SScrutinyUpdatePacket> {
    override fun receive(packet: C2SScrutinyUpdatePacket, player: ServerPlayerEntity, sender: PacketSender) {
        // verify usage of spyglass
        if (!player.isUsingItem) {
            return
        }

        val stack = player.activeItem
        if (stack.item !is SpyglassItem) {
            return
        }

        val level = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.SCRUTINY, stack)
        if (level < 1) {
            return
        }

        // update level
        val scrutiny = packet.scrutiny
        val clampedScrutiny = SpyglassEnchantmentHelper.clampScrutiny(scrutiny) // clamp server-side as verification

        stack.orCreateNbt.putInt(SCRUTINY_KEY, clampedScrutiny)
    }

    companion object {
        const val SCRUTINY_KEY = "scrutiny"
        const val SCRUTINY_TOOLTIP_TRANSLATION_KEY = "item.minecraft.spyglass.${SpyglassPlus.MOD_ID}.tooltip.scrutiny"
    }
}
