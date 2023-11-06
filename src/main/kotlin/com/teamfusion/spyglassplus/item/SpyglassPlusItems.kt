package com.teamfusion.spyglassplus.item

import com.teamfusion.spyglassplus.SpyglassPlus
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object SpyglassPlusItems {
    private fun register(id: String, item: Item): Item {
        return Registry.register(Registries.ITEM, Identifier(SpyglassPlus.MOD_ID, id), item)
    }
}
