package com.teamfusion.spyglassplus.enchantment

/**
 * An enchantment that applies the glowing effect to entities within view when scoping.
 * @see SpyglassPlusEnchantments.INDICATE
 */
class IndicateEnchantment(weight: Rarity) : ScopingEnchantment(weight) {
    override fun getMinPower(level: Int): Int {
        return 15
    }

    override fun getMaxPower(level: Int): Int {
        return super.getMinPower(level) + 50
    }
}
