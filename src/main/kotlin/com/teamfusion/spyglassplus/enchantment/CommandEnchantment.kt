package com.teamfusion.spyglassplus.enchantment

/**
 * An enchantment that notifies allied entities to attack the targeted entity when scoping.
 * @see SpyglassPlusEnchantments.COMMAND
 */
class CommandEnchantment(weight: Rarity) : ScopingEnchantment(weight) {
    override fun isTreasure(): Boolean {
        return true
    }
}
