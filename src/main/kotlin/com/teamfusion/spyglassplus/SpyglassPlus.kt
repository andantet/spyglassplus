package com.teamfusion.spyglassplus

import com.teamfusion.spyglassplus.enchantment.SpyglassPlusEnchantments
import com.teamfusion.spyglassplus.handler.ScrutinyScrollHandler
import com.teamfusion.spyglassplus.item.SpyglassPlusItemGroups
import com.teamfusion.spyglassplus.item.SpyglassPlusItems
import com.teamfusion.spyglassplus.networking.SpyglassPlusPacketTypes
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import org.slf4j.LoggerFactory

object SpyglassPlus : ModInitializer {
    const val MOD_ID = "spyglassplus"
    const val MOD_NAME = "Spyglass+"

    private val logger = LoggerFactory.getLogger(MOD_NAME)

    override fun onInitialize() {
        logger.info("Initializing $MOD_NAME")

        SpyglassPlusItems
        SpyglassPlusItemGroups
        SpyglassPlusEnchantments

        registerEvents()
    }

    private fun registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(SpyglassPlusPacketTypes.C2S_SCRUTINY_UPDATE, ScrutinyScrollHandler())
    }
}
