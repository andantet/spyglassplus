package com.teamfusion.spyglassplus.client

import com.teamfusion.spyglassplus.client.event.ClientMouseScrollCallback
import com.teamfusion.spyglassplus.client.event.ClientWorldBrightnessCallback
import com.teamfusion.spyglassplus.client.handler.IlluminateBrightnessHandler
import com.teamfusion.spyglassplus.client.handler.ScrutinyScrollHandler
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.option.Perspective
import net.minecraft.item.ItemStack
import net.minecraft.item.SpyglassItem

object SpyglassPlusClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerEvents()
    }

    private fun registerEvents() {
        ClientMouseScrollCallback.EVENT.register(ScrutinyScrollHandler())
        ClientWorldBrightnessCallback.EVENT.register(IlluminateBrightnessHandler())
    }

    fun getActiveSpyglassItemStack(client: MinecraftClient, player: ClientPlayerEntity): ItemStack? {
        // if not in first person, spyglass does not function
        if (client.options.perspective != Perspective.FIRST_PERSON) {
            return null
        }

        // check for item use
        if (!player.isUsingItem) {
            return null
        }

        // check for correct item in use
        val itemStack = player.activeItem
        val item = itemStack.item
        if (item !is SpyglassItem) {
            return null
        }
        
        return itemStack
    }
}
