package com.teamfusion.spyglassplus.client

import com.teamfusion.spyglassplus.client.event.ClientMouseScrollCallback
import com.teamfusion.spyglassplus.client.event.ClientWorldBrightnessCallback
import com.teamfusion.spyglassplus.client.event.FovMultiplierUpdateCallback
import com.teamfusion.spyglassplus.client.handler.ClientIlluminateBrightnessHandler
import com.teamfusion.spyglassplus.client.handler.ClientIndicateHandler
import com.teamfusion.spyglassplus.client.handler.ClientScrutinyHandler
import com.teamfusion.spyglassplus.networking.SpyglassPlusPacketTypes
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
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
        val scrutinyHandler = ClientScrutinyHandler()
        ClientMouseScrollCallback.EVENT.register(scrutinyHandler)
        FovMultiplierUpdateCallback.EVENT.register(scrutinyHandler)
        ItemTooltipCallback.EVENT.register(scrutinyHandler)

        ClientWorldBrightnessCallback.EVENT.register(ClientIlluminateBrightnessHandler())

        ClientPlayNetworking.registerGlobalReceiver(SpyglassPlusPacketTypes.S2C_ENABLE_INDICATE_GLOWING, ClientIndicateHandler.Enable())
        ClientPlayNetworking.registerGlobalReceiver(SpyglassPlusPacketTypes.S2C_DISABLE_INDICATE_GLOWING, ClientIndicateHandler.Disable())
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
