package com.teamfusion.spyglassplus.client.handler

import com.teamfusion.spyglassplus.networking.S2CDisableIndicateGlowingPacket
import com.teamfusion.spyglassplus.networking.S2CEnableIndicateGlowingPacket
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.Disconnect
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler

object ClientIndicateHandler : Disconnect {
    private val glowingEntities: MutableSet<Entity> = mutableSetOf()

    override fun onPlayDisconnect(handler: ServerPlayNetworkHandler?, server: MinecraftServer?) {
        glowingEntities.clear()
    }

    fun isGlowing(entity: Entity): Boolean {
        return glowingEntities.contains(entity)
    }

    class Enable : ClientPlayNetworking.PlayPacketHandler<S2CEnableIndicateGlowingPacket> {
        override fun receive(
            packet: S2CEnableIndicateGlowingPacket,
            player: ClientPlayerEntity,
            responseSender: PacketSender
        ) {
            val entityId = packet.entityId
            val entity = player.world.getEntityById(entityId)
            if (entity != null) {
                glowingEntities.add(entity)
            }
        }
    }

    class Disable : ClientPlayNetworking.PlayPacketHandler<S2CDisableIndicateGlowingPacket> {
        override fun receive(
            packet: S2CDisableIndicateGlowingPacket,
            player: ClientPlayerEntity,
            responseSender: PacketSender
        ) {
            val entityId = packet.entityId
            val entity = player.world.getEntityById(entityId)
            if (entity != null) {
                glowingEntities.remove(entity)
            }
        }
    }
}
