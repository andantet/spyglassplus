package com.teamfusion.spyglassplus.enchantment

import net.minecraft.enchantment.Enchantment
import kotlin.math.max
import kotlin.math.min

object SpyglassEnchantmentHelper {
    val MAX_SCRUTINY_STACK_LEVEL = getEnchantmentBound(SpyglassPlusEnchantments.SCRUTINY, 3)
    val MIN_SCRUTINY_STACK_LEVEL = getEnchantmentBound(SpyglassPlusEnchantments.SCRUTINY, -3)

    /**
     * Clamps the given scrutiny level between its bounds.
     */
    fun clampScrutiny(level: Int): Int {
        return min(max(level, MIN_SCRUTINY_STACK_LEVEL), MAX_SCRUTINY_STACK_LEVEL)
    }

    private fun getEnchantmentBound(enchantment: Enchantment, scale: Int): Int {
        return enchantment.maxLevel * scale
    }
}
