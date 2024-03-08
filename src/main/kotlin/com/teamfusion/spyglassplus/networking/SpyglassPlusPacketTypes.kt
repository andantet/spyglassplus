package com.teamfusion.spyglassplus.networking

import com.teamfusion.spyglassplus.SpyglassPlus
import net.fabricmc.fabric.api.networking.v1.FabricPacket
import net.fabricmc.fabric.api.networking.v1.PacketType
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

object SpyglassPlusPacketTypes {
    val C2S_SCRUTINY_UPDATE = create("c2s_scrutiny_update", ::C2SScrutinyUpdatePacket)

    private fun <T : FabricPacket> create(id: String, constructor: (PacketByteBuf) -> T): PacketType<T> {
        return PacketType.create(Identifier(SpyglassPlus.MOD_ID, id), constructor)
    }
}
