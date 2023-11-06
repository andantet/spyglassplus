package com.teamfusion.spyglassplus.enchantment

/**
 * An enchantment that increases the zoom capability of scoping items.
 *
 * Can be adjusted through scrolling, dependent on level.
 *
 * @see SpyglassPlusEnchantments.SCRUTINY
 */
class ScrutinyEnchantment(weight: Rarity) : ScopingEnchantment(weight) {
    override fun getMaxLevel(): Int {
        return 3
    }

    override fun getMinPower(level: Int): Int {
        return 1 + 10 * (level - 1)
    }

    override fun getMaxPower(level: Int): Int {
        return super.getMinPower(level) + 50
    }
}
