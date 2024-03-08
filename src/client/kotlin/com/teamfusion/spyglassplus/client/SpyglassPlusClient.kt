package com.teamfusion.spyglassplus.client

import com.teamfusion.spyglassplus.client.event.ClientMouseScrollCallback
import net.fabricmc.api.ClientModInitializer

object SpyglassPlusClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerEvents()
    }

    private fun registerEvents() {
        ClientMouseScrollCallback.EVENT.register(SpyglassMouseScrollHandler())
    }
}
