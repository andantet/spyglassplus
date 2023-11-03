package com.teamfusion.spyglassplus

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object SpyglassPlus : ModInitializer {
    const val MOD_ID = "spyglassplus"
    const val MOD_NAME = "Spyglass+"

    private val logger = LoggerFactory.getLogger(MOD_NAME)

    override fun onInitialize() {
        logger.info("Initializing $MOD_NAME")
    }
}
