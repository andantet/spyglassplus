package com.teamfusion.spyglassplus.enchantment

import com.teamfusion.spyglassplus.SpyglassPlus
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object SpyglassPlusEnchantments {
    val SCRUTINY = register("scrutiny", ScrutinyEnchantment(Enchantment.Rarity.UNCOMMON))
    val ILLUMINATE = register("illuminate", IlluminateEnchantment(Enchantment.Rarity.UNCOMMON))
    val INDICATE = register("indicate", IndicateEnchantment(Enchantment.Rarity.UNCOMMON))
    val DISCOVERY = register("discovery", DiscoveryEnchantment(Enchantment.Rarity.UNCOMMON))
    val COMMAND = register("command", CommandEnchantment(Enchantment.Rarity.COMMON))

    private fun register(id: String, enchantment: Enchantment): Enchantment {
        return Registry.register(Registries.ENCHANTMENT, Identifier(SpyglassPlus.MOD_ID, id), enchantment)
    }
}
