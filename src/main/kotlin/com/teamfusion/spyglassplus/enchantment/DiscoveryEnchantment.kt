package com.teamfusion.spyglassplus.enchantment

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack

/**
 * An enchantment that displays statistics about the targeted entity when scoping.
 *
 * This information includes health, strength, behavior, name, etc. This depends on the level.
 *
 * @see SpyglassPlusEnchantments.DISCOVERY
 */
class DiscoveryEnchantment(rarity: Rarity) : ScopingEnchantment(rarity) {
    override fun getMaxLevel(): Int {
        return 2
    }

    override fun isTreasure(): Boolean {
        return true
    }

    companion object {
        fun getLevel(stack: ItemStack?): Int {
            return EnchantmentHelper.getLevel(SpyglassPlusEnchantments.DISCOVERY, stack)
        }
    }
}
