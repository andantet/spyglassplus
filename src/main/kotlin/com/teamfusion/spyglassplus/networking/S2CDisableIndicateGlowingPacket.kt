package com.teamfusion.spyglassplus.networking

import net.fabricmc.fabric.api.networking.v1.FabricPacket
import net.fabricmc.fabric.api.networking.v1.PacketType
import net.minecraft.network.PacketByteBuf

/**
 * A packet sent from the server to the client to update the player's indicated glowing entities.
 */
class S2CDisableIndicateGlowingPacket(
    /**
     * The entity id to disable.
     */
    val entityId: Int
) : FabricPacket {
    constructor(buf: PacketByteBuf) : this(
        buf.readVarInt()
    )

    override fun write(buf: PacketByteBuf) {
        buf.writeVarInt(entityId)
    }

    override fun getType(): PacketType<*> {
        return SpyglassPlusPacketTypes.S2C_DISABLE_INDICATE_GLOWING
    }
}
