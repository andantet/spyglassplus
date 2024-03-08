package com.teamfusion.spyglassplus.networking

import net.fabricmc.fabric.api.networking.v1.FabricPacket
import net.fabricmc.fabric.api.networking.v1.PacketType
import net.minecraft.network.PacketByteBuf

/**
 * A packet sent from the client to the server to update the player's current scrutiny level.
 */
class C2SScrutinyUpdatePacket(
    /**
     * The updated scrutiny level.
     */
    val scrutiny: Int
) : FabricPacket {
    constructor(buf: PacketByteBuf) : this(
        buf.readVarInt()
    )

    override fun write(buf: PacketByteBuf) {
        buf.writeVarInt(scrutiny)
    }

    override fun getType(): PacketType<*> {
        return SpyglassPlusPacketTypes.C2S_SCRUTINY_UPDATE
    }
}
