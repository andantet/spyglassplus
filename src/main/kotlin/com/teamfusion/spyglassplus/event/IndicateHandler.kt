package com.teamfusion.spyglassplus.event

import com.teamfusion.spyglassplus.enchantment.SpyglassPlusEnchantments
import com.teamfusion.spyglassplus.networking.S2CDisableIndicateGlowingPacket
import com.teamfusion.spyglassplus.networking.S2CEnableIndicateGlowingPacket
import com.teamfusion.spyglassplus.raycast.SpyglassRaycasting
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.EndTick
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World

class IndicateHandler : ItemUsageCallback, EndTick {
    private val userToWatched: MutableMap<LivingEntity, Entity> = mutableMapOf()

    override fun onUsageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        if (world !is ServerWorld) {
            return
        }

        val level = EnchantmentHelper.getLevel(SpyglassPlusEnchantments.INDICATE, stack)
        if (level < 1) {
            return
        }

        val watched = userToWatched[user]
        val entity = SpyglassRaycasting.raycast(user)
        if (watched != entity) {
            watched?.let { disableGlowing(world.server, it) }

            if (entity == null) {
                userToWatched.remove(user)
            } else {
                userToWatched[user] = entity
                enableGlowing(world.server, entity)
            }
        }
    }

    override fun onEndTick(server: MinecraftServer) {
        // check for finished using
        userToWatched.toMap().forEach { (user, watched) ->
            if (!user.isUsingItem) {
                userToWatched.remove(user)
                disableGlowing(server, watched)
            }
        }
    }

    private fun enableGlowing(server: MinecraftServer, entity: Entity) {
        val packet = S2CEnableIndicateGlowingPacket(entity.id)
        server.playerManager.playerList.forEach { player ->
            ServerPlayNetworking.send(player, packet)
        }
    }

    private fun disableGlowing(server: MinecraftServer, entity: Entity) {
        val packet = S2CDisableIndicateGlowingPacket(entity.id)
        server.playerManager.playerList.forEach { player ->
            ServerPlayNetworking.send(player, packet)
        }
    }
}
