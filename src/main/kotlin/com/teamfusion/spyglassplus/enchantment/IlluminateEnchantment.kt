package com.teamfusion.spyglassplus.enchantment

/**
 * An enchantment that applies a night vision effect on scoping items.
 * @see SpyglassPlusEnchantments.ILLUMINATE
 */
class IlluminateEnchantment(weight: Rarity) : ScopingEnchantment(weight) {
    override fun getMinPower(level: Int): Int {
        return 15
    }

    override fun getMaxPower(level: Int): Int {
        return super.getMinPower(level) + 50
    }
}
